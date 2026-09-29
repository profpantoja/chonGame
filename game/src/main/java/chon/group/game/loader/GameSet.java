package chon.group.game.loader;

import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Panel;
import chon.group.game.core.platform.config.ControlType;
import chon.group.game.gateway.GameGateway;
import chon.group.game.menu.MenuHandler;

public class GameSet {

        private int canvasWidth;
        private int canvasHeight;
        private Environment environment;
        private MenuHandler menu;
        private Panel panel;
        private ControlType control;
        private int apiPort;
        private GameGateway gateway;

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

        public ControlType getControl() {
                return control;
        }

        public void setControl(ControlType control) {
                this.control = control;
        }
        
        public int getApiPort() {
                return apiPort;
        }

        public void setApiPort(int apiPort) {
                this.apiPort = apiPort;
        }

        public GameGateway getGateway() {
                return gateway;
        }

        public void setGameGateway(GameGateway gateway) {
                this.gateway = gateway;
        }

        private void load() {
                GameLoader loader = new GameLoader("/game.json");
                this.menu = loader.createMenuHandler();
                this.environment = loader.createEnvironment();
                /* Define some size properties for both Canvas and Environment */
                this.canvasWidth = loader.getDisplayWidth();
                this.canvasHeight = loader.getDisplayHeight();
                /* Define the Joystick Type for controlling the game */
                this.control = loader.getPlatform().getControl();
                this.apiPort = loader.getPlatform().getApi().getPort();
        }

}