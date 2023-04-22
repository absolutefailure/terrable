package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.CustomInputProcessor;
import com.mygdx.game.Terrable;
import com.mygdx.game.map.Map;
import com.mygdx.game.player.Player;
import com.mygdx.game.save.SaveGame;

public class GameScreen implements Screen {
   

    Map map;

	Player player;

    BitmapFont font = new BitmapFont();
	final int MAP_SIZE_X = 5000; // blocks
	final int MAP_SIZE_Y = 300; // blocks

    private Boolean isPaused = false;
    private Vector2 mouseInWorld2D = new Vector2();
    private Vector3 mouseInWorld3D = new Vector3();

    private Texture resumeTexture;
    private Texture achievementsTexture;
    private Texture exitsaveTexture;
    
    final Terrable game;

    CustomInputProcessor customInputProcessor;

    public int volume;
    public GameScreen(final Terrable game, int volume){
        this.game = game;

        this.volume = volume;
	
		// Create player and set starting position
		player = new Player(0,0); 
		
        map = new Map(MAP_SIZE_X, MAP_SIZE_Y);

        resumeTexture = new Texture("menubuttons/resume.png");
        achievementsTexture = new Texture("menubuttons/achievements.png");
        exitsaveTexture = new Texture("menubuttons/saveexit.png");

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
		customInputProcessor = new CustomInputProcessor();
		inputMultiplexer.addProcessor(customInputProcessor);
		Gdx.input.setInputProcessor(inputMultiplexer);

    }



  

    @Override
    public void render(float delta) {

        Gdx.graphics.setTitle(""+Gdx.graphics.getFramesPerSecond());
        if (!isPaused){
            delta *= 60;
        }else{
            delta = 0;
        }
        
        if (delta > 2f){delta = 2f;}

        //victory screen?????? emt 
        if(player.getY() > 10000){
            game.setScreen(game.achievementScreen);
        }

        game.batch.begin();
        if (player.getPlayerHealth() > 0){
            game.batch.setProjectionMatrix(game.cam.combined());

            // draw world 
            
    
    
            map.Draw(game.batch, player, volume, delta);
    
            player.Update(map, game.cam.getCamera(), game.batch, volume, delta, MAP_SIZE_X, MAP_SIZE_Y);
            
            map.UpdateWater(game.batch, player, delta);
            // draw hud
            game.batch.setProjectionMatrix(game.hudCam.combined());
    
    
            player.DrawHud(game.batch, game.hudCam, map.getMapArray(), delta, customInputProcessor, MAP_SIZE_X, MAP_SIZE_Y);
        
            
        }else{
            game.batch.setProjectionMatrix(game.cam.combined());

            // draw world 

    
            map.Draw(game.batch, player, volume, delta);
            map.UpdateWater(game.batch, player, delta);
            // draw hud
            game.batch.setProjectionMatrix(game.hudCam.combined());
    
            font.draw(game.batch, "You died", 780, 450);

        
            
        }

        if(isPaused){
            if(button(1600 / 2 - 100, 900 / 2 +50, 200,50,resumeTexture)){
                isPaused = false;
                player.setPaused(false);
            }
            if(button(1600 / 2 - 100, 900 / 2 -50, 200,50,achievementsTexture)){
                game.setScreen(game.achievementScreen);
            }
            if(button(1600 / 2 - 100, 900 / 2 - 150, 200,50,exitsaveTexture)){
                isPaused = false;
                player.setPaused(false);
                SaveGame.Save(map, player);
                game.setScreen(game.mainMenuScreen);
            }
        }
        
        
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if(isPaused){
                isPaused = false;
                player.setPaused(false);
            }else{
                isPaused = true;
                player.setPaused(true);
            }
			//game.setScreen(game.mainMenuScreen);
		}

    }

    public boolean button(int x, int y, int buttonSizeX, int buttonSizeY, Texture t) {

        mouseInWorld3D.x = Gdx.input.getX();
        mouseInWorld3D.y = Gdx.input.getY();
        mouseInWorld3D.z = 0;
        game.hudCam.getCamera().unproject(mouseInWorld3D);
        mouseInWorld2D.x = mouseInWorld3D.x;
        mouseInWorld2D.y = mouseInWorld3D.y;
        game.batch.setColor(0.5f, 0.5f, 0.5f, 1);
        if (mouseInWorld2D.x > x && mouseInWorld2D.x < x + buttonSizeX && mouseInWorld2D.y > y
                && mouseInWorld2D.y < y + buttonSizeY) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                return true;
            }
            game.batch.setColor(1, 1, 1, 1);
            game.batch.draw(t, x, y);
        } else {
            game.batch.draw(t, x, y);
        }
        
        game.batch.setColor(1, 1, 1, 1);
        return false;
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
