package chon.group.game;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Direction;
import chon.group.game.core.agent.Object;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;
import chon.group.game.core.weapon.Panel;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.domain.weapon.Cannon;
import chon.group.game.domain.weapon.Lancer;
import chon.group.game.menu.Action;
import chon.group.game.menu.Item;
import chon.group.game.menu.Menu;
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
                /* Define some size properties for both Canvas and Environment */
                this.canvasWidth = 1280;
                this.canvasHeight = 780;

                /** Define a general panel for life, energy, points, and objects. */
                panel = new Panel(
                                240,
                                110);

                /** Define the game's menus. */
                Item startMenu1 = new Item("Start", Action.START);
                Item startMenu2 = new Item("About", Action.ENTER);
                Menu startMenu = new Menu(
                                "CHON GAME",
                                List.of(startMenu1, startMenu2),
                                400);

                Item pauseMenu1 = new Item("Back", Action.CONTINUE);
                Item pauseMenu2 = new Item("Exit", Action.RESET);
                Item pauseMenu3 = new Item("About", Action.NONE);
                Menu pauseMenu = new Menu(
                                "PAUSED",
                                List.of(pauseMenu1, pauseMenu2, pauseMenu3),
                                400);

                Item gameOverMenu1 = new Item("Continue", Action.CONTINUE);
                Item gameOverMenu2 = new Item("Exit", Action.RESET);
                Menu gaveOverMenu = new Menu(
                                "GAME OVER",
                                List.of(gameOverMenu1, gameOverMenu2),
                                400);

                this.menu = new MenuHandler(startMenu, pauseMenu, null, gaveOverMenu);

                /* Initialize the game environment, levels, agents and weapons */
                Level level0 = new Level(
                                0,
                                0,
                                canvasHeight,
                                1280,
                                "/images/environment/chonGame.png");

                Level level1 = new Level(
                                0,
                                0,
                                canvasHeight,
                                8024,
                                "/images/environment/castleLong.png");

                Level level2 = new Level(
                                0,
                                0,
                                canvasHeight,
                                8000,
                                "/images/environment/mountain.png");

                environment = new Environment(
                                this.canvasHeight,
                                level1.getWidth(),
                                this.canvasWidth,
                                panel);

                Agent chonBota = new Agent(400, 390, 90, 65, 3, 1000, Direction.IDLE, "/images/agents/chonBota.png",
                                false,
                                false);
                Weapon cannon = new Cannon(400, 390, 0, 0, 3, 0, 0.05, "", false);
                Weapon lancer = new Lancer(400, 390, 0, 0, 3, 0, 0.05, "", false);

                chonBota.setWeapon(cannon);
                chonBota.setWeapon(lancer);

                Agent chonBot1 = new Agent(920, 440, 90, 65, 1, 500, Direction.IDLE, "/images/agents/chonBot.png", true,
                                true);
                Agent chonBot2 = new Agent(2920, 640, 90, 65, 1, 500, Direction.IDLE, "/images/agents/chonBot.png",
                                true,
                                true);
                Agent chonBot3 = new Agent(4920, 300, 90, 65, 1, 500, Direction.IDLE, "/images/agents/chonBot.png",
                                true,
                                true);
                Agent chonBot4 = new Agent(6920, 500, 90, 65, 1, 500, Direction.IDLE, "/images/agents/chonBot.png",
                                true,
                                true);
                environment.setProtagonist(chonBota);
                environment.setPauseImage("/images/environment/pause.png");
                environment.setGameOverImage("/images/environment/gameover.png");

                level1.getAgents().add(chonBot1);
                level1.getAgents().add(chonBot2);
                level1.getAgents().add(chonBot3);
                level1.getAgents().add(chonBot4);

                /* Set up some collectable objects */
                List<Object> objects = new ArrayList<>();
                objects.add(new Object(
                                200,
                                350,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                "/images/agents/coin.png",
                                false,
                                false,
                                true,
                                false,
                                200));
                objects.add(new Object(
                                400,
                                380,
                                32,
                                32,
                                0,
                                1,
                                Direction.IDLE,
                                "/images/agents/coin.png",
                                false,
                                true,
                                true,
                                true, 200));
                objects.add(new Object(
                                1000,
                                600,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                "/images/agents/coin.png",
                                false,
                                false,
                                true,
                                false,
                                200));
                objects.add(new Object(
                                1200,
                                704,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false,
                                false,
                                200));
                objects.add(new Object(
                                1200,
                                650,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false,
                                true,
                                200));
                objects.add(new Object(
                                1200,
                                596,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false,
                                true,
                                200));
                objects.add(new Object(
                                1400,
                                380,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                "/images/agents/coin.png",
                                false,
                                false,
                                true,
                                false,
                                200));
                objects.add(new Object(
                                1800,
                                650,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                "/images/agents/coin.png",
                                false,
                                false,
                                true,
                                false,
                                200));
                objects.add(new Object(
                                1900,
                                350,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false,
                                true,
                                0));
                objects.add(new Object(
                                2000,
                                580,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                "/images/agents/coin.png",
                                false,
                                false,
                                true,
                                false,
                                200));
                objects.add(new Object(
                                2150,
                                550,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false,
                                true,
                                0));
                objects.add(new Object(
                                2300,
                                380,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                "/images/agents/coin.png",
                                false,
                                false,
                                true,
                                false,
                                200));
                objects.add(new Object(
                                2500,
                                680,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false,
                                true,
                                0));
                objects.add(new Object(
                                2600,
                                500,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                "/images/agents/coin.png",
                                false, false,
                                true,
                                false,
                                200));
                objects.add(new Object(
                                2700,
                                450,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false,
                                true,
                                0));
                objects.add(new Object(
                                2850,
                                650,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false,
                                true,
                                0));
                objects.add(new Object(
                                2900,
                                380,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                "/images/agents/coin.png",
                                false,
                                false,
                                true,
                                false,
                                200));
                objects.add(new Object(
                                3150,
                                450,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false,
                                true,
                                0));
                objects.add(new Object(
                                3200,
                                400,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                "/images/agents/coin.png",
                                false,
                                false,
                                true,
                                false,
                                200));
                objects.add(new Object(
                                3500,
                                350,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false,
                                true,
                                0));
                objects.add(new Object(
                                3850,
                                550,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png", false,
                                false,
                                false,
                                true,
                                0));
                objects.add(new Object(
                                4100,
                                500,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                "/images/agents/coin.png",
                                false,
                                false,
                                true,
                                false, 200));
                objects.add(new Object(
                                4350,
                                550,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png", false,
                                false,
                                false, true, 0));
                objects.add(new Object(
                                4550,
                                550,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false, true, 0));
                objects.add(new Object(
                                4650,
                                350,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false, true, 0));
                objects.add(new Object(
                                4850,
                                600,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false, true, 0));
                objects.add(new Object(
                                5000,
                                380,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                "/images/agents/coin.png",
                                false,
                                false,
                                true,
                                false,
                                200));
                objects.add(new Object(
                                5150,
                                650,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false,
                                true,
                                0));
                objects.add(new Object(
                                5300,
                                300,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false,
                                true,
                                0));
                objects.add(new Object(
                                5600,
                                550,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                "/images/objects/box.png",
                                false,
                                false,
                                false,
                                true,
                                0));
                objects.add(new Object(
                                6200,
                                400,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                "/images/agents/coin.png",
                                false,
                                false,
                                true,
                                false,
                                200));
                // Register objects into the environment and count total collectibles
                level1.setObjects(objects);
                level1.countCollectibles();
                environment.getLevels().add(level0);
                environment.getLevels().add(level1);
                environment.getLevels().add(level2);
                environment.setCurrentLevel(level0);
        }

}
