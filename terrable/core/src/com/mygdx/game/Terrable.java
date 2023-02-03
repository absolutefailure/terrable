package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.map.Map;

public class Terrable extends ApplicationAdapter {
	SpriteBatch batch;
	
	Map map;

	Player player;

	final int SCREEN_X = 1600;
	final int SCREEN_Y = 900;

	final int MAP_SIZE_X = 5000; // blocks
	final int MAP_SIZE_Y = 300; // blocks


    public OrthographicCamera cam; 


	@Override
	public void create () {
		batch = new SpriteBatch();

		Gdx.graphics.setWindowedMode(SCREEN_X, SCREEN_Y);

		cam = new OrthographicCamera();



		// Create player and set starting position
		player = new Player(0,0); 

		map = new Map(MAP_SIZE_X, MAP_SIZE_Y);
		map.GenerateNewMap(player);

		cam.setToOrtho(false, SCREEN_X , SCREEN_Y);

	}

	@Override
	public void render () {
		// CLEAR SCREEN WITH SKY COLOR
		ScreenUtils.clear(0.35f, 0.7f, 1f, 1);


		batch.setProjectionMatrix(cam.combined);

		//Gdx.graphics.setTitle(""+Gdx.graphics.getFramesPerSecond());
		
		


		// draw stuff 
		batch.begin();

		map.Draw(batch, player);

		player.Update(map, cam, batch);
		
	
		batch.end();



		cam.position.set(player.getX(), player.getY(),0f);
		cam.update();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
