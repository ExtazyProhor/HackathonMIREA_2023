package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.RealClasses.AnimatedPictureBox;

public class Bird extends AnimatedPictureBox {

    public static final float gravity = 0.1f;
    public static final float speedX = -0.4f;
    public float speedY;
    public float angle;

    public Bird(float x, float y, float sizeX, float sizeY, String path,
                              String extension, int quantity, boolean isIncrease, float time) {
        super(x, y, sizeX, sizeY, path, extension, quantity, isIncrease, time);
        speedY = 0;

        angle = getAngle(speedY);
    }

    private static float getAngle(float speed){
        if (speed > 0) return 1.0f/3;
        float angle = (float)Math.asin(speed);
        if (angle < -90) angle = 90;
        return angle;
    }
}
