package com.mygdx.game.mobs;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.Player;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.elements;

public class Chicken extends Mob {
    private Texture mobTexture;
    private float mobPosX;
    private float mobPosY;
    private int mobSizeX;
    private int mobSizeY;
    private float gravity;
    private float acceleration;
    private int mobHealth;
    private String type;
    private int element;

    public Chicken(float x, float y, Texture texture) {
        super();
        this.mobPosX = x;
        this.mobPosY = y;

        mobTexture = texture;

        mobSizeX = 20;
        mobSizeY = 40;

        gravity = 0;
        mobHealth = 5;
        type = "friendly";

        element = elements.FEATHER;
        
    }

    @Override
    public void Update(Map map, Batch batch, Player player, int volume) {
        float oldMobX = mobPosX;
        float oldMobY = mobPosY;

        Random rand = new Random();




        //mob up down movement
        gravity -= 0.25;

        mobPosY += gravity;


        int startBlockX = (int)(mobPosX / 25 - 1600 / 25 / 2) +2500;
        int endBlockX = (startBlockX + 1600 / 25) ;


        Block[][] mapArray = map.getMapArray();

        for (int x = startBlockX; x < endBlockX; x++){
            if (mapArray[x][0].getPosX() > mobPosX - 100 && mapArray[x][0].getPosX() < mobPosX + 100) {
                for (int y = 0; y < mapArray[x].length; y++){
                    if (mapArray[x][y].isCollision() && mobPosX + mobSizeX > mapArray[x][y].getPosX()
                    && mobPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                    && mobPosY + mobSizeY > mapArray[x][y].getPosY()
                    && mobPosY < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {
                        mobPosY = oldMobY;
                        if (Math.abs(acceleration) > 0.1f && mapArray[x-1][y-1].isCollision() || mapArray[x+1][y-1].isCollision()) {
                            gravity = 4;
                        }else{
                            gravity = 0;
                        }
                    }
                }
            }
        }

        //mob left right movement
        if(rand.nextInt(1000)<10){
            Boolean move = rand.nextBoolean();
            if (move){
                Boolean direction = rand.nextBoolean();
                if (direction){
                    acceleration =0.5f;
                }else{
                    acceleration = -0.5f;
                }
            }else {
                acceleration = 0;
            }
        }        
        
        mobPosX += acceleration;

        for (int x = startBlockX; x < endBlockX; x++){
            if (mapArray[x][0].getPosX() > mobPosX - 100 && mapArray[x][0].getPosX() < mobPosX + 100) {
                for (int y = 0; y < mapArray[x].length; y++){
                    if (mapArray[x][y].isCollision() && mobPosX + mobSizeX > mapArray[x][y].getPosX()
                    && mobPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                    && mobPosY + mobSizeY > mapArray[x][y].getPosY()
                    && mobPosY < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {
                       
                        mobPosX = oldMobX;
                    }
                }
            }
        }


        batch.draw(mobTexture, mobPosX, mobPosY);
    }

    public float getMobPosX() {
        return mobPosX;
    }

    public void setMobPosX(float mobPosX) {
        this.mobPosX = mobPosX;
    }

    public float getMobPosY() {
        return mobPosY;
    }

    public void setMobPosY(float mobPosY) {
        this.mobPosY = mobPosY;
    }

    public int getMobHealth() {
        return mobHealth;
    }

    public void setMobHealth(int mobHealth) {
        this.mobHealth = mobHealth;
    }

    public int getMobSizeX() {
        return mobSizeX;
    }

    public int getMobSizeY() {
        return mobSizeY;
    }

    public String getType() {
        return type;
    }

    public int getElement() {
        return element;
    }
    
}
