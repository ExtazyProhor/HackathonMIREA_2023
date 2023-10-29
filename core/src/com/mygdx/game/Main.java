package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.RealClasses.Rectangle;
import com.mygdx.game.Screens.FlappyBird;

import java.util.Random;

public class Main extends Game {
	public static SpriteBatch batch;
	public static Preferences prefs;

	public static FreeTypeFontGenerator generator;
	public static FreeTypeFontGenerator.FreeTypeFontParameter parameter;
	public static GlyphLayout gl;

	public static Sound clickSound;

	public static float scrX;
	public static float scrY;
	public static float pppX; // pixels per percent width
	public static float pppY; // pixels per percent height

	public FlappyBird flappyBird;

	public static Random random;

	@Override
	public void create () {
		scrX = Gdx.graphics.getWidth();
		scrY = Gdx.graphics.getHeight();
		pppX = scrX/100;
		pppY = scrY/100;

		prefs = Gdx.app.getPreferences("HackathonMainSaves");

		clickSound = Gdx.audio.newSound(Gdx.files.internal("general/click.mp3"));

		random = new Random();

		initializationFont();
		batch = new SpriteBatch();
		batch.enableBlending();

		flappyBird = new FlappyBird(this);

		setScreen(flappyBird);
	}

	void initializationFont(){
		generator = new FreeTypeFontGenerator(Gdx.files.internal("general/Visitor Rus.ttf"));
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
				"абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ" +
				"абвгддждзеёжзійклмнопрстуўфхцчшыьэюяАБВГДДЖДЗЕЁЖЗІЙКЛМНОПРСТУЎФХЦЧШЫЬЭЮЯ" +
				"0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
		parameter.borderWidth = (int)(pppY / 2);
		parameter.borderColor = Color.valueOf("000000ff");
		gl = new GlyphLayout();
	}

	@Override
	public void dispose () {
		batch.dispose();
		generator.dispose();
		clickSound.dispose();
	}

	public static float textureAspectRatio(Texture texture, boolean toHeight){
		if(toHeight) return (float) texture.getWidth() / texture.getHeight();
		return (float) texture.getHeight() / texture.getWidth();
	}

	public static String divisionDigits(long value){
		String number;
		if(value >= 100000000000L) number = Long.toString(value/1000000000);
		else if(value >= 100000000) number = Long.toString(value/1000000);
		else if(value >= 100000) number = Long.toString(value/1000);
		else number = Long.toString(value);
		String newNumber = "";
		if(number.length() % 3 != 0){
			newNumber += number.substring(0, number.length() % 3);
			number = number.substring(number.length() % 3);
			if(number.length() > 0) newNumber += '.';
		}
		while(number.length() != 0){
			newNumber += number.substring(0, 3);
			number = number.substring(3);
			if(number.length() > 0) newNumber += '.';
		}
		if(value >= 100000000000L) newNumber += " B";
		else if(value >= 100000000) newNumber += " M";
		else if(value >= 100000) newNumber += " K";
		return newNumber;
	}

	public static void draw(Texture picture, Rectangle rectangle){
		batch.draw(picture, rectangle.getX(), rectangle.getY(),
				rectangle.getSizeX(), rectangle.getSizeY());
	}

	public static void draw(Texture picture, Rectangle rectangle, float x, float y, boolean flipX, boolean flipY){
		batch.draw(picture, x, y, rectangle.getSizeX(), rectangle.getSizeY(), 0, 0,
				picture.getWidth(), picture.getHeight(), flipX, flipY);
	}
}