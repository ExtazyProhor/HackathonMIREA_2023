package com.mygdx.game.RealClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import static com.mygdx.game.Main.*;

public class Button extends Rectangle {
    private float textX;
    private float textY;
    private boolean withText = true;

    private BitmapFont font;

    private String text;
    private final Texture picture;

    public Button(float x, float y, float sizeX, float sizeY, String path, String text, int textColor, int textSize){
        super(x, y, sizeX, sizeY);
        font = new BitmapFont();
        parameter.color = new Color(textColor);
        parameter.size = textSize;
        font = generator.generateFont(parameter);
        this.text = text;
        this.picture = new Texture(path);
        gl.setText(font, text);
        this.textX = (int) gl.width;
        this.textY = (int) gl.height;
    }

    public Button(float x, float y, float sizeX, float sizeY, String path){
        super(x, y, sizeX, sizeY);
        withText = false;
        this.picture = new Texture(path);
    }

    public void draw() {
        batch.draw(picture, x, y, sizeX, sizeY);
        if (withText) {
            this.font.draw(batch, this.text, this.x + (float)(sizeX - textX) / 2, y + (float)(sizeY + textY) / 2);
        }
    }

    public void draw(float angle, boolean flipX, boolean flipY) {
        batch.draw(picture, x, y, sizeX/2, sizeY/2, sizeX, sizeY, 1, 1, angle, 0, 0,
                picture.getWidth(), picture.getHeight(), flipX, flipY);
        if (withText) {
            this.font.draw(batch, this.text, this.x + (float)(sizeX - textX) / 2, y + (float)(sizeY + textY) / 2);
        }
    }

    public void changeText(String newText){
        this.text = newText;
        gl.setText(font, text);
        this.textX = (int) gl.width;
        this.textY = (int) gl.height;
    }

    public void setColor(float r, float g, float b){
        this.font.setColor(r, g, b, 1);
    }

    public void placeCenter() {
        this.x = (scrX - this.sizeX) / 2;
    }

    public boolean isTouched(boolean withSound){
        if(super.isInside(Gdx.input.getX(), scrY - Gdx.input.getY())){
            if(withSound) clickSound.play();
            return true;
        }
        return false;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public void dispose(){
        this.picture.dispose();
        if(withText) this.font.dispose();
    }
}
