package com.mygdx.game.player;

import com.mygdx.game.map.Block;

public class Item {

    private float x;
    private float y;
    private float acceleration = 0;
    private float gravity = 0;
    private float shakeTimer;
    private boolean shakeDirection;



    private int element;
    private int amount;
    private int damage = 2;
    private int health;

    private boolean isWeapon, isFood, isResource;




    // for crafting
    private int removeAmount;

    public Item(){

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

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }
    public float getShakeTimer() {
        return shakeTimer;
    }

    public void setShakeTimer(float shakeTimer) {
        this.shakeTimer = shakeTimer;
    }

    public boolean isShakeDirection() {
        return shakeDirection;
    }

    public void setShakeDirection(boolean shakeDirection) {
        this.shakeDirection = shakeDirection;
    }
    public void Update(Block[][] mapArray, float playerPosX, float playerPosY, float delta){
        float itemOldX = x;
        float itemOldY = y;
        int loopStartX = (int)(((x)-50) / 25) +((5000)/2);
        int loopStartY = (int)((300)/2 - ((y+50) / 25)) ;
        if (loopStartY < 0){loopStartY = 0;}
        if(loopStartY > 300-5){loopStartY = 300-5;}
        if (loopStartX < 0){loopStartX = 0;}
        if(loopStartX > 5000-5){loopStartX = 5000-5;}

        if (x + 12 >= playerPosX -30
        && x <= playerPosX + 20 +30
        && y + 12 >= playerPosY - 30
        && y <= playerPosY + 49 + 30) {
            if (y + 12 > playerPosY-30 && y < playerPosY+25){
                y += 5 * delta;
            }
            if (y < playerPosY+49+30 && y + 12 > playerPosY+25){
                y -= 5 * delta;
            }
        }


        gravity -= 0.1f * delta;
        y += gravity * delta;
        
         
        for (int i = loopStartX; i < loopStartX + 4; i++){
            for (int j = loopStartY; j < loopStartY+4; j++){
                if (mapArray[i][j].isCollision() && x+12 > mapArray[i][j].getPosX()
                && x < mapArray[i][j].getPosX() + mapArray[i][j].getBLOCKSIZE()
                && y + 12 > mapArray[i][j].getPosY()
                && y < mapArray[i][j].getPosY() + mapArray[i][j].getBLOCKSIZE()) {
                    float blockTop = mapArray[i][j].getPosY() + mapArray[i][j].getBLOCKSIZE();
                    float blockBottom = (mapArray[i][j].getPosY());
                    float dy = y-itemOldY ;
            
                    if (dy > 0) {
                        y = blockBottom - 12f;
                    } else {
                        y = blockTop;
                    }
                    //y = itemOldY;
                    gravity *= Math.pow(0.7f, delta);
                    break;
                }
            }
        }
        if (x + 12 >= playerPosX -30
        && x <= playerPosX + 20 +30
        && y + 12 >= playerPosY - 30
        && y <= playerPosY + 49 + 30) {
            if (x + 12 > playerPosX-30 && x < playerPosX+10){
                x += 5 * delta;
            }
            if (x < playerPosX+20+30 && x + 12 > playerPosX+10){
                x -= 5 * delta;
            }
        }
        x += acceleration * delta;
        acceleration *= Math.pow(0.96f, delta);
 
         for (int i = loopStartX; i < loopStartX + 4; i++){
             for (int j = loopStartY; j < loopStartY+4; j++){
                 if (mapArray[i][j].isCollision() && x+12 > mapArray[i][j].getPosX()
                 && x < mapArray[i][j].getPosX() + mapArray[i][j].getBLOCKSIZE()
                 && y + 12 > mapArray[i][j].getPosY()
                 && y < mapArray[i][j].getPosY() + mapArray[i][j].getBLOCKSIZE()) {
                    float blockRight = mapArray[i][j].getPosX() + mapArray[i][j].getBLOCKSIZE();
                    float blockLeft = (mapArray[i][j].getPosX());
                    float dx = x-itemOldX ;
            
                    if (dx > 0) {
                        x = blockLeft - 12f;
                    } else {
                        x = blockRight;
                    }
                    //x = itemOldX;
                    acceleration *= Math.pow(0.7f, delta);
                    break;
                 }
             }
         }

        if (shakeDirection){
            shakeTimer += 0.2 * delta;
            if(shakeTimer > 5){
                shakeDirection = false;
            }
        }else{
            shakeTimer -= 0.2 * delta;
            if(shakeTimer < 0){
                shakeDirection = true;
            }
        }

    }
}
