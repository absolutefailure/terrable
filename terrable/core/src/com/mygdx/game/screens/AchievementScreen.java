package com.mygdx.game.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Terrable;
import com.mygdx.game.player.Achievement;
import com.mygdx.game.player.AchievementManager;

public class AchievementScreen implements Screen {
    private  SpriteBatch batch;
    private  BitmapFont font;
    private  BitmapFont nameFont;
    final Terrable game;
    ArrayList<Achievement> achievementlist = AchievementManager.getAchievements();

    public AchievementScreen(final Terrable game, int volume) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/Cambria18.fnt"));
        nameFont = new BitmapFont(Gdx.files.internal("fonts/Cambria18.fnt"));
        font.getData().setScale((float) 1.5);
        nameFont.getData().setScale(2);
    }

    @Override
    public void render(float delta) {
        batch.begin();

        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Draw the title
        font.setColor(Color.WHITE);
        font.draw(batch, "Achievements", 0, Gdx.graphics.getHeight() - 50, Gdx.graphics.getWidth(), Align.center, false);
        
        // Draw the list of achievements
        float y = Gdx.graphics.getHeight() - 150;
        int split = 0;
        for (Achievement achievement : achievementlist) {
            nameFont.setColor(achievement.isUnlocked() ? Color.GREEN : Color.RED);
            if (split % 2 == 0){
                nameFont.draw(batch, achievement.getName(), 50, y, Gdx.graphics.getWidth() - 100, Align.left, false);
                font.draw(batch, achievement.getDescription(), 50, y - 50, Gdx.graphics.getWidth() - 100, Align.left, false);
            }else {
                nameFont.draw(batch, achievement.getName(), 50, y, Gdx.graphics.getWidth() - 100, Align.center, false);
                font.draw(batch, achievement.getDescription(), 50, y - 50, Gdx.graphics.getWidth() - 100, Align.center + 4, false);
                y -= 100;
            }
            font.setColor(Color.WHITE);
            split++;
        }
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {

            game.setScreen(game.gameScreen);
    
            
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {

    }
}