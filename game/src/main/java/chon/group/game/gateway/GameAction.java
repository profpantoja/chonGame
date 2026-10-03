package chon.group.game.gateway;

/** A command received from an external agent. */
public record GameAction(
        String requestId,
        String agentId,
                String slotId,
        long expectedTick,
        String name,
                String direction) {

        public GameAction(
                        String requestId,
                        String agentId,
                        long expectedTick,
                        String name,
                        String direction) {
                this(requestId, agentId, agentId, expectedTick, name, direction);
        }
}