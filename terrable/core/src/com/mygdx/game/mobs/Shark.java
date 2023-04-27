package com.mygdx.game.mobs;

import java.util.Random;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.Element;
import com.mygdx.game.player.Player;

public class Shark extends Mob{
    private TextureRegion[][] mobTexture;
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
    private String hit;
    private int direction = 0;

    private Random rand;
    private Sound sharkBiteSound;

    private float brightness = 1f;
    
    public Shark(float x, float y, TextureRegion[][] texture, Sound sound) {
        super();
        this.mobPosX = x;
        this.mobPosY = y;

        mobTexture = texture;

        sharkBiteSound= sound;

        mobSizeX = 50;
        mobSizeY = 30;

        gravity = 0;
        mobHealth = 10;
        type = "hostile";
        element = Element.RAWFISH;
        hit = "not";
    
        rand = new Random();
    }

    @Override
    public void Update(Map map, Batch batch, Player player, int volume, float delta, int mapSizeX, int mapSizeY) {
        float oldMobX = mobPosX;
        float oldMobY = mobPosY;

        mobPosY += gravity * delta;

        int startBlockX = (int)(mobPosX / 25 - 200 / 25 / 2) +(mapSizeX/2);
        int endBlockX = (startBlockX + 400 / 25) ;

        Block[][] mapArray = map.getMapArray();

        for (int x = startBlockX; x < endBlockX; x++){
            if (mapArray[x][0].getPosX() > mobPosX - 100 && mapArray[x][0].getPosX() < mobPosX + 100) {
                for (int y = 0; y < mapArray[x].length; y++){
                    if ( mobPosX + mobSizeX > mapArray[x][y].getPosX()
                    && mobPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                    && mobPosY + mobSizeY > mapArray[x][y].getPosY()
                    && mobPosY < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {
                        if(mapArray[x][y].getBrightness() > brightness){
                            brightness += 0.01f * delta;
                        }else{
                            brightness -= 0.01f * delta;
                        }
                        if(mapArray[x][y].getElement() == Element.WATER3 || mapArray[x][y].getElement() == Element.WATER1 || mapArray[x][y].getElement() == Element.WATER2 || mapArray[x][y].getElement() == Element.WATER4 || mapArray[x][y].getElement() == Element.WATER5){
                            if(250 > Math.sqrt((player.getY() - mobPosY) * (player.getY() - mobPosY) + (player.getX() - mobPosX) * (player.getX() - mobPosX))){
                                if (mobPosY < player.getY()){
                                    gravity = 1.5f;
                                }else if (mobPosY > player.getY()){
                                    gravity = -1.5f;
                                }
                            } else {
                                gravity -= ((rand.nextFloat() * 0.2f) - 0.1f) * delta;
                            }
                        } else {
                            mobPosY = oldMobY;
                        }
                    }
                }
            }
        }

        //mob left right movement
        if (40 > Math.sqrt((player.getY() - mobPosY) * (player.getY() - mobPosY) + (player.getX() - mobPosX) * (player.getX() - mobPosX))) {
            if (mobPosX < player.getX()){
                acceleration = 1.5f;
                direction = 3;
                soundTimer += 1 * delta;
                if(soundTimer >= 120) {
                    sharkBiteSound.play(volume/100f);
                    soundTimer = 0;
                }
            }else if (mobPosX > player.getX()){
                acceleration = -1.5f;
                direction = 2;
                soundTimer += 1 * delta;
                if(soundTimer >= 120) {
                    sharkBiteSound.play(volume/100f);
                    soundTimer = 0;
                }
            }
        } else if (90 > Math.sqrt((player.getY() - mobPosY) * (player.getY() - mobPosY) + (player.getX() - mobPosX) * (player.getX() - mobPosX))) {
            if (mobPosX < player.getX()){
                acceleration = 1.5f;
                direction = 3;
                soundTimer += 1 * delta;
            }else if (mobPosX > player.getX()){
                acceleration = -1.5f;
                direction = 2;
                soundTimer += 1 * delta;
            }
        } else if(250 > Math.sqrt((player.getY() - mobPosY) * (player.getY() - mobPosY) + (player.getX() - mobPosX) * (player.getX() - mobPosX))){
            if (mobPosX < player.getX()){
                acceleration = 1.5f;
                direction = 1;
                soundTimer += 1 * delta;
            }else if (mobPosX > player.getX()){
                acceleration = -1.5f;
                direction = 0;
                soundTimer += 1 * delta;
            }
        } else {
            acceleration -= ((rand.nextFloat() * 0.6f) - 0.3f) * delta;
            if (acceleration > 0) {
                direction = 1;
            } else {
                direction = 0;
            }
        }
        
        mobPosX += acceleration * delta;

        for (int x = startBlockX; x < endBlockX; x++){
            if (mapArray[x][0].getPosX() > mobPosX - 100 && mapArray[x][0].getPosX() < mobPosX + 100) {
                for (int y = 0; y < mapArray[x].length; y++){
                    if (mapArray[x][y].isCollision() && mobPosX + mobSizeX > mapArray[x][y].getPosX()
                    && mobPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                    && mobPosY + mobSizeY > mapArray[x][y].getPosY()
                    && mobPosY < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {
                        if(mapArray[x][y].getElement() != Element.WATER3 ){
                            mobPosX = oldMobX;
                        }
                    }
                }
            }
        }

        //knockback
        if (hit == "left") {
            if (gravity > 0) {
                acceleration = 1f;
            } else {
                hit = "not";
            }
        } else if (hit == "right") {
            if (gravity > 0) {
                acceleration = -1f;
            } else {
                hit = "not";
            }
        }

        batch.setColor(brightness,brightness,brightness,1f);
        batch.draw(mobTexture[0][direction], mobPosX, mobPosY);
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
    
}
