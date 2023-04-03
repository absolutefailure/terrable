package com.mygdx.game.player;

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

    private Texture arrowRight;
    private Texture arrowLeft;

    private Texture backgroundTexture;
    private Texture[] pages = new Texture[8];

    private Texture recipeButtonTexture;

    private Vector2 mouseInWorld2D = new Vector2();
    private Vector3 mouseInWorld3D = new Vector3();


    public Recipebook(){
        arrowRight = new Texture("arrowright.png");
        arrowLeft = new Texture("arrowleft.png");
        recipeButtonTexture = new Texture("recipeButton.png");
        backgroundTexture = new Texture("recipes/recipeBook.png");
        pages[0] = new Texture("recipes/page1.png");
        pages[1] = new Texture("recipes/page2.png");
        pages[2] = new Texture("recipes/page3.png");
        pages[3] = new Texture("recipes/page4.png");
        pages[4] = new Texture("recipes/page5.png");
        pages[5] = new Texture("recipes/page6.png");
        pages[6] = new Texture("recipes/page7.png");
        pages[7] = new Texture("recipes/page8.png");


    }

    public void Draw(Batch batch, HudCamera cam){

        if (button(batch, cam,900,400,25,25,recipeButtonTexture) && !isOpen){
            isOpen = true;
        }else if(button(batch, cam,900,400,25,25,recipeButtonTexture)){
            isOpen = false;
        }


        if (isOpen){
            batch.draw(backgroundTexture,1081 - (271 / 2), 245 - (88 / 2));
            batch.draw(pages[pageNumber], 1106 - (271 / 2), 275 - (88 / 2));

            if (pageNumber > 0 && button(batch,cam,1085 - (271 / 2), 250 - (88 / 2), 25, 25, arrowLeft)){
                if(pageNumber > 0){
                    pageNumber--;
                }
            }
            if (pageNumber < 7 && button(batch,cam,1251 - (271 / 2), 250 - (88 / 2), 25, 25, arrowRight)){
                if(pageNumber < 7){
                    pageNumber++;
                }
            }
        }

    }

    public void setOpen(boolean isOpen){
        this.isOpen = isOpen;
    }

    public boolean isOpen(){
        return isOpen;
    }

    private boolean button(Batch batch,HudCamera cam, int x, int y, int buttonSizeX, int buttonSizeY, Texture t) {

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
        batch.setColor(1,1,1, 1);

        return false;
    }

}