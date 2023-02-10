package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.camera.GameCamera;
import com.mygdx.game.camera.HudCamera;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MainMenuScreen;

public class Terrable extends Game {
	public SpriteBatch batch;
	
	public GameCamera cam;
	public HudCamera hudCam;

	public final int WIDTH = 1600;
	public final int HEIGHT = 900;

	public GameScreen gameScreen;
	public MainMenuScreen mainMenuScreen;

	Terrable game = this;



	
	public void create (){
		batch = new SpriteBatch();




		cam = new GameCamera(WIDTH, HEIGHT);
		hudCam = new HudCamera(WIDTH, HEIGHT);

		Gdx.graphics.setWindowedMode(WIDTH, HEIGHT);

		mainMenuScreen = new MainMenuScreen(this);
		gameScreen = new GameScreen(this);

		this.setScreen(mainMenuScreen);


	}
	
	@Override
	public void render () {


		//batch.setProjectionMatrix(cam.combined());
		super.render();

	}


}
