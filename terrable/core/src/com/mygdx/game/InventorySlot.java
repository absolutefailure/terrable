package com.mygdx.game;

import java.io.Serializable;

public class InventorySlot implements Serializable {
    private int element;
    private int amount;



    public InventorySlot(){

    }

    public void additem(){
        amount++;
    }
    public void removeItem(){
        amount--;
    }

    public int getElement() {
        return element;
    }


    public void setElement(int element) {
        this.element = element;
    }


    public int getAmount() {
        return amount;
    }


    public void setAmount(int amount) {
        this.amount = amount;
    }


    
}
