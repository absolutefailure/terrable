package com.mygdx.game.map;
import static com.mygdx.game.map.elements.*;

public class Block {
    
    final int BLOCKSIZE = 25; // block size in pixels

    private int posX;
    private int posY;

    private int element = 0;

    private float blockHealth;

    private float brightness;

    boolean collision;
    boolean permanent;

    public Block(int x, int y, int element, boolean collision){
        this.posX = x;
        this.posY = y;
        this.element = element;
        if (element == EMPTY){
            this.blockHealth = 0;
        }else if(element == GROUND){
            this.blockHealth = 50;
            this.permanent = true;
        }else if(element == GRASS){
            this.blockHealth = 50;
            this.permanent = true;
        }else if(element == WOOD){
            this.blockHealth = 100;
        }else if(element == LEAVES){
            this.blockHealth = 25;
        }else if(element == LADDER) {
            this.blockHealth = 25;
        }
        this.collision = collision;
        brightness = 1.f;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
    
    public int getElement() {
        return element;
    }

    public void setElement(int element) {
        this.element = element;
    }
    
    public int getBLOCKSIZE() {
        return BLOCKSIZE;
    }

    public float getBlockHealth() {
        return blockHealth;
    }

    public void setBlockHealth(float blockHealth) {
        this.blockHealth = blockHealth;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }
}
