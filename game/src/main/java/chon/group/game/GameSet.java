package chon.group.game;

import java.util.ArrayList;
import java.util.List;

import chon.group.game.animation.Animation;
import chon.group.game.animation.AnimationType;
import chon.group.game.animation.Frame;
import chon.group.game.core.agent.Agent;
import chon.group.game.core.agent.Direction;
import chon.group.game.core.agent.Object;
import chon.group.game.core.environment.Environment;
import chon.group.game.core.environment.Level;
import chon.group.game.core.environment.Panel;
import chon.group.game.core.weapon.ConcreteShot;
import chon.group.game.core.weapon.ConcreteWeapon;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.menu.Action;
import chon.group.game.menu.Item;
import chon.group.game.menu.Menu;
import chon.group.game.menu.MenuHandler;
import chon.group.game.sound.Sound;
import chon.group.game.sound.SoundEvent;
import chon.group.game.sound.SoundType;
import javafx.scene.image.Image;

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
                Item settingsMenu1 = new Item("[-] Sound [+]", Action.VOLUME);
                Item settingsMenu2 = new Item("Return", Action.POP);
                Menu settingsMenu = new Menu(
                                "SETTINGS",
                                List.of(settingsMenu1, settingsMenu2),
                                0.7,
                                400);

                Item startMenu1 = new Item("Start", Action.START);
                Item startMenu2 = new Item("Settings", Action.ENTER, settingsMenu);
                Item startMenu3 = new Item("About", Action.NONE);
                Menu startMenu = new Menu(
                                "CHON GAME",
                                List.of(startMenu1, startMenu2, startMenu3),
                                0.7,
                                400);

                Item pauseMenu1 = new Item("Back", Action.CONTINUE);
                Item pauseMenu2 = new Item("Debug", Action.DEBUG);
                Item pauseMenu3 = new Item("Settings", Action.ENTER, settingsMenu);
                Item pauseMenu4 = new Item("Exit", Action.RESET);
                Menu pauseMenu = new Menu(
                                "PAUSED",
                                List.of(pauseMenu1, pauseMenu2, pauseMenu3, pauseMenu4),
                                0.63,
                                400);

                Item gameOverMenu1 = new Item("Continue", Action.CONTINUE);
                Item gameOverMenu2 = new Item("Exit", Action.RESET);
                Menu gaveOverMenu = new Menu(
                                "GAME OVER",
                                List.of(gameOverMenu1, gameOverMenu2),
                                0.7,
                                400);

                this.menu = new MenuHandler(startMenu, pauseMenu, null, gaveOverMenu);

                /* Initialize the game environment, levels, agents and weapons */
                Level level0 = new Level(
                                0,
                                0,
                                canvasHeight,
                                1280,
                                0,
                                0,
                                "/images/environment/chonGame.png");
                Animation level0Animation = new Animation();
                level0Animation.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/environment/chonGame.png")
                                                .toExternalForm()),
                                1280,
                                canvasHeight));
                level0.getAnimationSet().add(AnimationType.IDLE, level0Animation);
                level0.getAnimationState().setCurrentAnimation(level0Animation);

                Animation level1Animation = new Animation();
                Level level1 = new Level(
                                0,
                                0,
                                canvasHeight,
                                8024,
                                260,
                                canvasHeight,
                                "/images/environment/castleLong.png");
                level1Animation.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/environment/castleLong.png")
                                                .toExternalForm()),
                                8024,
                                canvasHeight));
                level1.getAnimationSet().add(AnimationType.IDLE, level1Animation);
                level1.getAnimationState().setCurrentAnimation(level1Animation);

                Animation level2Animation = new Animation();
                Level level2 = new Level(
                                0,
                                0,
                                canvasHeight,
                                8024,
                                380,
                                590,
                                "/images/environment/insideCastle.png");
                level2Animation.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/environment/insideCastle.png")
                                                .toExternalForm()),
                                8024,
                                canvasHeight));
                level2.getAnimationSet().add(AnimationType.IDLE, level2Animation);
                level2.getAnimationState().setCurrentAnimation(level2Animation);

                environment = new Environment(
                                this.canvasHeight,
                                level1.getWidth(),
                                this.canvasWidth,
                                panel);

                Agent chonBota = new Agent(
                                400,
                                390,
                                90,
                                65,
                                2,
                                5000,
                                Direction.IDLE,
                                false,
                                true);
                /* Animation frames for chonBota. It is just used for the protagonitst. */
                Animation idleChonBota = new Animation();
                idleChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/idle/chonBota_001.png")
                                                .toExternalForm()),
                                65,
                                90));
                idleChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/idle/chonBota_002.png")
                                                .toExternalForm()),
                                65,
                                90));
                idleChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/idle/chonBota_003.png")
                                                .toExternalForm()),
                                65,
                                90));
                idleChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/idle/chonBota_004.png")
                                                .toExternalForm()),
                                65,
                                90));
                idleChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/idle/chonBota_004.png")
                                                .toExternalForm()),
                                65,
                                90));
                idleChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/idle/chonBota_005.png")
                                                .toExternalForm()),
                                65,
                                90));
                idleChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/idle/chonBota_005.png")
                                                .toExternalForm()),
                                65,
                                90));
                idleChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/idle/chonBota_006.png")
                                                .toExternalForm()),
                                65,
                                90));

                Animation hoveringChonBota = new Animation(150, true);
                hoveringChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/walk/chonBota_Walk_001.png")
                                                .toExternalForm()),
                                65,
                                90));
                hoveringChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/walk/chonBota_Walk_002.png")
                                                .toExternalForm()),
                                65,
                                90));
                hoveringChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/walk/chonBota_Walk_003.png")
                                                .toExternalForm()),
                                65,
                                90));
                hoveringChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/walk/chonBota_Walk_004.png")
                                                .toExternalForm()),
                                65,
                                90));
                hoveringChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/walk/chonBota_Walk_005.png")
                                                .toExternalForm()),
                                65,
                                90));
                hoveringChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/walk/chonBota_Walk_006.png")
                                                .toExternalForm()),
                                65,
                                90));
                hoveringChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/walk/chonBota_Walk_007.png")
                                                .toExternalForm()),
                                65,
                                90));
                hoveringChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/walk/chonBota_Walk_008.png")
                                                .toExternalForm()),
                                65,
                                90));
                hoveringChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/walk/chonBota_Walk_009.png")
                                                .toExternalForm()),
                                65,
                                90));
                hoveringChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/walk/chonBota_Walk_010.png")
                                                .toExternalForm()),
                                65,
                                90));

                Animation shootingChonBota = new Animation(100, false);
                shootingChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/attack/chonBota_Attack_001.png")
                                                .toExternalForm()),
                                65,
                                90));

                Animation hitChonBota = new Animation(200, false);
                hitChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/hit/chonBota_Hit_001.png")
                                                .toExternalForm()),
                                65,
                                90));

                Animation deadChonBota = new Animation(100, false);
                deadChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/dead/chonBota_Dead_001.png")
                                                .toExternalForm()),
                                65,
                                90));
                deadChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/dead/chonBota_Dead_002.png")
                                                .toExternalForm()),
                                65,
                                90));
                deadChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/dead/chonBota_Dead_003.png")
                                                .toExternalForm()),
                                65,
                                90));
                deadChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/dead/chonBota_Dead_004.png")
                                                .toExternalForm()),
                                65,
                                90));
                deadChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/dead/chonBota_Dead_005.png")
                                                .toExternalForm()),
                                65,
                                90));
                deadChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/dead/chonBota_Dead_006.png")
                                                .toExternalForm()),
                                65,
                                90));
                deadChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/dead/chonBota_Dead_007.png")
                                                .toExternalForm()),
                                65,
                                90));
                deadChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/dead/chonBota_Dead_007.png")
                                                .toExternalForm()),
                                65,
                                90));

                chonBota.getAnimationSet().add(AnimationType.IDLE, idleChonBota);
                chonBota.getAnimationSet().add(AnimationType.WALK, hoveringChonBota);
                chonBota.getAnimationSet().add(AnimationType.ATTACK, shootingChonBota);
                chonBota.getAnimationSet().add(AnimationType.DAMAGE, hitChonBota);
                chonBota.getAnimationSet().add(AnimationType.TERMINATE, deadChonBota);

                chonBota.getAnimationState().setCurrentAnimation(idleChonBota);

                /** Game's Weapons and Shots. */
                // New Canno with Missile.
                Animation missileShot = new Animation(0, true);
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile001.png").toExternalForm()),
                                64,
                                42));
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile002.png").toExternalForm()),
                                64,
                                42));
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile003.png").toExternalForm()),
                                64,
                                42));
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile004.png").toExternalForm()),
                                64,
                                42));
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile005.png").toExternalForm()),
                                64,
                                42));
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile006.png").toExternalForm()),
                                64,
                                42));
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile007.png").toExternalForm()),
                                64,
                                42));
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile008.png").toExternalForm()),
                                64,
                                42));

                ConcreteShot missile = new ConcreteShot(
                                42,
                                64,
                                3,
                                0,
                                false,
                                800);
                missile.getAnimationSet().add(AnimationType.IDLE, missileShot);
                Weapon cannon = new ConcreteWeapon(
                                0,
                                0,
                                3,
                                0,
                                0.05,
                                false,
                                150,
                                missile);

                Animation fireballShot = new Animation();
                fireballShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/fireball/fireball001.png").toExternalForm()),
                                64,
                                42));
                fireballShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/fireball/fireball002.png").toExternalForm()),
                                64,
                                42));
                fireballShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/fireball/fireball003.png").toExternalForm()),
                                64,
                                42));

                ConcreteShot fireball = new ConcreteShot(
                                42,
                                64,
                                3,
                                0,
                                false,
                                500);
                fireball.getAnimationSet().add(AnimationType.IDLE, fireballShot);
                // New Lancer with Fireball.
                Weapon lancer = new ConcreteWeapon(
                                0,
                                0,
                                3,
                                0,
                                0.05,
                                false,
                                150,
                                fireball);
                chonBota.setWeapon(cannon);
                chonBota.setWeapon(lancer);

                /* Animation frames for chonBota. It can be reused among all instances. */
                Animation idleChonBot = new Animation();
                idleChonBot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBot/idle/chonBot_001.png")
                                                .toExternalForm()),
                                65,
                                90));
                idleChonBot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBot/idle/chonBot_002.png")
                                                .toExternalForm()),
                                65,
                                90));
                idleChonBot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBot/idle/chonBot_003.png")
                                                .toExternalForm()),
                                65,
                                90));
                idleChonBot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBot/idle/chonBot_004.png")
                                                .toExternalForm()),
                                65,
                                90));
                idleChonBot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBot/idle/chonBot_004.png")
                                                .toExternalForm()),
                                65,
                                90));

                Animation deadChonBot = new Animation(200, false);
                deadChonBot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBot/dead/chonBot_dead_001.png")
                                                .toExternalForm()),
                                65,
                                90));
                deadChonBot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBot/dead/chonBot_dead_002.png")
                                                .toExternalForm()),
                                65,
                                90));
                deadChonBot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBot/dead/chonBot_dead_002.png")
                                                .toExternalForm()),
                                65,
                                90));
                deadChonBot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBot/dead/chonBot_dead_003.png")
                                                .toExternalForm()),
                                65,
                                90));
                deadChonBot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBot/dead/chonBot_dead_003.png")
                                                .toExternalForm()),
                                65,
                                90));
                deadChonBot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBot/dead/chonBot_dead_004.png")
                                                .toExternalForm()),
                                65,
                                90));
                deadChonBot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBot/dead/chonBot_dead_005.png")
                                                .toExternalForm()),
                                65,
                                90));
                /*
                 * Enemies instances. For each instance is necessary the Agent object and the
                 * Animation Set.
                 */
                Agent chonBot1 = new Agent(
                                920,
                                440,
                                90,
                                65,
                                1,
                                500,
                                Direction.IDLE,
                                false,
                                false);
                chonBot1.getAnimationSet().add(AnimationType.IDLE, idleChonBot);
                chonBot1.getAnimationSet().add(AnimationType.TERMINATE, deadChonBot);
                Agent chonBot2 = new Agent(
                                2920,
                                640,
                                90,
                                65,
                                1,
                                500,
                                Direction.IDLE,
                                true,
                                true);
                chonBot2.getAnimationSet().add(AnimationType.IDLE, idleChonBot);
                chonBot2.getAnimationSet().add(AnimationType.TERMINATE, deadChonBot);
                Agent chonBot3 = new Agent(
                                4920,
                                300,
                                90,
                                65,
                                1,
                                500,
                                Direction.IDLE,
                                true,
                                false);
                chonBot3.getAnimationSet().add(AnimationType.IDLE, idleChonBot);
                chonBot3.getAnimationSet().add(AnimationType.TERMINATE, deadChonBot);
                Agent chonBot4 = new Agent(
                                6920,
                                500,
                                90,
                                65,
                                1,
                                500,
                                Direction.IDLE,
                                true,
                                true);
                chonBot4.getAnimationSet().add(AnimationType.IDLE, idleChonBot);
                chonBot4.getAnimationSet().add(AnimationType.TERMINATE, deadChonBot);
                Agent chonBot5 = new Agent(
                                1500,
                                500,
                                90,
                                65,
                                1,
                                500,
                                Direction.IDLE,
                                true,
                                true);
                chonBot5.getAnimationSet().add(AnimationType.IDLE, idleChonBot);
                chonBot5.getAnimationSet().add(AnimationType.TERMINATE, deadChonBot);

                chonBot1.getAnimationState().setCurrentAnimation(idleChonBot);
                chonBot2.getAnimationState().setCurrentAnimation(idleChonBot);
                chonBot3.getAnimationState().setCurrentAnimation(idleChonBot);
                chonBot4.getAnimationState().setCurrentAnimation(idleChonBot);
                chonBot5.getAnimationState().setCurrentAnimation(idleChonBot);

                /* Setting the protagonist and some Images in the Environment system. */
                environment.setProtagonist(chonBota);
                environment.setPauseImage("/images/environment/pause.png");
                environment.setGameOverImage("/images/environment/gameover.png");

                /* Animation frames for objects. It can be reused among all instances. */
                Animation idleCoin = new Animation();
                idleCoin.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/objects/chonCoin/coin_001.png").toExternalForm()),
                                32,
                                32));
                idleCoin.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/objects/chonCoin/coin_002.png").toExternalForm()),
                                32,
                                32));
                idleCoin.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/objects/chonCoin/coin_003.png").toExternalForm()),
                                32,
                                32));
                idleCoin.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/objects/chonCoin/coin_004.png").toExternalForm()),
                                32,
                                32));
                idleCoin.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/objects/chonCoin/coin_003.png").toExternalForm()),
                                32,
                                32));
                idleCoin.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/objects/chonCoin/coin_002.png").toExternalForm()),
                                32,
                                32));
                Animation terminateCoin = new Animation(100, false);
                terminateCoin.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/objects/chonCoin/coin_001.png").toExternalForm()),
                                32,
                                32));
                Animation idleBox = new Animation();
                idleBox.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/objects/box/box_001.png").toExternalForm()),
                                64,
                                64));
                Animation terminateBox = new Animation(100, false);
                terminateBox.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/objects/box/box_002.png").toExternalForm()),
                                64,
                                64));
                terminateBox.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/objects/box/box_003.png").toExternalForm()),
                                64,
                                64));
                terminateBox.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/objects/box/box_004.png").toExternalForm()),
                                64,
                                64));
                terminateBox.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/objects/box/box_005.png").toExternalForm()),
                                64,
                                64));
                terminateBox.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/objects/box/box_006.png").toExternalForm()),
                                64,
                                64));

                Animation hitBox = new Animation(100, false);
                hitBox.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/objects/box/box_002.png").toExternalForm()),
                                64,
                                64));

                /*
                 * Set up some collectable objects. For each instance is necessary the Object
                 * and the Animation Set.
                 */
                List<Object> objects = new ArrayList<>();
                Object coin1 = new Object(
                                200,
                                350,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                false,
                                false,
                                true,
                                false,
                                200);
                coin1.getAnimationSet().add(AnimationType.IDLE, idleCoin);
                coin1.getAnimationSet().add(AnimationType.TERMINATE, terminateCoin);
                coin1.getAnimationState().setCurrentAnimation(idleCoin);
                objects.add(coin1);
                Object coin2 = new Object(
                                400,
                                380,
                                32,
                                32,
                                0,
                                1,
                                Direction.IDLE,
                                false,
                                true,
                                true,
                                true, 200);
                coin2.getAnimationSet().add(AnimationType.IDLE, idleCoin);
                coin2.getAnimationSet().add(AnimationType.TERMINATE, terminateCoin);
                coin2.getAnimationState().setCurrentAnimation(idleCoin);
                objects.add(coin2);
                Object coin3 = new Object(
                                1000,
                                600,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                false,
                                false,
                                true,
                                false,
                                200);
                coin3.getAnimationSet().add(AnimationType.IDLE, idleCoin);
                coin3.getAnimationSet().add(AnimationType.TERMINATE, terminateCoin);
                coin3.getAnimationState().setCurrentAnimation(idleCoin);
                objects.add(coin3);
                Object box1 = new Object(
                                1200,
                                704,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                false,
                                200);
                box1.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box1.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box1.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box1);
                Object box2 = new Object(
                                1200,
                                650,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                200);
                box2.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box2.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box2.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box2);
                Object box3 = new Object(
                                1200,
                                596,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                200);
                box3.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box3.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box3.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box3);
                Object coin4 = new Object(
                                1400,
                                380,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                false,
                                false,
                                true,
                                false,
                                200);
                coin4.getAnimationSet().add(AnimationType.IDLE, idleCoin);
                coin4.getAnimationSet().add(AnimationType.TERMINATE, terminateCoin);
                coin4.getAnimationState().setCurrentAnimation(idleCoin);
                objects.add(coin4);
                Object coin5 = new Object(
                                1800,
                                650,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                false,
                                false,
                                true,
                                false,
                                200);
                coin5.getAnimationSet().add(AnimationType.IDLE, idleCoin);
                coin5.getAnimationSet().add(AnimationType.TERMINATE, terminateCoin);
                coin5.getAnimationState().setCurrentAnimation(idleCoin);
                objects.add(coin5);
                Object box4 = new Object(
                                1900,
                                350,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                0);
                box4.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box4.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box4.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box4);
                Object coin6 = new Object(
                                2000,
                                580,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                false,
                                false,
                                true,
                                false,
                                200);
                coin6.getAnimationSet().add(AnimationType.IDLE, idleCoin);
                coin6.getAnimationSet().add(AnimationType.TERMINATE, terminateCoin);
                coin6.getAnimationState().setCurrentAnimation(idleCoin);
                objects.add(coin6);
                Object box5 = new Object(
                                2150,
                                550,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                0);
                box5.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box5.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box5.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box5);
                Object coin7 = new Object(
                                2300,
                                380,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                false,
                                false,
                                true,
                                false,
                                200);
                coin7.getAnimationSet().add(AnimationType.IDLE, idleCoin);
                coin7.getAnimationSet().add(AnimationType.TERMINATE, terminateCoin);
                coin7.getAnimationState().setCurrentAnimation(idleCoin);
                objects.add(coin7);
                Object box6 = new Object(
                                2500,
                                680,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                0);
                box6.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box6.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box6.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box6);
                Object coin8 = new Object(
                                2600,
                                500,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                false, false,
                                true,
                                false,
                                200);
                coin8.getAnimationSet().add(AnimationType.IDLE, idleCoin);
                coin8.getAnimationSet().add(AnimationType.TERMINATE, terminateCoin);
                coin8.getAnimationState().setCurrentAnimation(idleCoin);
                objects.add(coin8);
                Object box7 = new Object(
                                2700,
                                450,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                0);
                box7.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box7.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box7.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box7);
                Object box8 = new Object(
                                2850,
                                650,
                                64,
                                64,
                                0,
                                5000,
                                Direction.IDLE,
                                true,
                                true,
                                false,
                                true,
                                0);
                box8.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box8.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box8.getAnimationSet().add(AnimationType.DAMAGE, hitBox);
                box8.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box8);
                Object coin9 = new Object(
                                2900,
                                380,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                false,
                                false,
                                true,
                                false,
                                200);
                coin9.getAnimationSet().add(AnimationType.IDLE, idleCoin);
                coin9.getAnimationSet().add(AnimationType.TERMINATE, terminateCoin);
                coin9.getAnimationState().setCurrentAnimation(idleCoin);
                objects.add(coin9);
                Object box9 = new Object(
                                3150,
                                450,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                0);
                box9.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box9.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box9.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box9);
                Object coin10 = new Object(
                                3200,
                                400,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                false,
                                false,
                                true,
                                false,
                                200);
                coin10.getAnimationSet().add(AnimationType.IDLE, idleCoin);
                coin10.getAnimationSet().add(AnimationType.TERMINATE, terminateCoin);
                coin10.getAnimationState().setCurrentAnimation(idleCoin);
                objects.add(coin10);
                Object box10 = new Object(
                                3500,
                                350,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                0);
                box10.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box10.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box10.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box10);
                Object box11 = new Object(
                                3850,
                                550,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                0);
                box11.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box11.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box11.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box11);
                Object coin11 = new Object(
                                4100,
                                500,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                false,
                                false,
                                true,
                                false, 200);
                coin11.getAnimationSet().add(AnimationType.IDLE, idleCoin);
                coin11.getAnimationSet().add(AnimationType.TERMINATE, terminateCoin);
                coin11.getAnimationState().setCurrentAnimation(idleCoin);
                objects.add(coin11);
                Object box12 = new Object(
                                4350,
                                550,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                0);
                box12.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box12.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box12.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box12);
                Object box13 = new Object(
                                4550,
                                550,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                0);
                box13.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box13.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box13.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box13);
                Object box14 = new Object(
                                4650,
                                350,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false, true, 0);
                box14.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box14.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box14.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box14);
                Object box15 = new Object(
                                4850,
                                600,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false, true, 0);
                box15.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box15.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box15.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box15);
                Object coin12 = new Object(
                                5000,
                                380,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                false,
                                false,
                                true,
                                false,
                                200);
                coin12.getAnimationSet().add(AnimationType.IDLE, idleCoin);
                coin12.getAnimationSet().add(AnimationType.TERMINATE, terminateCoin);
                coin12.getAnimationState().setCurrentAnimation(idleCoin);
                objects.add(coin12);
                Object box16 = new Object(
                                5150,
                                650,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                0);
                box16.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box16.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box16.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box16);
                Object box17 = new Object(
                                5300,
                                300,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                0);
                box17.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box17.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box17.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box17);
                Object box18 = new Object(
                                5600,
                                550,
                                64,
                                64,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                0);
                box18.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box18.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box18.getAnimationState().setCurrentAnimation(idleBox);
                objects.add(box18);
                Object coin13 = new Object(
                                6200,
                                400,
                                32,
                                32,
                                0,
                                0,
                                Direction.IDLE,
                                false,
                                false,
                                true,
                                false,
                                200);
                coin13.getAnimationSet().add(AnimationType.IDLE, idleCoin);
                coin13.getAnimationSet().add(AnimationType.TERMINATE, terminateCoin);
                coin13.getAnimationState().setCurrentAnimation(idleCoin);
                objects.add(coin13);
                // ChonBota sounds.
                Sound lancerShotSound = new Sound(
                                "/sounds/agents/chonBota/lancer_shot.wav",
                                SoundType.SFX,
                                false);
                Sound chonBotaHit = new Sound(
                                "/sounds/agents/chonBota/hit.wav",
                                SoundType.SFX,
                                false);
                Sound chonBotaTerminate = new Sound(
                                "/sounds/agents/chonBot/terminate.wav",
                                SoundType.SFX,
                                false);
                Sound chonBotaWalk = new Sound(
                                "/sounds/agents/chonBota/walk.wav",
                                SoundType.SFX,
                                false);
                chonBota.getSoundSet().add(SoundEvent.ATTACK, lancerShotSound);
                chonBota.getSoundSet().add(SoundEvent.WALK, chonBotaWalk);
                chonBota.getSoundSet().add(SoundEvent.DAMAGE, chonBotaHit);
                chonBota.getSoundSet().add(SoundEvent.TERMINATE, chonBotaTerminate);
                // ChonBots Sounds.
                Sound chonBotTerminate = new Sound(
                                "/sounds/agents/chonBot/terminate.wav",
                                SoundType.SFX,
                                false);
                chonBot1.getSoundSet().add(SoundEvent.TERMINATE, chonBotTerminate);
                chonBot2.getSoundSet().add(SoundEvent.TERMINATE, chonBotTerminate);
                chonBot3.getSoundSet().add(SoundEvent.TERMINATE, chonBotTerminate);
                chonBot4.getSoundSet().add(SoundEvent.TERMINATE, chonBotTerminate);
                chonBot5.getSoundSet().add(SoundEvent.TERMINATE, chonBotTerminate);
                // Box sounds.
                Sound boxTerminate = new Sound(
                                "/sounds/objects/crate_terminate.wav",
                                SoundType.SFX,
                                false);
                box1.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box2.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box3.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box4.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box5.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box6.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box7.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box8.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box9.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box10.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box11.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box12.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box13.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box14.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box15.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box16.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box17.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                box18.getSoundSet().add(SoundEvent.TERMINATE, boxTerminate);
                // Coin sounds.
                Sound coinTerminate = new Sound(
                                "/sounds/objects/coin.wav",
                                SoundType.SFX,
                                false);
                coin1.getSoundSet().add(SoundEvent.COLLECT, coinTerminate);
                coin2.getSoundSet().add(SoundEvent.COLLECT, coinTerminate);
                coin3.getSoundSet().add(SoundEvent.COLLECT, coinTerminate);
                coin4.getSoundSet().add(SoundEvent.COLLECT, coinTerminate);
                coin5.getSoundSet().add(SoundEvent.COLLECT, coinTerminate);
                coin6.getSoundSet().add(SoundEvent.COLLECT, coinTerminate);
                coin7.getSoundSet().add(SoundEvent.COLLECT, coinTerminate);
                coin8.getSoundSet().add(SoundEvent.COLLECT, coinTerminate);
                coin9.getSoundSet().add(SoundEvent.COLLECT, coinTerminate);
                coin10.getSoundSet().add(SoundEvent.COLLECT, coinTerminate);
                coin11.getSoundSet().add(SoundEvent.COLLECT, coinTerminate);
                coin12.getSoundSet().add(SoundEvent.COLLECT, coinTerminate);
                coin13.getSoundSet().add(SoundEvent.COLLECT, coinTerminate);
                // Level sounds.
                Sound level0BackgroundSound = new Sound(
                                "/sounds/levels/openingMusic.mp3",
                                SoundType.MUSIC,
                                true);
                level0.getSoundSet().add(SoundEvent.BACKGROUND, level0BackgroundSound);
                Sound level1AmbientSound = new Sound(
                                "/sounds/levels/forestAmbient.mp3",
                                SoundType.AMBIENT,
                                true);
                Sound level1BackgroundSound = new Sound(
                                "/sounds/levels/forestMusic.mp3",
                                SoundType.MUSIC,
                                true);
                level1.getSoundSet().add(SoundEvent.AMBIENT, level1AmbientSound);
                level1.getSoundSet().add(SoundEvent.BACKGROUND, level1BackgroundSound);
                Sound level2AmbientSound = new Sound(
                                "/sounds/levels/castleAmbient.mp3",
                                SoundType.AMBIENT,
                                true);
                Sound level2BackgroundSound = new Sound(
                                "/sounds/levels/castleMusic.mp3",
                                SoundType.MUSIC,
                                true);
                level2.getSoundSet().add(SoundEvent.AMBIENT, level2AmbientSound);
                level2.getSoundSet().add(SoundEvent.BACKGROUND, level2BackgroundSound);

                // Register objects into the environment and count total collectibles
                level1.getAgents().add(chonBot1);
                level1.getAgents().add(chonBot2);
                level1.getAgents().add(chonBot3);
                level1.getAgents().add(chonBot4);
                level1.setObjects(objects);
                level1.countCollectibles();
                level2.getAgents().add(chonBot5);
                environment.getLevels().add(level0);
                environment.getLevels().add(level1);
                environment.getLevels().add(level2);
                environment.setCurrentLevel(level0);
        }

}
