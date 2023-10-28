package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Bird;
import com.mygdx.game.Main;
import com.mygdx.game.RealClasses.AnimatedPictureBox;
import com.mygdx.game.RealClasses.Button;
import com.mygdx.game.RealClasses.Collision;
import com.mygdx.game.RealClasses.PictureBox;
import com.mygdx.game.RealClasses.Rectangle;
import com.mygdx.game.RealClasses.TextBox;

import static com.mygdx.game.Main.*;

import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements Screen {
    Main game;
    State state = State.START;

    PictureBox backGround;
    PictureBox base;
    Rectangle pz; // playZone
    float ppX;
    float ppY;

    PictureBox startOverlay;
    Button restartButton;

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
    Texture deadBird;

    // Sounds
    Sound pointSound;
    Sound deadSound;
    Sound killSound;

    // score
    long score;
    long highScore;
    TextBox scoreUI;
    TextBox highScoreUI;

    public FlappyBird(Main game) {
        this.game = game;

        pointSound = Gdx.audio.newSound(Gdx.files.internal("sounds/point.mp3"));
        deadSound = Gdx.audio.newSound(Gdx.files.internal("sounds/dead.wav"));
        killSound = Gdx.audio.newSound(Gdx.files.internal("sounds/kill.wav"));

        boolean isWider = scrX / scrY > defaultAspectRatio;
        float x = scrX;
        float y = scrY;

        if (isWider) x = scrY * defaultAspectRatio;
        else y = scrX / defaultAspectRatio;

        pz = new Rectangle((scrX - x) / 2, (scrY - y) / 2, x, y);
        ppX = pz.getSizeX() / 100;
        ppY = pz.getSizeY() / 100;
        //distanceBetweenPipes = ppY * 25;
        distanceBetweenPipes = ppY * 35;

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
        startOverlay = new PictureBox(pz.getX(), pz.getY(), pz.getSizeX(),
                pz.getSizeY(), "start overlay.png");
        base = new PictureBox(pz.getX(), pz.getY(), pz.getSizeX(),
                pz.getSizeX() / 3, "base.png");
        restartButton = new Button(pz.getX() + ppX * 20, pz.getY() + ppY * 60, ppX * 60, ppX * 24,
                "restart.png", "restart", 0xffffffff, (int)ppX * 12);

        birds = new ArrayList<>();
        deadBird = new Texture("birds/deadBird.png");
        pipe = new PictureBox(pz.getX() + ppX * 20, 0, 75 * ppY * pipeAspectRatio, 75 * ppY,
                "bluePipe.png");

        scoreUI = new TextBox(pz.getX() + ppX * 20, pz.getY() + ppY * 98, String.valueOf(score),
                0xffffffff, (int)ppX * 10);
        highScoreUI = new TextBox(pz.getX() + ppX * 40, pz.getY() + ppY * 98, String.valueOf(score),
                0xffffffff, (int)ppX * 10);

        respawn();
    }

    @Override
    public void render(float delta) {
        batch.begin();
        draw();
        switch (state){
            case START:
                startOverlay.draw();
                if (Gdx.input.isTouched()) state = State.FLY;
                break;
            case DEAD:
                moveDeadBird();
                break;
            case FLY:
                moveDeadBird();
                movePipes();
                moveBirds();
                isCollide();
                break;
            case PAUSE:
                restartButton.draw();
                if(Gdx.input.justTouched()){
                    if(restartButton.isTouched(true)){
                        respawn();
                        state = State.FLY;
                    }
                }
                break;
        }
        batch.end();
    }

    public void respawn(){
        pipesCoordinateY = pz.getY() + ppY * 72;
        birds.clear();
        for(int i = 0; i < birdsQuantity; ++i){
            birds.add(spawnBird(pz.getX() + (i + 2) * 40 * ppY));
        }
        score = 0;
        highScore = prefs.getLong("highScore", 0);
    }

    public Bird spawnBird(float coordinateX){
        boolean isGreen = random.nextBoolean();
        String filePath = isGreen ? "greenBird-" : "redBird-";
        return new Bird(coordinateX, pz.getY() + 40 * ppY, pz.getSizeX() * 0.17f,
                pz.getSizeX() * 0.12f, "birds/" + filePath, "png", 3, true, 0.07f, isGreen);

    }

    public void moveDeadBird(){
        for(int i = 0; i < birdsQuantity; ++i){
            if(birds.get(i).isAlive) continue;

            birds.get(i).addCoordinates(0, -Bird.gravity * ppY * 7);
            batch.draw(deadBird, birds.get(i).getX() + ppX * 15, birds.get(i).getY(),
                    deadBird.getWidth() / 2.0f, deadBird.getHeight() / 2.0f,
                    birds.get(i).getSizeX(), birds.get(i).getSizeY(), 1, 1, 90, 0, 0,
                    deadBird.getWidth(), deadBird.getHeight(), false, false);

            if(birds.get(i).getY() + birds.get(i).getSizeX()
                    < pz.getSizeX()/3 && birds.get(i).isGreen) state = State.PAUSE;
        }
    }

    public void moveBirds(){
        for(int i = 0; i < birdsQuantity; ++i){
            if(!birds.get(i).isAlive) continue;
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
                birds.get(i).speedY = 1.2f;
            }
        }

        if(birds.get(0).getX() + birds.get(0).getSizeX() < pz.getX()) {
            birds.get(0).dispose();
            float x = birds.remove(0).getX();
            birds.add(spawnBird(x + 120 * ppY));
        }
    }

    public void movePipes(){
        if(Gdx.input.isTouched()) {
            pipesCoordinateY -= Gdx.input.getDeltaY() * 2f;
        }
        if(pipesCoordinateY < 25 * ppY + pz.getY() + distanceBetweenPipes){
            pipesCoordinateY = 25 * ppY + pz.getY() + distanceBetweenPipes;
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
            if(!birds.get(i).isAlive) continue;
            if(Collision.isCollision(pipe_1, birds.get(i)) ||
                    Collision.isCollision(pipe_2, birds.get(i))) {
                birds.get(i).isAlive = false;
                if(birds.get(i).isGreen){
                    state = State.DEAD;
                    deadSound.play(0.75f);
                } else killSound.play();
                break;
            }
        }
    }

    public void death(){

    }

    public void draw(){
        ScreenUtils.clear(1, 1, 1, 1);
        backGround.draw();

        pipe.draw(pipe.getX(), pipesCoordinateY, false, true);
        pipe.draw(pipe.getX(), pipesCoordinateY - 75 * ppY - distanceBetweenPipes);

        for(int i = 0; i < birdsQuantity; ++i){
            if (!birds.get(i).isAlive) continue;
            birds.get(i).draw(birds.get(i).angle);
            birds.get(i).getAngle();
        }
        moveDeadBird();

        base.draw();
        space[0].draw();
        space[1].draw();
    }

    @Override
    public void dispose() {
        backGround.dispose();
        startOverlay.dispose();

        for(int i = 0; i < birdsQuantity; ++i){
            birds.get(i).dispose();
        }

        base.dispose();
        deadBird.dispose();

        space[0].dispose();
        space[1].dispose();

        pipe.dispose();

        restartButton.dispose();
        scoreUI.dispose();
        highScoreUI.dispose();

        killSound.dispose();
        deadSound.dispose();
        pointSound.dispose();
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

    private enum State{
        START,
        DEAD,
        FLY,
        PAUSE
    }
}