package com.mygdx.game;

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

import static com.mygdx.game.map.elements.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Date;

public class Player {
    private Vector2 mouseInWorld2D = new Vector2();
    private Vector3 mouseInWorld3D = new Vector3();

    private final int INVENTORY_SLOT_MAX = 32;

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
    private Texture hotbarTexture;
    private Texture inventoryTexture;
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

    private int grab = -1;
    private boolean isGrabbed;
    private int selectedSlot = 0;
    private boolean isInventoryOpen = false;
    private ArrayList<Integer> usedSlots = new ArrayList<>();

    private ArrayList<Mob> mobs;

    private ArrayList<InventorySlot> inventory;
    private ArrayList<InventorySlot> droppedItems;
    BitmapFont font = new BitmapFont();

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
        hotbarTexture = new Texture("hotbar.png");
        inventoryTexture = new Texture("crafting.png");

        textures = new Texture("tileset2.png");
        blockTextures = TextureRegion.split(textures, 25, 25);

        blockBreakingAnimation = TextureRegion.split(blockBreakingTexture, 25, 25);

        inventory = new ArrayList<>();
        for (int i = 0; i < 46; i++) {
            inventory.add(new InventorySlot());
        }
        droppedItems = new ArrayList<>();

        mobs = Map.getMobs();

        gravity = 0;
        acceleration = 0;
    }
    
    // UPDATE AND DRAW PLAYER
    public void Update(Map map, Camera cam, Batch batch, int volume, float delta) {
        float oldX = playerPosX;
        float oldY = playerPosY;


        // JUMP
        if (onGround) {
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
                            float blockTop = mapArray[x][y].getPosY();
                            float blockBottom = mapArray[x][y].getPosY() + 25;
                            float dy = playerPosY - oldY;

                            if (dy > 0) {
                                playerPosY = blockTop - playerSizeY;
                            } else {
                                playerPosY = blockBottom;
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
                            float blockTop = mapArray[x][y].getPosX();
                            float blockBottom = mapArray[x][y].getPosX() + 25;
                            float dx = playerPosX - oldX;
                            if (dx > 0) {
                                playerPosX = blockTop - playerSizeX;
                            } else {
                                playerPosX = blockBottom;
                            }
                            //playerPosX = oldX;
                            acceleration = -acceleration * PLAYER_BOUNCINESS * delta;
                        }
                        // SET BLOCK HEALTH TO MAX IF LEFT MOUSE BUTTON IS NOT DOWN
                        if (mapArray[x][y].getElement() != EMPTY && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {

                            mapArray[x][y].setBlockHealth(mapArray[x][y].getMaxhealth());

                        }

                        // CHECK IF MOUSE IS INSIDE CURRENT BLOCK
                        if (!isInventoryOpen && mouseInWorld2D.y > mapArray[x][y].getPosY()
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
                                                float damage = inventory.get(selectedSlot).getDamage() * delta;
                                                //damage modifiers for pickaxes
                                                if ((inventory.get(selectedSlot)).getElement() == WOODPICKAXE || (inventory.get(selectedSlot)).getElement() == STONEPICKAXE ||
                                                 (inventory.get(selectedSlot)).getElement() == IRONPICKAXE || (inventory.get(selectedSlot)).getElement() == DIAMONDPICKAXE ) {
                                                    if (mapArray[x][y].getElement() == STONE || mapArray[x][y].getElement() == IRON || mapArray[x][y].getElement() == COAL || mapArray[x][y].getElement() == DIAMOND) {
                                                        damage *= 2;
                                                    }
                                                }
                                                //damage modifiers for axes
                                                if ((inventory.get(selectedSlot)).getElement() == WOODAXE || (inventory.get(selectedSlot)).getElement() == STONEAXE ||
                                                 (inventory.get(selectedSlot)).getElement() == IRONAXE || (inventory.get(selectedSlot)).getElement() == DIAMONDAXE ) {
                                                    if (mapArray[x][y].getElement() == WOOD || mapArray[x][y].getElement() == PLANKS) {
                                                        damage *= 3;
                                                    }
                                                }

                                                //block damage calculated based on block health and weapon damage
                                                if (mapArray[x][y].getMaxhealth() >= 0 && mapArray[x][y].getMaxhealth() < 101) {
                                                    mapArray[x][y].setBlockHealth(mapArray[x][y].getBlockHealth() - damage);
                                                } else if (mapArray[x][y].getMaxhealth() > 199 && mapArray[x][y].getMaxhealth() < 220 && inventory.get(selectedSlot).getDamage() > 4) {
                                                    mapArray[x][y].setBlockHealth(mapArray[x][y].getBlockHealth() - damage);
                                                } else if (mapArray[x][y].getMaxhealth() > 221 && mapArray[x][y].getMaxhealth() < 251 && inventory.get(selectedSlot).getDamage() > 24) {
                                                    mapArray[x][y].setBlockHealth(mapArray[x][y].getBlockHealth() - damage);
                                                } else if (mapArray[x][y].getMaxhealth() > 252 && inventory.get(selectedSlot).getDamage() > 49) {
                                                    mapArray[x][y].setBlockHealth(mapArray[x][y].getBlockHealth() - damage);
                                                } else {
                                                    mapArray[x][y].setBlockHealth(mapArray[x][y].getBlockHealth() - 0);
                                                }

                                                soundTimer += 1 * delta;
                                            } else {
                                                // hitting blocks damages weapon and ultimately breaks them
                                                if (inventory.get(selectedSlot).isWeapon()){
                                                    inventory.get(selectedSlot).setHealth(inventory.get(selectedSlot).getHealth()-1);
                                                    if(inventory.get(selectedSlot).getHealth() == 0) {
                                                        inventory.get(selectedSlot).removeItem();
                                                        weaponBreakingSound.play(volume / 200f);
                                                    }
                                                }
                                                if (mapArray[x][y-1].getElement() == REDFLOWER || mapArray[x][y-1].getElement() == TALLGRASS){
                                                    Random rand = new Random();
                                                    InventorySlot newItem = new InventorySlot();
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
                                                InventorySlot newItem = new InventorySlot();
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

                                        // IF RIGHT MOUSE BUTTON IS DOWN, PLACE BLOCK IN HAND
                                        if  (inventory.get(selectedSlot).isPlaceable()
                                                && mapArray[x][y].getElement() == EMPTY
                                                && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                                            if (!(playerPosX + playerSizeX > mapArray[x][y].getPosX()
                                                    && playerPosX < mapArray[x][y].getPosX()
                                                            + mapArray[x][y].getBLOCKSIZE()
                                                    && playerPosY + playerSizeY > mapArray[x][y].getPosY()
                                                    && playerPosY < mapArray[x][y].getPosY()
                                                            + mapArray[x][y].getBLOCKSIZE())
                                                    || inventory.get(selectedSlot).getElement() == LADDER
                                                    || inventory.get(selectedSlot).getElement() == TALLGRASS
                                                    || inventory.get(selectedSlot).getElement() == REDFLOWER) {
                                                if (inventory.get(selectedSlot).getAmount() > 0) {
   
                                                    // Placing a door block makes a 2 block tall door and prevent placing a door in 1 block space
                                                    if (inventory.get(selectedSlot).getElement() == DOOR && mapArray[x][y-1].getElement() == EMPTY){
                                                        mapArray[x][y].setElement(DOOR1);
                                                        mapArray[x][y - 1].setElement(DOOR2);

                                                        inventory.get(selectedSlot).removeItem();

                                                    }else if (inventory.get(selectedSlot).getElement() != DOOR) {
                                                        mapArray[x][y].setElement(inventory.get(selectedSlot).getElement());

                                                        // prevent collision on certain blocks
                                                        switch (inventory.get(selectedSlot).getElement()) {
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

                                                        inventory.get(selectedSlot).removeItem();
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
            if (!isInventoryOpen &&
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
                        if (thisMob.getMobHealth() - inventory.get(selectedSlot).getDamage() <= 0) {
                            
                            Random rand = new Random();
                            InventorySlot newItem = new InventorySlot();
                            newItem.setElement(thisMob.getElement());
                            newItem.setAmount(1);
                            newItem.setX(thisMob.getMobPosX()+6);
                            newItem.setY(thisMob.getMobPosY()+6);
                            newItem.setResource(true);
                            newItem.setAcceleration(rand.nextFloat() * 2 - 1);
                            droppedItems.add(newItem);

                            mobs.remove(i);
                        } else {
                            thisMob.setMobHealth(thisMob.getMobHealth() - inventory.get(selectedSlot).getDamage());
                        }
                    }
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
            InventorySlot item = droppedItems.get(a);
            

            item.Update(mapArray, playerPosX, playerPosY, delta);
            if (item.getX() + 12 >= playerPosX 
            && item.getX() <= playerPosX + playerSizeX
            && item.getY() + 12 >= playerPosY 
            && item.getY() <= playerPosY + playerSizeY ) {

                for (int i = 0; i < 36; i++){
                    int slotIndex = -1;
                    for (int o = 0; o < 36; o++) {
                        if (item.getElement() == inventory.get(o).getElement()
                                && item.getAmount() + inventory.get(o).getAmount() < INVENTORY_SLOT_MAX) {
                            slotIndex = o;
                            break;
                        }
                    }
                    if (slotIndex > 0) {
                        inventory.get(slotIndex).setAmount(inventory.get(slotIndex).getAmount() + item.getAmount());
                        break;
                    } else {
                        if (!item.isWeapon() && inventory.get(i).getElement() == item.getElement()
                                && inventory.get(i).getAmount() + item.getAmount() < INVENTORY_SLOT_MAX) {
                            inventory.get(i).setAmount(inventory.get(i).getAmount() + item.getAmount());
                            inventory.get(i).setWeapon(item.isWeapon());
                            inventory.get(i).setFood(item.isFood());
                            inventory.get(i).setResource(item.isResource());
                            inventory.get(i).setDamage(item.getDamage());
                            inventory.get(i).setHealth(item.getHealth());
                            break;
                        }else if (inventory.get(i).getAmount() == 0) {
                            inventory.get(i).setAmount(item.getAmount());
                            inventory.get(i).setElement(item.getElement());
                            inventory.get(i).setWeapon(item.isWeapon());
                            inventory.get(i).setFood(item.isFood());
                            inventory.get(i).setResource(item.isResource());
                            inventory.get(i).setDamage(item.getDamage());
                            inventory.get(i).setHealth(item.getHealth());
                            break;
                        } 
                    }
                    
                }
                droppedItems.remove(item);

            
            }

            
            batch.draw(blockTextures[0][item.getElement()-1], item.getX(),item.getY()+item.getShakeTimer(),12,12);
        }


        // DRAW PLAYER
        batch.draw(playerTexture, cam.position.x+acceleration,cam.position.y-gravity);
        cam.position.set( Math.round(playerPosX-acceleration),  Math.round(playerPosY+gravity), 0);
        cam.update();
    }

    public void DrawHud(Batch batch, HudCamera cam) {

        // change selected slot
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            selectedSlot = 0;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            selectedSlot = 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            selectedSlot = 2;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            selectedSlot = 3;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            selectedSlot = 4;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            selectedSlot = 5;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
            selectedSlot = 6;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
            selectedSlot = 7;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
            selectedSlot = 8;
        }

        // show/hide inventory
        if (!isInventoryOpen) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.TAB) || Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                isInventoryOpen = true;
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.TAB) || Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            isInventoryOpen = false;
            for (int j = 36; j < 45; j++){
                for (int i = 0; i < 36; i++){
                    int slotIndex = -1;
                    for (int o = 0; o < 36; o++) {
                        if (inventory.get(j).getElement() == inventory.get(o).getElement()
                                && inventory.get(j).getAmount() + inventory.get(o).getAmount() < INVENTORY_SLOT_MAX) {
                            slotIndex = o;
                            break;
                        }
                    }
                    if (slotIndex > 0) {
                        inventory.get(slotIndex).setAmount(inventory.get(slotIndex).getAmount() + inventory.get(j).getAmount());
                        break;
                    } else {
                        if (!inventory.get(j).isWeapon() && inventory.get(i).getElement() == inventory.get(j).getElement()
                                && inventory.get(i).getAmount() + inventory.get(j).getAmount() < INVENTORY_SLOT_MAX) {
                            inventory.get(i).setAmount(inventory.get(i).getAmount() + inventory.get(j).getAmount());
                            inventory.get(i).setWeapon(inventory.get(j).isWeapon());
                            inventory.get(i).setFood(inventory.get(j).isFood());
                            inventory.get(i).setResource(inventory.get(j).isResource());
                            inventory.get(i).setDamage(inventory.get(j).getDamage());
                            inventory.get(i).setHealth(inventory.get(j).getHealth());
                            break;
                        }else if (inventory.get(i).getAmount() == 0) {
                            inventory.get(i).setAmount(inventory.get(j).getAmount());
                            inventory.get(i).setElement(inventory.get(j).getElement());
                            inventory.get(i).setWeapon(inventory.get(j).isWeapon());
                            inventory.get(i).setFood(inventory.get(j).isFood());
                            inventory.get(i).setResource(inventory.get(j).isResource());
                            inventory.get(i).setDamage(inventory.get(j).getDamage());
                            inventory.get(i).setHealth(inventory.get(j).getHealth());
                            break;
                        } 
                    }
                  
                }
                inventory.get(j).setAmount(0);
            }

            
        }

        // DRAW PLAYER HEALTH
        for (int i = 0; i < playerHealth; i++) {
            batch.draw(healthTexture, 32 + 20 * i, 10);
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && isGrabbed) {
            isGrabbed = false;
        }


        // hotbar
        batch.draw(hotbarTexture, 800 - (261 / 2), 0);

        for (int i = 0; i < 9; i++) {
            CharSequence str = Integer.toString(inventory.get(i).getAmount());
            if (inventory.get(i).getAmount() > 0 && grab != i) {
                batch.draw(blockTextures[0][inventory.get(i).getElement() - 1], 802 - (261 / 2) + (29 * i), 2);
                if (!inventory.get(i).isWeapon()){
                    font.draw(batch, str, 802 - (261 / 2) + (29 * i), 15);
                }
                

                if (cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * i)
                        && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * i)) + 28
                        && cam.getInputInGameWorld().y >= 0 && cam.getInputInGameWorld().y < 28
                        && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && grab == -1) {
                    grab = i;
                    isGrabbed = true;
                }
            }
            if (!isGrabbed) {

                if (cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * i)
                        && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * i)) + 28
                        && cam.getInputInGameWorld().y >= 0 && cam.getInputInGameWorld().y < 28 && grab > -1
                        && grab != i) {
                    if (!inventory.get(grab).isWeapon() && inventory.get(grab).getElement() == inventory.get(i).getElement()
                            && inventory.get(i).getAmount() + inventory.get(grab).getAmount() < INVENTORY_SLOT_MAX) {
                        inventory.get(i).setAmount(inventory.get(i).getAmount() + inventory.get(grab).getAmount());
                        inventory.get(grab).setAmount(0);
                        inventory.get(grab).setElement(0);
                        grab = -1;
                    } else {
                        InventorySlot reserveSlot = inventory.get(grab);
                        InventorySlot reserveSlot2 = inventory.get(i);
                        inventory.set(i, reserveSlot);
                        inventory.set(grab, reserveSlot2);
                        grab = -1;
                    }
                }else if (cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * i)
                && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * i)) + 28
                && cam.getInputInGameWorld().y >= 0 && cam.getInputInGameWorld().y < 28 && grab > -1
                && grab == i){
                    grab = -1;
                }
            }
            if (i == selectedSlot) {
                batch.draw(outlineTexture, 802 - (261 / 2) + (29 * i), 2);
            }
        }
        // inventory
        if (isInventoryOpen) {
            batch.draw(inventoryTexture, 800 - (261 / 2), 250 - (88 / 2));

            int invDrawRow = 0;
            int invDrawColumn = 0;
            for (int i = 9; i < 36; i++) {
                CharSequence str = Integer.toString(inventory.get(i).getAmount());
                if (inventory.get(i).getAmount() > 0 && grab != i) {
                    batch.draw(blockTextures[0][inventory.get(i).getElement() - 1], 802 - (261 / 2) + (29 * invDrawRow),250 - (88 / 2) + 2 + (invDrawColumn * 29));
                    if (!inventory.get(i).isWeapon()){
                        font.draw(batch, str, 802 - (261 / 2) + (29 * invDrawRow),250 - (88 / 2) + 15 + (invDrawColumn * 29));
                    }

                    if (cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * invDrawRow)
                            && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * invDrawRow)) + 28
                            && cam.getInputInGameWorld().y >= 254 - (90 / 2) + (invDrawColumn * 29)
                            && cam.getInputInGameWorld().y <= 254 - (90 / 2) + 28 + (invDrawColumn * 29)
                            && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && grab == -1) {
                        grab = i;
                        isGrabbed = true;
                    }
                }
                if (!isGrabbed) {

                    if (cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * invDrawRow)
                    && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * invDrawRow)) + 28
                    && cam.getInputInGameWorld().y >= 254 - (90 / 2) + (invDrawColumn * 29)
                    && cam.getInputInGameWorld().y <= 254 - (90 / 2) + 29 + (invDrawColumn * 29) && grab > -1
                            && grab != i) {

                        if (!inventory.get(grab).isWeapon() && inventory.get(grab).getElement() == inventory.get(i).getElement()
                                && inventory.get(i).getAmount()
                                        + inventory.get(grab).getAmount() < INVENTORY_SLOT_MAX) {
                            inventory.get(i).setAmount(inventory.get(i).getAmount() + inventory.get(grab).getAmount());
                            inventory.get(grab).setAmount(0);
                            inventory.get(grab).setElement(0);
                            grab = -1;
                        } else {
                            InventorySlot reserveSlot = inventory.get(grab);
                            InventorySlot reserveSlot2 = inventory.get(i);
                            inventory.set(i, reserveSlot);
                            inventory.set(grab, reserveSlot2);
                            grab = -1;
                        }
                    }else if (cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * invDrawRow)
                    && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * invDrawRow)) + 28
                    && cam.getInputInGameWorld().y >= 254 - (90 / 2) + (invDrawColumn * 29)
                    && cam.getInputInGameWorld().y <= 254 - (90 / 2) + 29 + (invDrawColumn * 29) && grab > -1
                        && grab == i){
                            grab = -1;
                        }
                }
                invDrawRow++;
                if (invDrawRow % 9 == 0) {
                    invDrawColumn++;
                    invDrawRow = 0;
                }
            }

            // crafting slots
            invDrawRow = 0;
            invDrawColumn = 0;
            for (int i = 36; i < 45; i++) {
                CharSequence str = Integer.toString(inventory.get(i).getAmount());
                if (inventory.get(i).getAmount() > 0 && grab != i) {
                    batch.draw(blockTextures[0][inventory.get(i).getElement() - 1], 860 - (261 / 2) + (29 * invDrawRow),
                            363 - (88 / 2) + 2 + (invDrawColumn * 29));
                    if (!inventory.get(i).isWeapon()){
                        font.draw(batch, str, 860 - (261 / 2) + (29 * invDrawRow),363 - (88 / 2) + 15 + (invDrawColumn * 29));
                    }

                    if (cam.getInputInGameWorld().x >= 858 - (261 / 2) + (29 * invDrawRow)
                        && cam.getInputInGameWorld().x <= (858 - (261 / 2) + (29 * invDrawRow)) + 28
                        && cam.getInputInGameWorld().y >= 365 - (90 / 2) + (invDrawColumn * 29)
                        && cam.getInputInGameWorld().y <= 365 - (90 / 2) + 29 + (invDrawColumn * 29)
                            && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && grab == -1) {
                        grab = i;
                        isGrabbed = true;
                    }
                }
                if (!isGrabbed) {

                    if (cam.getInputInGameWorld().x >= 858 - (261 / 2) + (29 * invDrawRow)
                        && cam.getInputInGameWorld().x <= (858 - (261 / 2) + (29 * invDrawRow)) + 28
                        && cam.getInputInGameWorld().y >= 365 - (90 / 2) + (invDrawColumn * 29)
                        && cam.getInputInGameWorld().y <= 365 - (90 / 2) + 29 + (invDrawColumn * 29) && grab > -1
                        && grab != i) {

                        if (!inventory.get(grab).isWeapon() && inventory.get(grab).getElement() == inventory.get(i).getElement()
                                && inventory.get(i).getAmount()
                                        + inventory.get(grab).getAmount() < INVENTORY_SLOT_MAX) {
                            inventory.get(i).setAmount(inventory.get(i).getAmount() + inventory.get(grab).getAmount());
                            inventory.get(grab).setAmount(0);
                            inventory.get(grab).setElement(0);
                            grab = -1;
                        } else {
                            InventorySlot reserveSlot = inventory.get(grab);
                            InventorySlot reserveSlot2 = inventory.get(i);
                            inventory.set(i, reserveSlot);
                            inventory.set(grab, reserveSlot2);
                            grab = -1;
                        }
                    }else if (cam.getInputInGameWorld().x >= 858 - (261 / 2) + (29 * invDrawRow)
                    && cam.getInputInGameWorld().x <= (858 - (261 / 2) + (29 * invDrawRow)) + 28
                    && cam.getInputInGameWorld().y >= 365 - (90 / 2) + (invDrawColumn * 29)
                    && cam.getInputInGameWorld().y <= 365 - (90 / 2) + 29 + (invDrawColumn * 29) && grab > -1
                    && grab == i){
                        grab = -1;
                    }
                }
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    if (cam.getInputInGameWorld().x >= 858 - (261 / 2) + (29 * invDrawRow)
                            && cam.getInputInGameWorld().x <= (858 - (261 / 2) + (29 * invDrawRow)) + 28
                            && cam.getInputInGameWorld().y >= 365 - (90 / 2) + (invDrawColumn * 29)
                            && cam.getInputInGameWorld().y <= 365 - (90 / 2) + 29 + (invDrawColumn * 29) && grab > -1
                            && grab != i && !usedSlots.contains(i)) {
                        int putAmount = 1;
                        if (!inventory.get(grab).isWeapon() && inventory.get(grab).getElement() == inventory.get(i).getElement()
                                && inventory.get(i).getAmount() + putAmount < INVENTORY_SLOT_MAX) {
                            inventory.get(i).setAmount(inventory.get(i).getAmount() + putAmount);
                            inventory.get(grab).removeItem();
                            usedSlots.add(i);
                            if (inventory.get(grab).getAmount() <= 0) {
                                inventory.get(grab).setElement(0);
                                grab = -1;
                            }
                        } else if (inventory.get(i).getAmount() == 0
                                && inventory.get(i).getAmount() + putAmount < INVENTORY_SLOT_MAX) {
                            inventory.get(i).setElement(inventory.get(grab).getElement());
                            inventory.get(i).setDamage(inventory.get(grab).getDamage());
                            inventory.get(i).setHealth(inventory.get(grab).getHealth());
                            inventory.get(i).setResource(inventory.get(grab).isResource());
                            inventory.get(i).setFood(inventory.get(grab).isFood());
                            inventory.get(i).setWeapon(inventory.get(grab).isWeapon());
                            inventory.get(i).setAmount(inventory.get(i).getAmount() + putAmount);
                            inventory.get(grab).removeItem();
                            usedSlots.add(i);
                            if (inventory.get(grab).getAmount() <= 0) {
                                inventory.get(grab).setElement(0);
                                grab = -1;
                            }
                        }
                    }
                }else{
                    usedSlots.clear();
                }
                invDrawRow++;
                if (invDrawRow % 3 == 0) {
                    invDrawColumn++;
                    invDrawRow = 0;
                }
            }

            // crafting
            InventorySlot newItem = Crafting.craft(inventory.subList(36, 45));
            if (newItem != null) {
                CharSequence str = Integer.toString(newItem.getAmount());
                batch.draw(blockTextures[0][newItem.getElement() - 1], 976 - (261 / 2), 392 - (88 / 2) + 2);
                if (!newItem.isWeapon()){
                    font.draw(batch, str, 976 - (261 / 2), 392 - (88 / 2) + 15);
                }
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && cam.getInputInGameWorld().x > 976 - (261 / 2)
                        && cam.getInputInGameWorld().x < (976 - (261 / 2) + 25)
                        && cam.getInputInGameWorld().y > 392 - (88 / 2)
                        && cam.getInputInGameWorld().y < 392 - (88 / 2) + 27) {
                    int slotIndex = -1;
                    boolean craftingSuccess = false;
                    if (!newItem.isWeapon()){
                        for (int i = 0; i < 36; i++) {
                            if (inventory.get(i).getElement() == newItem.getElement()
                                    && inventory.get(i).getAmount() + newItem.getAmount() < INVENTORY_SLOT_MAX) {
                                slotIndex = i;
                                break;
                            }
                        }
                    }

                    if (slotIndex > 0) {
                        inventory.get(slotIndex).setAmount(inventory.get(slotIndex).getAmount() + newItem.getAmount());
                        craftingSuccess = true;
                    } else {
                        for (int i = 0; i < 36; i++) {
                            if (inventory.get(i).getAmount() == 0) {
                                inventory.get(i).setAmount(newItem.getAmount());
                                inventory.get(i).setElement(newItem.getElement());
                                inventory.get(i).setWeapon(newItem.isWeapon());
                                inventory.get(i).setFood(newItem.isFood());
                                inventory.get(i).setResource(newItem.isResource());
                                inventory.get(i).setDamage(newItem.getDamage());
                                inventory.get(i).setHealth(newItem.getHealth());
                                craftingSuccess = true;
                                break;
                            } else if (!newItem.isWeapon() && inventory.get(i).getElement() == newItem.getElement()
                                    && inventory.get(i).getAmount() + newItem.getAmount() < INVENTORY_SLOT_MAX) {
                                inventory.get(i).setAmount(inventory.get(i).getAmount() + newItem.getAmount());
                                inventory.get(i).setWeapon(newItem.isWeapon());
                                inventory.get(i).setFood(newItem.isFood());
                                inventory.get(i).setResource(newItem.isResource());
                                inventory.get(i).setDamage(newItem.getDamage());
                                inventory.get(i).setHealth(newItem.getHealth());
                                craftingSuccess = true;
                                break;
                            }
                        }
                    }
                    if (craftingSuccess) {
                        for (int i = 36; i < 45; i++) {
                            inventory.get(i).setAmount(inventory.get(i).getAmount() - newItem.getRemoveAmount());
                            if (inventory.get(i).getAmount() < 0) {
                                inventory.get(i).setAmount(0);
                                inventory.get(i).setElement(0);
                            }
                        }
                    }
                }
            }

        }

        // draw grabbed item
        if (grab > -1 && inventory.get(grab).getAmount() > 0) {
            CharSequence str = Integer.toString(inventory.get(grab).getAmount());
            batch.draw(blockTextures[0][inventory.get(grab).getElement() - 1], cam.getInputInGameWorld().x - 12,
                    cam.getInputInGameWorld().y - 12);
            if (!inventory.get(grab).isWeapon()){
                font.draw(batch, str, cam.getInputInGameWorld().x - 12, cam.getInputInGameWorld().y + 1);
            }
            
        }
        if(!isGrabbed && grab > -1){

            InventorySlot item = new InventorySlot();
            item.setX(playerPosX);
            item.setY(playerPosY+playerSizeY+15);
            if (cam.getInputInGameWorld().x > 800){
                item.setAcceleration(7);
            }else{
                item.setAcceleration(-7);
            }
            item.setAmount(inventory.get(grab).getAmount());
            item.setElement(inventory.get(grab).getElement());
            item.setDamage(inventory.get(grab).getDamage());
            item.setFood(inventory.get(grab).isFood());
            item.setResource(inventory.get(grab).isResource());
            item.setWeapon(inventory.get(grab).isWeapon());
            item.setHealth(inventory.get(grab).getHealth());
            droppedItems.add(item);
            inventory.get(grab).setAmount(0);
            grab = -1;
        }else if (!isGrabbed) {
            grab = -1;
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

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public ArrayList<InventorySlot> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<InventorySlot> inventory) {
        this.inventory = inventory;
    }

    public void resetInventory() {
        inventory.clear();
        droppedItems.clear();
        for (int i = 0; i < 46; i++) {
            inventory.add(new InventorySlot());
        }
    }

}
