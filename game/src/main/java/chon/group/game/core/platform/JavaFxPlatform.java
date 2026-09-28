package chon.group.game.core.platform;

import chon.group.game.core.platform.config.ControlType;
import chon.group.game.drawer.client.JavaFxDrawer;
import chon.group.game.drawer.service.GameMediator;
import chon.group.game.joystick.client.ExternalJoystick;
import chon.group.game.joystick.client.JavaFxJoystick;
import chon.group.game.joystick.client.Joystick;
import chon.group.game.joystick.service.JoystickMediator;
import chon.group.game.loader.GameSet;
import chon.group.game.sound.client.JavaFxPlayer;
import chon.group.game.sound.service.GameSoundManager;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JavaFxPlatform implements PlatformBuilder {

    private final Stage stage;
    private final GameSet gameSet;
    private final Platform platform;

    private Scene scene;

    public JavaFxPlatform(Stage stage, GameSet gameSet) {
        this.stage = stage;
        this.gameSet = gameSet;
        this.platform = new Platform();
    }

    @Override
    public void buildDrawer() {
        Canvas canvas = new Canvas(
                gameSet.getCanvasWidth(),
                gameSet.getCanvasHeight());

        GraphicsContext graphicsContext =
                canvas.getGraphicsContext2D();

        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        this.scene = new Scene(
                root,
                gameSet.getCanvasWidth(),
                gameSet.getCanvasHeight());

        stage.setTitle("Chon: The Learning Game");
        stage.setScene(scene);

        platform.setDrawer(
                new GameMediator(
                        new JavaFxDrawer(graphicsContext)));
    }

    @Override
    public void buildJoystick() {
        Joystick joystickClient;
        if (gameSet.getControl() == ControlType.API) {
            joystickClient = new ExternalJoystick();
        } else {
            joystickClient = new JavaFxJoystick(scene);
        }
        platform.setJoystick(
                new JoystickMediator(joystickClient));
    }

    @Override
    public void buildSoundPlayer() {
        platform.setSoundManager(
                new GameSoundManager(
                        new JavaFxPlayer()));
    }

    @Override
    public Platform build() {
        return platform;
    }
}