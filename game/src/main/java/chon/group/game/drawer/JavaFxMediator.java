package chon.group.game.drawer;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.weapon.ExplosionEffect;
import chon.group.game.core.weapon.Shot;
import chon.group.game.ui.Menu;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class JavaFxMediator implements EnvironmentDrawer {

    private GraphicsContext gc;
    private Environment environment;

    public JavaFxMediator(Environment environment, GraphicsContext gc, Menu mainMenu) { this.environment = environment; this.gc = gc; }
    public void setEnvironment(Environment e) { this.environment = e; }
    public void setMenu(Menu m) { }
    
    @Override
    public void renderGame() { clearEnvironment(); drawBackground(); drawObjects(); drawAgents(); drawShots(); drawAreaEffects(); drawPanel(); drawMessages(); }
    
    @Override
    public void drawBossWarningScreen() {
        renderGame(); 
        gc.setFill(new Color(0, 0, 0, 0.7));
        gc.fillRect(0, 0, environment.getCanvasWidth(), environment.getCanvasHeight());
        Image warningImage = environment.getBossWarningImage();
        if (warningImage != null) {
            double x = (environment.getCanvasWidth() - warningImage.getWidth()) / 2.0;
            double y = (environment.getCanvasHeight() - warningImage.getHeight()) / 2.0;
            gc.drawImage(warningImage, x, y);
        }
    }

    @Override
    public void drawAgents() {
        if (environment.getCurrentLevel() == null || environment.getProtagonist() == null) return;
        double scrollX = environment.getScrollX();
        
        List<Agent> allAgents = new ArrayList<>(environment.getCurrentLevel().getAgents());
        allAgents.add(environment.getProtagonist());

        for (Agent agent : allAgents) {
            if (agent == null) continue;
            Image frame = agent.getImage();
            if (frame == null) continue;

            double x = agent.getX() - scrollX;
            double y = agent.getY();
            double w = agent.getWidth();
            double h = agent.getHeight();

            if (agent.isFlipped()) {
                gc.drawImage(frame, x + w, y, -w, h);
            } else {
                gc.drawImage(frame, x, y, w, h);
            }
        }
    }

    public void drawAreaEffects() { if (environment.getAreaEffects() == null) return; double scrollX = environment.getScrollX(); for (ExplosionEffect effect : environment.getAreaEffects()) { if (effect.getImage() != null) { gc.drawImage(effect.getImage(), effect.getX() - scrollX, effect.getY(), effect.getWidth(), effect.getHeight()); } } }
    
    @Override
    public void drawMainMenu(Menu menu) {
        Image menuImage = menu.getCurrentImage();
        
        // --- INÍCIO DO DEBUG ---
        gc.setFill(Color.BLUEVIOLET); // Uma cor bem distinta
        gc.fillRect(0, 0, environment.getCanvasWidth(), environment.getCanvasHeight());

        if (menuImage != null) {
            // 2. Imprime as dimensões da imagem carregada. Se for 0.0, a imagem está com problema.
            System.out.println("[DEBUG MEDIATOR] Desenhando imagem. Largura: " + menuImage.getWidth() + ", Altura: " + menuImage.getHeight());
            
            // 3. Tenta desenhar a imagem do menu.
            gc.drawImage(menuImage, 0, 0, environment.getCanvasWidth(), environment.getCanvasHeight());
        } else {
            System.out.println("[DEBUG MEDIATOR] Imagem do menu é NULA.");
            gc.setFill(Color.RED);
            gc.fillRect(0, 0, environment.getCanvasWidth(), environment.getCanvasHeight());
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("ERRO: IMAGEM DO MENU NULA", environment.getCanvasWidth() / 2.0, environment.getCanvasHeight() / 2.0);
        }
        // --- FIM DO DEBUG ---
    }
    
    @Override
    public void drawPauseScreen() { Image pauseImage = environment.getCurrentPauseImage(); if (pauseImage != null) { gc.drawImage(pauseImage, 0, 0, environment.getCanvasWidth(), environment.getCanvasHeight()); } else { gc.setFill(new Color(0, 0, 0, 0.7)); gc.fillRect(0, 0, environment.getCanvasWidth(), environment.getCanvasHeight()); gc.setFill(Color.WHITE); gc.setFont(Font.font("Verdana", FontWeight.BOLD, 48)); gc.setTextAlign(TextAlignment.CENTER); gc.fillText("PAUSED", environment.getCanvasWidth() / 2.0, environment.getCanvasHeight() / 2.0); } }
    @Override
    public void drawGameOver() { Image gameOverImage = environment.getGameOverMenu().getCurrentImage(); if (gameOverImage != null) { gc.drawImage(gameOverImage, 0, 0, environment.getCanvasWidth(), environment.getCanvasHeight()); } else { gc.setFill(Color.RED); gc.fillRect(0, 0, environment.getCanvasWidth(), environment.getCanvasHeight()); gc.setFill(Color.WHITE); gc.setFont(Font.font("Verdana", FontWeight.BOLD, 48)); gc.setTextAlign(TextAlignment.CENTER); gc.fillText("GAME OVER", environment.getCanvasWidth() / 2.0, environment.getCanvasHeight() / 2.0); } }
    @Override
    public void drawLevelClearScreen() { gc.setFill(new Color(0, 0, 0, 0.5)); gc.fillRect(0, 0, environment.getCanvasWidth(), environment.getCanvasHeight()); gc.setFill(Color.WHITE); gc.setFont(Font.font("Verdana", FontWeight.BOLD, 50)); gc.setTextAlign(TextAlignment.CENTER); gc.fillText("NÍVEL CONCLUÍDO!", environment.getCanvasWidth() / 2.0, environment.getCanvasHeight() / 2.0); }
    @Override
    public void drawWinScreen() { Image winImage = environment.getWinImage(); if (winImage != null) { gc.drawImage(winImage, 0, 0, environment.getCanvasWidth(), environment.getCanvasHeight()); } else { gc.setFill(new Color(0.1, 0.5, 0.1, 0.9)); gc.fillRect(0, 0, environment.getCanvasWidth(), environment.getCanvasHeight()); gc.setFill(Color.WHITE); gc.setFont(Font.font("Verdana", FontWeight.BOLD, 64)); gc.setTextAlign(TextAlignment.CENTER); gc.fillText("VOCÊ VENCEU!", environment.getCanvasWidth() / 2.0, environment.getCanvasHeight() / 2.0); } }
    @Override
    public void clearEnvironment() { gc.clearRect(0, 0, environment.getCanvasWidth(), environment.getCanvasHeight()); }
    @Override
    public void drawBackground() { if (environment.getCurrentLevel() == null) return; Image bgImage = environment.getCurrentLevel().getImage(); if (bgImage != null) { gc.drawImage(bgImage, -environment.getScrollX(), 0, environment.getCurrentLevel().getWidth(), environment.getCanvasHeight()); } }
    @Override
    public void drawObjects() { if (environment.getCurrentLevel() == null) return; double scrollX = environment.getScrollX(); for (Object obj : environment.getCurrentLevel().getObjects()) { if (obj.getImage() != null && !obj.isCollectible()) { gc.drawImage(obj.getImage(), obj.getX() - scrollX, obj.getY(), obj.getWidth(), obj.getHeight()); } } }
    @Override
    public void drawShots() { if (environment.getCurrentLevel() == null) return; double scrollX = environment.getScrollX(); for (Shot shot : environment.getCurrentLevel().getShots()) { if (shot.getImage() != null) { gc.drawImage(shot.getImage(), shot.getX() - scrollX, shot.getY(), shot.getWidth(), shot.getHeight()); } } }
    @Override
    public void drawPanel() { if (environment.getProtagonist() == null) return; gc.setFill(Color.rgb(0, 0, 0, 0.5)); gc.fillRoundRect(10, 10, 250, 120, 15, 15); gc.setFill(Color.WHITE); gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); gc.setEffect(new DropShadow(3, Color.BLACK)); gc.fillText("Life: " + environment.getProtagonist().getHealth(), 25, 40); gc.fillText("Energy: " + String.format("%.0f", environment.getProtagonist().getEnergy() * 100), 25, 70); gc.fillText("Score: " + environment.getScore(), 25, 100); if(environment.getCurrentLevel() != null) { gc.fillText("Kills: " + environment.getCurrentLevel().getEnemiesKilledCount() + "/" + environment.getCurrentLevel().getTotalEnemiesToSpawn(), 150, 40);} gc.setEffect(null); }
    @Override
    public void drawMessages() {}
    @Override
    public void drawSingleLifeBar() {}
    @Override
    public void drawSingleEnergyBar() {}
    @Override
    public void drawDebugPanel() {}
}