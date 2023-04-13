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

    private float waterTimer = 0;

    public Block(int x, int y, int element, boolean collision, int permanent){
        this.posX = x;
        this.posY = y;
        this.element = element;
        this.permanent = permanent;
        this.blockHealth = BLOCKMAXHP.get(element);

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
    

    public float getWaterTimer() {
        return waterTimer;
    }

    public void setWaterTimer(float waterTimer) {
        this.waterTimer = waterTimer;
    }

    public int getMaxhealth(){
       return BLOCKMAXHP.get(element);
        
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
                }else if (amount > 0 && furnaceSlot1.getElement() == COPPER && (furnaceSlot3.getElement() == COPPERINGOT || furnaceSlot3.getAmount() == 0) && furnaceSlot3.getAmount() + amount <= 32) {
                    furnaceSlot3.setElement(COPPERINGOT);
                    furnaceSlot3.setAmount(furnaceSlot3.getAmount() + amount);
                    furnaceSlot3.setResource(true);
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
