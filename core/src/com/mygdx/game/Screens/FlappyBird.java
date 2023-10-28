package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
    public static Rectangle pz; // playZone
    public static float ppX;
    public static float ppY;

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

    float time = 0;
    float targetTime = 1;

    // Sounds
    Sound pointSound;
    Sound deadSound;
    Sound killSound;

    Music music;

    // score
    long score;
    long highScore;
    TextBox scoreText;
    TextBox highScoreText;
    PictureBox scoreUI;
    PictureBox highScoreUI;

    Rectangle[] livesPositions;
    Texture redHeart;
    Texture greyHeart;
    final int MAX_LIVES = 3;
    int lives;

    public FlappyBird(Main game) {
        this.game = game;

        pointSound = Gdx.audio.newSound(Gdx.files.internal("sounds/point.mp3"));
        deadSound = Gdx.audio.newSound(Gdx.files.internal("sounds/dead.wav"));
        killSound = Gdx.audio.newSound(Gdx.files.internal("sounds/kill.wav"));

        music = Gdx.audio.newMusic(Gdx.files.internal("music/track.mp3"));
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();

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
                pz.getSizeY(), "UI/start overlay.png");
        base = new PictureBox(pz.getX(), pz.getY(), pz.getSizeX(),
                pz.getSizeX() / 3, "base.png");
        restartButton = new Button(pz.getX() + ppX * 20, pz.getY() + ppY * 55, ppX * 60, ppX * 24,
                "restart.png", "restart", 0xffffffff, (int)ppX * 12);

        birds = new ArrayList<>();
        deadBird = new Texture("birds/deadBird.png");
        pipe = new PictureBox(pz.getX() + ppX * 20, 0, 75 * ppY * pipeAspectRatio, 75 * ppY,
                "pipes/pipe-0.png");

        scoreText = new TextBox(pz.getCornerLU().x + 35 * ppX, pz.getY() + ppY * 98,
                String.valueOf(score), 0xffffffff, (int)ppX * 10);
        respawn();
        highScoreText = new TextBox(pz.getCornerLU().x + 75 * ppX, pz.getY() + ppY * 98,
                String.valueOf(highScore), 0xffffffff, (int)ppX * 10);

        float uiHeight = 40 * ppX / 3;
        scoreUI = new PictureBox(pz.getCornerLU().x + 20 * ppX, pz.getCornerLU().y - uiHeight,
                40 * ppX, uiHeight, "UI/score.png");
        highScoreUI = new PictureBox(pz.getCornerLU().x + 60 * ppX, pz.getCornerLU().y - uiHeight,
                40 * ppX, uiHeight, "UI/highScore.png");

        float healthSize = 6 * ppX;
        livesPositions = new Rectangle[3];
        livesPositions[0] = new Rectangle(pz.getCornerLU().x + ppX, pz.getCornerLU().y - ppX -  healthSize,
                healthSize,healthSize);
        livesPositions[1] = new Rectangle(pz.getCornerLU().x + 7 * ppX, pz.getCornerLU().y - 4 * ppX -  healthSize,
                healthSize,healthSize);
        livesPositions[2] = new Rectangle(pz.getCornerLU().x + 13 * ppX, pz.getCornerLU().y - ppX -  healthSize,
                healthSize,healthSize);

        redHeart = new Texture("UI/redHeart.png");
        greyHeart = new Texture("UI/greyHeart.png");
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
            case FLY:
                movePipes();
                moveBirds();
                isCollide();
                break;
            case LOSE:
                moveDeadBirds();
                time += Gdx.graphics.getDeltaTime();
                if(time >= targetTime){
                    time = 0;
                    state = State.PAUSE;
                }
                break;
            case PAUSE:
                moveDeadBirds();
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
        scoreText.changeText("0");
        highScore = prefs.getLong("highScore", 0);
        lives = 3;
    }

    public Bird spawnBird(float coordinateX){
        boolean isGreen = random.nextBoolean();
        String filePath = isGreen ? "greenBird-" : "redBird-";
        return new Bird(coordinateX, pz.getY() + 40 * ppY, pz.getSizeX() * 0.17f,
                pz.getSizeX() * 0.12f, "birds/" + filePath, "png", 3, true, 0.07f, isGreen);

    }

    public void moveBirds() {
        for(int i = 0; i < birdsQuantity; ++i){
            float x1 = birds.get(i).getCornerRU().x;
            birds.get(i).moveBird();
            float x2 = birds.get(i).getCornerRU().x;
            if(!(x1 > pz.getX() + ppX * 20 && x2 < pz.getX() + ppX * 20)) continue;
            if(birds.get(i).isGreen){
                score++;
                scoreText.changeText(String.valueOf(score));
                pointSound.play();
            } else {
                death(i);
            }
        }

        if(birds.get(0).getVirtualX() + birds.get(0).getSizeX() < pz.getX()) {
            birds.get(0).dispose();
            float x = birds.remove(0).getVirtualX();
            birds.add(spawnBird(x + 120 * ppY));
        }
    }

    public void moveDeadBirds(){
        for(int i = 0; i < birdsQuantity; ++i){
            if (birds.get(i).isAlive) continue;
            birds.get(i).moveBird();
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
                    death(i);
                } else {
                    killSound.play();
                    score++;
                    scoreText.changeText(String.valueOf(score));
                }
                break;
            }
        }
    }

    public void death(int index){
        deadSound.play(0.75f);
        deathBirdIndex = index;
        if(--lives > 0) return;
        state = State.LOSE;
        if (score > highScore) {
            prefs.putLong("highScore", score);
            prefs.flush();
            highScore = score;
            highScoreText.changeText(String.valueOf(highScore));
        }
    }

    public void draw(){
        ScreenUtils.clear(1, 1, 1, 1);
        backGround.draw();

        pipe.draw(pipe.getX(), pipesCoordinateY, false, true);
        pipe.draw(pipe.getX(), pipesCoordinateY - 75 * ppY - distanceBetweenPipes);

        for(int i = 0; i < birdsQuantity; ++i){
            birds.get(i).draw();
        }

        int i = 0;
        for(; i < lives; ++i){
            Main.draw(redHeart, livesPositions[i]);
        }
        for(; i < MAX_LIVES; ++i) {
            Main.draw(greyHeart, livesPositions[i]);
        }

        scoreUI.draw();
        highScoreUI.draw();
        scoreText.draw();
        highScoreText.draw();

        base.draw();
        space[0].draw();
        space[1].draw();
    }

    @Override
    public void dispose() {
        redHeart.dispose();
        greyHeart.dispose();

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
        scoreText.dispose();
        highScoreText.dispose();

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
        FLY,
        LOSE,
        PAUSE
    }
}