package com.nuntendo.devgame;

import javafx.scene.image.ImageView;

public class Enemy {
    ImageView imageView;
    int direction;
    int stepsRemaining;

    Enemy(ImageView imageView, int direction, int stepsRemaining) {
        this.imageView = imageView;
        this.direction = direction;
        this.stepsRemaining = stepsRemaining;
    }
}