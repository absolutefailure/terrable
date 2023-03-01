package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Terrable;
import com.mygdx.game.save.SaveGame;

public class MainMenuScreen implements Screen {
    

    final Terrable game;

    private Texture playButton;
    private Texture exitButton;
    private Sound menuSound;

    public MainMenuScreen(final Terrable game){
        this.game = game;

        playButton = new Texture("play.png");
        exitButton = new Texture("exit.png");
        menuSound = Gdx.audio.newSound(Gdx.files.internal("sounds/moaiSpawnSound.mp3"));
		game.cam.setPosition(game.WIDTH/2, game.HEIGHT/2);
        game.cam.update();

        game.gameScreen.map.GenerateNewMap(game.gameScreen.player);
        game.gameScreen.player.setPlayerHealth(10);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,1);
        game.batch.setProjectionMatrix(game.cam.combined());


        game.batch.begin();
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            SaveGame.Save(game.gameScreen.map.getMapArray(), game.gameScreen.player);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            game.gameScreen.map.setMapArray(null);
            System.gc();
            SaveGame.Load(game.gameScreen.map, game.gameScreen.player);
        }
        
        if (game.cam.getInputInGameWorld().x > game.WIDTH/2-250 && game.cam.getInputInGameWorld().x < game.WIDTH/2+250 && game.cam.getInputInGameWorld().y > game.HEIGHT/2 -150 && game.cam.getInputInGameWorld().y < game.HEIGHT/2 -50 ){
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                menuSound.stop();
                // reset game if player is dead
                if (game.gameScreen.player.getPlayerHealth() <= 0){
                    game.gameScreen.map.setMapArray(null);
                    game.gameScreen.player.resetInventory();
                    System.gc();
                    game.gameScreen.player.setPlayerHealth(10);
                    game.gameScreen.map.GenerateNewMap(game.gameScreen.player);
                }
                game.setScreen(game.gameScreen);
            }
            game.batch.setColor(0.5f,0.5f,0.5f,1);
            game.batch.draw(playButton,game.WIDTH/2-250, game.HEIGHT/2+50);
        }else{
            game.batch.draw(playButton,game.WIDTH/2-250, game.HEIGHT/2+50);
        }
        game.batch.setColor(1,1,1,1);


        if (game.cam.getInputInGameWorld().x > game.WIDTH/2-250 && game.cam.getInputInGameWorld().x < game.WIDTH/2+250 && game.cam.getInputInGameWorld().y > game.HEIGHT/2 && game.cam.getInputInGameWorld().y < game.HEIGHT/2 + 100 ){
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                Gdx.app.exit();
            }
            game.batch.setColor(0.5f,0.5f,0.5f,1);
            game.batch.draw(exitButton,game.WIDTH/2-250, game.HEIGHT/2-100);
        }else{
            game.batch.draw(exitButton,game.WIDTH/2-250, game.HEIGHT/2-100);
        }
        game.batch.setColor(1,1,1,1);

        game.batch.end();

    }

    @Override
    public void show() {
        game.cam.setPosition(game.WIDTH/2, game.HEIGHT/2);
        game.cam.update();
        menuSound.loop(0.01f);
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
}
