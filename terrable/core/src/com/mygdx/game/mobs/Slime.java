package com.mygdx.game.mobs;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.Player;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;

public class Slime extends Mob{
    private Texture mobTexture;
    private float mobPosX;
    private float mobPosY;
    private int mobSizeX;
    private int mobSizeY;
    private float gravity;
    private float acceleration;
    private Sound slimeSound;
    private int jumpTimer;

    public Slime(float x, float y, Texture texture, Sound sound) {
        super();
        this.mobPosX = x;
        this.mobPosY = y;

        mobTexture = texture;

        slimeSound = sound;

        mobSizeX = 20;
        mobSizeY = 20;

        gravity = 0;
        
    }

    @Override
    public void Update(Map map, Batch batch, Player player, int volume) {
        float oldMobX = mobPosX;
        float oldMobY = mobPosY;

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
        if(200 > Math.sqrt((player.getY() - mobPosY) * (player.getY() - mobPosY) + (player.getX() - mobPosX) * (player.getX() - mobPosX))){
            jumpTimer += 1;
            if (jumpTimer == 300) {
                gravity = 5;
                slimeSound.play(volume/200f);
                jumpTimer = 0;
            }
            if (mobPosX < player.getX()){
                acceleration +=0.3f;
            }else if (mobPosX > player.getX()){
                acceleration -= 0.3f;
            }
        }
        
        mobPosX += acceleration;
        acceleration *= 0.5f;

        for (int x = startBlockX; x < endBlockX; x++){
            if (mapArray[x][0].getPosX() > mobPosX - 100 && mapArray[x][0].getPosX() < mobPosX + 100) {
                for (int y = 0; y < mapArray[x].length; y++){
                    if (mapArray[x][y].isCollision() && mobPosX + mobSizeX > mapArray[x][y].getPosX()
                    && mobPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                    && mobPosY + mobSizeY > mapArray[x][y].getPosY()
                    && mobPosY < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {
                       
                        mobPosX = oldMobX;
                        acceleration = 0;
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
}
