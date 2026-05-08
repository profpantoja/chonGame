package chon.group.game.loader;

import chon.group.game.core.environment.Environment;
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
                GameLoader loader = new GameLoader("/game.json");
                this.menu = loader.createMenuHandler();
                this.environment = loader.createEnvironment();
                /* Define some size properties for both Canvas and Environment */
                this.canvasWidth = loader.getDisplayWidth();
                this.canvasHeight = loader.getDisplayHeight();
        }

}