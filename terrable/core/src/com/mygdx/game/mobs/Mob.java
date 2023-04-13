package com.mygdx.game.mobs;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;
import com.mygdx.game.map.Element;
import com.mygdx.game.player.Player;

public class Mob {
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
    private float brightness = 1f;
    private String hit;
    private boolean onGround;

    // private Sound mobScreamSound;

    public Mob(float x, float y, Texture texture, Sound sound) {
        this.mobPosX = x;
        this.mobPosY = y;

        mobTexture = texture;

        // mobScreamSound = sound;

        mobSizeX = 20;
        mobSizeY = 40;

        gravity = 0;
        mobHealth = 10;
        type = "hostile";
        element = Element.BLUEPRINT;
        hit = "not";
        onGround = true;
    }

    public Mob() {

    }

    public void Update(Map map, Batch batch, Player player, int volume, float delta, int mapSizeX, int mapSizeY) {
        // store current position of the mob
        float oldMobX = mobPosX;
        float oldMobY = mobPosY;

        // Apply gravity to the mob
        gravity -= 0.25 * delta;
        mobPosY += gravity * delta;

        // calculate starting block based on mob position and map size
        int startBlockX = (int) (((mobPosX) - 200) / 25) + (mapSizeX / 2);
        int startBlockY = (int) (mapSizeY / 2 - ((mobPosY + 200) / 25));

        // ensure that starting block is within the map boundaries
        if(startBlockX < 0 ){startBlockX = 0;}
        if(startBlockX > mapSizeX-16 ){startBlockX = mapSizeX - 16;}
        if(startBlockY < 0 ){startBlockY = 0;}
        if(startBlockY > mapSizeY-16 ){startBlockY = mapSizeY - 16;}

        // get the 2D array of blocks from the map object
        Block[][] mapArray = map.getMapArray();

        // loop through a 16x16 block area around the mob's current position
        for (int x = startBlockX; x < startBlockX + 16; x++) {
            for (int y = startBlockY; y < startBlockY + 16; y++) {

                // check if the mob is colliding with a block
                if (mobPosX + mobSizeX > mapArray[x][y].getPosX()
                        && mobPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                        && mobPosY + mobSizeY > mapArray[x][y].getPosY()
                        && mobPosY < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {

                    // adjust mob brightness based on block brightness
                    if (mapArray[x][y].getBrightness() > brightness) {
                        brightness += 0.01f * delta;
                    } else {
                        brightness -= 0.01f * delta;
                    }

                    // reset the mob's Y position to the old position and stop gravity if it collides with a solid block
                    if (mapArray[x][y].isCollision()) {
                        mobPosY = oldMobY;
                        onGround = true;
                        if (Math.abs(acceleration) > 0.1f && mapArray[x - 1][y - 1].isCollision()
                                || mapArray[x + 1][y - 1].isCollision()) {
                            gravity = 4;
                        } else {
                            gravity = 0;
                        }
                    }
                }
            }
        }

        // calculate left/right movement towards the player
        if (200 > Math.sqrt((player.getY() - mobPosY) * (player.getY() - mobPosY)
                + (player.getX() - mobPosX) * (player.getX() - mobPosX))) {
            if (mobPosX < player.getX()) {
                acceleration += 0.5f * delta;
            } else if (mobPosX > player.getX()) {
                acceleration -= 0.5f * delta;
            }
        }

        // play a sound if the mob is near the player
        if (mobPosX + 1 > player.getX() && mobPosX - 1 < player.getX()) {
            soundTimer += 1;
            if (soundTimer == 100) {
                // mobScreamSound.play(volume/400f);
                soundTimer = 0;
            }
        } else {
            soundTimer = 99;
        }

        mobPosX += acceleration * delta;
        acceleration *= Math.pow(0.5f, delta);

        // loop through a 16x16 block area around the mob's current position
        for (int x = startBlockX; x < startBlockX + 16; x++) {
            for (int y = startBlockY; y < startBlockY + 16; y++) {
                // Check if the mob is colliding with a block
                if (mapArray[x][y].isCollision() && mobPosX + mobSizeX > mapArray[x][y].getPosX()
                        && mobPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                        && mobPosY + mobSizeY > mapArray[x][y].getPosY()
                        && mobPosY < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {
                    // reset the mob's X position to the old position and stop its acceleration
                    mobPosX = oldMobX;
                    acceleration = 0;
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

        batch.setColor(brightness, brightness, brightness, 1f);
        batch.draw(mobTexture, mobPosX, mobPosY);
        batch.setColor(1f, 1f, 1f, 1f);

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