package com.mygdx.game.RealClasses;

import com.badlogic.gdx.graphics.Texture;
import static com.mygdx.game.Main.*;

public class PictureBox extends Rectangle {
    private Texture picture;

    public PictureBox(float x, float y, float sizeX, float sizeY, String path){
        super(x, y, sizeX, sizeY);
        picture = new Texture(path);
    }
    public PictureBox(Rectangle rectangle, String path){
        super(rectangle.x, rectangle.y, rectangle.sizeX, rectangle.sizeY);
        picture = new Texture(path);
    }

    public void draw(){
        batch.draw(picture, x, y, sizeX, sizeY);
    }
    public void draw(float x, float y){
        batch.draw(picture, x, y, sizeX, sizeY);
    }
    public void draw(float x, float y, float sizeX, float sizeY, int rX, int rY, int rSizeX, int rSizeY, boolean flipX, boolean flipY){
        batch.draw(picture, x, y, sizeX, sizeY, rX, rY, rSizeX, rSizeY, flipX, flipY);
    }
    public void draw(float x, float y, boolean flipX, boolean flipY){
        batch.draw(picture, x, y, sizeX, sizeY, 0, 0, picture.getWidth(), picture.getHeight(), flipX, flipY);
    }
    public void draw(float angle){
        batch.draw(picture, x, y, sizeX/2, sizeY/2, sizeX, sizeY, 1, 1, angle, 0, 0,
                picture.getWidth(), picture.getHeight(), false, false);
    }
    public void draw(float x, float y, float sizeX, float sizeY){
        batch.draw(picture, x, y, sizeX, sizeY, 0, 0, picture.getWidth(), picture.getHeight(), false, false);
    }

    public float getTextureAspectRatio(){
        return (float)this.picture.getWidth() / (float)this.picture.getHeight();
    }

    public void changeTexture(String path){
        this.picture.dispose();
        this.picture = new Texture(path);
    }

    public void dispose(){
        this.picture.dispose();
    }
}
