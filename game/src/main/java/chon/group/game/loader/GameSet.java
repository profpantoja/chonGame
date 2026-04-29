package chon.group.game.loader;

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
                // environment.setProtagonist(chonBota);
                environment.setProtagonist(protagonist);
                environment.setPauseImage("/images/environment/pause.png");
                environment.setGameOverImage("/images/environment/gameover.png");
                environment.setTheEndImage("/images/environment/theEnd.png");

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

                // Register objects into the environment and count total collectibles
                level4.getAgents().add(chonBot1);
                level4.getAgents().add(chonBot2);
                level4.getAgents().add(chonBot3);
                level4.getAgents().add(chonBot4);

                level4.setObjects(objects);

                level6.getAgents().add(chonBot5);

                Object box19 = box.copy(1200, 596);
                level7.getObjects().add(box19);

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
