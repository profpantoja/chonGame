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

            // Start the game loop
            AnimationTimer timer = new AnimationTimer() {
                public void handle(long now) {
                    try {
                        if (gateway != null) {
                            gateway.processPendingActions(chonGame.getTick());
                            gateway.updateControlledAgents(chonGame);
                        }
                        chonGame.loop();

                        var snapshot = snapshotBuilder.build(
                                chonGame,
                                chonGame.getTick());

                        if (gateway != null) {
                            gateway.publish(snapshot);
                        }
                    } catch (RuntimeException exception) {
                        exception.printStackTrace();
                    }
                }
            };

            theStage.setOnCloseRequest(event -> {
                timer.stop();
                if (gateway != null) {
                    try {
                        gateway.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });

            timer.start();

            theStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}