package com.mygdx.game.mobs;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.Player;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;

public class Chicken {
    private Texture mobTexture;
    private float mobPosX;
    private float mobPosY;
    private int mobSizeX;
    private int mobSizeY;
    private float gravity;
    private float acceleration;

    public Chicken(float x, float y, Texture texture) {
        this.mobPosX = x;
        this.mobPosY = y;

        mobTexture = texture;

        mobSizeX = 20;
        mobSizeY = 40;

        gravity = 0;
        
    }

    public void Update(Map map, Batch batch, Player player) {
        float oldMobX = mobPosX;
        float oldMobY = mobPosY;

        Random rand = new Random();




        //mob up down movement
        gravity -= 0.25;

        mobPosY += gravity;





        Block[][] mapArray = map.getMapArray();

        for (int x = 0; x < mapArray.length; x++){
            if (mapArray[x][0].getPosX() > mobPosX - 100 && mapArray[x][0].getPosX() < mobPosX + 100) {
                for (int y = 0; y < mapArray[x].length; y++){
                    if (mapArray[x][y].isCollision() && mobPosX + mobSizeX > mapArray[x][y].getPosX()
                        && mobPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                        && mobPosY > mapArray[x][y].getPosY() - mapArray[x][y].getBLOCKSIZE()
                        && mobPosY - mobSizeY < mapArray[x][y].getPosY()) {
                        mobPosY = oldMobY;
                        if (mapArray[x-1][y-1].isCollision() || mapArray[x+1][y-1].isCollision()) {
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

        for (int x = 0; x < mapArray.length; x++){
            if (mapArray[x][0].getPosX() > mobPosX - 100 && mapArray[x][0].getPosX() < mobPosX + 100) {
                for (int y = 0; y < mapArray[x].length; y++){
                    if (mapArray[x][y].isCollision() && mobPosX + mobSizeX > mapArray[x][y].getPosX()
                        && mobPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                        && mobPosY > mapArray[x][y].getPosY() - mapArray[x][y].getBLOCKSIZE()
                        && mobPosY - mobSizeY < mapArray[x][y].getPosY()) {
                       
                        mobPosX = oldMobX;
                    }
                }
            }
        }


        batch.draw(mobTexture, mobPosX, mobPosY - 15);
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
    
}
