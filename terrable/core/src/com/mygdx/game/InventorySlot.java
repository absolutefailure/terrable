package com.mygdx.game;

public class InventorySlot {
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
