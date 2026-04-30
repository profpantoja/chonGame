package chon.group.game.loader;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;
import chon.group.game.core.environment.Panel;
import chon.group.game.menu.MenuHandler;

public class GameSet {

        private int canvasWidth;
        private int canvasHeight;
        private Environment environment;
        private MenuHandler menu;
        private Panel panel;

        public GameSet() {
                this.load();
        }

        public int getCanvasWidth() {
                return canvasWidth;
        }

        public void setCanvasWidth(int canvasWidth) {
                this.canvasWidth = canvasWidth;
        }

        public int getCanvasHeight() {
                return canvasHeight;
        }

        public void setCanvasHeight(int canvasHeight) {
                this.canvasHeight = canvasHeight;
        }

        public Environment getEnvironment() {
                return environment;
        }

        public void setEnvironment(Environment environment) {
                this.environment = environment;
        }

        public MenuHandler getMenu() {
                return menu;
        }

        public void setMenu(MenuHandler menu) {
                this.menu = menu;
        }

        public Panel getPanel() {
                return panel;
        }

        public void setPanel(Panel panel) {
                this.panel = panel;
        }

        private void load() {
                /* Just testing the loader. It needs to be removed. */
                GameLoader loader = new GameLoader("/game.json");
                Agent protagonist = loader.createProtagonist();
                this.menu = loader.createMenuHandler();
                /* Initialize levels */
                Level level0 = loader.createLevel(0);
                Level level1 = loader.createLevel(1);
                Level level2 = loader.createLevel(2);
                Level level3 = loader.createLevel(3);
                Level level4 = loader.createLevel(4);
                Level level5 = loader.createLevel(5);
                Level level6 = loader.createLevel(6);
                Level level7 = loader.createLevel(7);

                /* Define some size properties for both Canvas and Environment */
                this.canvasWidth = 1280;
                this.canvasHeight = 780;

                /** Define a general panel for life, energy, points, and objects. */
                panel = new Panel(
                                240,
                                110);

                /* Initialize the game environment, agents and weapons */
                environment = new Environment(
                                level1.getWidth(),
                                this.canvasWidth,
                                panel);

                environment.setProtagonist(protagonist);
                environment.setPauseImage("/images/environment/pause.png");
                environment.setGameOverImage("/images/environment/gameover.png");
                environment.setTheEndImage("/images/environment/theEnd.png");

                environment.getLevels().add(level0);
                environment.getLevels().add(level1);
                environment.getLevels().add(level2);
                environment.getLevels().add(level3);
                environment.getLevels().add(level4);
                environment.getLevels().add(level5);
                environment.getLevels().add(level6);
                environment.getLevels().add(level7);
                environment.setCurrentLevel(level0);
        }

}