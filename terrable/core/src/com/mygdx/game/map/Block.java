package com.mygdx.game.map;
import static com.mygdx.game.map.Element.*;

import java.util.Date;

import com.mygdx.game.player.Item;


public class Block {
    
    private final int BLOCKSIZE = 25; // block size in pixels

    private int posX;
    private int posY;

    private int element = 0;

    private float blockHealth;

    private float brightness;

    private boolean collision;
    private int permanent;

    public float brightnessLevel;

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
        }else if(element == DOOR1) {
            this.blockHealth = 75;
        }else if(element == DOOR2) {
            this.blockHealth = 75;
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
        }else if(element == STICK) {
            this.blockHealth = 1;
        }else if(element == TORCH) {
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
        furnaceSlot1 = null;
        furnaceSlot2 = null;
        furnaceSlot3 = null;
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
        }else if (element == DOOR1){
            return 75;
        }else if (element == DOOR2){
            return 75;
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
        } else if (element == FURNACE || element == FURNACE2){
            return 300;
        }else if (element == SAND ){
            return 40;
        }else if (element == CACTUS){
            return 30;
        }else if (element == SANDSTONE){
            return 200;
        }else if (element == GLASS){
            return 10;
        }else {
            return 1;
        }
        
        

        
    }



    //FURNACE
    private Item furnaceSlot1;
    private Item furnaceSlot2;
    private Item furnaceSlot3;

    private Long furnaceStartTimer;
    
    public Long getFurnaceStartTimer() {
        return furnaceStartTimer;
    }

    public void setFurnaceStartTimer(Long furnaceStartTimer) {
        this.furnaceStartTimer = furnaceStartTimer;
    }

    public Item getFurnaceSlot1() {
        return furnaceSlot1;
    }

    public void setFurnaceSlot1(Item furnaceSlot1) {
        this.furnaceSlot1 = furnaceSlot1;
    }

    public Item getFurnaceSlot2() {
        return furnaceSlot2;
    }

    public void setFurnaceSlot2(Item furnaceSlot2) {
        this.furnaceSlot2 = furnaceSlot2;
    }

    public Item getFurnaceSlot3() {
        return furnaceSlot3;
    }

    public void setFurnaceSlot3(Item furnaceSlot3) {
        this.furnaceSlot3 = furnaceSlot3;
    }

    public void checkFurnace(){
        if (furnaceSlot1.getAmount() > 0 && furnaceSlot2.getAmount() > 0){

            if (furnaceSlot2.getElement() == COALITEM || furnaceSlot2.getElement() == WOOD || furnaceSlot2.getElement() == PLANKS){
                int amount = (int) Math.floor((new Date().getTime() - furnaceStartTimer)/5000); 
                if (amount > furnaceSlot1.getAmount()){amount = furnaceSlot1.getAmount();}
                if (amount > furnaceSlot2.getAmount()){amount = furnaceSlot2.getAmount();}
                this.element = FURNACE2;
                if (amount > 0 && furnaceSlot1.getElement() == IRON && (furnaceSlot3.getElement() == IRONINGOT || furnaceSlot3.getAmount() == 0) && furnaceSlot3.getAmount() + amount <= 32){
                    furnaceSlot3.setElement(IRONINGOT);
                    furnaceSlot3.setAmount(furnaceSlot3.getAmount() + amount);
                    furnaceSlot3.setResource(true);
                    furnaceSlot1.setAmount(furnaceSlot1.getAmount() - amount);
                    furnaceSlot2.setAmount(furnaceSlot2.getAmount() - amount);
                    furnaceStartTimer = new Date().getTime();
                } else if (amount > 0 && furnaceSlot1.getElement() == RAWSTEAK && (furnaceSlot3.getElement() == STEAK || furnaceSlot3.getAmount() == 0) && furnaceSlot3.getAmount() + amount <= 32) {
                    furnaceSlot3.setElement(STEAK);
                    furnaceSlot3.setAmount(furnaceSlot3.getAmount() + amount);
                    furnaceSlot3.setFood(true);
                    furnaceSlot1.setAmount(furnaceSlot1.getAmount() - amount);
                    furnaceSlot2.setAmount(furnaceSlot2.getAmount() - amount);
                    furnaceStartTimer = new Date().getTime();
                } else if (amount > 0 && furnaceSlot1.getElement() == RAWCHICKENMEAT && (furnaceSlot3.getElement() == CHICKENMEAT || furnaceSlot3.getAmount() == 0) && furnaceSlot3.getAmount() + amount <= 32) {
                    furnaceSlot3.setElement(CHICKENMEAT);
                    furnaceSlot3.setAmount(furnaceSlot3.getAmount() + amount);
                    furnaceSlot3.setFood(true);
                    furnaceSlot1.setAmount(furnaceSlot1.getAmount() - amount);
                    furnaceSlot2.setAmount(furnaceSlot2.getAmount() - amount);
                    furnaceStartTimer = new Date().getTime();
                }else if (amount > 0 && furnaceSlot1.getElement() == SAND && (furnaceSlot3.getElement() == GLASS || furnaceSlot3.getAmount() == 0) && furnaceSlot3.getAmount() + amount <= 32) {
                    furnaceSlot3.setElement(GLASS);
                    furnaceSlot3.setAmount(furnaceSlot3.getAmount() + amount);
                    furnaceSlot1.setAmount(furnaceSlot1.getAmount() - amount);
                    furnaceSlot2.setAmount(furnaceSlot2.getAmount() - amount);
                    furnaceStartTimer = new Date().getTime();
                }
            }
        }else{
            this.element = FURNACE;
        }
    }


}
