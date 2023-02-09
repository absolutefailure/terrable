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

	private Player player;



	final int MAP_SIZE_X = 5000; // blocks
	final int MAP_SIZE_Y = 300; // blocks

    final Terrable game;


    public GameScreen(final Terrable game){
        this.game = game;

	
        


		// Create player and set starting position
		player = new Player(0,0); 

		
        map = new Map(MAP_SIZE_X, MAP_SIZE_Y);

        

    }



  

    @Override
    public void render(float delta) {
 	// CLEAR SCREEN WITH SKY COLOR
        ScreenUtils.clear(0.35f, 0.7f, 1f, 1);

        game.batch.setProjectionMatrix(game.cam.combined());
        game.batch.begin();

        

        //Gdx.graphics.setTitle(""+Gdx.graphics.getFramesPerSecond());
        
        
        

        // draw stuff 


        map.Draw(game.batch, player);

        player.Update(map, game.cam.getCamera(), game.batch);
        
    
    
        

        game.batch.end();

        game.cam.setPosition(player.getX(), player.getY()); 
        game.cam.update();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || player.getPlayerHealth() <= 0) {

			game.setScreen(game.mainMenuScreen);

            
		}

    }



    @Override
    public void show() {
        // TODO Auto-generated method stub
        map.GenerateNewMap(player);
        player.setPlayerHealth(5);
    }


    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
        
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub


        
    }

    @Override
    public void dispose() {

        
    }
}
