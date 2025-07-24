package chon.group.game.drawer;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Object;
import chon.group.game.core.weapon.Shot;
import chon.group.game.core.environment.Environment;
import chon.group.game.messaging.Message;
import chon.group.game.ui.Menu;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class JavaFxMediator implements EnvironmentDrawer {

    private GraphicsContext gc;
    private Environment environment;
    private Menu mainMenu;

    public JavaFxMediator(Environment environment, GraphicsContext gc, Menu mainMenu) {
        this.environment = environment;
        this.gc = gc;
        this.mainMenu = mainMenu;
    }

    @Override
    public void drawMainMenu(Menu menu) {
        try {
            gc.setFill(new Color(0.1, 0.1, 0.1, 1));
            gc.fillRect(0, 0, environment.getCanvasWidth(), environment.getCanvasHeight());
            gc.setTextAlign(TextAlignment.CENTER);
            
            // TÍTULO ALTERADO PARA "THE MAGE"
            gc.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
            gc.setFill(Color.WHITE);
            gc.fillText("The Mage", environment.getCanvasWidth() / 2.0, environment.getCanvasHeight() / 4.0);

            gc.setFont(Font.font("Verdana", FontWeight.NORMAL, 30));
            double startY = environment.getCanvasHeight() / 2.0;
            double lineSpacing = 60;

            for (int i = 0; i < menu.getOptions().size(); i++) {
                if (i == menu.getSelectedOptionIndex()) {
                    gc.setFill(Color.YELLOW);
                } else {
                    gc.setFill(Color.WHITE);
                }
                gc.fillText(menu.getOptions().get(i), environment.getCanvasWidth() / 2.0, startY + (i * lineSpacing));
            }
            
            gc.setTextAlign(TextAlignment.LEFT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- MÉTODOS RESTANTES (sem alteração) ---
    public void setEnvironment(Environment e) { this.environment = e; }
    public void setMenu(Menu m) { this.mainMenu = m; }
    @Override
    public void renderGame() { clearEnvironment(); drawBackground(); drawAgents(); drawObjects(); drawShots(); drawPanel(); drawMessages(); }
    @Override
    public void clearEnvironment() { gc.clearRect(0, 0, environment.getCanvasWidth(), environment.getCanvasHeight()); }
    @Override
    public void drawBackground() { if (environment.getCurrentLevel() == null) return; Image bgImage = environment.getCurrentLevel().getImage(); if (bgImage != null) { gc.drawImage(bgImage, -environment.getScrollX(), 0); } }
    @Override
    public void drawAgents() { if(environment.getCurrentLevel()==null || environment.getProtagonist() == null) return; double scrollX = environment.getScrollX(); for (Agent agent : environment.getCurrentLevel().getAgents()) { if(agent.getImage() != null) gc.drawImage(agent.getImage(), agent.getX() - scrollX, agent.getY(), agent.getWidth(), agent.getHeight()); } if(environment.getProtagonist().getImage()!=null) gc.drawImage(environment.getProtagonist().getImage(), environment.getProtagonist().getX() - scrollX, environment.getProtagonist().getY(), environment.getProtagonist().getWidth(), environment.getProtagonist().getHeight()); }
    @Override
    public void drawObjects() { if (environment.getCurrentLevel() == null) return; double scrollX = environment.getScrollX(); for (Object obj : environment.getCurrentLevel().getObjects()) { if (obj.getImage() != null && !obj.isCollectible()) { gc.drawImage(obj.getImage(), obj.getX() - scrollX, obj.getY(), obj.getWidth(), obj.getHeight()); } } }
    @Override
    public void drawShots() { if (environment.getCurrentLevel() == null) return; double scrollX = environment.getScrollX(); for (Shot shot : environment.getCurrentLevel().getShots()) { if (shot.getImage() != null) { gc.drawImage(shot.getImage(), shot.getX() - scrollX, shot.getY(), shot.getWidth(), shot.getHeight()); } } }
    @Override
    public void drawPanel() { if (environment.getProtagonist() == null) return; gc.setFill(Color.WHITE); gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20)); gc.fillText("Life: " + environment.getProtagonist().getHealth(), 20, 30); gc.fillText("Energy: " + String.format("%.0f", environment.getProtagonist().getEnergy() * 100), 20, 60); gc.fillText("Score: " + environment.getScore(), 20, 90); }
    @Override
    public void drawPauseScreen() { Image img = environment.getPauseImage(); if (img != null) { gc.drawImage(img, (environment.getCanvasWidth() - img.getWidth()) / 2, (environment.getCanvasHeight() - img.getHeight()) / 2); } }
    @Override
    public void drawGameOver() { Image img = environment.getGameOverImage(); if (img != null) { gc.drawImage(img, (environment.getCanvasWidth() - img.getWidth()) / 2, (environment.getCanvasHeight() - img.getHeight()) / 2); } }
    @Override
    public void drawWinScreen() { gc.setFill(new Color(0.1, 0.5, 0.1, 0.9)); gc.fillRect(0, 0, environment.getCanvasWidth(), environment.getCanvasHeight()); gc.setFill(Color.WHITE); gc.setFont(Font.font("Verdana", FontWeight.BOLD, 64)); gc.setTextAlign(TextAlignment.CENTER); gc.fillText("YOU WIN!", environment.getCanvasWidth() / 2.0, environment.getCanvasHeight() / 2.0); gc.setTextAlign(TextAlignment.LEFT); }
    @Override
    public void drawMessages() {}
    @Override
    public void drawSingleLifeBar() {}
    @Override
    public void drawSingleEnergyBar() {}
    @Override
    public void drawDebugPanel() {}
}