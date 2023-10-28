package com.mygdx.game.RealClasses;

public abstract class Collision {

    public static boolean isCollision(Line line1, Line line2){
        if(line1.isVertical && line2.isVertical) {
            return line1.a.x == line2.a.x;
        }
        else if(line1.isVertical){
            float collisionY = line2.getK() * line1.a.x + line2.getB();
            float collisionX = (collisionY - line2.getB()) / line2.getK();
            return collisionY >= Math.min(line1.a.y, line1.b.y) && collisionY <= Math.max(line1.a.y, line1.b.y)
                    && collisionY >= Math.min(line2.a.y, line2.b.y) && collisionY <= Math.max(line2.a.y, line2.b.y)
                    && collisionX >= Math.min(line2.a.x, line2.b.x) && collisionX <= Math.max(line2.a.x, line2.b.x);
        } else if(line2.isVertical){
            float collisionY = line1.getK() * line2.a.x + line1.getB();
            float collisionX = (collisionY - line1.getB()) / line1.getK();
            return collisionY >= Math.min(line1.a.y, line1.b.y) && collisionY <= Math.max(line1.a.y, line1.b.y)
                    && collisionY >= Math.min(line2.a.y, line2.b.y) && collisionY <= Math.max(line2.a.y, line2.b.y)
                    && collisionX >= Math.min(line1.a.x, line1.b.x) && collisionX <= Math.max(line1.a.x, line1.b.x);
        } else {
            if(line1.getK() == line2.getK()) {
                return line1.getB() == line2.getB();
            }
            float collisionX = (line2.getB() - line1.getB()) / (line1.getK() - line2.getK());
            return collisionX >= Math.min(line1.a.x, line1.b.x) && collisionX <= Math.max(line1.a.x, line1.b.x)
                    && collisionX >= Math.min(line2.a.x, line2.b.x) && collisionX <= Math.max(line2.a.x, line2.b.x);
        }
    }

    public static boolean isCollision(Rectangle rect_1, Rectangle rect_2, float angle_1){
        angle_1 = (float)Math.toRadians(angle_1);
        Point center_1 = new Point(rect_1);
        float diagonal_1 = (float) Math.sqrt(Math.pow(rect_1.getSizeX(), 2) + Math.pow(rect_1.getSizeY(), 2))  / 2;
        float originAngle_1 = (float)Math.atan(rect_1.getSizeY() / rect_1.getSizeX());

        Point a1 = new Point(center_1.x + (float)Math.cos(angle_1 + originAngle_1) * diagonal_1,
                center_1.y + (float)Math.sin(angle_1 + originAngle_1) * diagonal_1);
        Point a2 = new Point(center_1.x + (float)Math.cos(angle_1 - originAngle_1) * diagonal_1,
                center_1.y + (float)Math.sin(angle_1 - originAngle_1) * diagonal_1);
        Point a3 = new Point(center_1.x - (float)Math.cos(angle_1 + originAngle_1) * diagonal_1,
                center_1.y - (float)Math.sin(angle_1 + originAngle_1) * diagonal_1);
        Point a4 = new Point(center_1.x - (float)Math.cos(angle_1 - originAngle_1) * diagonal_1,
                center_1.y - (float)Math.sin(angle_1 - originAngle_1) * diagonal_1);

        Point b1 = new Point(rect_2.getX() + rect_2.getSizeX(), rect_2.getY() + rect_2.getSizeY());
        Point b2 = new Point(rect_2.getX() + rect_2.getSizeX(), rect_2.getY());
        Point b3 = new Point(rect_2.getX(), rect_2.getY());
        Point b4 = new Point(rect_2.getX(), rect_2.getY() + rect_2.getSizeY());

        Line[] lines_1 = {
                new Line(a1, a2),
                new Line(a2, a3),
                new Line(a3, a4),
                new Line(a4, a1)
        };
        Line[] lines_2 = {
                new Line(b1, b2),
                new Line(b2, b3),
                new Line(b3, b4),
                new Line(b4, b1)
        };

        for(int i = 0; i < 4; i++){
            for(int j  = 0; j < 4; j++){
                if (isCollision(lines_1[i], lines_2[j])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isCollision(Rectangle rectangle, Line line, float angle){
        angle = (float)Math.toRadians(angle);
        Point center_1 = new Point(rectangle);
        float diagonal_1 = (float) Math.sqrt(Math.pow(rectangle.getSizeX(), 2) + Math.pow(rectangle.getSizeY(), 2))  / 2;
        float originAngle_1 = (float)Math.atan(rectangle.getSizeY() / rectangle.getSizeX());

        Point a1 = new Point(center_1.x + (float)Math.cos(angle + originAngle_1) * diagonal_1,
                center_1.y + (float)Math.sin(angle + originAngle_1) * diagonal_1);
        Point a2 = new Point(center_1.x + (float)Math.cos(angle - originAngle_1) * diagonal_1,
                center_1.y + (float)Math.sin(angle - originAngle_1) * diagonal_1);
        Point a3 = new Point(center_1.x - (float)Math.cos(angle + originAngle_1) * diagonal_1,
                center_1.y - (float)Math.sin(angle + originAngle_1) * diagonal_1);
        Point a4 = new Point(center_1.x - (float)Math.cos(angle - originAngle_1) * diagonal_1,
                center_1.y - (float)Math.sin(angle - originAngle_1) * diagonal_1);

        Line[] lines_1 = {
                new Line(a1, a2),
                new Line(a2, a3),
                new Line(a3, a4),
                new Line(a4, a1)
        };

        for(int i = 0; i < 4; i++){
            if (isCollision(lines_1[i], line)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCollision(Rectangle rectangle, Point point){
        return rectangle.isInside(point.x, point.y);
    }

    public static boolean isCollision(Rectangle rect_1, Rectangle rect_2){
        if(rect_1.getX() > rect_2.getX() + rect_2.getSizeX()) return false;
        if(rect_2.getX() > rect_1.getX() + rect_1.getSizeX()) return false;
        if(rect_1.getY() > rect_2.getY() + rect_2.getSizeY()) return false;
        return !(rect_2.getY() > rect_1.getY() + rect_1.getSizeY());
    }
}
