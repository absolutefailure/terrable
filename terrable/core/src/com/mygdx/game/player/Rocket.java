package com.mygdx.game.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;

public class Rocket {
    private Texture rocketTexture;
    private float rocketPosX;
    private float rocketPosY;
    private int rocketSizeX;
    private int rocketSizeY;
    private float gravity;
    private float acceleration;
    private boolean isEngineOn = false;
    private TextureRegion[][] flameTexture;
    private float flameTimer = 0;

    private float brightness = 1f;



    public Rocket(float x, float y, Texture texture, TextureRegion[][] flameTexture) {
        this.rocketPosX = x;
        this.rocketPosY = y;

        rocketTexture = texture;
        this.flameTexture = flameTexture;
   

        rocketSizeX = 75;
        rocketSizeY = 75;

        gravity = 0;

    }


    public void update(Map map, Batch batch, Player player, int volume, float delta, int mapSizeX, int mapSizeY) {
      
        float oldRocketX = rocketPosX;
        float oldRocketY = rocketPosY;

        
        gravity -= 0.25 * delta;
        rocketPosY += gravity * delta;

    
        int startBlockX = (int) (((rocketPosX) - 200) / 25) + (mapSizeX / 2);
        int startBlockY = (int) (mapSizeY / 2 - ((rocketPosY + 200) / 25));

       
        if(startBlockX < 0 ){startBlockX = 0;}
        if(startBlockX > mapSizeX-16 ){startBlockX = mapSizeX - 16;}
        if(startBlockY < 0 ){startBlockY = 0;}
        if(startBlockY > mapSizeY-16 ){startBlockY = mapSizeY - 16;}

        
        Block[][] mapArray = map.getMapArray();

      
        for (int x = startBlockX; x < startBlockX + 16; x++) {
            for (int y = startBlockY; y < startBlockY + 16; y++) {

               
                if (rocketPosX + rocketSizeX > mapArray[x][y].getPosX()
                        && rocketPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                        && rocketPosY + rocketSizeY > mapArray[x][y].getPosY()
                        && rocketPosY < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {

                    
                    if (mapArray[x][y].getBrightness() > brightness) {
                        brightness += 0.01f * delta;
                    } else {
                        brightness -= 0.01f * delta;
                    }
                    if (mapArray[x][y].isCollision()) {
                        rocketPosY = oldRocketY;
                        gravity = 0;
                    }
                }
            }
        }





        rocketPosX += acceleration * delta;
        acceleration *= Math.pow(0.5f, delta);

        
        for (int x = startBlockX; x < startBlockX + 16; x++) {
            for (int y = startBlockY; y < startBlockY + 16; y++) {
                
                if (mapArray[x][y].isCollision() && rocketPosX + rocketSizeX > mapArray[x][y].getPosX()
                        && rocketPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                        && rocketPosY + rocketSizeY > mapArray[x][y].getPosY()
                        && rocketPosY < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {
                    
                    rocketPosX = oldRocketX;
                    acceleration = 0;
                }
            }
        }



        batch.setColor(brightness, brightness, brightness, 1f);
        batch.draw(rocketTexture, rocketPosX, rocketPosY);
        batch.setColor(1f, 1f, 1f, 1f);
        flameTimer -= 1 * delta;
        if(flameTimer < 0){
            flameTimer = 20;
        }

        if(isEngineOn){

            if(flameTimer > 15){
                batch.draw(flameTexture[0][3], rocketPosX+28, rocketPosY-40);
            }else if(flameTimer > 10){
                batch.draw(flameTexture[0][2], rocketPosX+28, rocketPosY-40);
            }else if(flameTimer > 5){
                batch.draw(flameTexture[0][1], rocketPosX+28, rocketPosY-40);
            }else{
                batch.draw(flameTexture[0][0], rocketPosX+28, rocketPosY-40);
            }
        }

    }
    public float getPosX(){
        return rocketPosX;
    }
    public float getPosY(){
        return rocketPosY;
    }
    public float getSizeX(){
        return rocketSizeX;
    }
    public float getSizeY(){
        return rocketSizeY;
    }
    public void addSpeed(float g){
        gravity += g;
    }


    public boolean isEngineOn() {
        return isEngineOn;
    }


    public void setEngineOn(boolean isEngineOn) {
        this.isEngineOn = isEngineOn;
    }
    
}

