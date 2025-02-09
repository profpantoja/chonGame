package chon.group.game.drawer;

import chon.group.game.domain.Projectile.Projectile;

public interface EnvironmentDrawer {

    void clearEnvironment();

    void drawBackground();

    void drawProjectile(Projectile projectile);

    void drawVictoryScreen();

    void drawGameOverScreen();

    void drawAgents();

    void drawLifeBar();

    void drawStatusPanel();

    void drawPauseScreen();

}