package com.mygdx.game.map;
import static com.mygdx.game.map.elements.*;

import java.io.Serializable;

public class Block implements Serializable {
    
    final transient int BLOCKSIZE = 25; // block size in pixels

    private int posX;
    private int posY;

    private int element = 0;

    private transient float blockHealth;

    private transient float brightness;

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
        }else if(element == STONEPICKAXE) {
            this.blockHealth = 2500;
        }else if(element == WOODPICKAXE) {
            this.blockHealth = 500;
        }else if(element == IRONPICKAXE) {
            this.blockHealth = 5000;
        }else if(element == DIAMONDPICKAXE) {
            this.blockHealth = 10000;
        }else if(element == STONEAXE) {
            this.blockHealth = 2500;
        }else if(element == WOODAXE) {
            this.blockHealth = 500;
        }else if(element == IRONAXE) {
            this.blockHealth = 5000;
        }else if(element == DIAMONDAXE) {
            this.blockHealth = 10000;
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
        } else if (element == PLANKS){
            return 75;
        } else if (element == STONEPICKAXE){
            return 2500;
        } else if (element == WOODPICKAXE){
            return 500;
        } else if (element == IRONPICKAXE){
            return 5000;
        } else if (element == DIAMONDPICKAXE){
            return 10000;
        } else if (element == STONEAXE){
            return 2500;
        } else if (element == WOODAXE){
            return 500;
        } else if (element == IRONAXE){
            return 5000;
        } else if (element == DIAMONDAXE){
            return 10000;
        } else if (element == IRONINGOT){
            return 50;
        } else{
            return 1;
        }
        
    }

}
