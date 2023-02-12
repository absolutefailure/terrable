package com.mygdx.game.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.Player;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;

public class Mob {
    private Texture mobTexture;
    private float mobPosX;
    private float mobPosY;
    private int mobSizeX;
    private int mobSizeY;
    private float gravity;
    private float acceleration;
    private int soundTimer;

    private Sound mobScreamSound;
  
    public Mob(float x, float y, Texture texture) {
        this.mobPosX = x;
        this.mobPosY = y;

        mobTexture = texture;

        mobScreamSound = Gdx.audio.newSound(Gdx.files.internal("sounds/mobScreamSound.mp3"));

        mobSizeX = 20;
        mobSizeY = 40;

        gravity = 0;
        
    }
    
    public void Update(Map map, Batch batch, Player player) {
        float oldMobX = mobPosX;
        float oldMobY = mobPosY;





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
        if(200 > Math.sqrt((player.getY() - mobPosY) * (player.getY() - mobPosY) + (player.getX() - mobPosX) * (player.getX() - mobPosX))){
            if (mobPosX < player.getX()){
                acceleration +=0.5f;
            }else if (mobPosX > player.getX()){
                acceleration -= 0.5f;
            }
        }
        if(mobPosX + 1 > player.getX() && mobPosX - 1 < player.getX()) {
            soundTimer += 1;
            if(soundTimer == 100) {
                mobScreamSound.play(0.01f);
                soundTimer = 0;
            }
        } else {
            soundTimer = 99;
        }
        
        
        
        mobPosX += acceleration;
        acceleration *= 0.5f;

        for (int x = 0; x < mapArray.length; x++){
            if (mapArray[x][0].getPosX() > mobPosX - 100 && mapArray[x][0].getPosX() < mobPosX + 100) {
                for (int y = 0; y < mapArray[x].length; y++){
                    if (mapArray[x][y].isCollision() && mobPosX + mobSizeX > mapArray[x][y].getPosX()
                        && mobPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                        && mobPosY > mapArray[x][y].getPosY() - mapArray[x][y].getBLOCKSIZE()
                        && mobPosY - mobSizeY < mapArray[x][y].getPosY()) {
                       
                        mobPosX = oldMobX;
                        acceleration = 0;
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