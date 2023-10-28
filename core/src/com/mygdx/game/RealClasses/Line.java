package com.mygdx.game.RealClasses;

public class Line {
    public Point a;
    public Point b;
    public boolean isVertical;

    public Line(Point a, Point b){
        this.a = a;
        this.b = b;
        isVertical = (int) this.a.x == (int) this.b.x;
    }

    public float getK(){
        if(isVertical) return 0;
        return (a.y - b.y) / (a.x - b.x);
    }

    public float getB(){
        return a.y - getK() * a.x;
    }
}