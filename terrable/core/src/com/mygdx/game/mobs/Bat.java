package com.mygdx.game.mobs;

import java.util.Random;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.Player;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.elements;

public class Bat extends Mob{
    private Texture mobTexture;
    private float mobPosX;
    private float mobPosY;
    private int mobSizeX;
    private int mobSizeY;
    private float gravity;
    private float acceleration;
    private float soundTimer;
    private int mobHealth;
    private String type;
    private int element;

    private Random rand;
    private Sound batScreamSound;
    
    public Bat(float x, float y, Texture texture, Sound sound) {
        super();
        this.mobPosX = x;
        this.mobPosY = y;

        mobTexture = texture;

        batScreamSound = sound;

        mobSizeX = 20;
        mobSizeY = 20;

        gravity = 0;
        mobHealth = 5;
        type = "harmful";
        element = elements.FEATHER;
    
        rand = new Random();
    }

    @Override
    public void Update(Map map, Batch batch, Player player, int volume, float delta) {
        float oldMobX = mobPosX;
        float oldMobY = mobPosY;





        //mob up down movement
        gravity -= ((rand.nextFloat() * 0.2f) - 0.1f) * delta;

        mobPosY += gravity * delta;

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
                acceleration +=0.5f * delta;
            }else if (mobPosX > player.getX()){
                acceleration -= 0.5f * delta;
            }
        }
        if(mobPosX + 1 > player.getX() && mobPosX - 1 < player.getX()) {
            soundTimer += 1 * delta;
            if(soundTimer >= 100) {
                batScreamSound.play(volume/200f);
                soundTimer = 0;
            }
        } else {
            soundTimer = 99;
        }
        
        
        acceleration -= ((rand.nextFloat() * 0.6f) - 0.3f) * delta;
        mobPosX += acceleration * delta;

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
