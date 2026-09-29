package chon.group.game.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import chon.group.game.Game;
import chon.group.game.core.environment.Environment;
import chon.group.game.joystick.GameCommand;
import chon.group.game.joystick.client.ExternalJoystick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TCP/JSON gateway for agents running outside the game JVM.
 *
 * <p>
 * Every connecting client is given its own {@link ExternalAgentController}: the
 * first connection controls the protagonist, and each subsequent connection
 * controls the next available agent from the current level (slot {@code n}
 * maps to {@code Level.getAgents().get(n - 1)}).
 * </p>
 */
public class GameGateway implements AutoCloseable {

    private final int requestedPort;
    private final ExternalJoystick protagonistJoystick;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ExecutorService clients = Executors.newCachedThreadPool();
    private final CopyOnWriteArrayList<ClientSession> connectedClients = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<ExternalAgentController> controllers = new CopyOnWriteArrayList<>();
    private final ConcurrentLinkedQueue<ExternalAgentController> pendingBotControllers = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<ExternalAgentController, ClientSession> sessions = new ConcurrentHashMap<>();
    private final AtomicBoolean protagonistClaimed = new AtomicBoolean(false);
    private final AtomicInteger nextBotSlot = new AtomicInteger(1);
    private volatile boolean running;
    private ServerSocket serverSocket;
    private volatile String latestObservation;
    private volatile GameSnapshotBuilder.GameSnapshot latestSnapshot;

    public GameGateway(int port, ExternalJoystick protagonistJoystick) {
        this.requestedPort = port;
        this.protagonistJoystick = protagonistJoystick;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(requestedPort);
        running = true;
        Thread acceptor = new Thread(this::acceptClients, "chon-game-gateway");
        acceptor.setDaemon(true);
        acceptor.start();
    }

    public int getPort() {
        return serverSocket == null ? requestedPort : serverSocket.getLocalPort();
    }

    public void publish(GameSnapshotBuilder.GameSnapshot snapshot) {
        String message;
        try {
            message = mapper.writeValueAsString(new ObservationMessage(snapshot));
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }
        latestSnapshot = snapshot;
        for (ClientSession client : connectedClients) {
            client.offerObservation(message);
        }
        latestObservation = message;
    }

    private void acceptClients() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                clients.submit(() -> handleClient(socket));
            } catch (IOException exception) {
                if (running) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private void handleClient(Socket socket) {
        ClientSession client = null;
        ExternalAgentController control = assignController();
        controllers.add(control);
        if (!control.isProtagonist()) {
            pendingBotControllers.offer(control);
        }
        try (socket;
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
            client = new ClientSession(socket, writer);
            connectedClients.add(client);
            sessions.put(control, client);
            client.sendControl(helloMessage(control));
            if (latestObservation != null) {
                client.offerObservation(latestObservation);
            }
            clients.submit(client::writeLatestObservation);
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    JsonNode message = mapper.readTree(line);
                    if ("action".equals(message.path("type").asText())) {
                        String requestId = message.path("requestId").asText("");
                        String validationError = validateAction(message);
                        if (validationError != null) {
                            client.sendActionAck(requestId, false, validationError);
                            continue;
                        }
                        if (!control.getActionQueue().offer(toGameAction(message))) {
                            client.sendActionAck(requestId, false, "ACTION_QUEUE_FULL");
                            continue;
                        }
                        client.sendActionAck(requestId, true, null);
                    } else {
                        client.sendControl("{\"type\":\"error\",\"message\":\"Unsupported message type\"}");
                    }
                } catch (IOException exception) {
                    client.sendControl("{\"type\":\"error\",\"message\":\"Invalid JSON\"}");
                }
            }
        } catch (SocketException exception) {
            if (running && !"Connection reset".equals(exception.getMessage())) {
                exception.printStackTrace();
            }
        } catch (IOException exception) {
            if (running) {
                exception.printStackTrace();
            }
        } finally {
            if (client != null) {
                connectedClients.remove(client);
                sessions.remove(control);
                client.close();
            }
            /* The game thread finishes releasing the bound agent and frees the slot. */
            control.close();
        }
    }

    /** Assigns the protagonist first, then the nearest unassigned living bot. */
    private synchronized ExternalAgentController assignController() {
        if (protagonistClaimed.compareAndSet(false, true)) {
            return new ExternalAgentController(0, protagonistJoystick, null);
        }
        int slot = nextBotSlot.getAndIncrement();
        return new ExternalAgentController(slot, new ExternalJoystick(), null);
    }

    private String findNearestAvailableAgentId() {
        if (latestSnapshot == null || latestSnapshot.self() == null) {
            return null;
        }

        String nearestId = null;
        long nearestDistance = Long.MAX_VALUE;
        for (GameSnapshotBuilder.EntitySnapshot agent : latestSnapshot.agents()) {
            if (agent.dead() || isAgentAssigned(agent.id())) {
                continue;
            }
            int dx = agent.x() - latestSnapshot.self().x();
            int dy = agent.y() - latestSnapshot.self().y();
            long distance = (long) dx * dx + (long) dy * dy;
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestId = agent.id();
            }
        }
        return nearestId;
    }

    private boolean isAgentAssigned(String agentId) {
        return controllers.stream()
                .anyMatch(control -> agentId.equals(control.getAssignedAgentId()));
    }

    private String helloMessage(ExternalAgentController control) {
        String role = control.isProtagonist() ? "protagonist" : ("agent-" + control.getSlot());
        String agentId = control.getAssignedAgentId();
        return "{\"type\":\"hello\",\"protocolVersion\":1,\"controls\":\""
            + role + "\",\"controlledAgentId\":"
            + (agentId == null ? "null" : "\"" + agentId + "\"") + "}";
    }

    /**
     * Applies queued actions to each client's own joystick. Must only be called
     * from the game thread.
     */
    public void processPendingActions(long currentTick) {
        for (ExternalAgentController control : controllers) {
            GameActionQueue queue = control.getActionQueue();
            GameAction action;
            while ((action = queue.poll()) != null) {
                if (action.expectedTick() >= 0 && action.expectedTick() > currentTick) {
                    queue.offer(action);
                    break;
                }
                applyExternalAction(control.getJoystick(), action);
            }
        }
    }

    /**
     * Moves every bot bound to a connected client and reclaims controllers whose
     * client disconnected. Must only be called from the game thread.
     */
    public void updateControlledAgents(Game game) {
        Environment environment = game.getEnvironment();
        if (environment.getCurrentLevel() != null) {
            environment.getCurrentLevel().getAgents().forEach(agent -> {
                if (!agent.isDead()) {
                    agent.setExternallyControlled(true);
                    agent.idle();
                }
            });
        }
        assignPendingBots();
        for (ExternalAgentController control : controllers) {
            if (control.isClosed()) {
                closeController(control);
                continue;
            }
            if (control.update(environment)) {
                closeController(control);
            }
        }
    }

    private void assignPendingBots() {
        while (true) {
            ExternalAgentController control = pendingBotControllers.peek();
            if (control == null) {
                return;
            }
            if (control.isClosed()) {
                pendingBotControllers.poll();
                continue;
            }

            String assignedAgentId = findNearestAvailableAgentId();
            if (assignedAgentId == null) {
                return;
            }

            pendingBotControllers.poll();
            control.assignAgent(assignedAgentId);
            ClientSession session = sessions.get(control);
            if (session != null) {
                session.sendEvent("agent_assigned", assignedAgentId);
            }
        }
    }

    private void closeController(ExternalAgentController control) {
        if (control.isProtagonist()) {
            control.getJoystick().clear();
            ClientSession session = sessions.get(control);
            if (session != null) {
                session.sendEvent("game_over", "GAME_OVER");
            }
            return;
        } else {
            control.release();
        }

        ClientSession session = sessions.remove(control);
        if (session != null) {
            session.sendEvent("agent_dead", "AGENT_DEAD");
            session.closeConnection();
        }
        controllers.remove(control);
    }

    private String validateAction(JsonNode message) {
        JsonNode action = message.path("action");
        String name = action.path("name").asText("").toUpperCase();
        if (!java.util.Set.of("MOVE", "ATTACK", "CONFIRM", "PAUSE",
                "MENU_UP", "MENU_DOWN", "MENU_LEFT", "MENU_RIGHT",
                "UP", "DOWN", "LEFT", "RIGHT").contains(name)) {
            return "UNKNOWN_ACTION";
        }
        if ("MOVE".equals(name)) {
            String direction = action.path("direction").asText("").toUpperCase();
            if (!java.util.Set.of("UP", "DOWN", "LEFT", "RIGHT").contains(direction)) {
                return "INVALID_DIRECTION";
            }
        }
        return null;
    }

    private GameAction toGameAction(JsonNode message) {
        JsonNode action = message.path("action");
        return new GameAction(
                message.path("requestId").asText(""),
                message.path("agentId").asText(""),
                message.path("expectedTick").asLong(-1),
                action.path("name").asText(""),
                action.path("direction").asText(null));
    }

    private void applyExternalAction(ExternalJoystick joystick, GameAction action) {
        String name = action.name().toUpperCase();
        switch (name) {
            case "MOVE" -> applyExternalMovement(joystick, action.direction());
            case "ATTACK" -> joystick.press(GameCommand.ATTACK);
            case "CONFIRM" -> joystick.press(GameCommand.CONFIRM);
            case "PAUSE" -> joystick.press(GameCommand.PAUSE);
            case "MENU_UP", "UP" -> joystick.press(GameCommand.UP);
            case "MENU_DOWN", "DOWN" -> joystick.press(GameCommand.DOWN);
            case "MENU_LEFT", "LEFT" -> joystick.press(GameCommand.LEFT);
            case "MENU_RIGHT", "RIGHT" -> joystick.press(GameCommand.RIGHT);
            default -> {
            }
        }
    }

    private void applyExternalMovement(ExternalJoystick joystick, String direction) {
        if (direction == null) {
            return;
        }
        try {
            GameCommand command = GameCommand.valueOf(direction.toUpperCase());
            joystick.hold(command);
            releaseOtherDirections(joystick, command);
        } catch (IllegalArgumentException exception) {
            // Invalid actions are ignored by the external joystick.
        }
    }

    private void releaseOtherDirections(ExternalJoystick joystick, GameCommand activeCommand) {
        for (GameCommand direction : new GameCommand[] {
                GameCommand.UP, GameCommand.DOWN, GameCommand.LEFT, GameCommand.RIGHT }) {
            if (direction != activeCommand) {
                joystick.release(direction);
            }
        }
    }

    private record ObservationMessage(String type, int protocolVersion,
            GameSnapshotBuilder.GameSnapshot payload) {
        private ObservationMessage(GameSnapshotBuilder.GameSnapshot payload) {
            this("observation", 1, payload);
        }
    }

    private static class ClientSession {
        private final Socket socket;
        private final BufferedWriter writer;
        private final Object observationMonitor = new Object();
        private volatile String latestObservation;
        private volatile boolean closed;

        private ClientSession(Socket socket, BufferedWriter writer) {
            this.socket = socket;
            this.writer = writer;
        }

        private void sendActionAck(String requestId, boolean accepted, String reason) throws IOException {
            String message = "{\"type\":\"action_ack\",\"requestId\":\""
                    + requestId + "\",\"accepted\":" + accepted;
            if (reason != null) {
                message += ",\"reason\":\"" + reason + "\"";
            }
            sendControl(message + "}");
        }

        private void sendEvent(String type, String reason) {
            try {
                sendControl("{\"type\":\"" + type + "\",\"reason\":\"" + reason + "\"}");
            } catch (IOException exception) {
                // The session is already being closed.
            }
        }

        private void sendControl(String message) throws IOException {
            synchronized (writer) {
                writer.write(message);
                writer.write('\n');
                writer.flush();
            }
        }

        private void offerObservation(String message) {
            synchronized (observationMonitor) {
                latestObservation = message;
                observationMonitor.notifyAll();
            }
        }

        private void writeLatestObservation() {
            try {
                while (!closed) {
                    String message;
                    synchronized (observationMonitor) {
                        while (!closed && latestObservation == null) {
                            observationMonitor.wait();
                        }
                        if (closed) {
                            return;
                        }
                        message = latestObservation;
                        latestObservation = null;
                    }
                    synchronized (writer) {
                        writer.write(message);
                        writer.write('\n');
                        writer.flush();
                    }
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            } catch (IOException exception) {
                // The reader closes the session when the remote client disconnects.
            }
        }

        private void close() {
            closed = true;
            synchronized (observationMonitor) {
                observationMonitor.notifyAll();
            }
        }

        private void closeConnection() {
            close();
            try {
                socket.close();
            } catch (IOException exception) {
                // The connection is already closed.
            }
        }
    }

    @Override
    public void close() throws IOException {
        running = false;
        clients.close();
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}