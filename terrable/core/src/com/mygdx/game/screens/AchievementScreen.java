package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Terrable;
import com.mygdx.game.player.Achievement;
import com.mygdx.game.player.AchievementManager;

public class AchievementScreen implements Screen {
    private  SpriteBatch batch;
    private  BitmapFont font;
    private final AchievementManager achievementManager;
    final Terrable game;

    public AchievementScreen(final Terrable game, int volume, AchievementManager achievementManager) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);
        this.achievementManager = achievementManager;
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
        Array<Achievement> achievements = achievementManager.getAchievements();
        float y = Gdx.graphics.getHeight() - 150;
        for (Achievement achievement : achievements) {
            font.setColor(achievement.isUnlocked() ? Color.GREEN : Color.RED);
            font.draw(batch, achievement.getName(), 50, y, Gdx.graphics.getWidth() - 100, Align.left, false);
            font.setColor(Color.WHITE);
            font.draw(batch, achievement.getDescription(), 50, y - 50, Gdx.graphics.getWidth() - 100, Align.left, false);
            y -= 100;
        }
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {

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