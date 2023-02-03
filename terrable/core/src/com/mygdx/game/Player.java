package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;

import static com.mygdx.game.map.elements.*;

public class Player {
    private Vector2 mouseInWorld2D = new Vector2();
    private Vector3 mouseInWorld3D = new Vector3();

    private float playerPosX;
    private float playerPosY;

    private int playerSizeX;
    private int playerSizeY;

    private boolean onGround;
    private int onGroundTimer;

    private float gravity;
    private float acceleration;

    final float PLAYER_BOUNCINESS = 0.0f; // 0 = NO BOUNCE
    final float PLAYER_FRICTION = 0.7f; // 1 = NO FRICTION

    private Texture playerTexture;
    private Texture outlineTexture;

    public Player(float x, float y) {

        this.playerPosX = x;
        this.playerPosY = y;

        playerSizeX = 20;
        playerSizeY = 49;

        onGround = false;
        onGroundTimer = 0;

        playerTexture = new Texture("jusju.png");
        outlineTexture = new Texture("outline.png");

        gravity = 0;
        acceleration = 0;
    }


    // UPDATE AND DRAW PLAYER
    public void Update(Map map, Camera cam, Batch batch) {
        float oldX = playerPosX;
        float oldY = playerPosY;

        // APPLY GRAVITY TO PLAYER
        playerPosY -= gravity;
        gravity += 0.25;

        // JUMP
        if (onGround) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                playerPosY += 1;
                gravity = -4;
                onGroundTimer = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                playerPosY += 1;
                gravity = -5;
                onGroundTimer = 0;
            }
        }


        Block[][] mapArray = map.getMapArray();
        
        for (int i = 0; i < mapArray.length; i++) {
            if (mapArray[i][0].getPosX() > playerPosX - 400 && mapArray[i][0].getPosX() < playerPosX + 400) {
                for (int j = 0; j < mapArray[i].length; j++) {
                    // COLLISION DETECTION AND PHYSICS ON Y-AXIS
                    // IF PLAYER IS INSIDE A BLOCK SET POSITION TO THE OLD POSITION
                    if (mapArray[i][j].isCollision() && playerPosX + playerSizeX > mapArray[i][j].getPosX()
                            && playerPosX < mapArray[i][j].getPosX() + mapArray[i][j].getBLOCKSIZE()
                            && playerPosY > mapArray[i][j].getPosY() - mapArray[i][j].getBLOCKSIZE()
                            && playerPosY - playerSizeY < mapArray[i][j].getPosY()) {
                        if (gravity > 0){
                            onGround = true;
                            onGroundTimer = 10;
                        }
                        playerPosY = oldY;
                        gravity = -gravity * PLAYER_BOUNCINESS;
                    }
                }
            }

        }

        // GET MOUSE WORLD COORDINATES
        mouseInWorld3D.x = Gdx.input.getX();
        mouseInWorld3D.y = Gdx.input.getY();
        mouseInWorld3D.z = 0;
        cam.unproject(mouseInWorld3D);
        mouseInWorld2D.x = mouseInWorld3D.x;
        mouseInWorld2D.y = mouseInWorld3D.y;

        // LEFT AND RIGHT MOVEMENT
        if (Gdx.input.isKeyPressed(Input.Keys.A) && acceleration > -3) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
                acceleration -= 2;
                if (acceleration < -2) acceleration = -4;
            }else {
                acceleration -= 1;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && acceleration < 3) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
                acceleration += 2;
                if (acceleration > 2) acceleration = 4;
            }else {
                acceleration += 1;
            }
        }

        playerPosX += acceleration;
        // FRICTION
        acceleration *= PLAYER_FRICTION;

        
        for (int i = 0; i < mapArray.length; i++) {
            if (mapArray[i][0].getPosX() > playerPosX - 400 && mapArray[i][0].getPosX() < playerPosX + 400) {
                for (int j = 0; j < mapArray[i].length; j++) {
                    if (mapArray[i][j].getPosY() > playerPosY - 400 && mapArray[i][j].getPosY() < playerPosY + 400) {
                        // COLLISION DETECTION AND PHYSICS ON X-AXIS
                        // IF PLAYER IS INSIDE A BLOCK SET POSITION TO THE OLD POSITION
                        if (mapArray[i][j].isCollision() && playerPosX + playerSizeX > mapArray[i][j].getPosX()
                                && playerPosX < mapArray[i][j].getPosX() + mapArray[i][j].getBLOCKSIZE()
                                && playerPosY > mapArray[i][j].getPosY() - mapArray[i][j].getBLOCKSIZE()
                                && playerPosY - playerSizeY < mapArray[i][j].getPosY()) {
                            playerPosX = oldX;
                            acceleration = -acceleration * PLAYER_BOUNCINESS;
                        }
                        // SET BLOCK HEALTH TO MAX IF LEFT MOUSE BUTTON IS NOT DOWN
                        if (mapArray[i][j].getElement() != EMPTY && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                            if (mapArray[i][j].getElement() == GROUND || mapArray[i][j].getElement() == GRASS){
                                mapArray[i][j].setBlockHealth(50);
                            }else if(mapArray[i][j].getElement() == WOOD){
                                mapArray[i][j].setBlockHealth(100);
                            }else if(mapArray[i][j].getElement() == LEAVES){
                                mapArray[i][j].setBlockHealth(25);
                            }
                            
                        }

                        // CHECK IF MOUSE IS INSIDE CURRENT BLOCK
                        if (mouseInWorld2D.y > mapArray[i][j].getPosY()
                            && mouseInWorld2D.y < mapArray[i][j].getPosY() + mapArray[i][j].getBLOCKSIZE()
                            && mouseInWorld2D.x > mapArray[i][j].getPosX()
                            && mouseInWorld2D.x < mapArray[i][j].getPosX() + mapArray[i][j].getBLOCKSIZE()) {
                            if(j > 3 && j < map.getMapSizeY()-3 && i > 3 && i < map.getMapSizeX()-3){
                                if(mapArray[i-1][j].getElement() == EMPTY || mapArray[i+1][j].getElement() == EMPTY || mapArray[i][j-1].getElement() == EMPTY || mapArray[i][j+1].getElement() == EMPTY){
                                    float distance = (float) Math
                                    .sqrt((mouseInWorld2D.y - playerPosY) * (mouseInWorld2D.y - playerPosY)
                                            + (mouseInWorld2D.x - playerPosX) * (mouseInWorld2D.x - playerPosX));
        
                                    
                                    // IF DISTANCE IS < 150 PIXELS DRAW BLOCK OUTLINE 
                                    if(distance <= 150){
                                        batch.draw(outlineTexture, mapArray[i][j].getPosX(), mapArray[i][j].getPosY());
        
                                        // IF LEFT MOUSE BUTTON IS DOWN REDUCE BLOCK HEALTH OR DESTROY IT (CHANGE TO EMPTY AND COLLISION OFF)
                                        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                                            if (mapArray[i][j].getBlockHealth() > 0) {
                                                mapArray[i][j].setBlockHealth(mapArray[i][j].getBlockHealth() - 1);  
                                            } else {
                                                mapArray[i][j].setElement(EMPTY); 
                                                mapArray[i][j].setCollision(false);
                                            }
                                        }
                                    }
                                }
                            }



                        }
                    }
                }
            }
        }





        if (onGroundTimer <= 0) {
            onGround = false;
        } else {
            onGroundTimer -= 1;
        }

        // DRAW PLAYER
        batch.draw(playerTexture, playerPosX, playerPosY - 25, playerSizeX, playerSizeY);
    }

    public float getX() {
        return playerPosX;
    }

    public void setX(float x) {
        this.playerPosX = x;
    }

    public float getY() {
        return playerPosY;
    }

    public void setY(float y) {
        this.playerPosY = y;
    }
}
