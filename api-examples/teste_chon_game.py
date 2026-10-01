"""
Agente para o Chon Game - versao Python do testeChonGame.ps1.

Conecta no gateway TCP do jogo (localhost:8765), recebe observacoes em JSON
(uma por linha) e decide uma acao por prioridade:
    1. Inimigo proximo  -> ataca ou esquiva (aleatorio)
    2. Moeda proxima    -> anda ate ela
    3. Caixa proxima    -> ataca
    4. Nada disso       -> acao aleatoria (com vies para a direita)

Usa apenas a biblioteca padrao do Python (3.8+).
Execucao:  python teste_chon_game.py
"""

import json
import os
import random
import select
import socket
import sys
import time
import uuid

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
AGENT_ID = "python-agent"

DIRECTIONS = ["UP", "DOWN", "LEFT", "RIGHT"]

# Cores ANSI para imitar o Write-Host -ForegroundColor
CYAN, RED, GREEN, YELLOW = "\033[96m", "\033[91m", "\033[92m", "\033[93m"
DARK_YELLOW, MAGENTA, DARK_GRAY, RESET = "\033[33m", "\033[95m", "\033[90m", "\033[0m"

if os.name == "nt":
    os.system("")  # habilita cores ANSI no terminal do Windows


def log(text, color=""):
    print(f"{color}{text}{RESET}" if color else text, flush=True)


class AgentDead(Exception):
    pass


# ---------------------------------------------------------------------------
# Conexao
# ---------------------------------------------------------------------------

client = None
recv_buffer = b""


def connect():
    global client
    client = socket.create_connection((HOST, PORT))
    client.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY, 1)


def send_game_action(name, direction=None):
    message = {
        "type": "action",
        "protocolVersion": 1,
        "requestId": str(uuid.uuid4()),
        "agentId": AGENT_ID,
        "expectedTick": -1,
        "action": {"name": name},
    }

    # Correcao: no .ps1 o campo direction ia vazio ("") ate no ATTACK/CONFIRM
    if direction:
        message["action"]["direction"] = direction

    if name == "MOVE":
        global last_move_direction
        last_move_direction = direction

    data = json.dumps(message, separators=(",", ":"))
    client.sendall((data + "\n").encode("utf-8"))
    log(f"Enviado: {data}", CYAN)


def read_available_messages():
    """Le tudo que ja chegou no socket, sem bloquear. Retorna lista de dicts."""
    global recv_buffer
    messages = []

    while True:
        ready, _, _ = select.select([client], [], [], 0)
        if not ready:
            break

        chunk = client.recv(65536)
        if not chunk:
            raise ConnectionError("O servidor encerrou a conexao.")
        recv_buffer += chunk

    # Correcao: guarda linhas incompletas no buffer em vez de perde-las
    while b"\n" in recv_buffer:
        line, recv_buffer = recv_buffer.split(b"\n", 1)
        line = line.strip()
        if line:
            try:
                messages.append(json.loads(line.decode("utf-8")))
            except json.JSONDecodeError:
                log(f"JSON invalido ignorado: {line[:80]!r}", DARK_GRAY)

    return messages


# ---------------------------------------------------------------------------
# Funcoes auxiliares de direcao e distancia
# ---------------------------------------------------------------------------

def get_random_direction():
    return random.choice(DIRECTIONS)


# ---------------------------------------------------------------------------
# Melhoria: memoria de direcoes bloqueadas (paredes / limites da fase)
# ---------------------------------------------------------------------------
# Guarda em qual coordenada cada direcao travou. Ex.: {"UP": 260} quer dizer
# "andar para cima nao funciona enquanto y == 260". Assim o agente lembra da
# parede mesmo andando ao longo dela, e esquece quando sai daquela linha.

last_move_direction = None
blocked_at = {}


def axis_value(direction, me):
    return me["y"] if direction in ("UP", "DOWN") else me["x"]


def mark_blocked(direction, me):
    if direction in DIRECTIONS:
        blocked_at[direction] = axis_value(direction, me)


def is_blocked(direction, me):
    return blocked_at.get(direction) == axis_value(direction, me)


def free_directions(me, exclude=()):
    options = [d for d in DIRECTIONS if not is_blocked(d, me) and d not in exclude]
    # Se tudo estiver bloqueado (canto + inimigo, por ex.), sorteia qualquer uma
    return options or [d for d in DIRECTIONS if d not in exclude] or DIRECTIONS


def get_facing_direction(self_x, self_y, target_x, target_y):
    delta_x = target_x - self_x
    delta_y = target_y - self_y

    if abs(delta_x) >= abs(delta_y):
        return "RIGHT" if delta_x >= 0 else "LEFT"

    return "DOWN" if delta_y >= 0 else "UP"


def get_dodge_direction(self_x, self_y, target_x, target_y):
    opposite = {"RIGHT": "LEFT", "LEFT": "RIGHT", "UP": "DOWN", "DOWN": "UP"}
    return opposite[get_facing_direction(self_x, self_y, target_x, target_y)]


def squared_distance(a, b):
    dx = a["x"] - b["x"]
    dy = a["y"] - b["y"]
    return dx * dx + dy * dy


# ---------------------------------------------------------------------------
# Prioridade 1 - inimigos
# ---------------------------------------------------------------------------

last_threat_action = 0.0
THREAT_COOLDOWN_SECONDS = 0.75


def find_nearby_agent(payload):
    me = payload.get("self")
    if me is None:
        return None

    nearby = [
        a for a in payload.get("agents") or []
        # Correcao: ignora o proprio protagonista caso ele venha na lista
        if a.get("id") != me.get("id")
        and abs(a["x"] - me["x"]) <= 500
        and abs(a["y"] - me["y"]) <= 500
        and not a.get("dead")
    ]

    if not nearby:
        return None

    return min(nearby, key=lambda a: squared_distance(a, me))


def invoke_threat_response(payload):
    global last_threat_action

    me = payload["self"]
    target = find_nearby_agent(payload)

    if target is None:
        return False

    now = time.monotonic()

    # Em cooldown: considera a ameaca "tratada" para nao buscar moeda/caixa
    if now - last_threat_action < THREAT_COOLDOWN_SECONDS:
        return True

    if random.choice(["ATTACK", "DODGE"]) == "ATTACK":
        facing = get_facing_direction(me["x"], me["y"], target["x"], target["y"])
        send_game_action("MOVE", facing)
        send_game_action("ATTACK")
        log(f"Agente proximo: {target['id']}. Ataque na direcao {facing}.", RED)
    else:
        dodge = get_dodge_direction(me["x"], me["y"], target["x"], target["y"])

        # Melhoria: se a fuga estiver bloqueada, foge para um dos lados
        if is_blocked(dodge, me):
            facing = get_facing_direction(me["x"], me["y"], target["x"], target["y"])
            dodge = random.choice(free_directions(me, exclude=(facing,)))

        send_game_action("MOVE", dodge)
        log(f"Agente proximo: {target['id']}. Esquiva para {dodge}.", YELLOW)

    last_threat_action = now
    return True


# ---------------------------------------------------------------------------
# Prioridade 2 - moedas
# ---------------------------------------------------------------------------

def find_nearby_coin(payload):
    me = payload.get("self")
    objects = payload.get("objects")

    if me is None or objects is None:
        return None

    coins = [
        o for o in objects
        if o.get("collectible") is True
        # Correcao: moeda ja coletada (dead) nao e mais alvo
        and not o.get("dead")
        and abs(o["x"] - me["x"]) <= 500
        and abs(o["y"] - me["y"]) <= 200
    ]

    if not coins:
        return None

    return min(coins, key=lambda o: squared_distance(o, me))


def invoke_coin_response(payload):
    me = payload["self"]
    coin = find_nearby_coin(payload)

    if coin is None:
        return False

    if me["x"] == coin["x"] and me["y"] == coin["y"]:
        return False

    direction = get_facing_direction(me["x"], me["y"], coin["x"], coin["y"])
    send_game_action("MOVE", direction)

    log(
        f"Moeda detectada em ({coin['x']},{coin['y']}) "
        f"Distancia de ({abs(coin['x'] - me['x'])}, {abs(coin['y'] - me['y'])}). "
        f"Indo para {direction}.",
        GREEN,
    )
    return True


# ---------------------------------------------------------------------------
# Prioridade 3 - caixas
# ---------------------------------------------------------------------------

def find_nearby_box(payload):
    me = payload.get("self")
    objects = payload.get("objects")

    if me is None or objects is None:
        return None

    boxes = [
        o for o in objects
        if o.get("collectible") is False
        and not o.get("dead")
        and abs(o["x"] - me["x"]) <= 200
        and abs(o["y"] - me["y"]) <= 20
    ]

    return boxes[0] if boxes else None


def invoke_box_response(payload):
    box = find_nearby_box(payload)

    if box is None:
        return False

    send_game_action("ATTACK")
    log(f"Box encontrada em ({box['x']},{box['y']}). Ataque executado.", DARK_YELLOW)
    return True


# ---------------------------------------------------------------------------
# Prioridade 4 - acao aleatoria
# ---------------------------------------------------------------------------

# Mesmos pesos do .ps1: 1 cima, 1 baixo, 1 esquerda, 6 direita, 4 ataque
RANDOM_ACTIONS = (
    ["MOVE_UP", "MOVE_DOWN", "MOVE_LEFT"]
    + ["MOVE_RIGHT"] * 6
    + ["ATTACK"] * 4
)


def invoke_random_action(me):
    # Melhoria: tira do sorteio os movimentos que dariam na parede
    options = [
        a for a in RANDOM_ACTIONS
        if a == "ATTACK" or not is_blocked(a[len("MOVE_"):], me)
    ]
    action = random.choice(options)

    if action == "ATTACK":
        send_game_action("ATTACK")
    else:
        send_game_action("MOVE", action[len("MOVE_"):])


# ---------------------------------------------------------------------------
# Loop principal
# ---------------------------------------------------------------------------

RANDOM_ACTION_INTERVAL_SECONDS = 1
CONFIRM_INTERVAL_SECONDS = 1
STATIONARY_TICK_LIMIT = 30
UNSTUCK_DURATION_SECONDS = 1  # no .ps1 eram 3s
LOOP_SLEEP_SECONDS = 0.02
INITIAL_CONFIRMS = 4


def main():
    last_random_action = time.monotonic()
    last_confirm = 0.0
    last_observed_x = None
    last_observed_y = None
    stationary_ticks = 0
    unstuck_until = 0.0

    log("Conectado ao Chon Game.", GREEN)

    # Quatro confirms iniciais, separados por um segundo
    for number in range(1, INITIAL_CONFIRMS + 1):
        send_game_action("CONFIRM")
        log(f"Confirmacao inicial {number}/{INITIAL_CONFIRMS} enviada.", MAGENTA)
        if number < INITIAL_CONFIRMS:
            time.sleep(1)

    log("Monitoramento iniciado.", GREEN)

    while True:
        latest_observation = None

        for message in read_available_messages():
            msg_type = message.get("type")

            if msg_type == "hello":
                log(f"Gateway conectado. Controlando: {message.get('controls', '?')}.", GREEN)

            elif msg_type == "action_ack":
                # Correcao: o .ps1 nao mostrava quando a acao era recusada
                if message.get("accepted", True):
                    log("ACK recebido.", DARK_GRAY)
                else:
                    log(f"Acao recusada: {message.get('reason')}", RED)

            elif msg_type == "game_over":
                log("Game over recebido. CONFIRM sera enviado para tentar novamente.", YELLOW)

            elif msg_type == "agent_dead":
                log("O agente controlado morreu. Encerrando o cliente.", RED)
                raise AgentDead()

            elif msg_type == "observation":
                latest_observation = message

        if latest_observation is not None:
            payload = latest_observation.get("payload") or {}
            me = payload.get("self")
            current_state = str(payload.get("state"))
            now = time.monotonic()

            # Fora da fase jogavel (menu, game over...): so envia CONFIRM
            # Correcao: tambem cai aqui se ainda nao existe protagonista (self nulo)
            if current_state != "PlayableState" or me is None:
                log(f"Tick: {payload.get('tick')} | Estado: {current_state}")

                if now - last_confirm >= CONFIRM_INTERVAL_SECONDS:
                    send_game_action("CONFIRM")
                    log(f"Estado atual: {current_state}. CONFIRM enviado.", MAGENTA)
                    last_confirm = now

                time.sleep(LOOP_SLEEP_SECONDS)
                continue

            current_x, current_y = me["x"], me["y"]

            if current_x == last_observed_x and current_y == last_observed_y:
                stationary_ticks += 1
            else:
                stationary_ticks = 0

            last_observed_x, last_observed_y = current_x, current_y

            log(f"Tick: {payload.get('tick')} | Estado: {current_state} | "
                f"Posicao: ({current_x}, {current_y})")

            # Correcao: o .ps1 dava Start-Sleep e parava de ler o socket.
            # Aqui o MOVE continua "segurado" pelo tempo de destravamento, mas as mensagens seguem
            # sendo lidas (um agent_dead, por exemplo, nao fica esperando).
            if now < unstuck_until:
                # Correcao: nao conta "parado" durante o destravamento. Sem isso,
                # ao fim da espera o contador ja passava de 30 e disparava outro
                # destravamento na hora, e o agente nunca voltava as prioridades.
                stationary_ticks = 0
                time.sleep(LOOP_SLEEP_SECONDS)
                continue

            if stationary_ticks > STATIONARY_TICK_LIMIT:
                # Melhoria: a direcao que estava "segurada" nao funcionou,
                # entao marca como parede e sorteia entre as outras
                if last_move_direction:
                    mark_blocked(last_move_direction, me)
                    log(f"Direcao {last_move_direction} bloqueada em ({current_x}, {current_y}).", DARK_GRAY)
                unstuck_direction = random.choice(
                    free_directions(me, exclude=(last_move_direction,))
                )
                send_game_action("MOVE", unstuck_direction)
                log(
                    f"Protagonista parado por mais de {STATIONARY_TICK_LIMIT} ticks em "
                    f"({current_x}, {current_y}). Movimento forcado para {unstuck_direction}.",
                    YELLOW,
                )
                stationary_ticks = 0
                unstuck_until = now + UNSTUCK_DURATION_SECONDS
                time.sleep(LOOP_SLEEP_SECONDS)
                continue

            # PRIORIDADE 1 - INIMIGOS
            handled = invoke_threat_response(payload)

            # PRIORIDADE 2 - MOEDAS
            if not handled:
                handled = invoke_coin_response(payload)

            # PRIORIDADE 3 - BOXES
            if not handled:
                handled = invoke_box_response(payload)

            # PRIORIDADE 4 - ALEATORIO
            if not handled and now - last_random_action >= RANDOM_ACTION_INTERVAL_SECONDS:
                invoke_random_action(me)
                last_random_action = now

        time.sleep(LOOP_SLEEP_SECONDS)


if __name__ == "__main__":
    try:
        connect()
        main()
    except AgentDead:
        pass
    except KeyboardInterrupt:
        log("Interrompido pelo usuario.", YELLOW)
    except Exception as error:
        log(f"Erro: {error}", RED)
    finally:
        if client is not None:
            client.close()
        log("Conexao encerrada.")
        sys.exit(0)
