"""
Agente inteligente para o Chon Game (versao 2).

Evolucao do teste_chon_game.py (traducao do .ps1). Em vez de reagir por
sorteio, o agente usa o mapa inteiro que vem na observacao para perseguir
objetivos, na ordem de prioridade definida:

    1. Matar os inimigos
    2. Coletar o maximo de moedas
    3. Sobreviver (nunca encostar em inimigo, desviar de tiros)
    4. Terminar a fase

Arquitetura: agente baseado em objetivos com maquina de estados finitos.
A cada observacao o agente executa o ciclo:

    PERCEBER -> atualiza as crencas (posicao, direcao em que esta virado,
                paredes aprendidas, alvos inalcancaveis, eventos)
    DECIDIR  -> escolhe o estado/objetivo atual:
                  ESQUIVAR  : um tiro vem na nossa direcao
                  CACAR     : ha inimigo proximo, ou nenhuma moeda mais perto
                  COLETAR   : ha moeda mais perto que o inimigo mais proximo
                              (ou falta so 1 inimigo e ha moedas no fim da fase)
                  FUGIR     : ultimo inimigo perto, mas ainda nao e hora de mata-lo
                  FINALIZAR : nada a fazer, anda ate o fim da fase
    AGIR     -> converte o estado em MOVE/ATTACK, desviando de caixas

Mecanicas do jogo usadas (tiradas do game.json e do codigo Java):
  - O tiro "lancer" sai na frente do personagem, na direcao em que ele esta
    virado, anda ~150 px e mata um inimigo com um acerto (500 de dano).
  - Encostar em inimigo tira 1000 de vida (de 5000).
  - Moeda e coletada quando a posicao fica a menos de 20 px dela.
  - Andar para cima/baixo NAO muda o lado para o qual o personagem esta
    virado, entao da para mirar e "flutuar" na vertical sem perder a mira.
  - MOVE fica "segurado" ate chegar outro MOVE; nao existe comando de parar.

Usa apenas a biblioteca padrao (Python 3.8+).
Execucao:  python agente_chon_inteligente.py
"""

import json
import os
import random
import select
import socket
import time
import uuid

# ---------------------------------------------------------------------------
# Configuracao
# ---------------------------------------------------------------------------

# Control structure to setup SERVER_HOST in the two use cases (localhost or remote machine).
print("Are you running the script on another machine?")
print("1. Yes")
print("2. No")
option = input("Select an option: ")
option = str(option).strip().lower()

# Match case to initialize hostAdress based on the option selected by the user.
match option:
    case "1" | "yes" | "y":
        hostAdress = input("Type server's IP address: ")
           
    case "2" | "no" | "n":
        hostAdress = "localhost"
        
    case _:
        print("Invalid option!")

HOST = hostAdress
PORT = 8765
AGENT_ID = "python-agent-v2"
VERBOSE = False            # True mostra cada acao enviada (fica bem poluido)

# Tiro (lancer): nasce ~91 px a frente e alcanca ~150 px
SHOT_MIN_DX = 100          # mais perto que isso o tiro nasce depois do inimigo
SHOT_MAX_DX = 220          # mais longe que isso o tiro expira antes
SHOT_IDEAL_DX = 160
ALIGN_Y = 20               # diferenca de y aceita para o tiro acertar
ATTACK_COOLDOWN = 0.32     # cooldown da arma e 300 ms

# Seguranca
CONTACT_Y = 80             # faixa vertical em que o inimigo pode nos tocar
LOW_HEALTH_RATIO = 0.4     # abaixo disso o agente mantem mais distancia
SHOT_THREAT_DX = 260       # tiro inimigo a menos disso, na nossa faixa: esquivar
SHOT_THREAT_Y = 50

# Navegacao
COLLECT_TOLERANCE = 8      # coleta acontece a < 20 px; 8 por eixo e seguro
ENGAGE_DISTANCE = 600      # inimigo mais perto que isso vira prioridade
LOOKAHEAD = 90             # distancia para enxergar caixa no caminho
LANE_HALF = 40             # "largura" da faixa em que uma caixa atrapalha
STUCK_OBSERVATIONS = 8     # parado por isso tudo = direcao bloqueada
DETOUR_SECONDS = 0.5
NO_PROGRESS_SECONDS = 5    # desiste de um alvo se nao chega mais perto nesse tempo
IGNORE_SECONDS = 20        # ... e ignora ele por esse tempo
END_ZONE_FLEE_BUDGET = 6   # segundos fugindo do ultimo inimigo antes de desistir
                           # das moedas do fim e simplesmente mata-lo
FINISH_RATIO = 0.96        # fase termina em 95% da largura (+ margem)
END_ZONE_MARGIN = 60       # moedas perto dos 95% contam como "zona final"

LOOP_SLEEP = 0.02
CONFIRM_INTERVAL = 1
INITIAL_CONFIRMS = 4
STATUS_INTERVAL = 1

DIRECTIONS = ["UP", "DOWN", "LEFT", "RIGHT"]
OPPOSITE = {"UP": "DOWN", "DOWN": "UP", "LEFT": "RIGHT", "RIGHT": "LEFT"}
VERTICAL = ("UP", "DOWN")
HORIZONTAL = ("LEFT", "RIGHT")

# Cores ANSI
CYAN, RED, GREEN, YELLOW = "\033[96m", "\033[91m", "\033[92m", "\033[93m"
DARK_YELLOW, MAGENTA, DARK_GRAY, BLUE, RESET = (
    "\033[33m", "\033[95m", "\033[90m", "\033[94m", "\033[0m")

STATE_COLORS = {
    "ESQUIVAR": YELLOW, "CACAR": RED, "COLETAR": GREEN,
    "FINALIZAR": BLUE, "MENU": MAGENTA, "FUGIR": YELLOW,
}

if os.name == "nt":
    os.system("")  # habilita cores ANSI no terminal do Windows


def log(text, color=""):
    print(f"{color}{text}{RESET}" if color else text, flush=True)


class AgentDead(Exception):
    pass


# ---------------------------------------------------------------------------
# Conexao com o jogo
# ---------------------------------------------------------------------------

class GameConnection:
    def __init__(self, host, port):
        self.sock = socket.create_connection((host, port))
        self.sock.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY, 1)
        self.buffer = b""

    def send(self, name, direction=None):
        message = {
            "type": "action",
            "protocolVersion": 1,
            "requestId": str(uuid.uuid4()),
            "agentId": AGENT_ID,
            "expectedTick": -1,
            "action": {"name": name},
        }
        if direction:
            message["action"]["direction"] = direction

        data = json.dumps(message, separators=(",", ":"))
        self.sock.sendall((data + "\n").encode("utf-8"))
        if VERBOSE:
            log(f"Enviado: {data}", CYAN)

    def read_messages(self):
        messages = []
        while True:
            ready, _, _ = select.select([self.sock], [], [], 0)
            if not ready:
                break
            chunk = self.sock.recv(65536)
            if not chunk:
                raise ConnectionError("O servidor encerrou a conexao.")
            self.buffer += chunk

        while b"\n" in self.buffer:
            line, self.buffer = self.buffer.split(b"\n", 1)
            line = line.strip()
            if line:
                try:
                    messages.append(json.loads(line.decode("utf-8")))
                except json.JSONDecodeError:
                    log(f"JSON invalido ignorado: {line[:80]!r}", DARK_GRAY)
        return messages

    def close(self):
        self.sock.close()


# ---------------------------------------------------------------------------
# Agente
# ---------------------------------------------------------------------------

class ChonAgent:
    def __init__(self, connection):
        self.conn = connection
        self.reset_beliefs()
        self.last_confirm = 0.0
        self.last_status = 0.0
        self.state = None

    # ----------------------------------------------------------- crencas --

    def reset_beliefs(self):
        """Tudo que o agente 'sabe' sobre a fase atual."""
        self.facing = "RIGHT"          # personagem comeca virado para a direita
        self.held = None               # ultima direcao de MOVE enviada
        self.last_move_sent = 0.0
        self.last_attack = 0.0
        self.last_pos = None
        self.still_count = 0
        self.blocked = {}              # direcao -> lista de (coord_eixo, coord_outro)
        self.detour = None             # (direcao, ate_quando)
        self.target_id = None
        self.target_best = None
        self.target_progress_at = 0.0
        self.retreating = False
        self.flee_time = 0.0
        self.end_zone_enabled = True
        self.ignored = {}              # id do alvo -> ignorar ate
        self.prev_health = None
        self.prev_collected = None
        self.alive_enemies = set()
        self.level_key = None
        self.hover_flip = False

    # ---------------------------------------------------------- perceber --

    def perceive(self, payload, now):
        me = payload["self"]
        level = payload.get("level") or {}

        # Mudou de fase: as crencas antigas nao valem mais
        level_key = (level.get("description"), level.get("width"))
        if level_key != self.level_key:
            if self.level_key is not None:
                log("Nova fase detectada, reiniciando crencas.", MAGENTA)
            self.reset_beliefs()
            self.level_key = level_key

        # Para que lado esta virado (so muda com LEFT/RIGHT)
        if me.get("direction") in HORIZONTAL:
            self.facing = me["direction"]

        # Deteccao de "travado": segurando uma direcao e sem sair do lugar
        pos = (me["x"], me["y"])
        if pos == self.last_pos:
            self.still_count += 1
        else:
            self.still_count = 0
        self.last_pos = pos

        if self.held and self.still_count >= STUCK_OBSERVATIONS:
            self.mark_blocked(self.held, me)
            side = random.choice(self.free_perpendicular(self.held, me))
            self.detour = (side, now + DETOUR_SECONDS)
            log(f"Bloqueado indo para {self.held} em {pos}. Contornando por {side}.",
                DARK_GRAY)
            self.still_count = 0

        # Eventos (so para o log ficar explicativo)
        health = me.get("health", 0)
        if self.prev_health is not None and health < self.prev_health:
            log(f"Dano recebido: -{self.prev_health - health} "
                f"(vida {health}/{me.get('maxHealth')})", RED)
        self.prev_health = health

        collected = payload.get("collectedCount", 0)
        if self.prev_collected is not None and collected > self.prev_collected:
            log(f"Moeda coletada! Total: {collected} | score {payload.get('score')}",
                GREEN)
        self.prev_collected = collected

        alive_now = {a["id"] for a in self.enemies(payload)}
        for enemy_id in self.alive_enemies - alive_now:
            log(f"Inimigo abatido: {enemy_id[:8]}", RED)
        self.alive_enemies = alive_now

    # ------------------------------------------------ memoria de bloqueio --

    @staticmethod
    def axes(direction, me):
        """(coordenada no eixo do movimento, coordenada no outro eixo)."""
        if direction in VERTICAL:
            return me["y"], me["x"]
        return me["x"], me["y"]

    def mark_blocked(self, direction, me):
        self.blocked.setdefault(direction, []).append(self.axes(direction, me))

    def is_blocked(self, direction, me):
        """Bloqueado se ja travou nessa mesma linha, a ate 30 px de distancia.
        Parede de verdade trava de novo e e remarcada; caixa fica para tras."""
        main, other = self.axes(direction, me)
        for b_main, b_other in self.blocked.get(direction, []):
            if b_main == main and abs(b_other - other) < 30:
                return True
        return False

    def free_perpendicular(self, direction, me):
        options = VERTICAL if direction in HORIZONTAL else HORIZONTAL
        free = [d for d in options if not self.is_blocked(d, me)]
        return free or list(options)

    # ------------------------------------------------------- utilitarios --

    @staticmethod
    def enemies(payload):
        me = payload["self"]
        return [a for a in payload.get("agents") or []
                if a.get("id") != me.get("id") and not a.get("dead")
                and a.get("health", 1) > 0]

    def coins(self, payload, now):
        return [o for o in payload.get("objects") or []
                if o.get("collectible") and not o.get("dead")
                and self.ignored.get(o["id"], 0) < now]

    @staticmethod
    def boxes(payload):
        return [o for o in payload.get("objects") or []
                if not o.get("collectible") and not o.get("dead")]

    @staticmethod
    def manhattan(a, b):
        return abs(a["x"] - b["x"]) + abs(a["y"] - b["y"])

    def track_target(self, target, me, now):
        """Desiste de alvos que pararam de ficar mais perto (inalcancaveis).
        Mede progresso, nao tempo: um alvo longe pode levar 30s e tudo bem."""
        distance = self.manhattan(target, me)
        if target["id"] != self.target_id:
            self.target_id = target["id"]
            self.target_best = distance
            self.target_progress_at = now
            return True
        if distance < self.target_best - 4:
            self.target_best = distance
            self.target_progress_at = now
        elif now - self.target_progress_at > NO_PROGRESS_SECONDS:
            self.ignored[target["id"]] = now + IGNORE_SECONDS
            log(f"Sem progresso ate {target['id'][:8]} ha {NO_PROGRESS_SECONDS}s. "
                f"Ignorando por {IGNORE_SECONDS}s.", DARK_GRAY)
            self.target_id = None
            return False
        return True

    # ----------------------------------------------------------- decidir --

    def incoming_shot(self, payload):
        """Tiro vindo na nossa direcao, na nossa faixa. Os nossos tiros
        sempre se afastam da gente, entao nao entram aqui."""
        me = payload["self"]
        for s in payload.get("shots") or []:
            if abs(s["y"] - me["y"]) > SHOT_THREAT_Y:
                continue
            dx = me["x"] - s["x"]
            if s.get("direction") == "RIGHT" and 0 < dx < SHOT_THREAT_DX:
                return s
            if s.get("direction") == "LEFT" and 0 < -dx < SHOT_THREAT_DX:
                return s
        return None

    def decide(self, payload, now):
        me = payload["self"]

        shot = self.incoming_shot(payload)
        if shot:
            return "ESQUIVAR", shot

        enemies = sorted(self.enemies(payload), key=lambda a: self.manhattan(a, me))
        enemies = [e for e in enemies if self.ignored.get(e["id"], 0) < now]
        coins = sorted(self.coins(payload, now), key=lambda c: self.manhattan(c, me))

        nearest_enemy = enemies[0] if enemies else None
        nearest_coin = coins[0] if coins else None

        # Planejamento: a fase termina quando x >= 95% da largura E todos os
        # inimigos estao mortos. Se o ultimo inimigo morrer antes, pegar as
        # moedas do fim da fase encerra a fase no meio da coleta. Entao, com
        # um inimigo restante, primeiro coletamos a "zona final".
        width = (payload.get("level") or {}).get("width") or 0
        end_zone_x = 0.95 * width - END_ZONE_MARGIN
        end_coins = [c for c in coins if width and c["x"] >= end_zone_x]
        if len(enemies) == 1 and end_coins and self.end_zone_enabled:
            enemy = enemies[0]
            if abs(enemy["x"] - me["x"]) < SHOT_IDEAL_DX and \
                    abs(enemy["y"] - me["y"]) < CONTACT_Y:
                # Perto demais, mas ainda nao e hora de mata-lo. Se ele nos
                # persegue e a fuga demora, o plano nao vale a pena: mata.
                self.flee_time += LOOP_SLEEP
                if self.flee_time > END_ZONE_FLEE_BUDGET:
                    self.end_zone_enabled = False
                    log("O ultimo inimigo nao para de me perseguir. Mudanca de plano: "
                        "matar agora e abrir mao das moedas do fim.", MAGENTA)
                    return "CACAR", enemy
                return "FUGIR", enemy
            return "COLETAR", end_coins[0]

        # Prioridade 1: inimigo perto sempre vira o objetivo
        if nearest_enemy and self.manhattan(nearest_enemy, me) < ENGAGE_DISTANCE:
            return "CACAR", nearest_enemy

        # Prioridade 2: moeda que esta no caminho (mais perto que o inimigo)
        if nearest_coin and (nearest_enemy is None or
                             self.manhattan(nearest_coin, me)
                             < self.manhattan(nearest_enemy, me)):
            return "COLETAR", nearest_coin

        # Prioridade 1 de novo: ir atras do inimigo, mesmo longe
        if nearest_enemy:
            return "CACAR", nearest_enemy

        # Prioridade 4: terminar a fase
        return "FINALIZAR", None

    # ------------------------------------------------------------- agir --

    def move(self, direction, now):
        # MOVE fica segurado; so reenvia quando muda (ou a cada 0,5s por garantia)
        if direction != self.held or now - self.last_move_sent > 0.5:
            self.conn.send("MOVE", direction)
            self.held = direction
            self.last_move_sent = now

    def attack(self, now):
        if now - self.last_attack >= ATTACK_COOLDOWN:
            self.conn.send("ATTACK")
            self.last_attack = now
            return True
        return False

    def box_ahead(self, me, direction, payload):
        for box in self.boxes(payload):
            dx = box["x"] - me["x"]
            dy = box["y"] - me["y"]
            if direction == "RIGHT" and 0 <= dx <= LOOKAHEAD and abs(dy) < LANE_HALF:
                return box
            if direction == "LEFT" and 0 <= -dx <= LOOKAHEAD and abs(dy) < LANE_HALF:
                return box
            if direction == "DOWN" and 0 <= dy <= LOOKAHEAD * 0.6 and abs(dx) < LANE_HALF:
                return box
            if direction == "UP" and 0 <= -dy <= LOOKAHEAD * 0.6 and abs(dx) < LANE_HALF:
                return box
        return None

    def navigate(self, me, direction, payload, now, goal_x=None, goal_y=None):
        """Ajusta a direcao desejada: respeita desvios em andamento, contorna
        caixas e evita direcoes que ja se mostraram bloqueadas."""
        if self.detour and now < self.detour[1]:
            return self.detour[0]
        self.detour = None

        box = self.box_ahead(me, direction, payload)
        if box is None and not self.is_blocked(direction, me):
            return direction

        options = self.free_perpendicular(direction, me)
        if direction in HORIZONTAL:
            goal, pos, box_pos, neg, posd = goal_y, me["y"], box and box["y"], "UP", "DOWN"
        else:
            goal, pos, box_pos, neg, posd = goal_x, me["x"], box and box["x"], "LEFT", "RIGHT"

        if goal is not None and abs(goal - pos) > LANE_HALF:
            # Contorna pelo lado em que esta o objetivo
            preferred = neg if goal < pos else posd
        elif box is not None:
            # Senao, pelo lado oposto ao da caixa
            preferred = neg if box_pos >= pos else posd
        else:
            preferred = options[0]
        side = preferred if preferred in options else options[0]

        # Compromete-se com o desvio por um instante. Sem isso o agente
        # oscila entre "desviar" e "voltar para a rota" e nao sai do lugar.
        self.detour = (side, now + DETOUR_SECONDS * 0.6)
        return side

    def go_to(self, me, tx, ty, payload, now):
        dx, dy = tx - me["x"], ty - me["y"]
        if abs(dx) >= abs(dy):
            desired = "RIGHT" if dx > 0 else "LEFT"
        else:
            desired = "DOWN" if dy > 0 else "UP"
        self.move(self.navigate(me, desired, payload, now, goal_x=tx, goal_y=ty), now)

    def act_dodge(self, me, shot, now):
        # Sai da faixa do tiro pelo lado com mais espaco (e nao bloqueado)
        options = [d for d in VERTICAL if not self.is_blocked(d, me)] or list(VERTICAL)
        preferred = "UP" if shot["y"] >= me["y"] else "DOWN"
        self.move(preferred if preferred in options else options[0], now)

    def act_hunt(self, me, enemy, payload, now):
        dx = enemy["x"] - me["x"]
        dy = enemy["y"] - me["y"]
        toward = "RIGHT" if dx > 0 else "LEFT"
        away = OPPOSITE[toward]

        low_health = me.get("health", 1) < LOW_HEALTH_RATIO * me.get("maxHealth", 1)
        min_dx = SHOT_MIN_DX + (40 if low_health else 0)

        # 1) Perto demais: recua na horizontal antes de qualquer coisa.
        #    Histerese: uma vez recuando, vai ate a distancia ideal (e nao so
        #    ate o minimo), para sobrar folga para virar e atirar.
        if abs(dx) < min_dx and abs(dy) < CONTACT_Y:
            self.retreating = True
        elif abs(dx) >= SHOT_IDEAL_DX or abs(dy) >= CONTACT_Y:
            self.retreating = False
        if self.retreating:
            if not self.is_blocked(away, me):
                self.move(away, now)
            else:  # encurralado na parede: foge pela vertical
                self.move("UP" if dy > 0 else "DOWN", now)
            return "recuando"

        # 2) Longe: aproxima na horizontal (mira na distancia ideal)
        if abs(dx) > SHOT_MAX_DX:
            goal_x = enemy["x"] - SHOT_IDEAL_DX if dx > 0 else enemy["x"] + SHOT_IDEAL_DX
            self.go_to(me, goal_x, enemy["y"], payload, now)
            return "aproximando"

        # 3) Na distancia certa, mas desalinhado: ajusta o y
        if abs(dy) > ALIGN_Y:
            self.move(self.navigate(me, "DOWN" if dy > 0 else "UP", payload, now,
                                    goal_x=me["x"]), now)
            return "alinhando"

        # 4) Alinhado mas virado para o lado errado: vira (um passo na direcao dele)
        if self.facing != toward:
            self.move(toward, now)
            return "virando"

        # 5) Posicao de tiro: flutua na vertical (nao muda a mira) e atira
        if dy > 2:
            hover = "DOWN"
        elif dy < -2:
            hover = "UP"
        else:
            self.hover_flip = not self.hover_flip
            hover = "UP" if self.hover_flip else "DOWN"
        if self.is_blocked(hover, me):
            hover = OPPOSITE[hover]
        self.move(hover, now)
        if self.attack(now):
            log(f"Tiro para {toward} (dx={dx}, dy={dy}).", DARK_YELLOW)
        return "atirando"

    def act_flee(self, me, enemy, now):
        dx = enemy["x"] - me["x"]
        away = "LEFT" if dx > 0 else "RIGHT"
        if self.is_blocked(away, me):
            away = "UP" if enemy["y"] > me["y"] else "DOWN"
        self.move(away, now)

    def act_collect(self, me, coin, payload, now):
        dx, dy = coin["x"] - me["x"], coin["y"] - me["y"]
        if abs(dx) <= COLLECT_TOLERANCE:
            desired = "DOWN" if dy > 0 else "UP"
        elif abs(dy) <= COLLECT_TOLERANCE:
            desired = "RIGHT" if dx > 0 else "LEFT"
        else:
            desired = ("RIGHT" if dx > 0 else "LEFT") if abs(dx) >= abs(dy) \
                else ("DOWN" if dy > 0 else "UP")
        self.move(self.navigate(me, desired, payload, now,
                                goal_x=coin["x"], goal_y=coin["y"]), now)

    def act_finish(self, me, payload, now):
        level = payload.get("level") or {}
        width = level.get("width") or 0
        if width and me["x"] >= FINISH_RATIO * width:
            # Ja chegou: fica flutuando ate o jogo trocar de fase
            self.hover_flip = not self.hover_flip
            self.move("UP" if self.hover_flip else "DOWN", now)
            return
        self.move(self.navigate(me, "RIGHT", payload, now), now)

    # -------------------------------------------------------- loop geral --

    def step(self, payload, now):
        state_name = str(payload.get("state"))
        me = payload.get("self")

        if state_name != "PlayableState" or me is None:
            self.set_state("MENU", f"estado do jogo: {state_name}")
            if now - self.last_confirm >= CONFIRM_INTERVAL:
                self.conn.send("CONFIRM")
                self.last_confirm = now
            self.held = None  # ao voltar para o jogo, reenvia o MOVE
            return

        self.perceive(payload, now)
        state, target = self.decide(payload, now)

        # Inimigo por perto nunca e abandonado: desistir dele e virar as costas
        # para quem pode nos atacar. So alvos distantes entram nessa regra.
        abandonable = state == "COLETAR" or (
            state == "CACAR" and self.manhattan(target, me) >= ENGAGE_DISTANCE)
        if target is not None and abandonable:
            if not self.track_target(target, me, now):
                return  # desistiu; decide de novo na proxima observacao
        elif target is not None:
            self.target_id = None

        detail = ""
        if state == "ESQUIVAR":
            self.act_dodge(me, target, now)
            detail = f"tiro em ({target['x']},{target['y']})"
        elif state == "CACAR":
            phase = self.act_hunt(me, target, payload, now)
            detail = f"inimigo {target['id'][:8]} em ({target['x']},{target['y']}) - {phase}"
        elif state == "FUGIR":
            self.act_flee(me, target, now)
            detail = f"afastando do ultimo inimigo {target['id'][:8]}"
        elif state == "COLETAR":
            self.act_collect(me, target, payload, now)
            detail = f"moeda em ({target['x']},{target['y']})"
        else:
            self.act_finish(me, payload, now)
            detail = "indo para o fim da fase"

        self.set_state(state, detail)

        if now - self.last_status >= STATUS_INTERVAL:
            self.last_status = now
            log(f"[{state}] pos ({me['x']},{me['y']}) | vida {me.get('health')}"
                f"/{me.get('maxHealth')} | inimigos {len(self.alive_enemies)} | "
                f"moedas {payload.get('collectedCount')} | score {payload.get('score')}"
                f" | {detail}", DARK_GRAY)

    def set_state(self, state, reason):
        if state != self.state:
            log(f"Estado: {self.state} -> {state} ({reason})",
                STATE_COLORS.get(state, ""))
            self.state = state


# ---------------------------------------------------------------------------
# Programa principal
# ---------------------------------------------------------------------------

def run(conn):
    agent = ChonAgent(conn)
    log("Conectado ao Chon Game.", GREEN)

    for number in range(1, INITIAL_CONFIRMS + 1):
        conn.send("CONFIRM")
        log(f"Confirmacao inicial {number}/{INITIAL_CONFIRMS} enviada.", MAGENTA)
        if number < INITIAL_CONFIRMS:
            time.sleep(1)

    log("Monitoramento iniciado.", GREEN)

    while True:
        latest = None
        for message in conn.read_messages():
            msg_type = message.get("type")
            if msg_type == "hello":
                log(f"Gateway conectado. Controlando: {message.get('controls', '?')}.",
                    GREEN)
            elif msg_type == "action_ack":
                if not message.get("accepted", True):
                    log(f"Acao recusada: {message.get('reason')}", RED)
            elif msg_type == "game_over":
                log("Game over recebido. CONFIRM sera enviado para tentar novamente.",
                    YELLOW)
            elif msg_type == "agent_dead":
                log("O agente controlado morreu. Encerrando o cliente.", RED)
                raise AgentDead()
            elif msg_type == "observation":
                latest = message

        if latest is not None:
            agent.step(latest.get("payload") or {}, time.monotonic())

        time.sleep(LOOP_SLEEP)


if __name__ == "__main__":
    connection = None
    try:
        connection = GameConnection(HOST, PORT)
        run(connection)
    except AgentDead:
        pass
    except KeyboardInterrupt:
        log("Interrompido pelo usuario.", YELLOW)
    except Exception as error:
        log(f"Erro: {error}", RED)
    finally:
        if connection is not None:
            connection.close()
        log("Conexao encerrada.")
