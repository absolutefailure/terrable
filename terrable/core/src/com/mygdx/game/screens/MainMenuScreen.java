package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Terrable;
import com.mygdx.game.save.SaveGame;

public class MainMenuScreen implements Screen {

    final Terrable game;

    private Texture newButton;
    private Texture saveButton;
    private Texture loadButton;
    private Texture quitButton;
    private Texture playButton;
    private Texture backArrow;
    private Texture selectArrow;
    private Texture volumeSlider, volumeBar;
    private Sound menuSound;

    private boolean volumeGrab = false;

    private Boolean isGameLoaded;

    private Vector2 mouseInWorld2D = new Vector2();
    private Vector3 mouseInWorld3D = new Vector3();

    public int volume;

    public MainMenuScreen(final Terrable game, int volume) {
        this.volume = volume;
        this.game = game;
        isGameLoaded = false;
        newButton = new Texture("menubuttons/newbutton.png");
        saveButton = new Texture("menubuttons/savebutton.png");
        loadButton = new Texture("menubuttons/loadbutton.png");
        quitButton = new Texture("menubuttons/quitbutton.png");
        playButton = new Texture("menubuttons/playbutton.png");
        backArrow = new Texture("menubuttons/backarrow.png");
        selectArrow = new Texture("menubuttons/selectarrow.png");
        volumeSlider = new Texture("menubuttons/volume2.png");
        volumeBar = new Texture("menubuttons/volume.png");
        menuSound = Gdx.audio.newSound(Gdx.files.internal("sounds/moaiSpawnSound.mp3"));
        game.cam.setPosition(game.WIDTH / 2, game.HEIGHT / 2);
        game.cam.update();

        game.gameScreen.map.GenerateNewMap(game.gameScreen.player);
        game.gameScreen.player.setPlayerHealth(10);

    }

    public boolean button(int x, int y, int buttonSizeX, int buttonSizeY, Texture t) {

        mouseInWorld3D.x = Gdx.input.getX();
        mouseInWorld3D.y = Gdx.input.getY();
        mouseInWorld3D.z = 0;
        game.cam.getCamera().unproject(mouseInWorld3D);
        mouseInWorld2D.x = mouseInWorld3D.x;
        mouseInWorld2D.y = mouseInWorld3D.y;

        if (mouseInWorld2D.x > x && mouseInWorld2D.x < x + buttonSizeX && mouseInWorld2D.y > y
                && mouseInWorld2D.y < y + buttonSizeY) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                return true;
            }
            game.batch.draw(selectArrow, x - 75, y + 37);
            game.batch.setColor(1, 1, 1, 1);
            game.batch.draw(t, x, y);
        } else {
            game.batch.draw(t, x, y);
        }
        game.batch.setColor(0.5f, 0.5f, 0.5f, 1);

        return false;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.setProjectionMatrix(game.cam.combined());
        mouseInWorld3D.x = Gdx.input.getX();
        mouseInWorld3D.y = Gdx.input.getY();
        mouseInWorld3D.z = 0;
        game.cam.getCamera().unproject(mouseInWorld3D);
        mouseInWorld2D.x = mouseInWorld3D.x;
        mouseInWorld2D.y = mouseInWorld3D.y;
        game.batch.begin();

        //MAIN MENU BUTTONS
        if(!isGameLoaded) {
            if (button(550, 600, 500, 150, newButton)) {
                menuSound.stop();
                // reset game if player is dead
                if (game.gameScreen.player.getPlayerHealth() <= 0) {
                    game.gameScreen.map.setMapArray(null);
                    game.gameScreen.player.resetInventory();
                    System.gc();
                    game.gameScreen.player.setPlayerHealth(10);
                    game.gameScreen.map.GenerateNewMap(game.gameScreen.player);
                }
                game.setScreen(game.gameScreen);
                game.batch.draw(newButton, 550, 600);
    
            } else if (button(550, 450, 500, 150, saveButton)) {
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    SaveGame.Save(game.gameScreen.map.getMapArray(), game.gameScreen.player);
                }
                game.batch.draw(saveButton, 550, 450);
            } else if (button(550, 300, 500, 150, loadButton)) {
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    isGameLoaded = true;
                    //game.gameScreen.map.dispose();
                    SaveGame.Load(game.gameScreen.map, game.gameScreen.player);
                    System.gc();
                }
                game.batch.draw(loadButton, 550, 300);
            } else if (button(550, 150, 500, 150, quitButton)) {
                Gdx.app.exit();
                game.batch.draw(quitButton, 550, 150);
            }

        } else {
            if(button(550, 450, 500, 150, playButton)) {
                menuSound.stop();
                isGameLoaded = false;
                // reset game if player is dead
                if (game.gameScreen.player.getPlayerHealth() <= 0) {
                    game.gameScreen.map.setMapArray(null);
                    game.gameScreen.player.resetInventory();
                    System.gc();
                    game.gameScreen.player.setPlayerHealth(10);
                    game.gameScreen.map.GenerateNewMap(game.gameScreen.player);
                }
                game.setScreen(game.gameScreen);
                game.batch.draw(playButton, 550, 600);
            } else if(button(550, 300, 500, 150, backArrow)) {
                    isGameLoaded = false;
                game.batch.draw(backArrow, 550, 150);
            }
        }
        game.batch.setColor(0.5f, 0.5f, 0.5f, 1);

        game.batch.draw(volumeBar, 1300f,110f);
        game.batch.draw(volumeSlider,1300+volume,100);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (mouseInWorld2D.x > 1300+volume && mouseInWorld2D.x < 1300+volume+25 && mouseInWorld2D.y > 100
            && mouseInWorld2D.y < 120) {
                volumeGrab = true;
            }
        }else{
            volumeGrab = false;
        }
        int oldVolume = volume;
        if (volumeGrab){
            volume = (int)mouseInWorld2D.x-1310;
        }
        if (volume > 175){volume = 175;}
        if(volume < 0){volume = 0;}
        if (oldVolume != volume){
            menuSound.stop();
            menuSound.loop(volume / 2000f);
            game.gameScreen.setVolume(volume);
        }
    
        game.batch.end();
        }

    @Override
    public void show() {
        game.cam.setPosition(game.WIDTH / 2, game.HEIGHT / 2);
        game.cam.update();
        menuSound.loop(volume / 2000f);
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
