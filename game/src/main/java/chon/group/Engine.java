package chon.group;

import chon.group.game.Game;
import chon.group.game.core.platform.JavaFxPlatform;
import chon.group.game.core.platform.Platform;
import chon.group.game.core.platform.PlatformAssembler;
import chon.group.game.gateway.GameSnapshotBuilder;
import chon.group.game.loader.GameSet;
import chon.group.game.gateway.GameGateway;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The {@code Engine} class represents the main entry point of the application
 * and serves as the game engine for "Chon: The Learning Game."
 */
public class Engine extends Application {

    private static final long OBSERVATION_INTERVAL_NANOS = 100_000_000L;
    private static final System.Logger LOGGER = System.getLogger(Engine.class.getName());

    private final GameSnapshotBuilder snapshotBuilder = new GameSnapshotBuilder();

    /**
     * Main entry point of the application.
     *
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        try {
            GameSet gameSet = new GameSet();
            PlatformAssembler assembler = new PlatformAssembler();
            Platform platform = assembler.construct(
                    new JavaFxPlatform(theStage, gameSet), gameSet.getControl(), gameSet.getApiPort());

            Game chonGame = new Game(
                    gameSet.getEnvironment(),
                    platform.getSoundManager(),
                    platform.getDrawer(),
                    gameSet.getMenu(),
                    platform.getJoystick(),
                    0);

            final GameGateway gateway = platform.getGateway();
            if (gateway != null) {
                gateway.start();
            }

            // Start the game loop
            AnimationTimer timer = new AnimationTimer() {
                private long lastObservationNanos;

                public void handle(long now) {
                    try {
                        if (gateway != null) {
                            gateway.processPendingActions(chonGame);
                            gateway.updateControlledAgents(chonGame);
                        }
                        chonGame.loop();

                        if (gateway != null
                                && now - lastObservationNanos >= OBSERVATION_INTERVAL_NANOS) {
                            lastObservationNanos = now;
                            var snapshot = snapshotBuilder.build(chonGame, chonGame.getTick());
                            gateway.publish(snapshot);
                        }
                    } catch (RuntimeException exception) {
                        LOGGER.log(System.Logger.Level.ERROR, "Game loop failed", exception);
                    }
                }
            };

            theStage.setOnCloseRequest(event -> {
                timer.stop();
                if (gateway != null) {
                    try {
                        gateway.close();
                    } catch (Exception exception) {
                        LOGGER.log(System.Logger.Level.ERROR, "Could not close game gateway", exception);
                    }
                }
            });

            timer.start();

            theStage.show();
        } catch (Exception exception) {
            LOGGER.log(System.Logger.Level.ERROR, "Could not start game", exception);
        }
    }

}