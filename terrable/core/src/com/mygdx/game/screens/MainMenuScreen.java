package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Terrable;

public class MainMenuScreen implements Screen {
    

    final Terrable game;

    private Texture playButton;
    private Texture exitButton;

   

    public MainMenuScreen(final Terrable game){
        this.game = game;

        playButton = new Texture("play.png");
        exitButton = new Texture("exit.png");

		game.cam.setPosition(game.WIDTH/2, game.HEIGHT/2);
        game.cam.update();



    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,1);
        game.batch.setProjectionMatrix(game.cam.combined());


        game.batch.begin();
        

        
        
        if (game.cam.getInputInGameWorld().x > game.WIDTH/2-250 && game.cam.getInputInGameWorld().x < game.WIDTH/2+250 && game.cam.getInputInGameWorld().y > game.HEIGHT/2 -150 && game.cam.getInputInGameWorld().y < game.HEIGHT/2 -50 ){
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                //game.gameScreen.map.GenerateNewMap(game.gameScreen.player);
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
       game.gameScreen.map.dispose();
       System.gc();
        
    }

    @Override
    public void dispose() {
      

    }
}