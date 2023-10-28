package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.RealClasses.AnimatedPictureBox;

public class Bird extends AnimatedPictureBox {

    public static final float gravity = 0.1f;
    public static final float speedX = -0.4f;
    public float speedY;
    public float angle;
    public boolean isGreen;
    public boolean isAlive;

    public Bird(float x, float y, float sizeX, float sizeY, String path,
                              String extension, int quantity, boolean isIncrease, float time, boolean isGreen) {
        super(x, y, sizeX, sizeY, path, extension, quantity, isIncrease, time);
        super.setModePendulum();
        speedY = 0;
        this.isGreen = isGreen;
        this.isAlive = true;

        getAngle();
    }

    //public void move

    public void getAngle(){
        if(!isAlive) return;
        if (speedY > 0) {
            angle = -30;
            return;
        }
        angle = (float)Math.asin(speedY);
        if (angle < -90) angle = -90;
    }
}
