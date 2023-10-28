package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Bird;
import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.AnimatedPictureBox;
import com.mygdx.game.RealClasses.Collision;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.Rectangle;

import static com.mygdx.game.Main.*;

import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements Screen {
    Main game;

    PictureBox backGround;
    PictureBox base;
    Rectangle pz; // playZone
    float ppX;
    float ppY;

    PictureBox[] space;
    float defaultAspectRatio = 9.0f / 16.0f;

    // Pipes
    PictureBox pipe;
    float pipeAspectRatio = 52.0f/320.0f;
    float distanceBetweenPipes;
    float pipesCoordinateY;

    //Birds
    ArrayList<Bird> birds;
    int birdsQuantity = 3;
    int deathBirdIndex = -1;

    public FlappyBird(Main game) {
        this.game = game;

        boolean isWider = scrX / scrY > defaultAspectRatio;
        float x = scrX;
        float y = scrY;

        if (isWider) x = scrY * defaultAspectRatio;
        else y = scrX / defaultAspectRatio;

        pz = new Rectangle((scrX - x) / 2, (scrY - y) / 2, x, y);
        ppX = pz.getSizeX() / 100;
        ppY = pz.getSizeY() / 100;
        //distanceBetweenPipes = ppY * 20;
        distanceBetweenPipes = ppY * 30;
        pipesCoordinateY = ppY * 40;

        space = new PictureBox[2];
        if (isWider) {
            space[0] = new PictureBox(0, 0, (scrX - x) / 2, scrY, "space.png");
            space[1] = new PictureBox((scrX + x) / 2, 0, (scrX - x) / 2 + 1, scrY, "space.png");
        } else {
            space[0] = new PictureBox(0, 0, scrX, (scrY - y) / 2, "space.png");
            space[1] = new PictureBox(0, (scrY + y) / 2, scrX, (scrY - y) / 2 + 1, "space.png");
        }
        backGround = new PictureBox(pz.getX(), pz.getY(), pz.getSizeX(),
                pz.getSizeY(), "background.png");
        base = new PictureBox(pz.getX(), pz.getY(), pz.getSizeX(),
                pz.getSizeX() / 3, "base.png");

        birds = new ArrayList<>();

        for(int i = 0; i < birdsQuantity; ++i){
            Bird bird = new Bird(pz.getX() + (i + 2) * 40 * ppY, pz.getY() + 40 * ppY, x * 0.17f, x * 0.12f, "bird-", "png", 3, true, 0.07f);
            bird.setModePendulum();
            birds.add(bird);
        }

        pipe = new PictureBox(pz.getX() + ppX * 20, 0, 75 * ppY * pipeAspectRatio, 75 * ppY, "yellow_pipe.png");
    }

    @Override
    public void render(float delta) {
        draw();
        if(deathBirdIndex < 0){
            movePipes();
            moveBirds();
        }
    }

    public void moveBirds(){
        for(int i = 0; i < birdsQuantity; ++i){
            Bird bird = birds.get(i);
            bird.speedY -= Bird.gravity;
            if(bird.speedY < -1) bird.speedY = -1;
            birds.set(i, bird);
            birds.get(i).addCoordinates(Bird.speedX * ppY, birds.get(i).speedY * ppY);
            boolean jump = false;
            float coordinateY = (birds.get(i).getY() - pz.getY()) / ppY;
            if (coordinateY < 25) {
                jump = true;
            } else if (coordinateY < 60) {
                jump = random.nextInt(10) == 0;
            } else if (coordinateY < 75) {
                jump = random.nextInt(30) == 0;
            }
            if (jump){
                birds.get(i).speedY = 1.5f;
            }
        }

        if(birds.get(0).getX() + birds.get(0).getSizeX() < pz.getX()) {
            Bird bird = birds.remove(0);
            bird.addCoordinates(120 * ppY, 0);
            birds.add(bird);
        }
    }

    public void movePipes(){
        if(Gdx.input.isTouched()) {
            pipesCoordinateY -= Gdx.input.getDeltaY() * 2f;
        }
        if(pipesCoordinateY < 45 * ppY + pz.getY()){
            pipesCoordinateY = 45 * ppY + pz.getY();
        } else if (pipesCoordinateY > 93 * ppY + pz.getY()){
            pipesCoordinateY = 93 * ppY + pz.getY();
        }
    }

    public void isCollide(){
        Rectangle pipe_1 = new Rectangle(
                pz.getX() + ppX * 20,
                pipesCoordinateY,
                75 * ppY * pipeAspectRatio,
                75 * ppY);
        Rectangle pipe_2 = new Rectangle(
                pz.getX() + ppX * 20,
                pipesCoordinateY - 75 * ppY - distanceBetweenPipes,
                75 * ppY * pipeAspectRatio,
                75 * ppY);

        for(int i = 0; i < birdsQuantity; i++){
            //if(Collision.isCollision())
        }
    }

    public void draw(){
        batch.begin();
        ScreenUtils.clear(1, 1, 1, 1);
        backGround.draw();

        pipe.draw(pipe.getX(), pipesCoordinateY, false, true);
        pipe.draw(pipe.getX(), pipesCoordinateY - 75 * ppY - distanceBetweenPipes);

        for(int i = 0; i < birdsQuantity; ++i){
            birds.get(i).draw(birds.get(i).angle);
        }

        base.draw();
        space[0].draw();
        space[1].draw();

        batch.end();
    }

    @Override
    public void dispose() {
        backGround.dispose();

        for(int i = 0; i < birdsQuantity; ++i){
            birds.get(i).dispose();
        }

        base.dispose();

        space[0].dispose();
        space[1].dispose();

        pipe.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}