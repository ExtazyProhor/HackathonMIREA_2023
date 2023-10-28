package com.mygdx.game.RealClasses;

public class Rectangle {
    protected float x;
    protected float y;
    protected float sizeX;
    protected float sizeY;

    public Rectangle(float x, float y, float sizeX, float sizeY) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public Rectangle(){

    }

    public void setCoordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void addCoordinates(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void setSize(float x, float y) {
        this.sizeX = x;
        this.sizeY = y;
    }

    public void addSize(float x, float y) {
        this.sizeX += x;
        this.sizeY += y;
    }

    public boolean isInside(float x, float y) {
        if (x < this.x) return false;
        if (this.x + this.sizeX < x) return false;
        if (y < this.y) return false;
        return (this.y + this.sizeY >= y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSizeX() {
        return sizeX;
    }

    public float getSizeY() {
        return sizeY;
    }
}
