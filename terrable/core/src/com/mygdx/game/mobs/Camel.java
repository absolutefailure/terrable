package com.mygdx.game.mobs;


import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.Element;
import com.mygdx.game.player.Player;
import static com.mygdx.game.map.Element.*;

public class Camel extends Mob {
    private TextureRegion[][] mobTexture;
    private float mobPosX;
    private float mobPosY;
    private int mobSizeX;
    private int mobSizeY;
    private float gravity;
    private float acceleration;
    private int mobHealth;
    private String type;
    private int element;
    private float moveTimer;
    private int direction = 0;
    private float brightness = 1f;
    private String hit;
    private boolean onGround;
    
    public Camel(float x, float y, TextureRegion[][] texture) {
        super();
        this.mobPosX = x;
        this.mobPosY = y;

        mobTexture = texture;

        mobSizeX = 63;
        mobSizeY = 47;

        moveTimer = 0;
        gravity = 0;
        mobHealth = 5;
        type = "friendly";
        hit = "not";
        onGround = true;

        element = Element.RAWSTEAK;
        
    }

    @Override
    public void Update(Map map, Batch batch, Player player, int volume, float delta, int mapSizeX, int mapSizeY) {
        float oldMobX = mobPosX;
        float oldMobY = mobPosY;

        Random rand = new Random();




        //mob up down movement
        gravity -= 0.25 * delta;

        mobPosY += gravity * delta;


        int startBlockX = (int)(mobPosX / 25 - 200 / 25 / 2) +(mapSizeX/2);
        int endBlockX = (startBlockX + 400 / 25) ;

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
                            mobPosY = oldMobY;
                            onGround = true;
                            if (Math.abs(acceleration) > 0.1f && mapArray[x-1][y-1].isCollision() || mapArray[x+1][y-1].isCollision()) {
                                gravity = 4;
                            }else{
                                gravity = 0;
                            }
                        } else if(mapArray[x][y].getElement() == WATER1
                        || mapArray[x][y].getElement() == WATER2
                        || mapArray[x][y].getElement() == WATER3
                        || mapArray[x][y].getElement() == WATER4
                        || mapArray[x][y].getElement() == WATER5){
                            gravity *= Math.pow(0.99f, delta);
                            gravity += 0.1f * delta;
                        }
                    }
                }
            }
        }
        
        moveTimer -= 1 * delta;
        if (moveTimer < 0){
            moveTimer = rand.nextInt(1000);
            direction = rand.nextInt(2);
        }

        //mob left right movement
        if(moveTimer < 10000 && moveTimer >= 20){
            if (direction == 0){
                acceleration =0.5f;
            }else{
                acceleration = -0.5f;
            }
        } else if (moveTimer < 20) {
            acceleration = 0;
        }      
        
        mobPosX += acceleration * delta;

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
        
        //knockback
        if (hit == "left") {
            moveTimer = 100000;
            if (!onGround) {
                acceleration = 1f;
            } else {
                hit = "not";
                moveTimer = 20;
                acceleration = 0;
            }
        } else if (hit == "right") {
            moveTimer = 100000;
            if (!onGround) {
                acceleration = -1f;
            } else {
                hit = "not";
                moveTimer = 20;
                acceleration = 0;
            }
        }

        batch.setColor(brightness,brightness,brightness,1f);
        batch.draw(mobTexture[direction][0], mobPosX, mobPosY);
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
