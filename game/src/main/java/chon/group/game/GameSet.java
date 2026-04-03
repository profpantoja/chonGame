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
                                0,
                                0);
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
                                260,
                                canvasHeight);
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
                                380,
                                590);
                level2Animation.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/environment/insideCastle.png")
                                                .toExternalForm()),
                                8024,
                                canvasHeight));
                level2.getAnimationSet().add(AnimationType.IDLE, level2Animation);
                level2.getAnimationState().setCurrentAnimation(level2Animation);

                environment = new Environment(
                                level1.getWidth(),
                                this.canvasWidth,
                                panel);

                Agent chonBota = new Agent(
                                400,
                                390,
                                65,
                                90,
                                0.4,
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

                Animation punchChonBota = new Animation(50, false);
                punchChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/punch/chonBota_Punch_001.png")
                                                .toExternalForm()),
                                106,
                                90));
                punchChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/punch/chonBota_Punch_002.png")
                                                .toExternalForm()),
                                106,
                                90));
                punchChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/punch/chonBota_Punch_003.png")
                                                .toExternalForm()),
                                106,
                                90));
                punchChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/punch/chonBota_Punch_002.png")
                                                .toExternalForm()),
                                106,
                                90));
                punchChonBota.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/punch/chonBota_Punch_001.png")
                                                .toExternalForm()),
                                106,
                                90));

                chonBota.getAnimationSet().add(AnimationType.IDLE, idleChonBota);
                chonBota.getAnimationSet().add(AnimationType.WALK, hoveringChonBota);
                chonBota.getAnimationSet().add(AnimationType.ATTACK, punchChonBota);
                // chonBota.getAnimationSet().add(AnimationType.ATTACK, shootingChonBota);
                chonBota.getAnimationSet().add(AnimationType.DAMAGE, hitChonBota);
                chonBota.getAnimationSet().add(AnimationType.TERMINATE, deadChonBota);

                chonBota.getAnimationState().setCurrentAnimation(idleChonBota);

                /** Game's Weapons and Shots. */
                // New Canno with Missile.
                Animation missileShot = new Animation(0, true);
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile001.png").toExternalForm()),
                                88,
                                64));
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile002.png").toExternalForm()),
                                88,
                                64));
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile003.png").toExternalForm()),
                                88,
                                64));
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile004.png").toExternalForm()),
                                88,
                                64));
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile005.png").toExternalForm()),
                                88,
                                64));
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile006.png").toExternalForm()),
                                88,
                                64));
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile007.png").toExternalForm()),
                                88,
                                64));
                missileShot.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/weapons/missile/missile008.png").toExternalForm()),
                                88,
                                64));

                ConcreteShot missile = new ConcreteShot(
                                64,
                                88,
                                3,
                                0,
                                false,
                                250,
                                1200);
                missile.getAnimationSet().add(AnimationType.IDLE, missileShot);
                Weapon cannon = new ConcreteWeapon(
                                20,
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
                                500,
                                1200);
                fireball.getAnimationSet().add(AnimationType.IDLE, fireballShot);
                // New Lancer with Fireball.
                Weapon lancer = new ConcreteWeapon(
                                30,
                                0,
                                0,
                                3,
                                0,
                                0.05,
                                false,
                                300,
                                fireball);

                // Punches with Invisible Shots (punch with hitbox).
                Animation hitBoxPunch = new Animation();
                hitBoxPunch.getFrames().add(new Frame(new Image(
                                getClass().getResource("/images/agents/chonBota/punch/chonBota_Punch.png")
                                                .toExternalForm()),
                                30,
                                15));
                ConcreteShot punch = new ConcreteShot(
                                15,
                                30,
                                3,
                                0,
                                false,
                                500,
                                15);
                punch.getAnimationSet().add(AnimationType.IDLE, null);
                // punch.getAnimationSet().add(AnimationType.IDLE, hitBoxPunch);
                Weapon bareHands = new ConcreteWeapon(
                                30,
                                15,
                                15,
                                3,
                                0,
                                0.05,
                                false,
                                250,
                                punch);
                chonBota.setWeapon(cannon);

                chonBota.setWeapon(bareHands);
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
                Agent chonBot = new Agent(
                                920,
                                440,
                                65,
                                90,
                                0.6,
                                1,
                                500,
                                Direction.IDLE,
                                false,
                                false);
                chonBot.getAnimationSet().add(AnimationType.IDLE, idleChonBot);
                chonBot.getAnimationSet().add(AnimationType.TERMINATE, deadChonBot);

                Agent chonBot1 = chonBot.copy(920, 440);
                Agent chonBot2 = chonBot.copy(2920, 640);
                chonBot2.setVisibleBars(true);
                Agent chonBot3 = chonBot.copy(4920, 300);
                Agent chonBot4 = chonBot.copy(6920, 500);
                chonBot4.setVisibleBars(true);
                Agent chonBot5 = chonBot.copy(1500, 500);

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
                // Starting from Coins with a prototype object.
                List<Object> objects = new ArrayList<>();
                Object coin = new Object(
                                200,
                                350,
                                32,
                                32,
                                1,
                                0,
                                0,
                                Direction.IDLE,
                                false,
                                false,
                                true,
                                false,
                                200);
                coin.getAnimationSet().add(AnimationType.IDLE, idleCoin);
                coin.getAnimationSet().add(AnimationType.TERMINATE, terminateCoin);
                coin.getAnimationState().setCurrentAnimation(idleCoin);

                Object coin1 = coin.copy(200, 380);
                // Coin 2 is different since it is destructible.
                Object coin2 = coin.copy(30, 300);
                Object coin3 = coin.copy(400, 380);
                coin3.setDestructible(true);
                coin3.setVisibleBars(true);
                Object coin4 = coin.copy(1000, 280);
                Object coin5 = coin.copy(2300, 300);
                Object coin6 = coin.copy(3400, 600);
                Object coin7 = coin.copy(4100, 320);
                Object coin8 = coin.copy(5000, 500);
                Object coin9 = coin.copy(6200, 600);
                Object coin10 = coin.copy(6400, 350);
                Object coin11 = coin.copy(7000, 600);
                Object coin12 = coin.copy(7200, 500);
                Object coin13 = coin.copy(7400, 500);
                objects.add(coin1);
                objects.add(coin2);
                objects.add(coin3);
                objects.add(coin4);
                objects.add(coin5);
                objects.add(coin6);
                objects.add(coin7);
                objects.add(coin8);
                objects.add(coin9);
                objects.add(coin10);
                objects.add(coin11);
                objects.add(coin12);
                objects.add(coin13);

                // Creating Boxes.
                Object box = new Object(
                                1200,
                                704,
                                64,
                                32,
                                0.4,
                                0,
                                1000,
                                Direction.IDLE,
                                false,
                                false,
                                false,
                                true,
                                1);
                box.getAnimationSet().add(AnimationType.IDLE, idleBox);
                box.getAnimationSet().add(AnimationType.DAMAGE, hitBox);
                box.getAnimationSet().add(AnimationType.TERMINATE, terminateBox);
                box.getAnimationState().setCurrentAnimation(idleBox);

                Object box1 = box.copy(1200, 596);
                Object box2 = box.copy(1200, 650);
                Object box3 = box.copy(1200, 704);
                box3.setDestructible(false);
                Object box4 = box.copy(1900, 350);
                Object box5 = box.copy(2150, 550);
                Object box6 = box.copy(2500, 680);
                Object box7 = box.copy(2700, 450);
                Object box8 = box.copy(2850, 650);
                box8.setVisibleBars(true);
                box8.setHealth(5000);
                box8.setFullHealth(5000);
                box8.getAnimationState().setFlipped(true);
                Object box9 = box.copy(3150, 450);
                Object box10 = box.copy(3500, 350);
                Object box11 = box.copy(3850, 550);
                Object box12 = box.copy(4350, 550);
                Object box13 = box.copy(4550, 550);
                Object box14 = box.copy(4650, 350);
                Object box15 = box.copy(4850, 600);
                Object box16 = box.copy(5150, 450);
                Object box17 = box.copy(5350, 300);
                Object box18 = box.copy(5600, 550);
                objects.add(box1);
                objects.add(box2);
                objects.add(box3);
                objects.add(box4);
                objects.add(box5);
                objects.add(box6);
                objects.add(box8);
                objects.add(box9);
                objects.add(box10);
                objects.add(box11);
                objects.add(box12);
                objects.add(box13);
                objects.add(box14);
                objects.add(box15);
                objects.add(box16);
                objects.add(box17);
                objects.add(box18);
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
