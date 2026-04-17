package chon.group.game.core.environment.behavior;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;

public class BeatThemUp extends BaseBehavior {

    /**
     * Checks if an agent and an object intersect (collide) based on a direction.
     * 
     * In beat'em Up games, it adjusts the agent position.
     *
     * @param a the agent
     * @param b the object
     *
     */
    @Override
    protected void onCollision(Agent agent, Object object) {
        if (!this.intersect(agent, object)) {
            return;
        }
        int agentLeft = agent.getPosX();
        int agentRight = agent.getPosX() + agent.getHitbox().getWidth();
        int agentTop = agent.getPosY();
        int agentBottom = agent.getPosY() + agent.getHitbox().getHeight();

        int objectLeft = object.getPosX();
        int objectRight = object.getPosX() + object.getHitbox().getWidth();
        int objectTop = object.getPosY();
        int objectBottom = object.getPosY() + object.getHitbox().getHeight();

        int overlapLeft = agentRight - objectLeft;
        int overlapRight = objectRight - agentLeft;
        int overlapTop = agentBottom - objectTop;
        int overlapBottom = objectBottom - agentTop;

        int minOverlapX = Math.min(overlapLeft, overlapRight);
        int minOverlapY = Math.min(overlapTop, overlapBottom);

        if (minOverlapX < minOverlapY) {
            if (overlapLeft < overlapRight) {
                agent.setPosX(objectLeft - agent.getHitbox().getWidth() - 1);
            } else {
                agent.setPosX(objectRight + 1);
            }
        } else {
            if (overlapTop < overlapBottom) {
                agent.setPosY(objectTop - agent.getHitbox().getHeight() - 1);
            } else {
                agent.setPosY(objectBottom + 1);
            }
        }
    }

}
