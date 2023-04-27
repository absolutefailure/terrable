package com.mygdx.game.player;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.camera.HudCamera;

public class Recipebook {
    private int pageNumber = 0;
    private boolean isOpen = false;
    private boolean isUnlocked = false;
    private ArrayList<Integer> unlockedPages = new ArrayList<>();

    private Texture arrowRight;
    private Texture arrowLeft;
    private Texture brokenPage = new Texture("recipes/brokenpage.png");

    private Texture[] pages = new Texture[17];
    private Texture[] pages2 = new Texture[17];

    private Texture recipeButtonTexture;

    private Vector2 mouseInWorld2D = new Vector2();
    private Vector3 mouseInWorld3D = new Vector3();

    public Recipebook() {
        arrowRight = new Texture("arrowright.png");
        arrowLeft = new Texture("arrowleft.png");
        recipeButtonTexture = new Texture("recipeButton.png");
        pages[0] = new Texture("recipes/woodpage1.png");
        pages[1] = new Texture("recipes/woodpage2.png");
        pages[2] = new Texture("recipes/woodpage3.png");
        pages[3] = new Texture("recipes/woodpage4.png");
        pages[4] = new Texture("recipes/stonepage1.png");
        pages[5] = new Texture("recipes/stonepage2.png");
        pages[6] = new Texture("recipes/foodpage1.png");
        pages[7] = new Texture("recipes/coalpage1.png");
        pages[8] = new Texture("recipes/ironpage1.png");
        pages[9] = new Texture("recipes/ironpage2.png");
        pages[10] = new Texture("recipes/ironpage3.png");
        pages[11] = new Texture("recipes/copperpage1.png");
        pages[12] = new Texture("recipes/copperpage2.png");
        pages[13] = new Texture("recipes/diamondpage1.png");
        pages[14] = new Texture("recipes/rocketpage1.png");
        pages[15] = new Texture("recipes/rocketpage2.png");
        pages[16] = new Texture("recipes/rocketpage3.png");

        for (int i = 0; i < 17; i++){
            pages2[i] = brokenPage;
        }
    }

    public void Draw(Batch batch, HudCamera cam) {

        if(isUnlocked) {
            // Unlock pages based on unlocked achievements
            for (int i = 0; i < unlockedPages.size(); i++){

                // TIMBER
                if (unlockedPages.contains(0)){
                    pages2[0] = pages[0];
                    pages2[1] = pages[1];
                    pages2[2] = pages[2];
                    pages2[3] = pages[3];                   
                }   

                // Rock solid
                if (unlockedPages.contains(1)){
                    pages2[4] = pages[4];
                    pages2[5] = pages[5];
                    pages2[6] = pages[6];
                    pages2[7] = pages[7];
                }

                // Ironworks
                if (unlockedPages.contains(2)){
                    pages2[8] = pages[8];
                    pages2[9] = pages[9];
                    pages2[10] = pages[10];
                    pages2[11] = pages[11];
                    pages2[12] = pages[12];
                    pages2[13] = pages[13];
                }

                // What is this sorcery?
                if (unlockedPages.contains(3)){
                    pages2[14] = pages[14];
                    pages2[15] = pages[15];
                    pages2[16] = pages[16];
                }
            }
        }

        if (button(batch, cam, 900, 400, 25, 25, recipeButtonTexture) && !isOpen) {
            isOpen = true;
        } else if (button(batch, cam, 900, 400, 25, 25, recipeButtonTexture)) {
            isOpen = false;
        }

        if (isOpen) {
            batch.draw(pages2[pageNumber], 1081 - (271 / 2), 245 - (88 / 2));

            if (pageNumber > 0 && button(batch, cam, 1085 - (271 / 2), 250 - (88 / 2), 25, 25, arrowLeft)) {
                if (pageNumber > 0) {
                    pageNumber--;
                }
            }
            if (pageNumber < 16 && button(batch, cam, 1251 - (271 / 2), 250 - (88 / 2), 25, 25, arrowRight)) {
                if (pageNumber < 16) {
                    pageNumber++;
                }
            }
        }

    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean isUnlocked, int unlockedPage) {
        this.isUnlocked = isUnlocked;
        unlockedPages.add(unlockedPage);
    }
    public void reset(){
        isUnlocked = false;
        unlockedPages.clear();
        for (int i = 0; i < 17; i++){
            pages2[i] = brokenPage;
        }
    }

    private boolean button(Batch batch, HudCamera cam, int x, int y, int buttonSizeX, int buttonSizeY, Texture t) {

        mouseInWorld3D.x = Gdx.input.getX();
        mouseInWorld3D.y = Gdx.input.getY();
        mouseInWorld3D.z = 0;
        cam.getCamera().unproject(mouseInWorld3D);
        mouseInWorld2D.x = mouseInWorld3D.x;
        mouseInWorld2D.y = mouseInWorld3D.y;

        if (mouseInWorld2D.x > x && mouseInWorld2D.x < x + buttonSizeX && mouseInWorld2D.y > y
                && mouseInWorld2D.y < y + buttonSizeY) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                return true;
            }
            batch.setColor(0.8f, 0.8f, 0.8f, 1f);
            batch.draw(t, x, y);
        } else {
            batch.draw(t, x, y);
        }
        batch.setColor(1, 1, 1, 1);

        return false;
    }

}
