package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.Terrable;
import com.mygdx.game.map.Map;
import com.mygdx.game.player.Player;

public class GameScreen implements Screen {
   

    Map map;

	Player player;

    BitmapFont font = new BitmapFont();
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

        Gdx.graphics.setTitle(""+Gdx.graphics.getFramesPerSecond());
        delta *= 60;
        if (delta > 2f){delta = 2f;}

        if (player.getPlayerHealth() > 0){
            game.batch.setProjectionMatrix(game.cam.combined());

            // draw world 
            game.batch.begin();
    
    
            map.Draw(game.batch, player, volume, delta);
    
            player.Update(map, game.cam.getCamera(), game.batch, volume, delta);
            
            // draw hud
            game.batch.setProjectionMatrix(game.hudCam.combined());
    
    
            player.DrawHud(game.batch, game.hudCam);
        
            game.batch.end();
        }else{
            game.batch.setProjectionMatrix(game.cam.combined());

            // draw world 
            game.batch.begin();
    
            map.Draw(game.batch, player, volume, delta);
            
            // draw hud
            game.batch.setProjectionMatrix(game.hudCam.combined());
    
            font.draw(game.batch, "You died", 780, 450);
        
            game.batch.end();
        }


        
        


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {

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
