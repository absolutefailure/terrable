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
    int permanent;

    public Block(int x, int y, int element, boolean collision, int permanent){
        this.posX = x;
        this.posY = y;
        this.element = element;
        this.permanent = permanent;
        if (element == EMPTY){
            this.blockHealth = 0;
        }else if(element == GROUND){
            this.blockHealth = 50;
        }else if(element == GRASS){
            this.blockHealth = 50;
        }else if(element == WOOD){
            this.blockHealth = 100;
        }else if(element == STONE){
            this.blockHealth = 200;
        }else if(element == LEAVES){
            this.blockHealth = 25;
        }else if(element == LADDER) {
            this.blockHealth = 25;
        }else if(element == IRON) {
            this.blockHealth = 250;
            this.permanent = STONE;
        }else if(element == COAL) {
            this.blockHealth = 200;
            this.permanent = STONE;
        }else if(element == DIAMOND) {
            this.blockHealth = 300;
            this.permanent = STONE;
        }else if(element == TALLGRASS) {
            this.blockHealth = 1;
        }else if(element == REDFLOWER) {
            this.blockHealth = 1;
        }
        this.collision = collision;
        brightness = 0;
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

    public int getPermanent() {
        return permanent;
    }

    public void setPermanent(int permanent) {
        this.permanent = permanent;
    }

    public int getMaxhealth(){
        if (element == GROUND){
            return 50;
        } else if (element == GROUND){
            return 50;
        } else if (element == GRASS){
            return 50;
        } else if (element == WOOD){
            return 100;
        } else if (element == LEAVES){
            return 25;
        } else if (element == LADDER){
            return 25;
        } else if (element == STONE){
            return 200;
        } else if (element == COAL){
            return 200;
        } else if (element == IRON){
            return 250;
        } else if (element == DIAMOND){
            return 300;
        }else{
            return 1;
        }
        
    }

}
