package chon.group.game.drawer;

import chon.group.game.ui.Menu;

public interface EnvironmentDrawer {
    void renderGame();
    void clearEnvironment();
    void drawBackground();
    void drawAgents();
    void drawObjects();
    void drawSingleLifeBar();
    void drawSingleEnergyBar();
    void drawPanel();
    void drawDebugPanel();
    void drawPauseScreen();
    void drawMessages();
    void drawShots();
    void drawGameOver();
    void drawMainMenu(Menu menu);
    void drawLevelClearScreen();
    void drawBossWarningScreen(); // NOVO MÉTODO
    void drawWinScreen();
}