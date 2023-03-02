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

import static com.mygdx.game.map.elements.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private transient Vector2 mouseInWorld2D = new Vector2();
    private transient Vector3 mouseInWorld3D = new Vector3();

    private final int INVENTORY_SLOT_MAX = 32;

    private float playerPosX;
    private float playerPosY;

    private int playerSizeX;
    private int playerSizeY;

    private transient boolean onGround;
    private transient boolean onLadder;
    private transient int onGroundTimer;

    private transient int soundTimer;

    private float gravity;
    private transient float acceleration;

    final transient float PLAYER_BOUNCINESS = 0.0f; // 0 = NO BOUNCE
    final transient float PLAYER_FRICTION = 0.7f; // 1 = NO FRICTION

    private transient Texture playerTexture;
    private transient Texture outlineTexture;
    private transient Texture healthTexture;
    private transient Texture hotbarTexture;
    private transient Texture inventoryTexture;
    private transient Texture textures;
    private transient TextureRegion[][] blockTextures;

    private transient Texture blockBreakingTexture;
    private transient TextureRegion[][] blockBreakingAnimation;

    private transient Sound damageSound;
    private transient Sound stoneHitSound;
    private transient Sound groundHitSound;
    private transient Sound leavesHitSound;
    private transient Sound blockBreakingSound;
    private transient int soundEffect; // TEMPORARY

    private int playerHealth;

    private transient int grab = -1;
    private transient boolean isGrabbed;
    private transient int selectedSlot = 0;
    private transient boolean isInventoryOpen = false;

    private ArrayList<InventorySlot> inventory;
    transient BitmapFont font = new BitmapFont();

    public Player(float x, float y) {
        this.playerPosX = x;
        this.playerPosY = y;

        damageSound = Gdx.audio.newSound(Gdx.files.internal("sounds/damage.mp3"));
        stoneHitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/stoneHitSound.mp3"));
        groundHitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/groundHitSound.mp3"));
        leavesHitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/leavesHitSound.mp3"));
        blockBreakingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/blockBreakingSound.mp3"));

        playerSizeX = 20;
        playerSizeY = 49;

        onGround = false;
        onGroundTimer = 0;

        soundTimer = 0;

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

                if (onLadder) {
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

        for (int x = 0; x < mapArray.length; x++) {
            if (mapArray[x][0].getPosX() > playerPosX - 400 && mapArray[x][0].getPosX() < playerPosX + 400) {
                for (int y = 0; y < mapArray[x].length; y++) {
                    // COLLISION DETECTION AND PHYSICS ON Y-AXIS
                    // IF PLAYER IS INSIDE A BLOCK SET POSITION TO THE OLD POSITION
                    if (playerPosX + playerSizeX > mapArray[x][y].getPosX()
                            && playerPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                            && playerPosY + playerSizeY > mapArray[x][y].getPosY()
                            && playerPosY < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {
                        if (mapArray[x][y].isCollision()) {

                            if (gravity > 10) {
                                // Gdx.app.exit();
                                playerHealth -= 5;
                            } else if (gravity >= 9.5) {
                                playerHealth -= 4;
                                damageSound.play(0.2f);
                            } else if (gravity >= 9) {
                                playerHealth -= 3;
                                damageSound.play(0.2f);
                            } else if (gravity >= 8.25) {
                                playerHealth -= 2;
                                damageSound.play(0.2f);
                            } else if (gravity >= 7.25) {
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
                        } else if (mapArray[x][y].getElement() == LADDER) {
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

        playerPosX += acceleration;
        // FRICTION
        acceleration *= PLAYER_FRICTION;

        for (int x = 0; x < mapArray.length; x++) {
            if (mapArray[x][0].getPosX() > playerPosX - 400 && mapArray[x][0].getPosX() < playerPosX + 400) {
                for (int y = 0; y < mapArray[x].length; y++) {
                    if (mapArray[x][y].getPosY() > playerPosY - 400 && mapArray[x][y].getPosY() < playerPosY + 400) {
                        // COLLISION DETECTION AND PHYSICS ON X-AXIS
                        // IF PLAYER IS INSIDE A BLOCK SET POSITION TO THE OLD POSITION
                        if (mapArray[x][y].isCollision() && playerPosX + playerSizeX > mapArray[x][y].getPosX()
                                && playerPosX < mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                                && playerPosY + playerSizeY > mapArray[x][y].getPosY()
                                && playerPosY < mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {
                            playerPosX = oldX;
                            acceleration = -acceleration * PLAYER_BOUNCINESS;
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
                                                soundEffect = mapArray[x][y].getElement();
                                                if (inventory.get(selectedSlot).getAmount() > 5) {
                                                    mapArray[x][y].setBlockHealth(mapArray[x][y].getBlockHealth() - 25);
                                                } else {
                                                    mapArray[x][y].setBlockHealth(mapArray[x][y].getBlockHealth() - 3);
                                                }
                                                soundTimer += 1;
                                            } else {
                                                if (mapArray[x][y].getElement() == LEAVES
                                                        || mapArray[x][y].getElement() == TALLGRASS
                                                        || mapArray[x][y].getElement() == REDFLOWER) {
                                                    leavesHitSound.play(1, 0.5f, 0);
                                                } else {
                                                    blockBreakingSound.play();
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
                                                int slotIndex = -1;
                                                for (int i = 0; i < 36; i++) {
                                                    if (inventory.get(i).getElement() == mapArray[x][y].getElement()
                                                            && inventory.get(i).getAmount() < INVENTORY_SLOT_MAX) {
                                                        slotIndex = i;
                                                        break;
                                                    }
                                                }
                                                if (slotIndex > 0) {
                                                    inventory.get(slotIndex).addItem();
                                                } else {
                                                    for (int i = 0; i < 36; i++) {
                                                        if (inventory.get(i).getAmount() == 0) {
                                                            inventory.get(i).addItem();
                                                            inventory.get(i).setElement(mapArray[x][y].getElement());
                                                            break;
                                                        } else if (inventory.get(i).getElement() == mapArray[x][y]
                                                                .getElement()
                                                                && inventory.get(i).getAmount() < INVENTORY_SLOT_MAX) {
                                                            inventory.get(i).addItem();
                                                            break;
                                                        }
                                                    }
                                                }

                                                mapArray[x][y].setElement(EMPTY);
                                                mapArray[x][y].setCollision(false);

                                            }
                                        } else {
                                            soundTimer = 15;
                                        }
                                        batch.setColor(1, 1, 1, 1);
                                        batch.draw(outlineTexture, mapArray[x][y].getPosX(), mapArray[x][y].getPosY());

                                        // IF RIGHT MOUSE BUTTON IS DOWN, PLACE BLOCK IN HAND
                                        if (inventory.get(selectedSlot).isPlaceable()
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
                                                    mapArray[x][y].setElement(inventory.get(selectedSlot).getElement());
                                                    // prevent collision on certain blocks
                                                    switch (inventory.get(selectedSlot).getElement()) {
                                                        case LADDER:
                                                            break;
                                                        case TALLGRASS:
                                                            break;
                                                        case REDFLOWER:
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
                                }
                            }
                        }
                    }
                }
            }
        }
        if (soundTimer == 20) {
            if (soundEffect == GRASS || soundEffect == GROUND) {
                groundHitSound.play(1f, 0.8f, 0);
            } else if (soundEffect == STONE || soundEffect == IRON || soundEffect == COAL) {
                stoneHitSound.play();
                soundEffect = 0;
            }
            soundTimer -= 20;
        }

        if (onGroundTimer <= 0) {
            onGround = false;
        } else {
            onGroundTimer -= 1;
        }

        // DRAW PLAYER
        batch.draw(playerTexture, playerPosX, playerPosY);
        cam.position.set((int) playerPosX + playerSizeX / 2, (int) playerPosY + playerSizeY / 2, 0);
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
                font.draw(batch, str, 802 - (261 / 2) + (29 * i), 15);

                if (cam.getInputInGameWorld().x > 802 - (261 / 2) + (29 * i)
                        && cam.getInputInGameWorld().x < (802 - (261 / 2) + (29 * i)) + 25
                        && cam.getInputInGameWorld().y > 2 && cam.getInputInGameWorld().y < 27
                        && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && grab == -1) {
                    grab = i;
                    isGrabbed = true;
                }
            }
            if (!isGrabbed) {

                if (cam.getInputInGameWorld().x > 802 - (261 / 2) + (29 * i)
                        && cam.getInputInGameWorld().x < (802 - (261 / 2) + (29 * i)) + 25
                        && cam.getInputInGameWorld().y > 2 && cam.getInputInGameWorld().y < 27 && grab > -1
                        && grab != i) {
                    if (inventory.get(grab).getElement() == inventory.get(i).getElement()
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
                    batch.draw(blockTextures[0][inventory.get(i).getElement() - 1], 802 - (261 / 2) + (29 * invDrawRow),
                            250 - (88 / 2) + 2 + (invDrawColumn * 29));
                    font.draw(batch, str, 802 - (261 / 2) + (29 * invDrawRow),
                            250 - (88 / 2) + 15 + (invDrawColumn * 29));

                    if (cam.getInputInGameWorld().x > 802 - (261 / 2) + (29 * invDrawRow)
                            && cam.getInputInGameWorld().x < (802 - (261 / 2) + (29 * invDrawRow)) + 25
                            && cam.getInputInGameWorld().y > 250 - (88 / 2) + (invDrawColumn * 29)
                            && cam.getInputInGameWorld().y < 250 - (88 / 2) + 27 + (invDrawColumn * 29)
                            && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && grab == -1) {
                        grab = i;
                        isGrabbed = true;
                    }
                }
                if (!isGrabbed) {

                    if (cam.getInputInGameWorld().x > 802 - (261 / 2) + (29 * invDrawRow)
                            && cam.getInputInGameWorld().x < (802 - (261 / 2) + (29 * invDrawRow)) + 25
                            && cam.getInputInGameWorld().y > 250 - (88 / 2) + (invDrawColumn * 29)
                            && cam.getInputInGameWorld().y < 250 - (88 / 2) + 27 + (invDrawColumn * 29) && grab > -1
                            && grab != i) {

                        if (inventory.get(grab).getElement() == inventory.get(i).getElement()
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
                    font.draw(batch, str, 860 - (261 / 2) + (29 * invDrawRow),
                            363 - (88 / 2) + 15 + (invDrawColumn * 29));

                    if (cam.getInputInGameWorld().x > 860 - (261 / 2) + (29 * invDrawRow)
                            && cam.getInputInGameWorld().x < (860 - (261 / 2) + (29 * invDrawRow)) + 25
                            && cam.getInputInGameWorld().y > 363 - (88 / 2) + (invDrawColumn * 29)
                            && cam.getInputInGameWorld().y < 363 - (88 / 2) + 27 + (invDrawColumn * 29)
                            && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && grab == -1) {
                        grab = i;
                        isGrabbed = true;
                    }
                }
                if (!isGrabbed) {

                    if (cam.getInputInGameWorld().x > 860 - (261 / 2) + (29 * invDrawRow)
                            && cam.getInputInGameWorld().x < (860 - (261 / 2) + (29 * invDrawRow)) + 25
                            && cam.getInputInGameWorld().y > 363 - (88 / 2) + (invDrawColumn * 29)
                            && cam.getInputInGameWorld().y < 363 - (88 / 2) + 27 + (invDrawColumn * 29) && grab > -1
                            && grab != i) {

                        if (inventory.get(grab).getElement() == inventory.get(i).getElement()
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
                    }
                }
                if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                    if (cam.getInputInGameWorld().x > 860 - (261 / 2) + (29 * invDrawRow)
                            && cam.getInputInGameWorld().x < (860 - (261 / 2) + (29 * invDrawRow)) + 25
                            && cam.getInputInGameWorld().y > 363 - (88 / 2) + (invDrawColumn * 29)
                            && cam.getInputInGameWorld().y < 363 - (88 / 2) + 27 + (invDrawColumn * 29) && grab > -1
                            && grab != i) {
                        int putAmount = 1;
                        if (inventory.get(grab).getElement() == inventory.get(i).getElement()
                                && inventory.get(i).getAmount() + putAmount < INVENTORY_SLOT_MAX) {
                            inventory.get(i).setAmount(inventory.get(i).getAmount() + putAmount);
                            inventory.get(grab).removeItem();
                            if (inventory.get(grab).getAmount() <= 0) {
                                inventory.get(grab).setElement(0);
                                grab = -1;
                            }
                        } else if (inventory.get(i).getAmount() == 0
                                && inventory.get(i).getAmount() + putAmount < INVENTORY_SLOT_MAX) {
                            inventory.get(i).setElement(inventory.get(grab).getElement());
                            inventory.get(i).setAmount(inventory.get(i).getAmount() + putAmount);
                            inventory.get(grab).removeItem();
                            if (inventory.get(grab).getAmount() <= 0) {
                                inventory.get(grab).setElement(0);
                                grab = -1;
                            }
                        }
                    }
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
                font.draw(batch, str, 976 - (261 / 2), 392 - (88 / 2) + 15);
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && cam.getInputInGameWorld().x > 976 - (261 / 2)
                        && cam.getInputInGameWorld().x < (976 - (261 / 2) + 25)
                        && cam.getInputInGameWorld().y > 392 - (88 / 2)
                        && cam.getInputInGameWorld().y < 392 - (88 / 2) + 27) {
                    int slotIndex = -1;
                    boolean craftingSuccess = false;
                    for (int i = 0; i < 36; i++) {
                        if (inventory.get(i).getElement() == newItem.getElement()
                                && inventory.get(i).getAmount() + newItem.getAmount() < INVENTORY_SLOT_MAX) {
                            slotIndex = i;
                            break;
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
                                craftingSuccess = true;
                                break;
                            } else if (inventory.get(i).getElement() == newItem.getElement()
                                    && inventory.get(i).getAmount() + newItem.getAmount() < INVENTORY_SLOT_MAX) {
                                inventory.get(i).setAmount(inventory.get(i).getAmount() + newItem.getAmount());
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
            font.draw(batch, str, cam.getInputInGameWorld().x - 12, cam.getInputInGameWorld().y + 1);
        }

        if (!isGrabbed) {
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
        for (int i = 0; i < 46; i++) {
            inventory.add(new InventorySlot());
        }
    }

}
