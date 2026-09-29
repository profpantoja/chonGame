import json
import math
import random
import socket
import time
import uuid
from datetime import datetime, timedelta, timezone
from typing import Any, Optional


# =============================================================================
# Configuração da conexão
# =============================================================================

SERVER_HOST = "192.168.103.61"
SERVER_PORT = 8765

# Intervalo principal do cliente, equivalente aos 20 ms do PowerShell.
MAIN_LOOP_INTERVAL_SECONDS = 0.020

# Alcance de percepção do bot.
ENGAGE_RANGE_X = 300
ENGAGE_RANGE_Y = 300

# Distância máxima para executar um ataque corpo a corpo.
MELEE_RANGE = 60

# Intervalo mínimo entre ataques.
ATTACK_COOLDOWN_SECONDS = 0.500


# =============================================================================
# Estado global do bot
# =============================================================================

role: Optional[str] = None
agent_index = -1
controlled_agent_id: Optional[str] = None

last_attack = datetime.min.replace(tzinfo=timezone.utc)

next_wander_change = datetime.min.replace(tzinfo=timezone.utc)
wander_direction: Optional[str] = None


# =============================================================================
# Conexão TCP
# =============================================================================

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Desabilita o algoritmo de Nagle, equivalente a:
# $client.NoDelay = $true
client.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY, 1)

client.connect((SERVER_HOST, SERVER_PORT))

# O socket ficará em modo não bloqueante.
#
# Assim, o programa pode verificar se existem mensagens disponíveis sem ficar
# parado esperando o servidor enviar dados.
client.setblocking(False)

# Armazena dados recebidos que ainda não formaram uma linha JSON completa.
receive_buffer = ""


# =============================================================================
# Funções auxiliares
# =============================================================================

def utc_now() -> datetime:
    """
    Retorna a data e hora atual em UTC.

    Equivale a:
    :UtcNow
    """
    return datetime.now(timezone.utc)


def get_value(source: Any, key: str, default: Any = None) -> Any:
    """
    Recupera um valor de um dicionário ou objeto.

    As mensagens JSON são convertidas em dicionários pelo json.loads().
    Esta função deixa o restante do código mais legível e também facilita
    lidar com valores ausentes.
    """
    if source is None:
        return default

    if isinstance(source, dict):
        return source.get(key, default)

    return getattr(source, key, default)


def send_game_action(name: str, direction: Optional[str] = None) -> None:
    """
    Envia uma ação para o gateway do jogo.

    Exemplos de ações:

        send_game_action("ATTACK")
        send_game_action("MOVE", "UP")
    """

    message = {
        "type": "action",
        "protocolVersion": 1,
        "requestId": str(uuid.uuid4()),
        "agentId": "python-bot",
        "expectedTick": -1,
        "action": {
            "name": name
        }
    }

    # A direção só é adicionada quando tiver sido informada.
    if direction is not None:
        message["action"]["direction"] = direction

    # separators remove os espaços desnecessários, produzindo JSON compacto.
    json_text = json.dumps(
        message,
        ensure_ascii=False,
        separators=(",", ":")
    )

    # O protocolo utiliza uma mensagem JSON por linha.
    data = (json_text + "\n").encode("utf-8")

    client.sendall(data)

    print(f"\033[96mEnviado: {json_text}\033[0m")


def test_server_connection() -> bool:
    """
    Verifica se o socket ainda parece estar conectado.

    Em sockets TCP, não existe uma propriedade equivalente completamente
    confiável a TcpClient.Connected. Por isso, a confirmação definitiva
    acontece ao tentar receber ou enviar dados.

    Esta função verifica se o descritor do socket ainda está válido.
    """
    try:
        return client.fileno() != -1
    except OSError:
        return False


def read_available_messages() -> list[dict[str, Any]]:
    """
    Lê todas as mensagens que estiverem disponíveis no socket.

    Cada mensagem enviada pelo servidor deve estar em uma linha separada.
    Dados incompletos permanecem em receive_buffer até que o restante da linha
    seja recebido.
    """
    global receive_buffer

    messages: list[dict[str, Any]] = []

    if not test_server_connection():
        raise ConnectionError("O servidor encerrou a conexão.")

    while True:
        try:
            chunk = client.recv(65536)

            # recv() retornando bytes vazios significa que o outro lado
            # encerrou a conexão TCP.
            if chunk == b"":
                raise ConnectionError("O servidor encerrou a conexão.")

            receive_buffer += chunk.decode("utf-8")

        except BlockingIOError:
            # Não existem mais dados disponíveis neste momento.
            break

        except UnicodeDecodeError as error:
            raise ConnectionError(
                f"O servidor enviou dados UTF-8 inválidos: {error}"
            ) from error

        except OSError as error:
            raise ConnectionError(
                "A conexão com o servidor foi perdida."
            ) from error

    # Enquanto existir uma quebra de linha, existe pelo menos uma mensagem
    # completa dentro do buffer.
    while "\n" in receive_buffer:
        line, receive_buffer = receive_buffer.split("\n", 1)
        line = line.strip()

        if not line:
            continue

        try:
            message = json.loads(line)
            messages.append(message)

        except json.JSONDecodeError as error:
            print(
                "\033[91m"
                f"Mensagem JSON inválida ignorada: {error}. "
                f"Conteúdo: {line}"
                "\033[0m"
            )

    return messages


# =============================================================================
# Funções de direção e movimentação
# =============================================================================

def get_facing_direction(
    self_x: int,
    self_y: int,
    target_x: int,
    target_y: int
) -> str:
    """
    Retorna a direção principal entre o bot e o alvo.

    Se a diferença horizontal for maior ou igual à diferença vertical,
    o movimento será LEFT ou RIGHT.

    Caso contrário, será UP ou DOWN.
    """
    delta_x = target_x - self_x
    delta_y = target_y - self_y

    if abs(delta_x) >= abs(delta_y):
        if delta_x >= 0:
            return "RIGHT"

        return "LEFT"

    if delta_y >= 0:
        return "DOWN"

    return "UP"


def test_blocked_direction(
    direction: str,
    self_agent: dict[str, Any],
    level: Optional[dict[str, Any]]
) -> bool:
    """
    Verifica se uma direção vertical esbarra no topo ou na base jogável
    da fase.

    Atualmente, esta função trata apenas os limites UP e DOWN, seguindo
    exatamente a lógica do script PowerShell original.
    """
    if level is None or self_agent is None:
        return False

    top_y = int(get_value(level, "topY", 0))
    bottom_y = int(get_value(level, "bottomY", 0))

    self_y = int(get_value(self_agent, "y", 0))
    self_height = int(get_value(self_agent, "height", 0))

    if direction == "UP" and self_y <= top_y:
        return True

    if (
        direction == "DOWN"
        and (self_y + self_height) >= bottom_y
    ):
        return True

    return False


def get_clamped_direction(
    direction: str,
    self_x: int,
    target_x: int,
    self_agent: dict[str, Any],
    level: Optional[dict[str, Any]]
) -> str:
    """
    Troca UP ou DOWN por LEFT ou RIGHT quando a direção vertical estiver
    bloqueada pelo limite da fase.
    """
    blocked = test_blocked_direction(
        direction=direction,
        self_agent=self_agent,
        level=level
    )

    if not blocked:
        return direction

    if target_x >= self_x:
        return "RIGHT"

    return "LEFT"


def get_vertical_boundary_direction(
    self_agent: dict[str, Any],
    level: Optional[dict[str, Any]]
) -> Optional[str]:
    """
    Retorna uma direção para afastar o bot do limite vertical da fase.

    Se o bot estiver no topo, manda descer.
    Se estiver na base, .
    Caso contrário, retorna None.
    """
    if self_agent is None or level is None:
        return None

    top_y = int(get_value(level, "topY", 0))
    bottom_y = int(get_value(level, "bottomY", 0))

    self_y = int(get_value(self_agent, "y", 0))
    self_height = int(get_value(self_agent, "height", 0))

    if self_y <= top_y:
        return "DOWN"

    if (self_y + self_height) >= bottom_y:
        return "UP"

    return None

# =============================================================================
# Identificação do agente controlado
# =============================================================================

def get_controlled_agent(
    payload: dict[str, Any]
) -> Optional[dict[str, Any]]:
    """
    Recupera, dentro da observação, o agente que foi atribuído ao cliente.

    O gateway envia o ID estável do bot na mensagem hello ou em uma mensagem
    agent_assigned.
    """
    if controlled_agent_id is None:
        return None

    agents = get_value(payload, "agents")

    if agents is None:
        return None

    for agent in agents:
        agent_id = str(get_value(agent, "id", ""))

        if agent_id == controlled_agent_id:
            return agent

    return None


# =============================================================================
# Comportamento de exploração
# =============================================================================

def invoke_wander_behavior(
    me: dict[str, Any],
    level: Optional[dict[str, Any]]
) -> None:
    """
    Faz o bot vagar pela fase quando o protagonista estiver morto, ausente
    ou fora do alcance de percepção.

    A direção permanece ativa durante um intervalo aleatório entre 1 e
    2,5 segundos.
    """
    global next_wander_change
    global wander_direction

    now = utc_now()

    # Mantém a direção atual até chegar o momento de escolher outra.
    if now < next_wander_change:
        return

    # Primeiro verifica se o bot está encostando no limite vertical.
    boundary_direction = get_vertical_boundary_direction(
        self_agent=me,
        level=level
    )

    if boundary_direction is not None:
        wander_direction = boundary_direction

    else:
        # Remove da lista direções verticais que estejam bloqueadas.
        candidate_directions = [
            direction
            for direction in ("UP", "DOWN", "LEFT", "RIGHT")
            if not test_blocked_direction(
                direction=direction,
                self_agent=me,
                level=level
            )
        ]

        # Proteção adicional caso nenhuma direção tenha sido disponibilizada.
        if not candidate_directions:
            candidate_directions = ["LEFT", "RIGHT"]

        wander_direction = random.choice(candidate_directions)

    send_game_action(
        name="MOVE",
        direction=wander_direction
    )

    print(
        "\033[36m"
        f"Bot {role}: protagonista fora de alcance (300x300). "
        f"Vagando para {wander_direction}."
        "\033[0m"
    )

    next_wander_change = now + timedelta(
        seconds=random.uniform(1.0, 2.5)
    )


# =============================================================================
# Comportamento principal do bot
# =============================================================================

def invoke_bot_behavior(observation: dict[str, Any]) -> None:
    """
    Processa uma observação e determina o próximo comportamento do bot.

    O bot pode:

    1. Ficar inativo caso esteja morto.
    2. Vagar caso o protagonista esteja ausente ou fora de alcance.
    3. Perseguir o protagonista.
    4. Atacar quando estiver dentro do alcance corpo a corpo.
    """
    global last_attack

    payload = get_value(observation, "payload", {})

    protagonist = get_value(payload, "self")
    level = get_value(payload, "level")
    me = get_controlled_agent(payload)

    # Não executa ações se o agente ainda não tiver sido identificado
    # ou estiver morto.
    if me is None or bool(get_value(me, "dead", False)):
        return

    # Se o protagonista estiver ausente ou morto, o bot apenas vaga.
    if (
        protagonist is None
        or bool(get_value(protagonist, "dead", False))
    ):
        invoke_wander_behavior(
            me=me,
            level=level
        )
        return

    me_x = int(get_value(me, "x", 0))
    me_y = int(get_value(me, "y", 0))

    protagonist_x = int(get_value(protagonist, "x", 0))
    protagonist_y = int(get_value(protagonist, "y", 0))

    distance_x = protagonist_x - me_x
    distance_y = protagonist_y - me_y

    # Alcance de percepção: 300 pixels de distância em X
    # e 300 pixels de distância em Y.
    in_engage_range = (
        abs(distance_x) <= ENGAGE_RANGE_X
        and abs(distance_y) <= ENGAGE_RANGE_Y
    )

    if not in_engage_range:
        invoke_wander_behavior(
            me=me,
            level=level
        )
        return

    # Compara distâncias ao quadrado para evitar o cálculo de raiz quadrada.
    squared_distance = (
        distance_x * distance_x
        + distance_y * distance_y
    )

    melee_range_squared = MELEE_RANGE * MELEE_RANGE

    facing_direction = get_facing_direction(
        self_x=me_x,
        self_y=me_y,
        target_x=protagonist_x,
        target_y=protagonist_y
    )

    facing_direction = get_clamped_direction(
        direction=facing_direction,
        self_x=me_x,
        target_x=protagonist_x,
        self_agent=me,
        level=level
    )

    # Se estiver dentro da distância de ataque, respeita o cooldown
    # antes de enviar uma nova ação ATTACK.
    if squared_distance <= melee_range_squared:
        now = utc_now()

        elapsed_seconds = (now - last_attack).total_seconds()

        if elapsed_seconds >= ATTACK_COOLDOWN_SECONDS:
            send_game_action(name="ATTACK")

            print(
                "\033[91m"
                f"Bot {role}: alvo ao alcance. Ataque executado."
                "\033[0m"
            )

            last_attack = now

        return

    # Caso ainda não esteja dentro da distância de ataque,
    # continua perseguindo o protagonista.
    send_game_action(
        name="MOVE",
        direction=facing_direction
    )

    print(
        "\033[93m"
        f"Bot {role}: perseguindo protagonista em "
        f"({protagonist_x},{protagonist_y}). "
        f"Direção {facing_direction}."
        "\033[0m"
    )


# =============================================================================
# Processamento das mensagens do gateway
# =============================================================================

def process_message(
    message: dict[str, Any]
) -> Optional[dict[str, Any]]:
    """
    Processa uma mensagem recebida do gateway.

    Retorna a mensagem quando ela for uma observação.
    Para os demais tipos, retorna None.
    """
    global role
    global controlled_agent_id

    message_type = str(get_value(message, "type", ""))

    if message_type == "hello":
        role = str(get_value(message, "controls", ""))

        received_agent_id = get_value(
            message,
            "controlledAgentId"
        )

        if received_agent_id is not None:
            controlled_agent_id = str(received_agent_id)

        print(
            "\033[92m"
            f"Gateway conectado. Controle recebido: {role}. "
            f"Agente: {controlled_agent_id}."
            "\033[0m"
        )

    elif message_type == "action_ack":
        print("\033[90mACK recebido.\033[0m")

    elif message_type == "agent_dead":
        print(
            "\033[91m"
            "O bot controlado morreu. Encerrando o cliente."
            "\033[0m"
        )

        raise RuntimeError("AGENT_DEAD")

    elif message_type == "agent_assigned":
        # Mantida a estrutura do PowerShell original, que recebe o ID
        # do agente por meio da propriedade "reason".
        assigned_agent_id = get_value(message, "reason")

        if assigned_agent_id is not None:
            controlled_agent_id = str(assigned_agent_id)

        print(
            "\033[92m"
            f"Bot atribuído pelo gateway: {controlled_agent_id}."
            "\033[0m"
        )

    elif message_type == "observation":
        return message

    return None


# =============================================================================
# Loop principal
# =============================================================================

try:
    print(
        "\033[92m"
        "Conectado ao Chon Game (modo bot)."
        "\033[0m"
    )

    while True:
        if not test_server_connection():
            raise ConnectionError(
                "A conexão com o servidor foi perdida."
            )

        messages = read_available_messages()
        latest_observation: Optional[dict[str, Any]] = None

        # Se várias observações chegarem ao mesmo tempo, somente a mais
        # recente será processada.
        for message in messages:
            observation = process_message(message)

            if observation is not None:
                latest_observation = observation

        if latest_observation is not None:
            payload = get_value(
                latest_observation,
                "payload",
                {}
            )

            current_state = str(
                get_value(payload, "state", "")
            )

            # Recupera o tick da observação atual.
            tick = get_value(payload, "tick", "indisponível")

            # Localiza o agente atualmente controlado dentro da observação.
            controlled_agent = get_controlled_agent(payload)

            if controlled_agent is not None:
                bot_x = int(get_value(controlled_agent, "x", 0))
                bot_y = int(get_value(controlled_agent, "y", 0))

                position_text = f"({bot_x}, {bot_y})"
            else:
                position_text = "indisponível"

            print(
                f"Tick: {tick} | "
                f"Estado: {current_state} | "
                f"Controle: {role} | "
                f"Posição: {position_text}"
            )

            # O bot não navega pelos menus.
            # Apenas o cliente do protagonista deve confirmar os menus.
            if current_state == "PlayableState":
                invoke_bot_behavior(latest_observation)

        time.sleep(MAIN_LOOP_INTERVAL_SECONDS)

except KeyboardInterrupt:
    print(
        "\n\033[93m"
        "Execução interrompida pelo usuário."
        "\033[0m"
    )

except Exception as error:
    print(
        "\033[91m"
        f"Erro: {error}"
        "\033[0m"
    )

finally:
    try:
        client.shutdown(socket.SHUT_RDWR)
    except OSError:
        # O socket pode já estar desconectado.
        pass

    client.close()

    print("Conexão encerrada.")