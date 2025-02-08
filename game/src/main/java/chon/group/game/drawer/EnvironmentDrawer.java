package chon.group.game.drawer;

public interface EnvironmentDrawer {

    void clearEnvironment();

    void drawBackground();

    void drawAgents();

    void drawLifeBar();

    void drawStatusPanel();

    void drawPauseScreen();

    void drawGameOverScreen(); // New method added (Desenhar Game Over)

    void drawScorePanel(); // Novo método adicionado (Desenhar painel de pontuação)

}