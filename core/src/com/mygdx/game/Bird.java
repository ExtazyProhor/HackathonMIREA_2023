package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.RealClasses.AnimatedPictureBox;

import static com.mygdx.game.Main.*;
import static com.mygdx.game.Screens.FlappyBird.*;

public class Bird extends AnimatedPictureBox {

    public float virtualX;
    public static final float gravity = 0.1f;
    public static final float speedX = -0.4f;
    public static Texture deadBird;
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

        if (deadBird == null) deadBird = new Texture("birds/deadBird.png");

        virtualX = x;
        getAngle();
    }

    public float getVirtualX() {
        return virtualX;
    }

    public void moveBird() {
        virtualX += speedX * ppY;
        if (isAlive) {
            speedY -= gravity;
            if (speedY < -1.0f) speedY = -1.0f;
            System.out.println();
            System.out.println(x + "  " + y);
            addCoordinates(speedX * ppY, speedY * ppY);
            System.out.println(x + "  " + y);
            boolean jump = false;
            float coordinateY = (y - pz.getY()) / ppY;
            if (coordinateY < 25) {
                jump = true;
            } else if (coordinateY < 60) {
                jump = random.nextInt(10) == 0;
            } else if (coordinateY < 75) {
                jump = random.nextInt(30) == 0;
            }
            if (jump) {
                speedY = 1.2f;
            }

        } else {
            y -= Bird.gravity * ppY * 7;
        }
    }

    public void draw() {
        if (isAlive) {
            getAngle();
            draw(angle);
        } else {
            batch.draw(deadBird, x + ppX * 15, y, deadBird.getWidth() / 2.0f,
                    deadBird.getHeight() / 2.0f, sizeX, sizeY, 1, 1, 90, 0, 0,
                    deadBird.getWidth(), deadBird.getHeight(), false, false);
        }
    }

    public void getAngle() {
        if (!isAlive) return;
        if (speedY > 0) {
            angle = -30;
            return;
        }
        angle = (float) Math.asin(speedY);
        if (angle < -90) angle = -90;
    }
}
