package com.mygdx.game.RealClasses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.Main;

public class TextBox extends Rectangle {
    Position position;
    BitmapFont font;
    String text;

    public TextBox(float centerX, float y, String text, int textColor, int textSize) {
        position = Position.MIDDLE;
        font = new BitmapFont();
        Main.parameter.color = new Color(textColor);
        Main.parameter.size = textSize;
        font = Main.generator.generateFont(Main.parameter);
        this.text = text;
        Main.gl.setText(font, text);
        this.sizeX = (int) Main.gl.width;
        this.sizeY = (int) Main.gl.height;
        this.x = centerX - this.sizeX/2;
        this.y = y;
    }

    public void positionToLeft(float leftX){
        position = Position.LEFT;
        this.x = leftX;
    }

    public void positionToRight(float rightX){
        position = Position.RIGHT;
        this.x = rightX - this.sizeX;
    }

    public void positionToMiddleY(float middleY){
        this.y = middleY + this.sizeY/2;
    }

    public void positionToDown(float downY){
        this.y = downY + sizeY;
    }

    public void draw() {
        this.font.draw(Main.batch, this.text, x, y);
    }

    public void draw(float x, float y) {
        this.font.draw(Main.batch, this.text, x, y);
    }

    public void changeText(String newText){
        switch (this.position){
            case MIDDLE:
                this.x += sizeX/2;
                break;
            case RIGHT:
                this.x += sizeX;
                break;
        }
        this.text = newText;
        Main.gl.setText(font, text);
        this.sizeX = (int) Main.gl.width;
        this.sizeY = (int) Main.gl.height;
        switch (this.position){
            case MIDDLE:
                this.x -= sizeX/2;
                break;
            case RIGHT:
                this.x -= sizeX;
                break;
        }
    }

    public void setColor(float r, float g, float b){
        this.font.setColor(r, g, b, 1);
    }

    public void dispose(){
        this.font.dispose();
    }

    enum Position {
        LEFT,
        MIDDLE,
        RIGHT
    }
}
