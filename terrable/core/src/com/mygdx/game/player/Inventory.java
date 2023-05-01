package com.mygdx.game.player;

import java.util.ArrayList;
import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.CustomInputProcessor;
import com.mygdx.game.camera.HudCamera;
import com.mygdx.game.map.Block;

public class Inventory {
    private int grab = -1;
    private boolean isGrabbed;
    private float grabTimer = 0;
    private int selectedSlot = 0;
    private boolean isInventoryOpen = false;
    private boolean isFurnaceOpen = false;
    private ArrayList<Integer> usedSlots = new ArrayList<>();
    private BitmapFont font = new BitmapFont();
    private ArrayList<Item> items;

    private final int INVENTORY_SLOT_MAX = 32;

    private Texture hotbarTexture;
    private Texture inventoryTexture;
    private Texture furnaceMenuTexture;

    private int openFurnaceX = 0;
    private int openFurnaceY = 0;

    private ArrayList<Integer> discoveredItems;
    private AchievementManager achievements;
    private HudMessage message;

    public Inventory() {

        hotbarTexture = new Texture("hotbar.png");
        inventoryTexture = new Texture("crafting.png");
        furnaceMenuTexture = new Texture("furnacemenu.png");

        items = new ArrayList<>();
        for (int i = 0; i < 46; i++) {
            items.add(new Item());
        }

        discoveredItems = new ArrayList<>();
        achievements = new AchievementManager();
        message = new HudMessage();

    }

    public void Update(Batch batch, Player player, TextureRegion[][] blockTextures, HudCamera cam,
            Texture outlineTexture, Block[][] mapArray, float delta, CustomInputProcessor customInputProcessor, Recipebook recipebook) {
        if(!player.isPaused()){
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

            
            if (customInputProcessor.wasScrolledUp()) {
                selectedSlot--;
                if (selectedSlot < 0){selectedSlot = 0;}
            }else if(customInputProcessor.wasScrolledDown()){
                selectedSlot++;
                if (selectedSlot > 8){selectedSlot = 8;}
            }
    
            

            if (grabTimer >= 0) {
                grabTimer -= 1 * delta;
            }

            // show/hide inventory
            if (!isInventoryOpen && !player.isPaused()) {
                if ((Gdx.input.isKeyJustPressed(Input.Keys.TAB) || Gdx.input.isKeyJustPressed(Input.Keys.E))
                        && !isFurnaceOpen) {
                    isInventoryOpen = true;
                }

            } else if (Gdx.input.isKeyJustPressed(Input.Keys.TAB) || Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                isInventoryOpen = false;
                isFurnaceOpen = false;
                for (int j = 36; j < 45; j++) {
                    for (int i = 0; i < 36; i++) {
                        int slotIndex = -1;
                        for (int o = 0; o < 36; o++) {
                            if (items.get(j).getElement() == items.get(o).getElement()
                                    && items.get(j).getAmount() + items.get(o).getAmount() <= INVENTORY_SLOT_MAX) {
                                slotIndex = o;
                                break;
                            }
                        }
                        if (slotIndex > 0) {
                            items.get(slotIndex).setAmount(items.get(slotIndex).getAmount() + items.get(j).getAmount());
                            break;
                        } else {
                            if (!items.get(j).isWeapon() && items.get(i).getElement() == items.get(j).getElement()
                                    && items.get(i).getAmount() + items.get(j).getAmount() <= INVENTORY_SLOT_MAX) {
                                items.get(i).setAmount(items.get(i).getAmount() + items.get(j).getAmount());
                                items.get(i).setWeapon(items.get(j).isWeapon());
                                items.get(i).setFood(items.get(j).isFood());
                                items.get(i).setResource(items.get(j).isResource());
                                items.get(i).setDamage(items.get(j).getDamage());
                                items.get(i).setHealth(items.get(j).getHealth());
                                break;
                            } else if (items.get(i).getAmount() == 0) {
                                items.get(i).setAmount(items.get(j).getAmount());
                                items.get(i).setElement(items.get(j).getElement());
                                items.get(i).setWeapon(items.get(j).isWeapon());
                                items.get(i).setFood(items.get(j).isFood());
                                items.get(i).setResource(items.get(j).isResource());
                                items.get(i).setDamage(items.get(j).getDamage());
                                items.get(i).setHealth(items.get(j).getHealth());
                                break;
                            }
                        }

                    }
                    items.get(j).setAmount(0);
                }

            }

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && isGrabbed) {
                isGrabbed = false;
            }


            // hotbar
            batch.draw(hotbarTexture, 800 - (261 / 2), 0);

            if (isFurnaceOpen) {
                batch.draw(furnaceMenuTexture, 800 - (271 / 2), 245 - (88 / 2));
            } else if (isInventoryOpen) {
                batch.draw(inventoryTexture, 800 - (271 / 2), 245 - (88 / 2));
            }

            for (int i = 0; i < 9; i++) {
                CharSequence str = Integer.toString(items.get(i).getAmount());
                if (items.get(i).getAmount() > 0 && grab != i) {
                    batch.draw(blockTextures[0][items.get(i).getElement() - 1], 802 - (261 / 2) + (29 * i), 2);
                    if (!items.get(i).isWeapon()) {
                        font.draw(batch, str, 802 - (261 / 2) + (29 * i), 15);
                    }

                    if (cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * i)
                            && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * i)) + 28
                            && cam.getInputInGameWorld().y >= 0 && cam.getInputInGameWorld().y < 28
                            && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                            && items.get(45).getElement() != items.get(i).getElement()) {
                        Item reserveSlot = items.get(45);
                        Item reserveSlot2 = items.get(i);
                        items.set(i, reserveSlot);
                        items.set(45, reserveSlot2);
                        isGrabbed = true;
                    } else if (cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * i)
                            && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * i)) + 28
                            && cam.getInputInGameWorld().y >= 0 && cam.getInputInGameWorld().y < 28
                            && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && items.get(45).getAmount() == 0) {
                        int putAmount = (int) Math.ceil(items.get(i).getAmount() / 2f);
                        items.get(45).setElement(items.get(i).getElement());
                        items.get(45).setDamage(items.get(i).getDamage());
                        items.get(45).setHealth(items.get(i).getHealth());
                        items.get(45).setResource(items.get(i).isResource());
                        items.get(45).setFood(items.get(i).isFood());
                        items.get(45).setWeapon(items.get(i).isWeapon());
                        items.get(45).setAmount(putAmount);
                        items.get(i).setAmount(items.get(i).getAmount() - putAmount);
                        isGrabbed = true;
                        grabTimer = 15;
                    }
                }

                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                        && cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * i)
                        && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * i)) + 28
                        && cam.getInputInGameWorld().y >= 0 && cam.getInputInGameWorld().y < 28 && !isGrabbed) {

                    if (!items.get(45).isWeapon() && items.get(45).getElement() == items.get(i).getElement()
                            && items.get(i).getAmount() + items.get(45).getAmount() <= INVENTORY_SLOT_MAX) {
                        items.get(i).setAmount(items.get(i).getAmount() + items.get(45).getAmount());
                        items.get(45).setAmount(0);
                        items.get(45).setElement(0);
                        isGrabbed = false;
                    } else {
                        Item reserveSlot = items.get(45);
                        Item reserveSlot2 = items.get(i);
                        items.set(i, reserveSlot);
                        items.set(45, reserveSlot2);
                        isGrabbed = false;
                    }
                }

                if (i == selectedSlot) {
                    batch.draw(outlineTexture, 802 - (261 / 2) + (29 * i), 2);
                }
            }
            // inventory
            if (isInventoryOpen) {

                int invDrawRow = 0;
                int invDrawColumn = 0;
                for (int i = 9; i < 36; i++) {
                    CharSequence str = Integer.toString(items.get(i).getAmount());
                    if (items.get(i).getAmount() > 0) {
                        batch.draw(blockTextures[0][items.get(i).getElement() - 1], 802 - (261 / 2) + (29 * invDrawRow),
                                250 - (88 / 2) + 2 + (invDrawColumn * 29));
                        if (!items.get(i).isWeapon()) {
                            font.draw(batch, str, 802 - (261 / 2) + (29 * invDrawRow),
                                    250 - (88 / 2) + 15 + (invDrawColumn * 29));
                        }

                        if (cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * invDrawRow)
                                && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * invDrawRow)) + 28
                                && cam.getInputInGameWorld().y >= 254 - (90 / 2) + (invDrawColumn * 29)
                                && cam.getInputInGameWorld().y <= 254 - (90 / 2) + 28 + (invDrawColumn * 29)
                                && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                                && items.get(45).getElement() != items.get(i).getElement()) {
                            Item reserveSlot = items.get(45);
                            Item reserveSlot2 = items.get(i);
                            items.set(i, reserveSlot);
                            items.set(45, reserveSlot2);
                            isGrabbed = true;
                        } else if (cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * invDrawRow)
                                && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * invDrawRow)) + 28
                                && cam.getInputInGameWorld().y >= 254 - (90 / 2) + (invDrawColumn * 29)
                                && cam.getInputInGameWorld().y <= 254 - (90 / 2) + 28 + (invDrawColumn * 29)
                                && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && items.get(45).getAmount() == 0) {
                            int putAmount = (int) Math.ceil(items.get(i).getAmount() / 2f);
                            items.get(45).setElement(items.get(i).getElement());
                            items.get(45).setDamage(items.get(i).getDamage());
                            items.get(45).setHealth(items.get(i).getHealth());
                            items.get(45).setResource(items.get(i).isResource());
                            items.get(45).setFood(items.get(i).isFood());
                            items.get(45).setWeapon(items.get(i).isWeapon());
                            items.get(45).setAmount(putAmount);
                            items.get(i).setAmount(items.get(i).getAmount() - putAmount);
                            isGrabbed = true;
                            grabTimer = 15;
                        }
                    }

                    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                            && cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * invDrawRow)
                            && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * invDrawRow)) + 28
                            && cam.getInputInGameWorld().y >= 254 - (90 / 2) + (invDrawColumn * 29)
                            && cam.getInputInGameWorld().y <= 254 - (90 / 2) + 29 + (invDrawColumn * 29) && !isGrabbed) {

                        if (!items.get(45).isWeapon() && items.get(45).getElement() == items.get(i).getElement()
                                && items.get(i).getAmount()
                                        + items.get(45).getAmount() <= INVENTORY_SLOT_MAX) {
                            items.get(i).setAmount(items.get(i).getAmount() + items.get(45).getAmount());
                            items.get(45).setAmount(0);
                            items.get(45).setElement(0);
                            isGrabbed = false;
                        } else {
                            Item reserveSlot = items.get(45);
                            Item reserveSlot2 = items.get(i);
                            items.set(i, reserveSlot);
                            items.set(45, reserveSlot2);
                            isGrabbed = false;
                        }
                    }
                    if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && grabTimer < 0 && items.get(45).getAmount() > 0) {
                        if (cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * invDrawRow)
                                && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * invDrawRow)) + 28
                                && cam.getInputInGameWorld().y >= 254 - (90 / 2) + (invDrawColumn * 29)
                                && cam.getInputInGameWorld().y <= 254 - (90 / 2) + 29 + (invDrawColumn * 29)
                                && !usedSlots.contains(i)) {
                            int putAmount = 1;
                            if (!items.get(45).isWeapon() && items.get(45).getElement() == items.get(i).getElement()
                                    && items.get(i).getAmount() + putAmount <= INVENTORY_SLOT_MAX) {
                                items.get(i).setAmount(items.get(i).getAmount() + putAmount);
                                items.get(45).removeItem();
                                usedSlots.add(i);
                                if (items.get(45).getAmount() <= 0) {
                                    items.get(45).setElement(0);
                                    isGrabbed = false;
                                }
                            } else if (items.get(i).getAmount() == 0
                                    && items.get(i).getAmount() + putAmount <= INVENTORY_SLOT_MAX) {
                                items.get(i).setElement(items.get(45).getElement());
                                items.get(i).setDamage(items.get(45).getDamage());
                                items.get(i).setHealth(items.get(45).getHealth());
                                items.get(i).setResource(items.get(45).isResource());
                                items.get(i).setFood(items.get(45).isFood());
                                items.get(i).setWeapon(items.get(45).isWeapon());
                                items.get(i).setAmount(items.get(i).getAmount() + putAmount);
                                items.get(45).removeItem();
                                usedSlots.add(i);
                                if (items.get(45).getAmount() <= 0) {
                                    items.get(45).setElement(0);
                                    isGrabbed = false;
                                }
                            }
                        }
                    } else {
                        usedSlots.clear();
                    }
                    invDrawRow++;
                    if (invDrawRow % 9 == 0) {
                        invDrawColumn++;
                        invDrawRow = 0;
                    }
                }

                if (!isFurnaceOpen) {

                    // crafting slots
                    invDrawRow = 0;
                    invDrawColumn = 0;
                    for (int i = 36; i < 45; i++) {
                        CharSequence str = Integer.toString(items.get(i).getAmount());
                        if (items.get(i).getAmount() > 0) {
                            batch.draw(blockTextures[0][items.get(i).getElement() - 1], 860 - (261 / 2) + (29 * invDrawRow),
                                    363 - (88 / 2) + 2 + (invDrawColumn * 29));
                            if (!items.get(i).isWeapon()) {
                                font.draw(batch, str, 860 - (261 / 2) + (29 * invDrawRow),
                                        363 - (88 / 2) + 15 + (invDrawColumn * 29));
                            }

                            if (cam.getInputInGameWorld().x >= 858 - (261 / 2) + (29 * invDrawRow)
                                    && cam.getInputInGameWorld().x <= (858 - (261 / 2) + (29 * invDrawRow)) + 28
                                    && cam.getInputInGameWorld().y >= 365 - (90 / 2) + (invDrawColumn * 29)
                                    && cam.getInputInGameWorld().y <= 365 - (90 / 2) + 29 + (invDrawColumn * 29)
                                    && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                                    && items.get(45).getElement() != items.get(i).getElement()) {
                                Item reserveSlot = items.get(45);
                                Item reserveSlot2 = items.get(i);
                                items.set(i, reserveSlot);
                                items.set(45, reserveSlot2);
                                isGrabbed = true;
                            } else if (cam.getInputInGameWorld().x >= 858 - (261 / 2) + (29 * invDrawRow)
                                    && cam.getInputInGameWorld().x <= (858 - (261 / 2) + (29 * invDrawRow)) + 28
                                    && cam.getInputInGameWorld().y >= 365 - (90 / 2) + (invDrawColumn * 29)
                                    && cam.getInputInGameWorld().y <= 365 - (90 / 2) + 29 + (invDrawColumn * 29)
                                    && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
                                    && items.get(45).getAmount() == 0) {
                                int putAmount = (int) Math.ceil(items.get(i).getAmount() / 2f);
                                items.get(45).setElement(items.get(i).getElement());
                                items.get(45).setDamage(items.get(i).getDamage());
                                items.get(45).setHealth(items.get(i).getHealth());
                                items.get(45).setResource(items.get(i).isResource());
                                items.get(45).setFood(items.get(i).isFood());
                                items.get(45).setWeapon(items.get(i).isWeapon());
                                items.get(45).setAmount(putAmount);
                                items.get(i).setAmount(items.get(i).getAmount() - putAmount);
                                isGrabbed = true;
                                grabTimer = 15;
                            }
                        }

                        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                                && cam.getInputInGameWorld().x >= 858 - (261 / 2) + (29 * invDrawRow)
                                && cam.getInputInGameWorld().x <= (858 - (261 / 2) + (29 * invDrawRow)) + 28
                                && cam.getInputInGameWorld().y >= 365 - (90 / 2) + (invDrawColumn * 29)
                                && cam.getInputInGameWorld().y <= 365 - (90 / 2) + 29 + (invDrawColumn * 29)
                                && !isGrabbed) {

                            if (!items.get(45).isWeapon() && items.get(45).getElement() == items.get(i).getElement()
                                    && items.get(i).getAmount()
                                            + items.get(45).getAmount() <= INVENTORY_SLOT_MAX) {
                                items.get(i).setAmount(items.get(i).getAmount() + items.get(45).getAmount());
                                items.get(45).setAmount(0);
                                items.get(45).setElement(0);
                                isGrabbed = false;
                            } else {
                                Item reserveSlot = items.get(45);
                                Item reserveSlot2 = items.get(i);
                                items.set(i, reserveSlot);
                                items.set(45, reserveSlot2);
                                isGrabbed = false;
                            }
                        }

                        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && grabTimer < 0
                                && items.get(45).getAmount() > 0) {
                            if (cam.getInputInGameWorld().x >= 858 - (261 / 2) + (29 * invDrawRow)
                                    && cam.getInputInGameWorld().x <= (858 - (261 / 2) + (29 * invDrawRow)) + 28
                                    && cam.getInputInGameWorld().y >= 365 - (90 / 2) + (invDrawColumn * 29)
                                    && cam.getInputInGameWorld().y <= 365 - (90 / 2) + 29 + (invDrawColumn * 29)
                                    && !usedSlots.contains(i)) {
                                int putAmount = 1;
                                if (!items.get(45).isWeapon() && items.get(45).getElement() == items.get(i).getElement()
                                        && items.get(i).getAmount() + putAmount <= INVENTORY_SLOT_MAX) {
                                    items.get(i).setAmount(items.get(i).getAmount() + putAmount);
                                    items.get(45).removeItem();
                                    usedSlots.add(i);
                                    if (items.get(45).getAmount() <= 0) {
                                        items.get(45).setElement(0);
                                        isGrabbed = false;
                                    }
                                } else if (items.get(i).getAmount() == 0
                                        && items.get(i).getAmount() + putAmount <= INVENTORY_SLOT_MAX) {
                                    items.get(i).setElement(items.get(45).getElement());
                                    items.get(i).setDamage(items.get(45).getDamage());
                                    items.get(i).setHealth(items.get(45).getHealth());
                                    items.get(i).setResource(items.get(45).isResource());
                                    items.get(i).setFood(items.get(45).isFood());
                                    items.get(i).setWeapon(items.get(45).isWeapon());
                                    items.get(i).setAmount(items.get(i).getAmount() + putAmount);
                                    items.get(45).removeItem();
                                    usedSlots.add(i);
                                    if (items.get(45).getAmount() <= 0) {
                                        items.get(45).setElement(0);
                                        isGrabbed = false;
                                    }
                                }
                            }
                        } else {
                            usedSlots.clear();
                        }
                        invDrawRow++;
                        if (invDrawRow % 3 == 0) {
                            invDrawColumn++;
                            invDrawRow = 0;
                        }
                    }
                    // crafting
                    Item newItem = Crafting.craft(items.subList(36, 45));
                    if (newItem != null) {
                        CharSequence str = Integer.toString(newItem.getAmount());
                        batch.draw(blockTextures[0][newItem.getElement() - 1], 976 - (261 / 2), 392 - (88 / 2) + 2);
                        if (!newItem.isWeapon()) {
                            font.draw(batch, str, 976 - (261 / 2), 392 - (88 / 2) + 15);
                        }
                        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                                && cam.getInputInGameWorld().x > 976 - (261 / 2)
                                && cam.getInputInGameWorld().x < (976 - (261 / 2) + 25)
                                && cam.getInputInGameWorld().y > 392 - (88 / 2)
                                && cam.getInputInGameWorld().y < 392 - (88 / 2) + 27) {
                            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                                int slotIndex = -1;
                                boolean craftingSuccess = false;
                                if (!newItem.isWeapon()) {
                                    for (int i = 0; i < 36; i++) {
                                        if (items.get(i).getElement() == newItem.getElement()
                                                && items.get(i).getAmount() + newItem.getAmount() <= INVENTORY_SLOT_MAX) {
                                            slotIndex = i;
                                            break;
                                        }
                                    }
                                }

                                if (slotIndex > 0) {
                                    items.get(slotIndex).setAmount(items.get(slotIndex).getAmount() + newItem.getAmount());
                                    craftingSuccess = true;
                                } else {
                                    for (int i = 0; i < 36; i++) {
                                        if (items.get(i).getAmount() == 0) {
                                            items.get(i).setAmount(newItem.getAmount());
                                            items.get(i).setElement(newItem.getElement());
                                            items.get(i).setWeapon(newItem.isWeapon());
                                            items.get(i).setFood(newItem.isFood());
                                            items.get(i).setResource(newItem.isResource());
                                            items.get(i).setDamage(newItem.getDamage());
                                            items.get(i).setHealth(newItem.getHealth());
                                            craftingSuccess = true;
                                            break;
                                        } else if (!newItem.isWeapon() && items.get(i).getElement() == newItem.getElement()
                                                && items.get(i).getAmount() + newItem.getAmount() <= INVENTORY_SLOT_MAX) {
                                            items.get(i).setAmount(items.get(i).getAmount() + newItem.getAmount());
                                            items.get(i).setWeapon(newItem.isWeapon());
                                            items.get(i).setFood(newItem.isFood());
                                            items.get(i).setResource(newItem.isResource());
                                            items.get(i).setDamage(newItem.getDamage());
                                            items.get(i).setHealth(newItem.getHealth());
                                            craftingSuccess = true;
                                            break;
                                        }
                                    }
                                }
                                if (craftingSuccess) {
                                    for (int i = 36; i < 45; i++) {
                                        items.get(i).setAmount(items.get(i).getAmount() - newItem.getRemoveAmount());
                                        if (items.get(i).getAmount() < 0) {
                                            items.get(i).setAmount(0);
                                            items.get(i).setElement(0);
                                        }
                                    }
                                }
                            } else {
                                boolean craftingSuccess = false;
                                if (items.get(45).getAmount() == 0) {
                                    items.get(45).setAmount(newItem.getAmount());
                                    items.get(45).setElement(newItem.getElement());
                                    items.get(45).setWeapon(newItem.isWeapon());
                                    items.get(45).setFood(newItem.isFood());
                                    items.get(45).setResource(newItem.isResource());
                                    items.get(45).setDamage(newItem.getDamage());
                                    items.get(45).setHealth(newItem.getHealth());
                                    craftingSuccess = true;
                                    isGrabbed = true;
                                }
                                if (craftingSuccess) {
                                    for (int i = 36; i < 45; i++) {
                                        items.get(i).setAmount(items.get(i).getAmount() - newItem.getRemoveAmount());
                                        if (items.get(i).getAmount() < 0) {
                                            items.get(i).setAmount(0);
                                            items.get(i).setElement(0);
                                        }
                                    }
                                }
                            }

                        }
                    }
                } else {

                    // FURNACE
                    Item furnaceSlot1 = mapArray[openFurnaceX][openFurnaceY].getFurnaceSlot1();
                    Item furnaceSlot2 = mapArray[openFurnaceX][openFurnaceY].getFurnaceSlot2();
                    Item furnaceSlot3 = mapArray[openFurnaceX][openFurnaceY].getFurnaceSlot3();
                    if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                        if ((furnaceSlot1.getAmount() == 0) 
                        && cam.getInputInGameWorld().x > 860 - (261 / 2) + (29 * 1)
                        && cam.getInputInGameWorld().x < 860 - (261 / 2) + (29 * 1) + 25
                        && cam.getInputInGameWorld().y > 363 - (88 / 2) + 2 + (2 * 29)
                        && cam.getInputInGameWorld().y < 363 - (88 / 2) + 2 + (2 * 29) + 27 ) {
                            furnaceSlot1.setAmount(items.get(45).getAmount());
                            furnaceSlot1.setElement(items.get(45).getElement());
                            furnaceSlot1.setWeapon(items.get(45).isWeapon());
                            furnaceSlot1.setFood(items.get(45).isFood());
                            furnaceSlot1.setResource(items.get(45).isResource());
                            furnaceSlot1.setDamage(items.get(45).getDamage());
                            furnaceSlot1.setHealth(items.get(45).getHealth());
                            if (furnaceSlot1.getAmount() > 0 && furnaceSlot2.getAmount() > 0) {
                                mapArray[openFurnaceX][openFurnaceY].setFurnaceStartTimer(new Date().getTime());
                            }

                            items.get(45).setAmount(0);
                        }else if(furnaceSlot1.getElement() == items.get(45).getElement() 
                            && furnaceSlot1.getAmount() + items.get(45).getAmount() <= 32
                            && cam.getInputInGameWorld().x > 860 - (261 / 2) + (29 * 1)
                            && cam.getInputInGameWorld().x < 860 - (261 / 2) + (29 * 1) + 25
                            && cam.getInputInGameWorld().y > 363 - (88 / 2) + 2 + (2 * 29)
                            && cam.getInputInGameWorld().y < 363 - (88 / 2) + 2 + (2 * 29) + 27 ){
                            furnaceSlot1.setAmount(furnaceSlot1.getAmount() + items.get(45).getAmount());
                            items.get(45).setAmount(0);
                        }else if (items.get(45).getAmount() == 0 
                                && cam.getInputInGameWorld().x > 860 - (261 / 2) + (29 * 1)
                                && cam.getInputInGameWorld().x < 860 - (261 / 2) + (29 * 1) + 25
                                && cam.getInputInGameWorld().y > 363 - (88 / 2) + 2 + (2 * 29)
                                && cam.getInputInGameWorld().y < 363 - (88 / 2) + 2 + (2 * 29) + 27) {
                            items.get(45).setAmount(furnaceSlot1.getAmount());
                            items.get(45).setElement(furnaceSlot1.getElement());
                            items.get(45).setWeapon(furnaceSlot1.isWeapon());
                            items.get(45).setFood(furnaceSlot1.isFood());
                            items.get(45).setResource(furnaceSlot1.isResource());
                            items.get(45).setDamage(furnaceSlot1.getDamage());
                            items.get(45).setHealth(furnaceSlot1.getHealth());
                            furnaceSlot1.setAmount(0);
                            isGrabbed = true;
                        }
                    }
                    if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
                        && cam.getInputInGameWorld().x > 860 - (261 / 2) + (29 * 1)
                        && cam.getInputInGameWorld().x < 860 - (261 / 2) + (29 * 1) + 25
                        && cam.getInputInGameWorld().y > 363 - (88 / 2) + 2 + (2 * 29)
                        && cam.getInputInGameWorld().y < 363 - (88 / 2) + 2 + (2 * 29) + 27 ){
                            if(furnaceSlot1.getElement() == items.get(45).getElement() 
                                && furnaceSlot1.getAmount() + items.get(45).getAmount() <= 32){
                                furnaceSlot1.setAmount(furnaceSlot1.getAmount() + 1);
                                items.get(45).setAmount(items.get(45).getAmount() - 1);
                            } else if(furnaceSlot1.getAmount() == 0 && items.get(45).getAmount() > 0){
                                furnaceSlot1.setAmount(1);
                                furnaceSlot1.setElement(items.get(45).getElement());
                                furnaceSlot1.setWeapon(items.get(45).isWeapon());
                                furnaceSlot1.setFood(items.get(45).isFood());
                                furnaceSlot1.setResource(items.get(45).isResource());
                                furnaceSlot1.setDamage(items.get(45).getDamage());
                                furnaceSlot1.setHealth(items.get(45).getHealth());
                                items.get(45).setAmount(items.get(45).getAmount() - 1);
                                if (furnaceSlot1.getAmount() > 0 && furnaceSlot2.getAmount() > 0) {
                                    mapArray[openFurnaceX][openFurnaceY].setFurnaceStartTimer(new Date().getTime());
                                }
                            }

                    }



                    if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                        if ((furnaceSlot2.getAmount() == 0) 
                            && cam.getInputInGameWorld().x > 860 - (261 / 2) + (29 * 1)
                            && cam.getInputInGameWorld().x < 860 - (261 / 2) + (29 * 1) + 25
                            && cam.getInputInGameWorld().y > 363 - (88 / 2) + 2 + (0 * 29)
                            && cam.getInputInGameWorld().y < 363 - (88 / 2) + 2 + (0 * 29) + 27 ) {
                            furnaceSlot2.setAmount(items.get(45).getAmount());
                            furnaceSlot2.setElement(items.get(45).getElement());
                            furnaceSlot2.setWeapon(items.get(45).isWeapon());
                            furnaceSlot2.setFood(items.get(45).isFood());
                            furnaceSlot2.setResource(items.get(45).isResource());
                            furnaceSlot2.setDamage(items.get(45).getDamage());
                            furnaceSlot2.setHealth(items.get(45).getHealth());
                            if (furnaceSlot1.getAmount() > 0 && furnaceSlot2.getAmount() > 0) {
                                mapArray[openFurnaceX][openFurnaceY].setFurnaceStartTimer(new Date().getTime());
                            }

                            items.get(45).setAmount(0);
                        }else if(furnaceSlot2.getElement() == items.get(45).getElement() 
                            && furnaceSlot2.getAmount() + items.get(45).getAmount() <= 32
                            && cam.getInputInGameWorld().x > 860 - (261 / 2) + (29 * 1)
                            && cam.getInputInGameWorld().x < 860 - (261 / 2) + (29 * 1) + 25
                            && cam.getInputInGameWorld().y > 363 - (88 / 2) + 2 + (0 * 29)
                            && cam.getInputInGameWorld().y < 363 - (88 / 2) + 2 + (0 * 29) + 27){
                            furnaceSlot2.setAmount(furnaceSlot2.getAmount() + items.get(45).getAmount());
                            items.get(45).setAmount(0);
                        }else if (items.get(45).getAmount() == 0 
                            && cam.getInputInGameWorld().x > 860 - (261 / 2) + (29 * 1)
                            && cam.getInputInGameWorld().x < 860 - (261 / 2) + (29 * 1) + 25
                            && cam.getInputInGameWorld().y > 363 - (88 / 2) + 2 + (0 * 29)
                            && cam.getInputInGameWorld().y < 363 - (88 / 2) + 2 + (0 * 29) + 27) {
                            items.get(45).setAmount(furnaceSlot2.getAmount());
                            items.get(45).setElement(furnaceSlot2.getElement());
                            items.get(45).setWeapon(furnaceSlot2.isWeapon());
                            items.get(45).setFood(furnaceSlot2.isFood());
                            items.get(45).setResource(furnaceSlot2.isResource());
                            items.get(45).setDamage(furnaceSlot2.getDamage());
                            items.get(45).setHealth(furnaceSlot2.getHealth());
                            furnaceSlot2.setAmount(0);
                            isGrabbed = true;
                        }
                    }
                    if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
                        && cam.getInputInGameWorld().x > 860 - (261 / 2) + (29 * 1)
                        && cam.getInputInGameWorld().x < 860 - (261 / 2) + (29 * 1) + 25
                        && cam.getInputInGameWorld().y > 363 - (88 / 2) + 2 + (0 * 29)
                        && cam.getInputInGameWorld().y < 363 - (88 / 2) + 2 + (0 * 29) + 27){
                        if(furnaceSlot2.getElement() == items.get(45).getElement() 
                            && furnaceSlot2.getAmount() + items.get(45).getAmount() <= 32){
                            furnaceSlot2.setAmount(furnaceSlot2.getAmount() + 1);
                            items.get(45).setAmount(items.get(45).getAmount() - 1);
                        } else if(furnaceSlot2.getAmount() == 0 && items.get(45).getAmount() > 0){
                            furnaceSlot2.setAmount(1);
                            furnaceSlot2.setElement(items.get(45).getElement());
                            furnaceSlot2.setWeapon(items.get(45).isWeapon());
                            furnaceSlot2.setFood(items.get(45).isFood());
                            furnaceSlot2.setResource(items.get(45).isResource());
                            furnaceSlot2.setDamage(items.get(45).getDamage());
                            furnaceSlot2.setHealth(items.get(45).getHealth());
                            items.get(45).setAmount(items.get(45).getAmount() - 1);
                            if (furnaceSlot1.getAmount() > 0 && furnaceSlot2.getAmount() > 0) {
                                mapArray[openFurnaceX][openFurnaceY].setFurnaceStartTimer(new Date().getTime());
                            }
                        }

                    }


                    if (items.get(45).getAmount() == 0 && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                            && cam.getInputInGameWorld().x > 860 - (261 / 2) + (29 * 4)
                            && cam.getInputInGameWorld().x < 860 - (261 / 2) + (29 * 4) + 25
                            && cam.getInputInGameWorld().y > 363 - (88 / 2) + 2 + (1 * 29)
                            && cam.getInputInGameWorld().y < 363 - (88 / 2) + 2 + (1 * 29) + 27) {
                        items.get(45).setAmount(furnaceSlot3.getAmount());
                        items.get(45).setElement(furnaceSlot3.getElement());
                        items.get(45).setWeapon(furnaceSlot3.isWeapon());
                        items.get(45).setFood(furnaceSlot3.isFood());
                        items.get(45).setResource(furnaceSlot3.isResource());
                        items.get(45).setDamage(furnaceSlot3.getDamage());
                        items.get(45).setHealth(furnaceSlot3.getHealth());
                        furnaceSlot3.setAmount(0);
                        isGrabbed = true;
                    }
                    if (furnaceSlot1.getAmount() > 0) {
                        CharSequence str = Integer.toString(furnaceSlot1.getAmount());
                        batch.draw(blockTextures[0][furnaceSlot1.getElement() - 1], 860 - (261 / 2) + (29 * 1),
                                363 - (88 / 2) + 2 + (2 * 29));
                        if (!furnaceSlot1.isWeapon()) {
                            font.draw(batch, str, 860 - (261 / 2) + (29 * 1),
                                    363 - (88 / 2) + 2 + (2 * 29) + 15);
                        }
                    }
                    if (furnaceSlot2.getAmount() > 0) {
                        CharSequence str = Integer.toString(furnaceSlot2.getAmount());
                        batch.draw(blockTextures[0][furnaceSlot2.getElement() - 1], 860 - (261 / 2) + (29 * 1),
                                363 - (88 / 2) + 2 + (0 * 29));
                        if (!furnaceSlot2.isWeapon()) {
                            font.draw(batch, str, 860 - (261 / 2) + (29 * 1),
                                    363 - (88 / 2) + 2 + (0 * 29) + 15);
                        }
                    }
                    if (furnaceSlot3.getAmount() > 0) {
                        CharSequence str = Integer.toString(furnaceSlot3.getAmount());
                        batch.draw(blockTextures[0][furnaceSlot3.getElement() - 1], 860 - (261 / 2) + (29 * 4),
                                363 - (88 / 2) + 2 + (1 * 29));
                        if (!furnaceSlot3.isWeapon()) {
                            font.draw(batch, str, 860 - (261 / 2) + (29 * 4),
                                    363 - (88 / 2) + 2 + (1 * 29) + 15);
                        }
                    }

                    mapArray[openFurnaceX][openFurnaceY].checkFurnace();
                }

            }

            // draw grabbed item
            if (items.get(45).getAmount() > 0) {
                CharSequence str = Integer.toString(items.get(45).getAmount());
                batch.draw(blockTextures[0][items.get(45).getElement() - 1], cam.getInputInGameWorld().x - 12,
                        cam.getInputInGameWorld().y - 12);
                if (!items.get(45).isWeapon()) {
                    font.draw(batch, str, cam.getInputInGameWorld().x - 12, cam.getInputInGameWorld().y + 1);
                }

            }
            if (!isGrabbed && items.get(45).getAmount() > 0) {

                Item item = new Item();

                item.setY(player.getY() + player.getPlayerSizeY());
                if (cam.getInputInGameWorld().x > 800) {
                    item.setAcceleration(5);
                    item.setX(player.getX() + 60);
                } else {
                    item.setAcceleration(-5);
                    item.setX(player.getX() - 60);
                }
                item.setAmount(items.get(45).getAmount());
                item.setElement(items.get(45).getElement());
                item.setDamage(items.get(45).getDamage());
                item.setFood(items.get(45).isFood());
                item.setResource(items.get(45).isResource());
                item.setWeapon(items.get(45).isWeapon());
                item.setHealth(items.get(45).getHealth());
                player.addDroppedItem(item);
                items.get(45).setAmount(0);
            }

            //Update list of discovered items for achievements
            int discoveredItem = 0;
            for (int i = 0; i < 36; i++){
                discoveredItem = items.get(i).getElement();
                if (!(discoveredItems.contains(discoveredItem))){

                    //System.out.println("New Item discovered " + discoveredItem);
                    discoveredItems.add(discoveredItem);
                    //System.out.println(discoveredItems);
                    int obtainedItem = discoveredItem;
                    switch(obtainedItem){
                        case 3:
                            achievements.unlockAchievement("TIMBER");
                            message.setMessage("New achievement unlocked: TIMBER");
                            recipebook.setUnlocked(true, 0);
                            break;
                        case 6:
                            achievements.unlockAchievement("Rock solid");
                            message.setMessage("New achievement unlocked: Rock solid");
                            recipebook.setUnlocked(true, 1);
                            break;
                        case 15:
                            achievements.unlockAchievement("Toy or tool?");
                            message.setMessage("New achievement unlocked: Toy or tool?");
                            break;
                        case 16:
                            achievements.unlockAchievement("Stone age");
                            message.setMessage("New achievement unlocked: Stone age");
                            break;
                        case 23:
                            achievements.unlockAchievement("Ironworks");
                            message.setMessage("New achievement unlocked: Ironworks");
                            recipebook.setUnlocked(true, 2);
                            break;
                        case 28:
                            achievements.unlockAchievement("Let there be light!");
                            message.setMessage("New achievement unlocked: Let there be light!");
                            break;
                        case 57:
                            achievements.unlockAchievement("What is this sorcery?");
                            message.setMessage("New achievement unlocked: What is this sorcery?");
                            recipebook.setUnlocked(true, 3);
                            break;
                        case 56:
                            achievements.unlockAchievement("To the moon!");
                            message.setMessage("New achievement unlocked: To the moon!");
                            break;
                        case 65:
                            achievements.unlockAchievement("A magical stick?");
                            message.setMessage("New achievement unlocked: A magical stick?");
                            break;
                    }
                 
                }
            }
            
            if (Gdx.input.isKeyJustPressed(Input.Keys.Q) && items.get(selectedSlot).getAmount() > 0 && !player.isPaused()) {

                Item item = new Item();
        
                item.setY(player.getY() + player.getPlayerSizeY());
                if (cam.getInputInGameWorld().x > 800) {
                    item.setAcceleration(5);
                    item.setX(player.getX() + 60);
                } else {
                    item.setAcceleration(-5);
                    item.setX(player.getX() - 60);
                }
                item.setAmount(items.get(selectedSlot).getAmount());
                item.setElement(items.get(selectedSlot).getElement());
                item.setDamage(items.get(selectedSlot).getDamage());
                item.setFood(items.get(selectedSlot).isFood());
                item.setResource(items.get(selectedSlot).isResource());
                item.setWeapon(items.get(selectedSlot).isWeapon());
                item.setHealth(items.get(selectedSlot).getHealth());
                player.addDroppedItem(item);
                items.get(selectedSlot).setAmount(0);
            }

            if (items.get(selectedSlot).getElement() == 65){
                int x = 2500 + ((int) player.getX() / 25);
                int y = 150 - ((int) player.getY() / 25);
                for (int i = 0; i < 40; i++){
                    if (mapArray[x][y + i].getElement() == 58){
                        message.setMessage("You sense oil underground");
                    }
                }
            }
        }
    }
    
    public void unlockRecipeBook(Recipebook recipebook, int discoveredItem){
        int obtainedItem = discoveredItem;
        switch(obtainedItem){
            case 3:
                achievements.unlockAchievement("TIMBER");
                recipebook.setUnlocked(true, 0);
                break;
            case 6:
                achievements.unlockAchievement("Rock solid");
                recipebook.setUnlocked(true, 1);
                break;
            case 15:
                achievements.unlockAchievement("Toy or tool?");
                break;
            case 16:
                achievements.unlockAchievement("Stone age");
                break;
            case 8:
                achievements.unlockAchievement("Ironworks");
                recipebook.setUnlocked(true, 2);
                break;
            case 28:
                achievements.unlockAchievement("Let there be light!");
                break;
            case 57:
                achievements.unlockAchievement("What is this sorcery?");
                recipebook.setUnlocked(true, 3);
                break;
            case 56:
                achievements.unlockAchievement("To the moon!");
                break;
            case 65:
                achievements.unlockAchievement("A magical stick?");
                break;
        }
    }

    public boolean addItem(Item item) {
        for (int i = 0; i < 36; i++) {
            int slotIndex = -1;
            for (int o = 0; o < 36; o++) {
                if (!item.isWeapon() && item.getElement() == items.get(o).getElement()
                        && item.getAmount() + items.get(o).getAmount() <= INVENTORY_SLOT_MAX) {
                    slotIndex = o;
                    break;
                }
            }
            if (slotIndex > 0) {
                items.get(slotIndex).setAmount(items.get(slotIndex).getAmount() + item.getAmount());
                return true;
            } else {
                if (!item.isWeapon() && items.get(i).getElement() == item.getElement()
                        && items.get(i).getAmount() + item.getAmount() <= INVENTORY_SLOT_MAX) {
                    items.get(i).setAmount(items.get(i).getAmount() + item.getAmount());
                    items.get(i).setWeapon(item.isWeapon());
                    items.get(i).setFood(item.isFood());
                    items.get(i).setResource(item.isResource());
                    items.get(i).setDamage(item.getDamage());
                    items.get(i).setHealth(item.getHealth());
                    return true;
                } else if (items.get(i).getAmount() == 0) {
                    items.get(i).setAmount(item.getAmount());
                    items.get(i).setElement(item.getElement());
                    items.get(i).setWeapon(item.isWeapon());
                    items.get(i).setFood(item.isFood());
                    items.get(i).setResource(item.isResource());
                    items.get(i).setDamage(item.getDamage());
                    items.get(i).setHealth(item.getHealth());
                    return true;
                }
            }

        }
        return false;
    }

    public int getGrab() {
        return grab;
    }

    public void setGrab(int grab) {
        this.grab = grab;
    }

    public boolean isGrabbed() {
        return isGrabbed;
    }

    public void setGrabbed(boolean isGrabbed) {
        this.isGrabbed = isGrabbed;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public boolean isInventoryOpen() {
        return isInventoryOpen;
    }

    public void setInventoryOpen(boolean isInventoryOpen) {
        this.isInventoryOpen = isInventoryOpen;
    }

    public Item getItemByIndex(int index) {
        return items.get(index);
    }

    public Item getSelectedItem() {
        return items.get(selectedSlot);
    }

    public int getOpenFurnaceX() {
        return openFurnaceX;
    }

    public void setOpenFurnaceX(int openFurnaceX) {
        this.openFurnaceX = openFurnaceX;
    }

    public int getOpenFurnaceY() {
        return openFurnaceY;
    }

    public void setOpenFurnaceY(int openFurnaceY) {
        this.openFurnaceY = openFurnaceY;
    }

    public boolean isFurnaceOpen() {
        return isFurnaceOpen;
    }

    public void setFurnaceOpen(boolean isFurnaceOpen) {
        this.isFurnaceOpen = isFurnaceOpen;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public int getINVENTORY_SLOT_MAX() {
        return INVENTORY_SLOT_MAX;
    }

    public void reset() {
        items.clear();
        for (int i = 0; i < 46; i++) {
            items.add(new Item());
        }
        discoveredItems.clear();
        for (Achievement a: achievements.getAchievements2()){
            a.setUnlocked(false);
        }
    }

    public int getHover(HudCamera cam){
        if (items.get(45).getAmount() > 0){
            return items.get(45).getElement();
        }

        for (int i = 0; i < 9; i++) {
                if (items.get(i).getElement() != 0 && cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * i)
                        && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * i)) + 28
                        && cam.getInputInGameWorld().y >= 0 && cam.getInputInGameWorld().y < 28) {
                        return items.get(i).getElement();
                } 
        }
        if(isInventoryOpen){
            int invDrawColumn = 0;
            int invDrawRow = 0;
            for (int i = 9; i < 36; i++) {
                if (items.get(i).getElement() != 0 && cam.getInputInGameWorld().x >= 800 - (261 / 2) + (29 * invDrawRow)
                && cam.getInputInGameWorld().x <= (800 - (261 / 2) + (29 * invDrawRow)) + 28
                && cam.getInputInGameWorld().y >= 254 - (90 / 2) + (invDrawColumn * 29)
                && cam.getInputInGameWorld().y <= 254 - (90 / 2) + 28 + (invDrawColumn * 29)) {
                        return items.get(i).getElement();
                } 
                invDrawRow++;
                if (invDrawRow % 9 == 0) {
                    invDrawColumn++;
                    invDrawRow = 0;
                }
            }
            invDrawColumn = 0;
            invDrawRow = 0;
            for (int i = 36; i < 45; i++) {
                if (items.get(i).getElement() != 0 && cam.getInputInGameWorld().x >= 858 - (261 / 2) + (29 * invDrawRow)
                && cam.getInputInGameWorld().x <= (858 - (261 / 2) + (29 * invDrawRow)) + 28
                && cam.getInputInGameWorld().y >= 365 - (90 / 2) + (invDrawColumn * 29)
                && cam.getInputInGameWorld().y <= 365 - (90 / 2) + 29 + (invDrawColumn * 29)) {
                        return items.get(i).getElement();
                } 
                invDrawRow++;
                if (invDrawRow % 9 == 0) {
                    invDrawColumn++;
                    invDrawRow = 0;
                }
            }
    
        }



        return -1;
    }

    public AchievementManager getAchievementManager(){
        return achievements;
    }
    public ArrayList<Integer> getDiscoveredItems(){
        return discoveredItems;
    }
    public void setDiscoveredItems(ArrayList<Integer> discoveredItems){
        this.discoveredItems = discoveredItems;
    }

}
