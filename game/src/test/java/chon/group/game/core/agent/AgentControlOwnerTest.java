package chon.group.game.core.agent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AgentControlOwnerTest {

    @Test
    public void ownershipDistinguishesMasFromLocalAndAi() {
        Agent agent = new Agent(0, 0, 90, 80, 0.4, 1, 100,
                Direction.IDLE, false, false);

        assertEquals(AgentControlOwner.AI, agent.getControlOwner());
        assertFalse(agent.isExternallyControlled());

        agent.setControlOwner(AgentControlOwner.LOCAL);
        assertFalse(agent.isExternallyControlled());

        agent.setExternallyControlled(true);
        assertEquals(AgentControlOwner.MAS, agent.getControlOwner());
        assertTrue(agent.isExternallyControlled());
    }
}