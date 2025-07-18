package chon.group.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GamePanel {

    private final GraphicsContext gc;

    private final Image coinIcon;
    private final Image scoreIcon;
    private final Font pixelFont;


    public GamePanel(GraphicsContext gc, Font pixelFont){
        this.gc = gc;
        this.pixelFont = pixelFont;
        this.coinIcon = loadImage("/images/agents/coin.png");
        this.scoreIcon = loadImage("/images/environment/score.png");
    }

    private Image loadImage(String path) {
        var url = getClass().getResource(path);
        if (url == null) {
            throw new RuntimeException("Imagem não encontrada: " + path);
        }
        return new Image(url.toExternalForm());
    }

    public void drawPanel(int collected, int total, int score) {
        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        gc.fillRoundRect(10, 10, 220, 110, 20, 20);

        gc.setFont(pixelFont);

        gc.setFill(Color.WHITE);

        gc.drawImage(coinIcon, 20, 55, 24, 24);
        String collectedText = "" + collected;
        gc.setFill(Color.GOLD);
        gc.fillText(collectedText, 55, 75);

        gc.drawImage(scoreIcon, 20, 85, 24, 24);
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + score, 55, 104);
    }
}
