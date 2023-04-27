package com.mygdx.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.CustomInputProcessor;
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

    private Texture playerArm;
    private Sprite arm;
    private float armAngle = 0f;

    private Texture playerTexture;
    private TextureRegion[][] playerAnimation;
    private Texture outlineTexture;
    private Texture healthTexture;
    private Texture hungerTexture;
    private Texture oxygenTexture;
    private Texture textures;
    private Texture tooltipTexture;
    private Texture tutorialTexture;
    private Texture tutorialScreenTexture;
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
    private Sound eatingSound;

    private int playerHealth;
    private int playerHunger;
    private float walkAnimationTimer;

    private long lastHitTime;
    private long hungerTime;
    private long lastHungerHit;
    private float hudMessageTimer;
    private float healthRegenerateTimer;
    private int playerOxygen;
    private long lastDrowningHit;
    private long drowningTime;
    private boolean diving;

    private BitmapFont font;
    private BitmapFont redFont;
    private String elementString = "";
    private String tutorialString = " A: to move left,\n D: to move right,\n W or Space: to jump / go up ladder,\n S: to go down ladder,\n E / Tab: to open Inventory,\n Esc: to Pause / open Menu,\n Q: to drop item,\n Shift: to Sprint,\n Right Click: to place Block,\n 1 - 10: to change selected Hotbar slot,\n Hold Ctrl when placing a block: to disable Block collision";
    private boolean tutorialOn = false;
    private HudMessage message;

    private ArrayList<Mob> mobs;

    private ArrayList<Rocket> rockets;
    private Texture rocketTexture;
    private Texture rocketFlameTexture;
    private TextureRegion[][] rocketFlame;
    private int rocketNumber = -1;

    private Inventory inventory;

    private ArrayList<Item> droppedItems;
    private Recipebook recipeBook;

    private float cactusTimer;

    private boolean isGamePaused = false;

    private float brightness = 1f;

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
        eatingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/eatingSound.mp3"));

        playerSizeX = 23;
        playerSizeY = 49;

        onGround = false;
        onGroundTimer = 0;

        soundTimer = 0;
        lastHitTime = 0;
        hungerTime = 0;
        lastHungerHit = 0;
        hudMessageTimer = 0;
        healthRegenerateTimer = 0;
        lastDrowningHit = 0;
        drowningTime = 0;
        diving = false;

        cactusTimer = 0;

        playerArm = new Texture("playerarm.png");
        arm = new Sprite(playerArm);
        arm.setOrigin(2.5f, 17f);

        playerTexture = new Texture("jusju.png");
        playerAnimation = TextureRegion.split(playerTexture, 25, 50);
        outlineTexture = new Texture("outline.png");
        healthTexture = new Texture("heart.png");
        hungerTexture = new Texture("hunger.png");
        blockBreakingTexture = new Texture("breaktiles.png");
        oxygenTexture = new Texture("bubble.png");
        tooltipTexture = new Texture("tooltipbg.png");
        tutorialTexture = new Texture("tutorialbg.png");
        tutorialScreenTexture = new Texture("tutorialscreenbg.png");

        textures = new Texture("tileset.png");
        blockTextures = TextureRegion.split(textures, 25, 25);

        blockBreakingAnimation = TextureRegion.split(blockBreakingTexture, 25, 25);

        rocketTexture = new Texture("rocket.png");
        rocketFlameTexture = new Texture("rocketflame.png");
        rocketFlame = TextureRegion.split(rocketFlameTexture,20,40);

        inventory = new Inventory();
        droppedItems = new ArrayList<>();

        font = new BitmapFont(Gdx.files.internal("fonts/Cambria18.fnt"));
        redFont = new BitmapFont(Gdx.files.internal("fonts/RedCambria.fnt"));
        message = new HudMessage("");

        mobs = Map.getMobs();

        recipeBook = new Recipebook();

        gravity = 0;
        acceleration = 0;

        rockets = new ArrayList<>();
    }

    // UPDATE AND DRAW PLAYER
    public void Update(Map map, Camera cam, Batch batch, int volume, float delta, int mapSizeX, int mapSizeY) {
        float oldX = playerPosX;
        float oldY = playerPosY;
        elementString = "";

        // JUMP
        if (!isGamePaused && onGround && !inventory.isFurnaceOpen()) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                gravity = -4.3f;
                if(!onLadder){
                    acceleration *= 1.4f;
                }
                onGroundTimer = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) && onLadder) {

                
                gravity = +4.3f;
                onGroundTimer = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                gravity = -5.3f;
                if(!onLadder){
                    acceleration *= 1.4f;
                }
                onGroundTimer = 0;
            }
        }
        if (!onLadder) {
            gravity += 0.25 * delta;
        }

        if (cactusTimer > 400) {
            playerHealth--;
            damageSound.play(volume / 400f);
            cactusTimer = 0;
        }

        // APPLY GRAVITY TO PLAYER
        playerPosY -= gravity * delta;
        int startBlockX = (int) (playerPosX / 25 - 1600 / 25 / 2) + (mapSizeX / 2);
        int endBlockX = (startBlockX + 1600 / 25);

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
                        if (!mapArray[x][y].isCollision()) {
                            if (mapArray[x][y].getBrightness() > brightness) {
                                brightness += 0.01f * delta;
                            } else {
                                brightness -= 0.01f * delta;
                            }

                        }

                        if (mapArray[x][y].getElement() == CACTUS) {
                            cactusTimer += 1 * delta;
                        }
                        if (mapArray[x][y].isCollision()) {

                            if (gravity > 13) {
                                // Gdx.app.exit();
                                playerHealth -= 5;
                            } else if (gravity >= 12) {
                                playerHealth -= 4;
                                damageSound.play(volume / 200f);
                            } else if (gravity >= 11) {
                                playerHealth -= 3;
                                damageSound.play(volume / 200f);
                            } else if (gravity >= 10) {
                                playerHealth -= 2;
                                damageSound.play(volume / 200f);
                            } else if (gravity >= 9) {
                                playerHealth -= 1;
                                damageSound.play(volume / 200f);
                            }

                            if (gravity > 0) {
                                onGround = true;
                                onLadder = false;
                                onGroundTimer = 5;
                            }
                            float blockTop = mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE();
                            float blockBottom = (mapArray[x][y].getPosY());
                            float dy = playerPosY - oldY;

                            if (dy > 0) {
                                playerPosY = blockBottom - playerSizeY;
                            } else {
                                playerPosY = blockTop;
                            }

                            // playerPosY = oldY;
                            gravity = -gravity * PLAYER_BOUNCINESS * delta;
                        } else if (mapArray[x][y].getElement() == LADDER) {
                            gravity *= Math.pow(0.8f, delta);
                            onGround = true;
                            onLadder = true;
                            onGroundTimer = 5;
                        } else if (mapArray[x][y].getElement() == WATER1 || mapArray[x][y].getElement() == WATER2
                                || mapArray[x][y].getElement() == WATER3 || mapArray[x][y].getElement() == WATER4
                                || mapArray[x][y].getElement() == WATER5) {
                            gravity *= Math.pow(0.9f, delta);
                            onGround = true;
                            onLadder = true;
                            onGroundTimer = 5;
                        }
                    }

                    // CHECK IF PLAYER IS INSIDE WATER
                    float blockTop = mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE();
                    float blockBottom = (mapArray[x][y].getPosY());
                    if ((playerPosY + 40) > blockBottom && (playerPosY + 40) < blockTop) {
                        if (mapArray[x][y].getElement() == WATER1 || mapArray[x][y].getElement() == WATER2
                                || mapArray[x][y].getElement() == WATER3 || mapArray[x][y].getElement() == WATER4
                                || mapArray[x][y].getElement() == WATER5) {
                            diving = true;
                        } else {
                            diving = false;
                        }
                    }
                }
            }

        }

        if (diving) {

            // DEPLETE OXYGEN
            if (getPlayerOxygen() > 0 && !isGamePaused) {
                long currentTime = new Date().getTime();
                if (currentTime - drowningTime >= 3000) {
                    setPlayerOxygen(getPlayerOxygen() - 1);
                    drowningTime = currentTime;
                }
            }

            // LOSE HEALTH WHEN OXYGEN IS 0
            if (getPlayerOxygen() == 0 && !isGamePaused) {
                long currentTime = new Date().getTime();
                if (currentTime - lastDrowningHit >= 2000) {
                    setPlayerHealth(getPlayerHealth() - 1);
                    lastDrowningHit = currentTime;
                }
            }
        } else {
            // RESTORE OXYGEN
            setPlayerOxygen(10);
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
        if (!isGamePaused) {
            if (!inventory.isFurnaceOpen()) {
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && acceleration < 4) {
                        isRunning = true;
                        acceleration -= 1.5 * delta;
                    } else if (acceleration < 3) {
                        acceleration -= 1.1f * delta;
                    }
                }

                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && acceleration > -4) {
                        isRunning = true;
                        acceleration += 1.5 * delta;
                    } else if (acceleration > -3) {
                        acceleration += 1.1f * delta;
                    }
                }
            }
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
                            float dx = playerPosX - oldX;

                            if (dx > 0) {
                                playerPosX = blockLeft - playerSizeX;
                            } else {
                                playerPosX = blockRight;
                            }
                            // playerPosX = oldX;
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

                                float distance = (float) Math
                                        .sqrt((mouseInWorld2D.y - playerPosY) * (mouseInWorld2D.y - playerPosY)
                                                + (mouseInWorld2D.x - playerPosX)
                                                        * (mouseInWorld2D.x - playerPosX));

                                // IF DISTANCE IS < 150 PIXELS DRAW BLOCK OUTLINE
                                if (distance <= 150) {
                                    batch.setColor(1, 1, 1, 0.5f);
                                    boolean drawOutline = false;
                                    // Right click to open furnace menu
                                    if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && !isGamePaused
                                            && (mapArray[x][y].getElement() == FURNACE
                                                    || mapArray[x][y].getElement() == FURNACE2)
                                            && !inventory.isInventoryOpen()) {
                                        inventory.setInventoryOpen(true);
                                        inventory.setFurnaceOpen(true);
                                        inventory.setOpenFurnaceX(x);
                                        inventory.setOpenFurnaceY(y);

                                    }
                                    if (inventory.getSelectedItem().getElement() == BUCKET
                                            && mapArray[x][y].getElement() == OIL) {
                                        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                                            inventory.getSelectedItem().setElement(OILBUCKET);
                                            mapArray[x][y].setElement(EMPTY);
                                            mapArray[x][y].setCollision(false);
                                        }
                                        drawOutline = true;
                                    }

                                    // IF LEFT MOUSE BUTTON IS DOWN REDUCE BLOCK HEALTH OR DESTROY IT (CHANGE TO
                                    // EMPTY AND COLLISION OFF)
                                    if ((!mapArray[x - 1][y].isCollision()
                                            || !mapArray[x + 1][y].isCollision()
                                            || !mapArray[x][y - 1].isCollision()
                                            || !mapArray[x][y + 1].isCollision())
                                            && mapArray[x][y].getElement() != EMPTY
                                            && mapArray[x][y].getElement() != WATER1
                                            && mapArray[x][y].getElement() != WATER2
                                            && mapArray[x][y].getElement() != WATER3
                                            && mapArray[x][y].getElement() != WATER4
                                            && mapArray[x][y].getElement() != WATER5
                                            && mapArray[x][y].getElement() != OIL) {
                                        drawOutline = true;
                                        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                                            if (mapArray[x][y].getBlockHealth() > 0) {

                                                if (mapArray[x][y].getBlockHealth() * 100
                                                        / mapArray[x][y].getMaxhealth() < 99) {

                                                    if (mapArray[x][y].getBlockHealth() * 100
                                                            / mapArray[x][y].getMaxhealth() > 75) {
                                                        batch.draw(blockBreakingAnimation[0][0],
                                                                mapArray[x][y].getPosX(),
                                                                mapArray[x][y].getPosY());
                                                    } else if (mapArray[x][y].getBlockHealth() * 100
                                                            / mapArray[x][y].getMaxhealth() > 50) {
                                                        batch.draw(blockBreakingAnimation[0][1],
                                                                mapArray[x][y].getPosX(),
                                                                mapArray[x][y].getPosY());
                                                    } else if (mapArray[x][y].getBlockHealth() * 100
                                                            / mapArray[x][y].getMaxhealth() > 25) {
                                                        batch.draw(blockBreakingAnimation[0][2],
                                                                mapArray[x][y].getPosX(),
                                                                mapArray[x][y].getPosY());
                                                    } else {
                                                        batch.draw(blockBreakingAnimation[0][3],
                                                                mapArray[x][y].getPosX(),
                                                                mapArray[x][y].getPosY());
                                                    }
                                                }

                                                soundEffect = mapArray[x][y].getElement();

                                                // damage to block determined here
                                                float damage = inventory.getSelectedItem().getDamage() * delta;
                                                // damage modifiers for pickaxes
                                                if ((inventory.getSelectedItem()).getElement() == WOODPICKAXE
                                                        || (inventory.getSelectedItem()).getElement() == STONEPICKAXE ||
                                                        (inventory.getSelectedItem()).getElement() == IRONPICKAXE
                                                        || (inventory.getSelectedItem())
                                                                .getElement() == DIAMONDPICKAXE) {
                                                    if (mapArray[x][y].getElement() == STONE
                                                            || mapArray[x][y].getElement() == IRON
                                                            || mapArray[x][y].getElement() == COAL
                                                            || mapArray[x][y].getElement() == DIAMOND
                                                            ||mapArray[x][y].getElement() == COPPER) {
                                                        damage *= 2;
                                                    }
                                                }
                                                // damage modifiers for axes
                                                if ((inventory.getSelectedItem()).getElement() == WOODAXE
                                                        || (inventory.getSelectedItem()).getElement() == STONEAXE ||
                                                        (inventory.getSelectedItem()).getElement() == IRONAXE
                                                        || (inventory.getSelectedItem()).getElement() == DIAMONDAXE) {
                                                    if (mapArray[x][y].getElement() == WOOD
                                                            || mapArray[x][y].getElement() == PLANKS
                                                            || mapArray[x][y].getElement() == LADDER) {
                                                        damage *= 2;
                                                    }
                                                }

                                                // block damage calculated based on block health and weapon damage
                                                if (mapArray[x][y].getMaxhealth() >= 0
                                                        && mapArray[x][y].getMaxhealth() < 101) {
                                                    mapArray[x][y]
                                                            .setBlockHealth(mapArray[x][y].getBlockHealth() - damage);
                                                } else if (mapArray[x][y].getMaxhealth() > 199
                                                        && mapArray[x][y].getMaxhealth() < 220
                                                        && inventory.getSelectedItem().getDamage() > 4) {
                                                    mapArray[x][y]
                                                            .setBlockHealth(mapArray[x][y].getBlockHealth() - damage);
                                                } else if (mapArray[x][y].getMaxhealth() > 221
                                                        && mapArray[x][y].getMaxhealth() < 251
                                                        && inventory.getSelectedItem().getDamage() > 8) {
                                                    mapArray[x][y]
                                                            .setBlockHealth(mapArray[x][y].getBlockHealth() - damage);
                                                } else if (mapArray[x][y].getMaxhealth() > 252
                                                        && inventory.getSelectedItem().getDamage() > 11) {
                                                    mapArray[x][y]
                                                            .setBlockHealth(mapArray[x][y].getBlockHealth() - damage);
                                                } else {
                                                    mapArray[x][y].setBlockHealth(mapArray[x][y].getBlockHealth() - 0);
                                                }

                                                soundTimer += 1 * delta;
                                            } else {
                                                // hitting blocks damages weapon and ultimately breaks them
                                                if (inventory.getSelectedItem().isWeapon()) {
                                                    inventory.getSelectedItem()
                                                            .setHealth(inventory.getSelectedItem().getHealth() - 1);
                                                    if (inventory.getSelectedItem().getHealth() == 0) {
                                                        inventory.getSelectedItem().removeItem();
                                                        weaponBreakingSound.play(volume / 200f);
                                                    }
                                                }
                                                if (mapArray[x][y - 1].getElement() == REDFLOWER
                                                        || mapArray[x][y - 1].getElement() == TALLGRASS) {
                                                    Random rand = new Random();
                                                    Item newItem = new Item();
                                                    newItem.setElement(mapArray[x][y - 1].getElement());
                                                    newItem.setAmount(1);
                                                    newItem.setX(mapArray[x][y - 1].getPosX() + 6);
                                                    newItem.setY(mapArray[x][y - 1].getPosY() + 6);
                                                    newItem.setAcceleration(rand.nextFloat() * 2 - 1);
                                                    droppedItems.add(newItem);
                                                    mapArray[x][y - 1].setElement(EMPTY);
                                                }
                                                if (mapArray[x][y].getElement() == LEAVES
                                                        || mapArray[x][y].getElement() == TALLGRASS
                                                        || mapArray[x][y].getElement() == REDFLOWER) {
                                                    leavesHitSound.play(volume / 200f, 0.5f, 0);
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
                                                if (mapArray[x][y].getElement() == DOOR2) {
                                                    mapArray[x][y].setElement(DOOR);
                                                    mapArray[x][y + 1].setElement(EMPTY);
                                                    mapArray[x][y].setCollision(false);
                                                    mapArray[x][y + 1].setCollision(false);
                                                }
                                                if (mapArray[x][y].getElement() == DOOR1) {
                                                    mapArray[x][y].setElement(DOOR);
                                                    mapArray[x][y - 1].setElement(EMPTY);
                                                    mapArray[x][y].setCollision(false);
                                                    mapArray[x][y - 1].setCollision(false);
                                                }
                                                Random rand = new Random();
                                                Item newItem = new Item();
                                                newItem.setElement(mapArray[x][y].getElement());
                                                newItem.setAmount(1);
                                                newItem.setX(mapArray[x][y].getPosX() + 6);
                                                newItem.setY(mapArray[x][y].getPosY() + 6);
                                                newItem.setAcceleration(rand.nextFloat() * 2 - 1);
                                                if (mapArray[x][y].getElement() == COALITEM
                                                        || mapArray[x][y].getElement() == DIAMONDITEM) {
                                                    newItem.setResource(true);
                                                }
                                                droppedItems.add(newItem);
                                                mapArray[x][y].setElement(EMPTY);
                                                mapArray[x][y].setCollision(false);

                                            }
                                        }
                                    } else {
                                        soundTimer = 15;
                                    }

                                    batch.setColor(1, 1, 1, 1);

                                    elementString = elementNames.get(mapArray[x][y].getElement());

                                    // IF RIGHT MOUSE BUTTON IS DOWN, PLACE BLOCK IN HAND
                                    if (inventory.getSelectedItem().isPlaceable()
                                            && (mapArray[x][y].getElement() == EMPTY
                                                    || mapArray[x][y].getElement() == WATER1
                                                    || mapArray[x][y].getElement() == WATER2
                                                    || mapArray[x][y].getElement() == WATER3
                                                    || mapArray[x][y].getElement() == WATER4
                                                    || mapArray[x][y].getElement() == WATER5)
                                            && (mapArray[x - 1][y].getElement() != EMPTY
                                                    || mapArray[x + 1][y].getElement() != EMPTY
                                                    || mapArray[x][y - 1].getElement() != EMPTY
                                                    || mapArray[x][y + 1].getElement() != EMPTY
                                                    || mapArray[x][y].getPermanent() != EMPTY
                                                    || mapArray[x][y].getElement() > 0)) {
                                        drawOutline = true;
                                        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
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

                                                    // Placing a door block makes a 2 block tall door and prevent
                                                    // placing a door in 1 block space
                                                    if (inventory.getSelectedItem().getElement() == DOOR
                                                            && mapArray[x][y - 1].getElement() == EMPTY) {
                                                        mapArray[x][y].setElement(DOOR1);
                                                        mapArray[x][y - 1].setElement(DOOR2);

                                                        inventory.getSelectedItem().removeItem();

                                                    } else if (inventory.getSelectedItem().getElement() != DOOR) {
                                                        mapArray[x][y]
                                                                .setElement(inventory.getSelectedItem().getElement());
                                                        if (mapArray[x][y].getElement() == FURNACE) {
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

                                    }
                                    if (drawOutline) {
                                        batch.draw(outlineTexture, mapArray[x][y].getPosX(), mapArray[x][y].getPosY());
                                    }
                                    
                                    if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
                                    && !mapArray[x-1][y].isCollision()
                                    && !mapArray[x-2][y].isCollision()
                                    && !mapArray[x+1][y].isCollision()
                                    && !mapArray[x+1][y].isCollision()
                                    && !mapArray[x][y+1].isCollision()
                                    && !mapArray[x][y-1].isCollision()){
                                        if(inventory.getSelectedItem().getElement() == ROCKET){
                                            inventory.getSelectedItem().setAmount(0);
                                            rockets.add(new Rocket(mapArray[x][y].getPosX()-30, mapArray[x][y].getPosY(), rocketTexture, rocketFlame));
                                        }
                                    }


                                    // Right click to open door
                                    if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
                                            && (mapArray[x][y].getElement() == DOOR1
                                                    || mapArray[x][y].getElement() == DOOR2)) {

                                        if (!(playerPosX + playerSizeX > mapArray[x][y].getPosX()
                                                && playerPosX < mapArray[x][y].getPosX()
                                                        + mapArray[x][y].getBLOCKSIZE()
                                                && playerPosY + playerSizeY > mapArray[x][y].getPosY()
                                                && playerPosY < mapArray[x][y].getPosY()
                                                        + mapArray[x][y].getBLOCKSIZE())) {

                                            if (mapArray[x][y].getElement() == DOOR1) {

                                                if (mapArray[x][y].isCollision() == true) {
                                                    mapArray[x][y].setCollision(false);
                                                    mapArray[x][y - 1].setCollision(false);
                                                    doorCloseSound.play(volume / 200f, 1.1f, 0);
                                                } else if (mapArray[x][y].isCollision() == false) {
                                                    mapArray[x][y].setCollision(true);
                                                    mapArray[x][y - 1].setCollision(true);
                                                    doorOpenSound.play(volume / 200f, 1f, 0);
                                                }
                                            }

                                            if (mapArray[x][y].getElement() == DOOR2) {
                                                if (mapArray[x][y].isCollision() == true) {
                                                    mapArray[x][y].setCollision(false);
                                                    mapArray[x][y + 1].setCollision(false);
                                                    doorCloseSound.play(volume / 200f, 1f, 0);
                                                } else if (mapArray[x][y].isCollision() == false) {
                                                    mapArray[x][y].setCollision(true);
                                                    mapArray[x][y + 1].setCollision(true);
                                                    doorOpenSound.play(volume / 200f, 1.1f, 0);
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
        // SHOW / HIDE TUTORIAL
        if (!tutorialOn) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
                tutorialOn = true;
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            tutorialOn = false;
        }

        // check if mouse is inside mob
        for (int i = 0; i < mobs.size(); i++) {
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
                            newItem.setX(thisMob.getMobPosX() + 6);
                            newItem.setY(thisMob.getMobPosY() + 6);
                            newItem.setResource(true);
                            newItem.setAcceleration(rand.nextFloat() * 2 - 1);
                            droppedItems.add(newItem);
                            mobs.remove(i);
                        } else {
                            if (thisMob.getMobPosX() < getX()) {
                                thisMob.setGravity(4f);
                                thisMob.setOnGround(false);
                                thisMob.setHit("right");
                            } else {
                                thisMob.setGravity(4f);
                                thisMob.setOnGround(false);
                                thisMob.setHit("left");
                            }
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
        if (mobs.size() > 0 && !isGamePaused) {
            for (int i = 0; i < mobs.size(); i++) {
                Mob thisMob = mobs.get(i);
                long currentTime = new Date().getTime();
                long timeSinceLastHit = (currentTime - lastHitTime);
                if (thisMob.getType() == "hostile") {
                    if (20 >= (Math.sqrt((thisMob.getMobPosX() - getX()) * (thisMob.getMobPosX() - getX())
                            + (thisMob.getMobPosY() - getY()) * (thisMob.getMobPosY() - getY())))
                            && timeSinceLastHit > 2000) {
                        // Add knockback left and right
                        if (thisMob.getMobPosX() < getX()) {
                            int healthWhenHit = getPlayerHealth();
                            setPlayerHealth(healthWhenHit - 3);
                            damageSound.play(volume / 200f);
                            lastHitTime = currentTime;
                        } else if (thisMob.getMobPosX() > getX()) {
                            int healthWhenHit = getPlayerHealth();
                            setPlayerHealth(healthWhenHit - 3);
                            damageSound.play(volume / 200f);
                            lastHitTime = currentTime;
                        }
                    }
                }
            }
        }

        if (soundTimer >= 20) {
            if (soundEffect == GRASS || soundEffect == GROUND) {
                groundHitSound.play(volume / 200f, 0.8f, 0);
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

        for (int a = 0; a < droppedItems.size(); a++) {
            Item item = droppedItems.get(a);

            item.Update(mapArray, playerPosX, playerPosY, delta, mapSizeX, mapSizeY);
            if (item.getX() + 12 >= playerPosX
                    && item.getX() <= playerPosX + playerSizeX
                    && item.getY() + 12 >= playerPosY
                    && item.getY() <= playerPosY + playerSizeY) {

                if(inventory.addItem(item)){
                    droppedItems.remove(item);
                }
                

            }

            batch.draw(blockTextures[0][item.getElement() - 1], item.getX(), item.getY() + item.getShakeTimer(), 12,
                    12);
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !inventory.isInventoryOpen()) {
            armAngle -= 6 * delta;
            if (armAngle < 0) {
                armAngle = 180;
            }
        } else {
            armAngle = 10;
        }

        // DEPLETE HUNGER
        if (getPlayerHunger() > 0 && !isGamePaused) {
            long currentTime = new Date().getTime();
            if (currentTime - hungerTime >= 32000) {
                setPlayerHunger(playerHunger - 1);
                hungerTime = currentTime;
            }
        }

        // LOSE HEALTH IF HUNGER BAR IS EMPTY
        if (getPlayerHunger() == 0 && !isGamePaused) {
            long currentTime = new Date().getTime();
            if (currentTime - lastHungerHit >= 5000) {
                setPlayerHealth(getPlayerHealth() - 1);
                damageSound.play(volume / 200f);
                lastHungerHit = currentTime;
            }
        }

        // RESTORE HUNGER WITH FOOD
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && inventory.getSelectedItem().isFood() && !inventory.isInventoryOpen()) {
            if (getPlayerHunger() + 5 > 10) {
                setPlayerHunger(10);
            } else {
                setPlayerHunger(getPlayerHunger() + 5);
            }
            eatingSound.play(volume / 100f);
            inventory.getSelectedItem().removeItem();
        }

        // RESTORE HEALTH WHEN HUNGER BAR IS FULL-ish
        if (getPlayerHunger() >= 8 && !isGamePaused) {
            healthRegenerateTimer += 1 * delta;
        }

        if (healthRegenerateTimer > 300) {
            healthRegenerateTimer = 0;
            setPlayerHealth(getPlayerHealth() + 1);
        }

        if (getPlayerHealth() > 10) {
            setPlayerHealth(10);
        }



        //rocket
        for(int i = 0; i < rockets.size(); i++){
            Rocket rocket = rockets.get(i);
            rocket.update(map, batch, this, volume, delta, mapSizeX,mapSizeY);
            if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
            && mouseInWorld2D.x > rocket.getPosX()
            && mouseInWorld2D.x < rocket.getPosX() + rocket.getSizeX()
            && mouseInWorld2D.y > rocket.getPosY()
            && mouseInWorld2D.y < rocket.getPosY() + rocket.getSizeY()){
                rocketNumber = i;
                break;
            }else if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
                rocketNumber = -1;
            }
            rocket.setEngineOn(false);
        }

        if(rocketNumber > -1){
            gravity = 0;
            acceleration = 0;
            playerPosX = rockets.get(rocketNumber).getPosX()+30;
            playerPosY = rockets.get(rocketNumber).getPosY();
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                rockets.get(rocketNumber).setEngineOn(true);
                rockets.get(rocketNumber).addSpeed(0.3f * delta);
            }

        }else{
            // DRAW PLAYER
            batch.setColor(brightness,brightness,brightness,1f);
            arm.setColor(brightness,brightness,brightness,1f);
            arm.setPosition(cam.position.x+acceleration+16.5f,cam.position.y-gravity+17);
            arm.setRotation(armAngle);
            arm.draw(batch);
            if (inventory.getSelectedItem().getElement() > 0){
                float angleInRadians = (float)Math.toRadians(270+armAngle);

                float handX = (cam.position.x+acceleration+14) + 20f * MathUtils.cos(angleInRadians);
                float handY = (cam.position.y-gravity+27) + 20f * MathUtils.sin(angleInRadians);
                batch.draw(blockTextures[0][inventory.getSelectedItem().getElement()-1], handX,handY, 12.5f/2f, 12.5f/2f, 25/2f, 25/2f, 1f, 1f, 180+armAngle);
            }
            if(Math.abs(acceleration) > 0.1f){
                if(!isRunning){
                    walkAnimationTimer += 1*delta;
                }else{
                    walkAnimationTimer += 1.5f*delta;
                }
                
            }else{
                walkAnimationTimer = 0;
            }
            if(walkAnimationTimer > 20){walkAnimationTimer = 1;}
            if(onGround){
                if(walkAnimationTimer > 10){
                    batch.draw(playerAnimation[0][1], cam.position.x+acceleration,cam.position.y-gravity);
                }else if(walkAnimationTimer > 0){
                    batch.draw(playerAnimation[0][2], cam.position.x+acceleration,cam.position.y-gravity);
                }else if(walkAnimationTimer == 0){
                    batch.draw(playerAnimation[0][0], cam.position.x+acceleration,cam.position.y-gravity);
                }
            }else{
                batch.draw(playerAnimation[0][3], cam.position.x+acceleration,cam.position.y-gravity);
            }
            batch.setColor(1f,1f,1f,1f);
        }
    
    
        batch.setColor(1f,1f,1f,1f);
        
        cam.position.set( Math.round(playerPosX-acceleration),  Math.round(playerPosY+gravity), 0);
        cam.update();    
    }

    public void DrawHud(Batch batch, HudCamera cam, Block[][] mapArray, float delta,
            CustomInputProcessor customInputProcessor, int mapSizeX, int mapSizeY) {

        // DRAW PLAYER HEALTH
        for (int i = 0; i < playerHealth; i++) {
            batch.draw(healthTexture, 32 + 20 * i, 10);
        }

        // DRAW PLAYER HUNGER
        for (int i = 0; i < playerHunger; i++) {
            batch.draw(hungerTexture, 32 + 20 * i, 30);
        }

        if (inventory.getHover(cam) > -1) {
            elementString = elementNames.get(inventory.getHover(cam));
        }

        // DRAW OXYGEN BAR WHEN DIVING
        if (diving) {
            for (int i = 0; i < playerOxygen; i++) {
                batch.draw(oxygenTexture, 32 + 20 * i, 50);
            }
        }

        // DRAW TUTORIAL
        if (tutorialOn) {
            batch.draw(tutorialScreenTexture, 400, 150);
            font.draw(batch, tutorialString, 650, 700);
        } else {
            batch.draw(tutorialTexture, 0, 870);
            font.draw(batch, "press F1 for tutorial", 10, 890);
        }

        // DRAW HUNGER ALERT
        if (getPlayerHunger() == 0) {
            redFont.draw(batch, "You are starving! Eat something or you are going to die.", 10, 36);
        }

        // DRAW HUD MESSAGE
        if (message.getMessage() != "") {
            hudMessageTimer += 1 * delta;
        }
        if (hudMessageTimer < 300) {
            font.draw(batch, message.getMessage(), 750, 600);
        }
        if (hudMessageTimer > 300) {
            message.setMessage("");
            hudMessageTimer = 0;
        }

        // DRAW CURRENT ELEMENT SELECTED
        if (elementString.length() < 11) {
            batch.draw(tooltipTexture, 1400, 870);
            font.draw(batch, elementString, (1550 - elementString.length() * 4), 890);
        } else if (elementString.length() > 40) {
            batch.draw(tooltipTexture, 1400, 870);
            font.draw(batch, elementString, (1550 - elementString.length() * 5), 890);
        } else {
            batch.draw(tooltipTexture, 1400, 870);
            font.draw(batch, elementString, (1550 - elementString.length() * 6), 890);
        }
        inventory.Update(batch, this, blockTextures, cam, outlineTexture, mapArray, delta, customInputProcessor,
                recipeBook);

        if (inventory.isInventoryOpen()) {
            recipeBook.Draw(batch, cam);
        }
    }

    public void setPaused(boolean pause) {
        isGamePaused = pause;
        inventory.setInventoryOpen(false);
        inventory.setFurnaceOpen(false);
    }

    public boolean isPaused() {
        return isGamePaused;
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
    public Inventory getInventoryObject(){
        return inventory;
    }

    public void setInventory(ArrayList<Item> items) {
        inventory.setItems(items);
    }

    public void addDroppedItem(Item item) {
        droppedItems.add(item);
    }

    public void resetInventory() {
        inventory.reset();
        droppedItems.clear();
        rockets.clear();
        recipeBook.reset();
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

    public int getPlayerHunger() {
        return playerHunger;
    }

    public void setPlayerHunger(int playerHunger) {
        this.playerHunger = playerHunger;
    }

    public int getPlayerOxygen() {
        return playerOxygen;
    }

    public void setPlayerOxygen(int playerOxygen) {
        this.playerOxygen = playerOxygen;
    }
    public Recipebook getRecipebook(){
        return recipeBook;
    }
    public void addRockets(float x, float y){
        rockets.add(new Rocket(x, y, rocketTexture, rocketFlame));
    }
    public ArrayList<Rocket> getRockets(){
        return rockets;
    }

}
