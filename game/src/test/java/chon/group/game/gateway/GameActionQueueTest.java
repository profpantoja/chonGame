package chon.group.game.gateway;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GameActionQueueTest {

    @Test
    public void movementUpdatesCoalesceOnlyWithinTheSameSlot() {
        GameActionQueue queue = new GameActionQueue();
        GameAction allyOneOldMove = action("agent-1", "ally_1", "LEFT");
        GameAction allyTwoMove = action("agent-2", "ally_2", "UP");
        GameAction allyOneNewMove = action("agent-1", "ally_1", "RIGHT");

        assertTrue(queue.offer(allyOneOldMove));
        assertTrue(queue.offer(allyTwoMove));
        assertTrue(queue.offer(allyOneNewMove));

        assertEquals(allyTwoMove, queue.poll());
        assertEquals(allyOneNewMove, queue.poll());
        assertNull(queue.poll());
    }

    private GameAction action(String agentId, String slotId, String direction) {
        return new GameAction("request", agentId, slotId, -1, "MOVE", direction);
    }
}