package com.mygdx.game.mobs;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.Element;
import com.mygdx.game.player.Player;

public class Slime extends Mob{
    private Texture mobTexture;
    private float mobPosX;
    private float mobPosY;
    private int mobSizeX;
    private int mobSizeY;
    private float gravity;
    private float acceleration;
    private Sound slimeSound;
    private float jumpTimer;
    private int mobHealth;
    private String type;
    private int element;
    private float brightness = 1f;
    private String hit;
    private boolean onGround;

    public Slime(float x, float y, Texture texture, Sound sound) {
        super();
        this.mobPosX = x;
        this.mobPosY = y;

        mobTexture = texture;

        slimeSound = sound;

        mobSizeX = 20;
        mobSizeY = 20;

        gravity = 0;
        mobHealth = 10;
        type = "hostile";
        element = Element.SLIMEBALL;
        hit = "not";
        onGround = true;
        
    }

    @Override
    public void Update(Map map, Batch batch, Player player, int volume, float delta, int mapSizeX, int mapSizeY) {
        float oldMobX = mobPosX;
        float oldMobY = mobPosY;

        //mob up down movement
        gravity -= 0.25 * delta;

        mobPosY += gravity * delta;

        int startBlockX = (int)(mobPosX / 25 - 700 / 25 / 2) +(mapSizeX/2);
        int endBlockX = (startBlockX + 700 / 25) ;
        if (startBlockX > mapSizeX){ startBlockX = mapSizeX; } 
        if (startBlockX < 0){ startBlockX = 0; }
        if (endBlockX > mapSizeX){ endBlockX = mapSizeX; } 
        if (endBlockX < 0){ endBlockX = 0; }
        Block[][] mapArray = map.getMapArray();

        for (int x = startBlockX; x < endBlockX; x++){
            if (mapArray[x][0].getPosX() > mobPosX - 100 && mapArray[x][0].getPosX() < mobPosX + 100) {
                for (int y = 0; y < mapArray[x].length; y++){
                    if (mobPosX + mobSizeX > mapArray[x][y].getPosX()
                        && mobPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                        && mobPosY + mobSizeY > mapArray[x][y].getPosY()
                        && mobPosY < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {
                        if(mapArray[x][y].getBrightness() > brightness){
                            brightness += 0.01f * delta;
                        }else{
                            brightness -= 0.01f * delta;
                        }
                        if(mapArray[x][y].isCollision()){
                            onGround = true;
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
        }

        //mob left right movement
        if(200 > Math.sqrt((player.getY() - mobPosY) * (player.getY() - mobPosY) + (player.getX() - mobPosX) * (player.getX() - mobPosX))){
            jumpTimer += 1 * delta;
            if (jumpTimer >= 300) {
                gravity = 5;
                slimeSound.play(volume/200f);
                jumpTimer = 0;
            }
            if (mobPosX < player.getX()){
                acceleration +=0.3f * delta;
            }else if (mobPosX > player.getX()){
                acceleration -= 0.3f * delta;
            }
        }
        
        mobPosX += acceleration * delta;
        acceleration *= Math.pow(0.5f, delta);

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

        //knockback
        if (hit == "left") {
            if (!onGround) {
                acceleration = 1f;
            } else {
                hit = "not";
                acceleration = 0;
            }
        } else if (hit == "right") {
            if (!onGround) {
                acceleration = -1f;
            } else {
                hit = "not";
                acceleration = 0;
            }
        }

        batch.setColor(brightness,brightness,brightness,1f);
        batch.draw(mobTexture, mobPosX, mobPosY);
        batch.setColor(1f,1f,1f,1f);
        
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

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
    
}
