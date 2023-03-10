package com.mygdx.game;

import java.io.Serializable;

public class InventorySlot implements Serializable {

    private int element;
    private int amount;
    private int damage = 2;
    private int health;

    private boolean isWeapon, isFood, isResource;




    // for crafting
    private int removeAmount;

    public InventorySlot(){

    }

    public void addItem(){
        amount++;
    }
    
    public void removeItem(){
        amount--;
        if (amount <= 0){
            amount = 0;
            isFood = false;
            isWeapon = false;
            isResource = false;
            element = 0;
            damage = 2;
            health = 0;
        }
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
        if (this.amount <= 0){
            this.amount = 0;
            isFood = false;
            isWeapon = false;
            isResource = false;
            element = 0;
        }
    }

    public int getRemoveAmount() {
        return removeAmount;
    }

    public void setRemoveAmount(int removeAmount) {
        this.removeAmount = removeAmount;
    }
    public boolean isWeapon() {
        return isWeapon;
    }

    public void setWeapon(boolean isWeapon) {
        this.isWeapon = isWeapon;
        if (isWeapon){isFood = false; isResource = false;}
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isFood() {
        return isFood;
    }

    public void setFood(boolean isFood) {
        this.isFood = isFood;
        if (isFood){isWeapon = false; isResource = false;}
    }

    public boolean isResource() {
        return isResource;
    }

    public void setResource(boolean isResource) {
        this.isResource = isResource;
        if (isResource){isWeapon = false; isFood = false;}
    }

    public boolean isPlaceable(){
        if (!isResource && !isFood && !isWeapon){ return true;}
        return false;
    }
}
