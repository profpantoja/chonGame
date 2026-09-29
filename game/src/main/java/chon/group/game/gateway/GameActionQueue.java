package chon.group.game.gateway;

import java.util.concurrent.ConcurrentLinkedQueue;

/** Bridges network threads and the game thread without sharing mutable state. */
public class GameActionQueue {

    private static final int MAX_SIZE = 128;
    private final ConcurrentLinkedQueue<GameAction> actions = new ConcurrentLinkedQueue<>();

    public synchronized boolean offer(GameAction action) {
        if ("MOVE".equalsIgnoreCase(action.name())) {
            actions.removeIf(existing -> "MOVE".equalsIgnoreCase(existing.name()));
        }
        if (actions.size() >= MAX_SIZE) {
            return false;
        }
        return actions.offer(action);
    }

    public synchronized GameAction poll() {
        return actions.poll();
    }
}