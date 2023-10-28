package com.mygdx.game.RealClasses;

import static com.mygdx.game.Main.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class AnimatedPictureBox extends Rectangle {
    private final Texture[] pictures;
    private final int framesQuantity;
    private int currentFrame;
    private int direction;
    private Mode mode;
    private float currentTime;
    private final float maxTime;

    public AnimatedPictureBox(float x, float y, float sizeX, float sizeY, String path,
                              String extension, int quantity, boolean isIncrease, float time) {
        super(x, y, sizeX, sizeY);
        pictures = new Texture[quantity];
        for (int i = 0; i < quantity; ++i) {
            pictures[i] = new Texture(path + i + "." + extension);
        }
        framesQuantity = quantity;
        currentFrame = 0;
        mode = Mode.CYCLE;
        direction = isIncrease ? 1 : -1;
        currentTime = 0;
        maxTime = time;
    }

    public void draw() {
        batch.draw(pictures[currentFrame], x, y, sizeX, sizeY);
        frameTick();
    }

    public void draw(float x, float y) {
        batch.draw(pictures[currentFrame], x, y, sizeX, sizeY);
        frameTick();
    }

    public void draw(float x, float y, float sizeX, float sizeY, int rX, int rY,
                     int rSizeX, int rSizeY, boolean flipX, boolean flipY) {
        batch.draw(pictures[currentFrame], x, y, sizeX, sizeY, rX, rY, rSizeX, rSizeY, flipX, flipY);
        frameTick();
    }

    public void draw(float angle) {
        batch.draw(pictures[currentFrame], x, y, sizeX / 2, sizeY / 2, sizeX, sizeY, 1, 1, angle, 0, 0,
                pictures[currentFrame].getWidth(), pictures[currentFrame].getHeight(), false, false);
        frameTick();
    }

    public void draw(float x, float y, float sizeX, float sizeY) {
        batch.draw(pictures[currentFrame], x, y, sizeX, sizeY, 0, 0,
                pictures[currentFrame].getWidth(), pictures[currentFrame].getHeight(), false, false);
        frameTick();
    }

    private void frameTick() {
        currentTime += Gdx.graphics.getDeltaTime();
        if (currentTime < maxTime) return;
        currentTime %= maxTime;
        currentFrame += direction;
        switch (mode){
            case CYCLE:
                if (currentFrame < framesQuantity) return;
                currentFrame = 0;
                break;
            case PENDULUM:
                if(currentFrame >= framesQuantity) {
                    direction = -1;
                    currentFrame += 2 * direction;
                    break;
                }
                if(currentFrame < 0) {
                    direction = 1;
                    currentFrame += 2 * direction;
                    break;
                }
        }
    }

    public void setModeCycle() {
        this.mode = Mode.CYCLE;
    }

    public void setModePendulum() {
        this.mode = Mode.PENDULUM;
    }

    public void dispose() {
        for (int i = 0; i < framesQuantity; i++) {
            this.pictures[i].dispose();
        }
    }

    private enum Mode {
        CYCLE,
        PENDULUM
    }


}
