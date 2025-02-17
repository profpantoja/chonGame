package chon.group.enviroment;

import chon.group.agent.Agent;

public class CollisionDetector {

    public static boolean checkCollision(Agent agent1, Agent agent2) {

        int x1 = agent1.getPositionX();
        int y1 = agent1.getPositionY();
        int width1 = agent1.getWidth();
        int height1 = agent1.getHeight();

        int x2 = agent2.getPositionX();
        int y2 = agent2.getPositionY();
        int width2 = agent2.getWidth();
        int height2 = agent2.getHeight();

        return (x1 < x2 + width2 && x1 + width1 > x2 && y1 < y2 + height2 && y1 + height1 > y2);
    }
}
