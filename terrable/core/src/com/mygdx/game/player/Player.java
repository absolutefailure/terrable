package com.mygdx.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.camera.HudCamera;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;
import com.mygdx.game.mobs.Mob;

import static com.mygdx.game.map.Element.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Date;

public class Player {
    private Vector2 mouseInWorld2D = new Vector2();
    private Vector3 mouseInWorld3D = new Vector3();



    private float playerPosX;
    private float playerPosY;

    private int playerSizeX;
    private int playerSizeY;

    private boolean onGround;
    private boolean onLadder;
    private float onGroundTimer;

    private float soundTimer;

    private float gravity;
    private float acceleration;

    final float PLAYER_BOUNCINESS = 0f; // 0 = NO BOUNCE
    final float PLAYER_FRICTION = 0.7f; // 1 = NO FRICTION

    private Texture playerTexture;
    private Texture outlineTexture;
    private Texture healthTexture;
    private Texture textures;
    private TextureRegion[][] blockTextures;

    private Texture blockBreakingTexture;
    private TextureRegion[][] blockBreakingAnimation;

    private Sound damageSound;
    private Sound stoneHitSound;
    private Sound groundHitSound;
    private Sound leavesHitSound;
    private Sound blockBreakingSound;
    private Sound weaponBreakingSound;
    private Sound doorOpenSound;
    private Sound doorCloseSound;
    private int soundEffect; // TEMPORARY

    private int playerHealth;
    private long lastHitTime;

    private BitmapFont font;
    private String elementString = "";


    private ArrayList<Mob> mobs;

    private Inventory inventory;

    private ArrayList<Item> droppedItems;


    public Player(float x, float y) {
        this.playerPosX = x;
        this.playerPosY = y;

        damageSound = Gdx.audio.newSound(Gdx.files.internal("sounds/damage.mp3"));
        stoneHitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/stoneHitSound.mp3"));
        groundHitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/groundHitSound.mp3"));
        leavesHitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/leavesHitSound.mp3"));
        blockBreakingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/blockBreakingSound.mp3"));
        weaponBreakingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/weaponBreakingSound.mp3"));
        doorOpenSound = Gdx.audio.newSound(Gdx.files.internal("sounds/door-close.mp3"));
        doorCloseSound = Gdx.audio.newSound(Gdx.files.internal("sounds/door-open.mp3"));

        playerSizeX = 20;
        playerSizeY = 49;

        onGround = false;
        onGroundTimer = 0;

        soundTimer = 0;
        lastHitTime = 0;

        playerTexture = new Texture("jusju.png");
        outlineTexture = new Texture("outline.png");
        healthTexture = new Texture("heart.png");
        blockBreakingTexture = new Texture("breaktiles.png");

        textures = new Texture("tileset2.png");
        blockTextures = TextureRegion.split(textures, 25, 25);

        blockBreakingAnimation = TextureRegion.split(blockBreakingTexture, 25, 25);

        inventory = new Inventory();
        droppedItems = new ArrayList<>();

        font = new BitmapFont(Gdx.files.internal("fonts/Cambria.fnt"));

        mobs = Map.getMobs();

        gravity = 0;
        acceleration = 0;
    }
    
    // UPDATE AND DRAW PLAYER
    public void Update(Map map, Camera cam, Batch batch, int volume, float delta) {
        float oldX = playerPosX;
        float oldY = playerPosY;

        // JUMP
        if (onGround && !inventory.isFurnaceOpen()) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                gravity = -4.3f;
                onGroundTimer = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {

                if (onLadder) {
                    gravity = +4.3f;
                    onGroundTimer = 0;
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                gravity = -5.3f;
                onGroundTimer = 0;
            }
        }
        if (!onLadder){
            gravity += 0.25 * delta;
        }
        
        // APPLY GRAVITY TO PLAYER
        playerPosY -= gravity * delta;
        int startBlockX = (int)(playerPosX / 25 - 1600 / 25 / 2) +2500;
        int endBlockX = (startBlockX + 1600 / 25) ;

        Block[][] mapArray = map.getMapArray();

        for (int x = startBlockX; x < endBlockX; x++) {
            if (mapArray[x][0].getPosX() > playerPosX - 400 && mapArray[x][0].getPosX() < playerPosX + 400) {
                for (int y = 0; y < mapArray[x].length; y++) {
                    // COLLISION DETECTION AND PHYSICS ON Y-AXIS
                    // IF PLAYER IS INSIDE A BLOCK SET POSITION TO THE OLD POSITION
                    if (playerPosX + playerSizeX > mapArray[x][y].getPosX()
                            && playerPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                            && playerPosY + playerSizeY > mapArray[x][y].getPosY()
                            && playerPosY < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {
                        if (mapArray[x][y].isCollision()) {

                            if (gravity > 13) {
                                // Gdx.app.exit();
                                playerHealth -= 5;
                            } else if (gravity >= 12) {
                                playerHealth -= 4;
                                damageSound.play(volume/200f);
                            } else if (gravity >= 11) {
                                playerHealth -= 3;
                                damageSound.play(volume/200f);
                            } else if (gravity >= 10) {
                                playerHealth -= 2;
                                damageSound.play(volume/200f);
                            } else if (gravity >= 9) {
                                playerHealth -= 1;
                                damageSound.play(volume/200f);
                            }

                            if (gravity > 0) {
                                onGround = true;
                                onLadder = false;
                                onGroundTimer = 5;
                            }
                            float blockTop = mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE();
                            float blockBottom = (mapArray[x][y].getPosY());
                            float dy = playerPosY-oldY ;
                    
                            if (dy > 0) {
                                playerPosY = blockBottom - playerSizeY;
                            } else {
                                playerPosY = blockTop;
                            }
                            

                            //playerPosY = oldY;
                            gravity = -gravity * PLAYER_BOUNCINESS * delta ;
                        } else if (mapArray[x][y].getElement() == LADDER) {
                            gravity *= Math.pow(0.8f, delta);
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
        if (!inventory.isFurnaceOpen()){
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    isRunning = true;
                    acceleration -= 3;
                } else {
                    acceleration -= 1;
                }
            }
    
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    isRunning = true;
                    acceleration += 3;
                } else {
                    acceleration += 1;
                }
            }
        }


        if (acceleration > 3 && isRunning == false) {
            acceleration = 3;
        } else if (acceleration > 4 && isRunning == true) {
            acceleration = 4;
        }
        if (acceleration < -3 && isRunning == false) {
            acceleration = -3;
        } else if (acceleration < -4 && isRunning == true) {
            acceleration = -4;
        }

        playerPosX += acceleration * delta;
        // FRICTION
        acceleration *= Math.pow(PLAYER_FRICTION, delta);

        for (int x = startBlockX; x < endBlockX; x++) {
            if (mapArray[x][0].getPosX() > playerPosX - 400 && mapArray[x][0].getPosX() < playerPosX + 400) {
                for (int y = 0; y < mapArray[x].length; y++) {
                    if (mapArray[x][y].getPosY() > playerPosY - 400 && mapArray[x][y].getPosY() < playerPosY + 400) {
                        // COLLISION DETECTION AND PHYSICS ON X-AXIS
                        // IF PLAYER IS INSIDE A BLOCK SET POSITION TO THE OLD POSITION
                        if (mapArray[x][y].isCollision() && playerPosX + playerSizeX > mapArray[x][y].getPosX()
                                && playerPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                                && playerPosY + playerSizeY > mapArray[x][y].getPosY()
                                && playerPosY < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {
                            float blockRight = mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE();
                            float blockLeft = (mapArray[x][y].getPosX());
                            float dx = playerPosX-oldX ;
                    
                            if (dx > 0) {
                                playerPosX = blockLeft - playerSizeX;
                            } else {
                                playerPosX = blockRight;
                            }
                            //playerPosX = oldX;
                            acceleration = -acceleration * PLAYER_BOUNCINESS * delta;
                        }
                        // SET BLOCK HEALTH TO MAX IF LEFT MOUSE BUTTON IS NOT DOWN
                        if (mapArray[x][y].getElement() != EMPTY && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {

                            mapArray[x][y].setBlockHealth(mapArray[x][y].getMaxhealth());

                        }

                        // CHECK IF MOUSE IS INSIDE CURRENT BLOCK
                        if (!inventory.isInventoryOpen() && mouseInWorld2D.y > mapArray[x][y].getPosY()
                                && mouseInWorld2D.y < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()
                                && mouseInWorld2D.x > mapArray[x][y].getPosX()
                                && mouseInWorld2D.x < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()) {
                            if (y > 3 && y < map.getMapSizeY() - 3 && x > 3 && x < map.getMapSizeX() - 3) {
                                if (!mapArray[x - 1][y].isCollision() || !mapArray[x + 1][y].isCollision()
                                        || !mapArray[x][y - 1].isCollision()
                                        || !mapArray[x][y + 1].isCollision()) {
                                    float distance = (float) Math
                                            .sqrt((mouseInWorld2D.y - playerPosY) * (mouseInWorld2D.y - playerPosY)
                                                    + (mouseInWorld2D.x - playerPosX)
                                                            * (mouseInWorld2D.x - playerPosX));

                                    // IF DISTANCE IS < 150 PIXELS DRAW BLOCK OUTLINE
                                    if (distance <= 150) {
                                        // Right click to open furnace menu
                                        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
                                            && (mapArray[x][y].getElement() == FURNACE || mapArray[x][y].getElement() == FURNACE2) && !inventory.isInventoryOpen()){
                                            inventory.setInventoryOpen(true);
                                            inventory.setFurnaceOpen(true);
                                            inventory.setOpenFurnaceX(x);
                                            inventory.setOpenFurnaceY(y);

                                        } 
                                        batch.setColor(1, 1, 1, 0.5f);
                                        // IF LEFT MOUSE BUTTON IS DOWN REDUCE BLOCK HEALTH OR DESTROY IT (CHANGE TO
                                        // EMPTY AND COLLISION OFF)
                                        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                                                && mapArray[x][y].getElement() != EMPTY) {
                                            if (mapArray[x][y].getBlockHealth() > 0) {

                                                if (mapArray[x][y].getBlockHealth() * 100
                                                / mapArray[x][y].getMaxhealth() < 99 ){

                                                    if (mapArray[x][y].getBlockHealth() * 100
                                                    / mapArray[x][y].getMaxhealth() > 75) {
                                                        batch.draw(blockBreakingAnimation[0][0], mapArray[x][y].getPosX(),
                                                                mapArray[x][y].getPosY());
                                                    } else if (mapArray[x][y].getBlockHealth() * 100
                                                            / mapArray[x][y].getMaxhealth() > 50) {
                                                        batch.draw(blockBreakingAnimation[0][1], mapArray[x][y].getPosX(),
                                                                mapArray[x][y].getPosY());
                                                    } else if (mapArray[x][y].getBlockHealth() * 100
                                                            / mapArray[x][y].getMaxhealth() > 25) {
                                                        batch.draw(blockBreakingAnimation[0][2], mapArray[x][y].getPosX(),
                                                                mapArray[x][y].getPosY());
                                                    } else {
                                                        batch.draw(blockBreakingAnimation[0][3], mapArray[x][y].getPosX(),
                                                                mapArray[x][y].getPosY());
                                                    }
                                                }

                                                soundEffect = mapArray[x][y].getElement();

                                                //damage to block determined here
                                                float damage = inventory.getSelectedItem().getDamage() * delta;
                                                //damage modifiers for pickaxes
                                                if ((inventory.getSelectedItem()).getElement() == WOODPICKAXE || (inventory.getSelectedItem()).getElement() == STONEPICKAXE ||
                                                 (inventory.getSelectedItem()).getElement() == IRONPICKAXE || (inventory.getSelectedItem()).getElement() == DIAMONDPICKAXE ) {
                                                    if (mapArray[x][y].getElement() == STONE || mapArray[x][y].getElement() == IRON || mapArray[x][y].getElement() == COAL || mapArray[x][y].getElement() == DIAMOND) {
                                                        damage *= 2;
                                                    }
                                                }
                                                //damage modifiers for axes
                                                if ((inventory.getSelectedItem()).getElement() == WOODAXE || (inventory.getSelectedItem()).getElement() == STONEAXE ||
                                                 (inventory.getSelectedItem()).getElement() == IRONAXE || (inventory.getSelectedItem()).getElement() == DIAMONDAXE ) {
                                                    if (mapArray[x][y].getElement() == WOOD || mapArray[x][y].getElement() == PLANKS) {
                                                        damage *= 3;
                                                    }
                                                }

                                                //block damage calculated based on block health and weapon damage
                                                if (mapArray[x][y].getMaxhealth() >= 0 && mapArray[x][y].getMaxhealth() < 101) {
                                                    mapArray[x][y].setBlockHealth(mapArray[x][y].getBlockHealth() - damage);
                                                } else if (mapArray[x][y].getMaxhealth() > 199 && mapArray[x][y].getMaxhealth() < 220 && inventory.getSelectedItem().getDamage() > 4) {
                                                    mapArray[x][y].setBlockHealth(mapArray[x][y].getBlockHealth() - damage);
                                                } else if (mapArray[x][y].getMaxhealth() > 221 && mapArray[x][y].getMaxhealth() < 251 && inventory.getSelectedItem().getDamage() > 24) {
                                                    mapArray[x][y].setBlockHealth(mapArray[x][y].getBlockHealth() - damage);
                                                } else if (mapArray[x][y].getMaxhealth() > 252 && inventory.getSelectedItem().getDamage() > 49) {
                                                    mapArray[x][y].setBlockHealth(mapArray[x][y].getBlockHealth() - damage);
                                                } else {
                                                    mapArray[x][y].setBlockHealth(mapArray[x][y].getBlockHealth() - 0);
                                                }

                                                soundTimer += 1 * delta;
                                            } else {
                                                // hitting blocks damages weapon and ultimately breaks them
                                                if (inventory.getSelectedItem().isWeapon()){
                                                    inventory.getSelectedItem().setHealth(inventory.getSelectedItem().getHealth()-1);
                                                    if(inventory.getSelectedItem().getHealth() == 0) {
                                                        inventory.getSelectedItem().removeItem();
                                                        weaponBreakingSound.play(volume / 200f);
                                                    }
                                                }
                                                if (mapArray[x][y-1].getElement() == REDFLOWER || mapArray[x][y-1].getElement() == TALLGRASS){
                                                    Random rand = new Random();
                                                    Item newItem = new Item();
                                                    newItem.setElement(mapArray[x][y-1].getElement());
                                                    newItem.setAmount(1);
                                                    newItem.setX(mapArray[x][y-1].getPosX()+6);
                                                    newItem.setY(mapArray[x][y-1].getPosY()+6);
                                                    newItem.setAcceleration(rand.nextFloat() * 2 - 1);
                                                    droppedItems.add(newItem);
                                                    mapArray[x][y-1].setElement(EMPTY);
                                                }
                                                if (mapArray[x][y].getElement() == LEAVES
                                                        || mapArray[x][y].getElement() == TALLGRASS
                                                        || mapArray[x][y].getElement() == REDFLOWER) {
                                                    leavesHitSound.play(volume/200f, 0.5f, 0);
                                                } else {
                                                    blockBreakingSound.play(volume / 200f);
                                                }
                                                if (mapArray[x][y].getElement() == GRASS) {
                                                    mapArray[x][y].setElement(GROUND);
                                                }
                                                if (mapArray[x][y].getElement() == COAL) {
                                                    mapArray[x][y].setElement(COALITEM);
                                                }
                                                if (mapArray[x][y].getElement() == DIAMOND) {
                                                    mapArray[x][y].setElement(DIAMONDITEM);
                                                }

                                                // Both parts of door break when destroyed
                                                if (mapArray[x][y].getElement() == DOOR2){
                                                    mapArray[x][y].setElement(DOOR);
                                                    mapArray[x][y + 1].setElement(EMPTY);
                                                    mapArray[x][y].setCollision(false);
                                                    mapArray[x][y + 1].setCollision(false);
                                                }
                                                if (mapArray[x][y].getElement() == DOOR1){
                                                    mapArray[x][y].setElement(DOOR);
                                                    mapArray[x][y - 1].setElement(EMPTY);
                                                    mapArray[x][y].setCollision(false);
                                                    mapArray[x][y - 1].setCollision(false);
                                                }
                                                Random rand = new Random();
                                                Item newItem = new Item();
                                                newItem.setElement(mapArray[x][y].getElement());
                                                newItem.setAmount(1);
                                                newItem.setX(mapArray[x][y].getPosX()+6);
                                                newItem.setY(mapArray[x][y].getPosY()+6);
                                                newItem.setAcceleration(rand.nextFloat() * 2 - 1);
                                                droppedItems.add(newItem);
                                                mapArray[x][y].setElement(EMPTY);
                                                mapArray[x][y].setCollision(false);

                                            }
                                        } else {
                                            soundTimer = 15;
                                        }

                                        batch.setColor(1, 1, 1, 1);
                                        batch.draw(outlineTexture, mapArray[x][y].getPosX(), mapArray[x][y].getPosY());
                                            if (mapArray[x][y].getElement() == 0) {
                                                elementString = "";
                                            }else if (mapArray[x][y].getElement() == 1) {
                                                elementString = "Dirt";
                                            } else if (mapArray[x][y].getElement() == 2) {
                                                elementString = "Grass";
                                            } else if (mapArray[x][y].getElement() == 3) {
                                                elementString = "Wood";
                                            } else if (mapArray[x][y].getElement() == 4) {
                                                elementString = "Leaves";
                                            } else if (mapArray[x][y].getElement() == 5) {
                                                elementString = "Ladder";
                                            } else if (mapArray[x][y].getElement() == 6) {
                                                elementString = "Stone";
                                            } else if (mapArray[x][y].getElement() == 7) {
                                                elementString = "Coal";
                                            } else if (mapArray[x][y].getElement() == 8) {
                                                elementString = "Iron ore";
                                            } else if (mapArray[x][y].getElement() == 9) {
                                                elementString = "Diamond ore";
                                            } else if (mapArray[x][y].getElement() == 10) {
                                                elementString = "Tall grass";
                                            } else if (mapArray[x][y].getElement() == 11) {
                                                elementString = "Red flower";
                                            } else if (mapArray[x][y].getElement() == 12) {
                                                elementString = "Wooden planks";
                                            } else if (mapArray[x][y].getElement() == 13) {
                                                elementString = "Piece of coal";
                                            } else if (mapArray[x][y].getElement() == 14) {
                                                elementString = "Diamond";
                                            } else if (mapArray[x][y].getElement() == 15) {
                                                elementString = "Stone pick-axe";
                                            } else if (mapArray[x][y].getElement() == 16) {
                                                elementString = "Wooden pick-axe";
                                            } else if (mapArray[x][y].getElement() == 17) {
                                                elementString = "Iron pick-axe";
                                            } else if (mapArray[x][y].getElement() == 18) {
                                                elementString = "Diamond pick-axe";
                                            } else if (mapArray[x][y].getElement() == 19) {
                                                elementString = "Stone axe";
                                            } else if (mapArray[x][y].getElement() == 20) {
                                                elementString = "Wooden axe";
                                            } else if (mapArray[x][y].getElement() == 21) {
                                                elementString = "Iron axe";
                                            } else if (mapArray[x][y].getElement() == 22) {
                                                elementString = "Diamond axe";
                                            } else if (mapArray[x][y].getElement() == 23) {
                                                elementString = "Iron ingot";
                                            } else if (mapArray[x][y].getElement() == 24) {
                                                elementString = "Door";
                                            } else if (mapArray[x][y].getElement() == 27) {
                                                elementString = "Stick";
                                            } else if (mapArray[x][y].getElement() == 28) {
                                                elementString = "Torch";
                                            } else if (mapArray[x][y].getElement() == 29) {
                                                elementString = "Feather";
                                            } else if (mapArray[x][y].getElement() == 30) {
                                                elementString = "Ball of slime";
                                            }
                                        

                                        // IF RIGHT MOUSE BUTTON IS DOWN, PLACE BLOCK IN HAND
                                        if  (inventory.getSelectedItem().isPlaceable()
                                                && mapArray[x][y].getElement() == EMPTY
                                                && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                                            if (!(playerPosX + playerSizeX > mapArray[x][y].getPosX()
                                                    && playerPosX < mapArray[x][y].getPosX()
                                                            + mapArray[x][y].getBLOCKSIZE()
                                                    && playerPosY + playerSizeY > mapArray[x][y].getPosY()
                                                    && playerPosY < mapArray[x][y].getPosY()
                                                            + mapArray[x][y].getBLOCKSIZE())
                                                    || inventory.getSelectedItem().getElement() == LADDER
                                                    || inventory.getSelectedItem().getElement() == TALLGRASS
                                                    || inventory.getSelectedItem().getElement() == REDFLOWER) {
                                                if (inventory.getSelectedItem().getAmount() > 0) {
   
                                                    // Placing a door block makes a 2 block tall door and prevent placing a door in 1 block space
                                                    if (inventory.getSelectedItem().getElement() == DOOR && mapArray[x][y-1].getElement() == EMPTY){
                                                        mapArray[x][y].setElement(DOOR1);
                                                        mapArray[x][y - 1].setElement(DOOR2);

                                                        inventory.getSelectedItem().removeItem();

                                                    }else if (inventory.getSelectedItem().getElement() != DOOR) {
                                                        mapArray[x][y].setElement(inventory.getSelectedItem().getElement());
                                                        if (mapArray[x][y].getElement() == FURNACE){
                                                            mapArray[x][y].setFurnaceSlot1(new Item());
                                                            mapArray[x][y].setFurnaceSlot2(new Item());
                                                            mapArray[x][y].setFurnaceSlot3(new Item());
                                                        }
                                                        // prevent collision on certain blocks
                                                        switch (inventory.getSelectedItem().getElement()) {
                                                            case LADDER:
                                                                break;
                                                            case TALLGRASS:
                                                                break;
                                                            case REDFLOWER:
                                                                break;
                                                            case TORCH:
                                                                break;
                                                            default:
                                                                if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                                                                    mapArray[x][y].setCollision(false);
                                                                } else {
                                                                    mapArray[x][y].setCollision(true);
                                                                }
                                                                
                                                        }

                                                        inventory.getSelectedItem().removeItem();
                                                    }
                                                }
                                            }
                                        }

                                        // Right click to open door
                                        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
                                        && (mapArray[x][y].getElement()  == DOOR1 || mapArray[x][y].getElement()  == DOOR2)){

                                            if (mapArray[x][y].getElement() == DOOR1){
                                                
                                                if (mapArray[x][y].isCollision() == true){
                                                    mapArray[x][y].setCollision(false);
                                                    mapArray[x][y - 1].setCollision(false);
                                                    doorCloseSound.play(volume/200f, 1.1f, 0);
                                                }else if (mapArray[x][y].isCollision() == false) {
                                                    mapArray[x][y].setCollision(true);
                                                    mapArray[x][y - 1].setCollision(true);
                                                    doorOpenSound.play(volume/200f, 1f, 0);
                                                }
                                            }

                                            if (mapArray[x][y].getElement() == DOOR2){
                                                if (mapArray[x][y].isCollision() == true){
                                                    mapArray[x][y].setCollision(false);
                                                    mapArray[x][y + 1].setCollision(false);
                                                    doorCloseSound.play(volume/200f, 1f, 0);
                                                }else if (mapArray[x][y].isCollision() == false){
                                                    mapArray[x][y].setCollision(true);
                                                    mapArray[x][y + 1].setCollision(true);
                                                    doorOpenSound.play(volume/200f, 1.1f, 0);
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
        }

        // check if mouse is inside mob
        for(int i = 0; i < mobs.size(); i++) {
            Mob thisMob = mobs.get(i);
            if (!inventory.isInventoryOpen() &&
            mouseInWorld2D.x >= thisMob.getMobPosX() &&
            mouseInWorld2D.x <= thisMob.getMobPosX() + thisMob.getMobSizeX() &&
            mouseInWorld2D.y >= thisMob.getMobPosY() &&
            mouseInWorld2D.y <= thisMob.getMobPosY() + thisMob.getMobSizeY()) {
                
                // distance to mob
                float distance = (float) Math.sqrt((mouseInWorld2D.y - playerPosY) * (mouseInWorld2D.y - playerPosY) + 
                (mouseInWorld2D.x - playerPosX) * (mouseInWorld2D.x - playerPosX));
                
                if (distance <= 75) {
                    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        // add hit effect
                        if (thisMob.getMobHealth() - inventory.getSelectedItem().getDamage() <= 0) {
                            
                            Random rand = new Random();
                            Item newItem = new Item();
                            newItem.setElement(thisMob.getElement());
                            newItem.setAmount(1);
                            newItem.setX(thisMob.getMobPosX()+6);
                            newItem.setY(thisMob.getMobPosY()+6);
                            newItem.setResource(true);
                            newItem.setAcceleration(rand.nextFloat() * 2 - 1);
                            droppedItems.add(newItem);

                            mobs.remove(i);
                        } else {
                            thisMob.setMobHealth(thisMob.getMobHealth() - inventory.getSelectedItem().getDamage());
                        }
                    }
                }

                if (distance <= 150) {
                    elementString = thisMob.getClass().getSimpleName();
                }
            }
        }

        // Hit damage to player when mob is close
        if (mobs.size() > 0) {
            for(int i = 0; i < mobs.size(); i++) {
                Mob thisMob = mobs.get(i);
                long currentTime = new Date().getTime();
                long timeSinceLastHit = (currentTime - lastHitTime);
                if (thisMob.getType() == "harmful") {
                    if(20 >= (Math.sqrt((thisMob.getMobPosX() - getX()) * (thisMob.getMobPosX() - getX()) + (thisMob.getMobPosY() - getY()) * (thisMob.getMobPosY() - getY()))) && timeSinceLastHit > 2000) {
                        // Add knockback left and right
                        if (thisMob.getMobPosX() < getX()){
                            int healthWhenHit = getPlayerHealth();
                            setPlayerHealth(healthWhenHit-3);
                            lastHitTime = currentTime;
                        }else if (thisMob.getMobPosX() > getX()){
                            int healthWhenHit = getPlayerHealth();
                            setPlayerHealth(healthWhenHit-3);
                            lastHitTime = currentTime;
                        }
                    }
                }
            }
        }

        if (soundTimer >= 20) {
            if (soundEffect == GRASS || soundEffect == GROUND) {
                groundHitSound.play(volume/200f, 0.8f, 0);
            } else if (soundEffect == STONE || soundEffect == IRON || soundEffect == COAL) {
                stoneHitSound.play(volume / 200f);
                soundEffect = 0;
            }
            soundTimer = 0;
        }

        if (onGroundTimer <= 0) {
            onGround = false;
            onLadder = false;
        } else {
            onGroundTimer -= 1 * delta;
        }


        for (int a = 0; a < droppedItems.size(); a++){
            Item item = droppedItems.get(a);
            

            item.Update(mapArray, playerPosX, playerPosY, delta);
            if (item.getX() + 12 >= playerPosX 
            && item.getX() <= playerPosX + playerSizeX
            && item.getY() + 12 >= playerPosY 
            && item.getY() <= playerPosY + playerSizeY ) {

                inventory.addItem(item);
                droppedItems.remove(item);

            
            }

            
            batch.draw(blockTextures[0][item.getElement()-1], item.getX(),item.getY()+item.getShakeTimer(),12,12);
        }


        // DRAW PLAYER
        batch.draw(playerTexture, cam.position.x+acceleration,cam.position.y-gravity);
        cam.position.set( Math.round(playerPosX-acceleration),  Math.round(playerPosY+gravity), 0);
        cam.update();
    }

    public void DrawHud(Batch batch, HudCamera cam, Block[][] mapArray, float delta) {

     

        // DRAW PLAYER HEALTH
        for (int i = 0; i < playerHealth; i++) {
            batch.draw(healthTexture, 32 + 20 * i, 10);
        }

        // DRAW CURRENT ELEMENT SELECTED
        if (elementString.length() < 10) {
            font.draw(batch, elementString, (1560 - elementString.length() * 2) , 890);
        } else if (elementString.length() > 40) {
            font.draw(batch, elementString, (1560 - elementString.length() * 5) , 890);
        } else {
            font.draw(batch, elementString, (1560 - elementString.length() * 4) , 890);
        }   
        inventory.Update(batch, this, blockTextures, cam, outlineTexture, mapArray, delta);

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

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public ArrayList<Item> getInventory() {
        return inventory.getItems();
    }

    public void setInventory(ArrayList<Item> items) {
        inventory.setItems(items);
    }

    public void addDroppedItem(Item item){
        droppedItems.add(item);
    }

    public void resetInventory() {
        inventory.reset();
        droppedItems.clear();

    }

    public int getPlayerSizeX() {
        return playerSizeX;
    }

    public void setPlayerSizeX(int playerSizeX) {
        this.playerSizeX = playerSizeX;
    }

    public int getPlayerSizeY() {
        return playerSizeY;
    }

    public void setPlayerSizeY(int playerSizeY) {
        this.playerSizeY = playerSizeY;
    }

}