package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Player;
import com.mygdx.game.Terrable;
import com.mygdx.game.map.Map;

public class GameScreen implements Screen {
   

    Map map;

	Player player;


	final int MAP_SIZE_X = 5000; // blocks
	final int MAP_SIZE_Y = 300; // blocks

    final Terrable game;

    public int volume;
    public GameScreen(final Terrable game, int volume){
        this.game = game;

        this.volume = volume;
	
        


		// Create player and set starting position
		player = new Player(0,0); 
		
        map = new Map(MAP_SIZE_X, MAP_SIZE_Y);

        

    }



  

    @Override
    public void render(float delta) {
 	// CLEAR SCREEN WITH SKY COLOR
        ScreenUtils.clear(0.35f, 0.7f, 1f, 1);

        


        

        //Gdx.graphics.setTitle(""+Gdx.graphics.getFramesPerSecond());
        
        
        game.batch.setProjectionMatrix(game.cam.combined());

        // draw world 
        game.batch.begin();


        map.Draw(game.batch, player, volume);

        player.Update(map, game.cam.getCamera(), game.batch, volume);
        
        // draw hud
        game.batch.setProjectionMatrix(game.hudCam.combined());


        player.DrawHud(game.batch, game.hudCam);
    
        game.batch.end();

        
        


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || player.getPlayerHealth() <= 0) {

			game.setScreen(game.mainMenuScreen);

            
		}

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

    @Override
    public void dispose() {

        
    }

    public void setVolume(int volume){
        this.volume = volume;
    }
}
