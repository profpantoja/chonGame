package chon.group.game.drawer;

import java.util.List;

import chon.group.game.domain.agent.Agent;
import javafx.scene.image.Image;

public interface EnvironmentDrawer {

    void clearEnvironment();
    void drawBackground();
    void drawAgents();
    void drawLifeBar();
    void drawStatusPanel();
    void drawPauseScreen();

    int getEnvironmentWidth();
    int getEnvironmentHeight();
    int getEnvironmentPosX();
    int getEnvironmentPosY();
    Image getEnvironmentImage();
    List<Agent> getAgents();
    Agent getProtagonist();
    Image getPauseImage();

}