package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private boolean onLadder;
    private int onGroundTimer;

    private int soundTimer;

    private float gravity;
    private float acceleration;

    final float PLAYER_BOUNCINESS = 0.0f; // 0 = NO BOUNCE
    final float PLAYER_FRICTION = 0.7f; // 1 = NO FRICTION

    private Texture playerTexture;
    private Texture outlineTexture;
    private Texture healthTexture;

    private Texture blockBreakingTexture;
    private TextureRegion[][] blockBreakingAnimation;

    private Sound damageSound;
    private Sound stoneHitSound;

    private int playerHealth;

    public Player(float x, float y) {
        this.playerPosX = x;
        this.playerPosY = y;

        damageSound = Gdx.audio.newSound(Gdx.files.internal("damage.mp3"));
        stoneHitSound = Gdx.audio.newSound(Gdx.files.internal("stoneHitSound.mp3"));
        
        playerSizeX = 20;
        playerSizeY = 49;

        onGround = false;
        onGroundTimer = 0;

        soundTimer = 0;

        playerTexture = new Texture("jusju.png");
        outlineTexture = new Texture("outline.png");
        healthTexture = new Texture("heart.png");
        blockBreakingTexture = new Texture("breaktiles.png");

        blockBreakingAnimation = TextureRegion.split(blockBreakingTexture, 25, 25);


        gravity = 0;
        acceleration = 0;
    }

    // UPDATE AND DRAW PLAYER
    public void Update(Map map, Camera cam, Batch batch) {
        float oldX = playerPosX;
        float oldY = playerPosY;

        // APPLY GRAVITY TO PLAYER
        playerPosY -= gravity;

        // JUMP
        if (onGround) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                gravity = -4;
                onGroundTimer = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {

                if (onLadder){
                    gravity = +4;
                    onGroundTimer = 0;
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                gravity = -5;
                onGroundTimer = 0;
            }
        } else {
            gravity += 0.25;
        }
        
        Block[][] mapArray = map.getMapArray();

        for (int i = 0; i < mapArray.length; i++) {
            if (mapArray[i][0].getPosX() > playerPosX - 400 && mapArray[i][0].getPosX() < playerPosX + 400) {
                for (int j = 0; j < mapArray[i].length; j++) {
                    // COLLISION DETECTION AND PHYSICS ON Y-AXIS
                    // IF PLAYER IS INSIDE A BLOCK SET POSITION TO THE OLD POSITION
                    if (playerPosX + playerSizeX > mapArray[i][j].getPosX()
                            && playerPosX < mapArray[i][j].getPosX() + mapArray[i][j].getBLOCKSIZE()
                            && playerPosY > mapArray[i][j].getPosY() - mapArray[i][j].getBLOCKSIZE()
                            && playerPosY - playerSizeY < mapArray[i][j].getPosY()) {
                        if (mapArray[i][j].isCollision()) {
                            
                            
            
                          if(gravity > 10) {
                                // Gdx.app.exit();
                                playerHealth -= 5;
                            } else if(gravity >= 9.5) {
                                playerHealth -= 4;
                                damageSound.play(0.2f);
                            } else if(gravity >= 9) {
                                playerHealth -= 3;
                                damageSound.play(0.2f);
                            } else if(gravity >= 8.25) {
                                playerHealth -= 2;
                                damageSound.play(0.2f);
                            } else if(gravity >= 7.25) {
                                playerHealth -= 1;
                                damageSound.play(0.2f);
                            }

                            
  
                            

                            if (gravity > 0) {
                                onGround = true;
                                onLadder = false;
                                onGroundTimer = 5;
                            }
                            playerPosY = oldY;
                            gravity = -gravity * PLAYER_BOUNCINESS;
                        } else if (mapArray[i][j].getElement() == LADDER) {
                            gravity *= 0.8;
                            onGround = true;
                            onLadder = true;
                            onGroundTimer = 5;
                        }

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

        Boolean isRunning = false;

        // LEFT AND RIGHT MOVEMENT
        if (Gdx.input.isKeyPressed(Input.Keys.A) ) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                isRunning = true;
                acceleration -= 3;
            } else {
                acceleration -= 1;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) ) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                isRunning = true;
                acceleration += 3;
            } else {
                acceleration += 1;
            }
        }

        if (acceleration > 3 && isRunning == false) {
            acceleration = 3;
        } else if (acceleration > 4 && isRunning == true) {
            acceleration = 4;
        }
        if (acceleration < -3 && isRunning == false) {
            acceleration = -3;
        }else if (acceleration < -4 && isRunning == true) {
            acceleration = -4;
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
                           
                            mapArray[i][j].setBlockHealth(mapArray[i][j].getMaxhealth()); 

                        }

                        // CHECK IF MOUSE IS INSIDE CURRENT BLOCK
                        if (mouseInWorld2D.y > mapArray[i][j].getPosY()
                                && mouseInWorld2D.y < mapArray[i][j].getPosY() + mapArray[i][j].getBLOCKSIZE()
                                && mouseInWorld2D.x > mapArray[i][j].getPosX()
                                && mouseInWorld2D.x < mapArray[i][j].getPosX() + mapArray[i][j].getBLOCKSIZE()) {
                            if (j > 3 && j < map.getMapSizeY() - 3 && i > 3 && i < map.getMapSizeX() - 3) {
                                if (!mapArray[i - 1][j].isCollision()|| !mapArray[i + 1][j].isCollision()
                                        || !mapArray[i][j - 1].isCollision()
                                        || !mapArray[i][j + 1].isCollision()) {
                                    float distance = (float) Math
                                            .sqrt((mouseInWorld2D.y - playerPosY) * (mouseInWorld2D.y - playerPosY)
                                                    + (mouseInWorld2D.x - playerPosX)
                                                            * (mouseInWorld2D.x - playerPosX));

                                    // IF DISTANCE IS < 150 PIXELS DRAW BLOCK OUTLINE
                                    if (distance <= 150) {
                                        
                                        batch.setColor(1,1,1,0.5f);
                                        // IF LEFT MOUSE BUTTON IS DOWN REDUCE BLOCK HEALTH OR DESTROY IT (CHANGE TO
                                        // EMPTY AND COLLISION OFF)
                                        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                                            if (mapArray[i][j].getBlockHealth() > 0) {
                                                
                                                if(mapArray[i][j].getBlockHealth() * 100 / mapArray[i][j].getMaxhealth() > 75){
                                                    batch.draw(blockBreakingAnimation[0][0],mapArray[i][j].getPosX(), mapArray[i][j].getPosY() );
                                                }else if(mapArray[i][j].getBlockHealth() * 100 / mapArray[i][j].getMaxhealth() > 50){
                                                    batch.draw(blockBreakingAnimation[0][1],mapArray[i][j].getPosX(), mapArray[i][j].getPosY() );
                                                }else if(mapArray[i][j].getBlockHealth() * 100 / mapArray[i][j].getMaxhealth() > 25){
                                                    batch.draw(blockBreakingAnimation[0][2],mapArray[i][j].getPosX(), mapArray[i][j].getPosY() );
                                                }else {
                                                    batch.draw(blockBreakingAnimation[0][3],mapArray[i][j].getPosX(), mapArray[i][j].getPosY() );
                                                }
                                                
                                                mapArray[i][j].setBlockHealth(mapArray[i][j].getBlockHealth() - 1);
                                                soundTimer += 1;
                                            } else {
                                                mapArray[i][j].setElement(EMPTY);
                                                mapArray[i][j].setCollision(false);
                                            }
                                        }
                                        batch.setColor(1,1,1,1);
                                        batch.draw(outlineTexture, mapArray[i][j].getPosX(), mapArray[i][j].getPosY());

                                        // IF RIGHT MOUSE BUTTON IS DOWN, PLACE BLOCK IN HAND
                                        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                                            if (mapArray[i][j].getElement() == EMPTY) {
                                                mapArray[i][j].setElement(LADDER);
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
        if(soundTimer == 20) {
            stoneHitSound.play();
            soundTimer -= 20;
        }

        if (onGroundTimer <= 0) {
            onGround = false;
        } else {
            onGroundTimer -= 1;
        }

        
        
        // DRAW PLAYER
        batch.draw(playerTexture, playerPosX, playerPosY - 24);
        cam.position.set((int)playerPosX, (int)playerPosY - 25, 0); 
        cam.update();
    }


    public void DrawHud(Batch batch){

        // DRAW PLAYER HEALTH
        for (int i = 0; i < playerHealth; i++){
            batch.draw(healthTexture, 32 + 20 * i, 10);
        }
    
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

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

}
