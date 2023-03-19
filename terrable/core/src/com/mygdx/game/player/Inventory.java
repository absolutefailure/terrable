package com.mygdx.game.player;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.camera.HudCamera;

public class Inventory {
    private int grab = -1;
    private boolean isGrabbed;
    private int selectedSlot = 0;
    private boolean isInventoryOpen = false;
    private ArrayList<Integer> usedSlots = new ArrayList<>();
    BitmapFont font = new BitmapFont();
    private ArrayList<Item> items;

    private final int INVENTORY_SLOT_MAX = 32;

    private Texture hotbarTexture;
    private Texture inventoryTexture;

    public Inventory(){

        hotbarTexture = new Texture("hotbar.png");
        inventoryTexture = new Texture("crafting.png");

        items = new ArrayList<>();
        for (int i = 0; i < 46; i++) {
            items.add(new Item());
        }
    }

    public void Update(Batch batch, Player player, TextureRegion[][] blockTextures, HudCamera cam, Texture outlineTexture){
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
                        }else if (items.get(i).getAmount() == 0) {
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

        for (int i = 0; i < 9; i++) {
            CharSequence str = Integer.toString(items.get(i).getAmount());
            if (items.get(i).getAmount() > 0 && grab != i) {
                batch.draw(blockTextures[0][items.get(i).getElement() - 1], 802 - (261 / 2) + (29 * i), 2);
                if (!items.get(i).isWeapon()){
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
                    if (!items.get(grab).isWeapon() && items.get(grab).getElement() == items.get(i).getElement()
                            && items.get(i).getAmount() + items.get(grab).getAmount() <= INVENTORY_SLOT_MAX) {
                                items.get(i).setAmount(items.get(i).getAmount() + items.get(grab).getAmount());
                                items.get(grab).setAmount(0);
                                items.get(grab).setElement(0);
                        grab = -1;
                    } else {
                        Item reserveSlot = items.get(grab);
                        Item reserveSlot2 = items.get(i);
                        items.set(i, reserveSlot);
                        items.set(grab, reserveSlot2);
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
                CharSequence str = Integer.toString(items.get(i).getAmount());
                if (items.get(i).getAmount() > 0 && grab != i) {
                    batch.draw(blockTextures[0][items.get(i).getElement() - 1], 802 - (261 / 2) + (29 * invDrawRow),250 - (88 / 2) + 2 + (invDrawColumn * 29));
                    if (!items.get(i).isWeapon()){
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

                        if (!items.get(grab).isWeapon() && items.get(grab).getElement() == items.get(i).getElement()
                                && items.get(i).getAmount()
                                        + items.get(grab).getAmount() <= INVENTORY_SLOT_MAX) {
                                items.get(i).setAmount(items.get(i).getAmount() + items.get(grab).getAmount());
                                items.get(grab).setAmount(0);
                                items.get(grab).setElement(0);
                            grab = -1;
                        } else {
                            Item reserveSlot = items.get(grab);
                            Item reserveSlot2 = items.get(i);
                            items.set(i, reserveSlot);
                            items.set(grab, reserveSlot2);
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
                CharSequence str = Integer.toString(items.get(i).getAmount());
                if (items.get(i).getAmount() > 0 && grab != i) {
                    batch.draw(blockTextures[0][items.get(i).getElement() - 1], 860 - (261 / 2) + (29 * invDrawRow),
                            363 - (88 / 2) + 2 + (invDrawColumn * 29));
                    if (!items.get(i).isWeapon()){
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

                        if (!items.get(grab).isWeapon() && items.get(grab).getElement() == items.get(i).getElement()
                                && items.get(i).getAmount()
                                        + items.get(grab).getAmount() <= INVENTORY_SLOT_MAX) {
                            items.get(i).setAmount(items.get(i).getAmount() + items.get(grab).getAmount());
                            items.get(grab).setAmount(0);
                            items.get(grab).setElement(0);
                            grab = -1;
                        } else {
                            Item reserveSlot = items.get(grab);
                            Item reserveSlot2 = items.get(i);
                            items.set(i, reserveSlot);
                            items.set(grab, reserveSlot2);
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
                        if (!items.get(grab).isWeapon() && items.get(grab).getElement() == items.get(i).getElement()
                                && items.get(i).getAmount() + putAmount <= INVENTORY_SLOT_MAX) {
                                    items.get(i).setAmount(items.get(i).getAmount() + putAmount);
                                    items.get(grab).removeItem();
                            usedSlots.add(i);
                            if (items.get(grab).getAmount() <= 0) {
                                items.get(grab).setElement(0);
                                grab = -1;
                            }
                        } else if (items.get(i).getAmount() == 0
                                && items.get(i).getAmount() + putAmount <= INVENTORY_SLOT_MAX) {
                            items.get(i).setElement(items.get(grab).getElement());
                            items.get(i).setDamage(items.get(grab).getDamage());
                            items.get(i).setHealth(items.get(grab).getHealth());
                            items.get(i).setResource(items.get(grab).isResource());
                            items.get(i).setFood(items.get(grab).isFood());
                            items.get(i).setWeapon(items.get(grab).isWeapon());
                            items.get(i).setAmount(items.get(i).getAmount() + putAmount);
                            items.get(grab).removeItem();
                            usedSlots.add(i);
                            if (items.get(grab).getAmount() <= 0) {
                                items.get(grab).setElement(0);
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
            Item newItem = Crafting.craft(items.subList(36, 45));
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
                }
            }

        }

        // draw grabbed item
        if (grab > -1 && items.get(grab).getAmount() > 0) {
            CharSequence str = Integer.toString(items.get(grab).getAmount());
            batch.draw(blockTextures[0][items.get(grab).getElement() - 1], cam.getInputInGameWorld().x - 12,
                    cam.getInputInGameWorld().y - 12);
            if (!items.get(grab).isWeapon()){
                font.draw(batch, str, cam.getInputInGameWorld().x - 12, cam.getInputInGameWorld().y + 1);
            }
            
        }
        if(!isGrabbed && grab > -1){

            Item item = new Item();

            item.setY(player.getY()+player.getPlayerSizeY());
            if (cam.getInputInGameWorld().x > 800){
                item.setAcceleration(5);
                item.setX(player.getX()+60);
            }else{
                item.setAcceleration(-5);
                item.setX(player.getX()-60);
            }
            item.setAmount(items.get(grab).getAmount());
            item.setElement(items.get(grab).getElement());
            item.setDamage(items.get(grab).getDamage());
            item.setFood(items.get(grab).isFood());
            item.setResource(items.get(grab).isResource());
            item.setWeapon(items.get(grab).isWeapon());
            item.setHealth(items.get(grab).getHealth());
            player.addDroppedItem(item);
            items.get(grab).setAmount(0);
            grab = -1;
        }else if (!isGrabbed) {
            grab = -1;
        }
    }

    public void addItem(Item item){
        for (int i = 0; i < 36; i++){
            int slotIndex = -1;
            for (int o = 0; o < 36; o++) {
                if (item.getElement() == items.get(o).getElement()
                        && item.getAmount() + items.get(o).getAmount() <= INVENTORY_SLOT_MAX) {
                    slotIndex = o;
                    break;
                }
            }
            if (slotIndex > 0) {
                items.get(slotIndex).setAmount(items.get(slotIndex).getAmount() + item.getAmount());
                break;
            } else {
                if (!item.isWeapon() && items.get(i).getElement() == item.getElement()
                        && items.get(i).getAmount() + item.getAmount() <= INVENTORY_SLOT_MAX) {
                    items.get(i).setAmount(items.get(i).getAmount() + item.getAmount());
                    items.get(i).setWeapon(item.isWeapon());
                    items.get(i).setFood(item.isFood());
                    items.get(i).setResource(item.isResource());
                    items.get(i).setDamage(item.getDamage());
                    items.get(i).setHealth(item.getHealth());
                    break;
                }else if (items.get(i).getAmount() == 0) {
                    items.get(i).setAmount(item.getAmount());
                    items.get(i).setElement(item.getElement());
                    items.get(i).setWeapon(item.isWeapon());
                    items.get(i).setFood(item.isFood());
                    items.get(i).setResource(item.isResource());
                    items.get(i).setDamage(item.getDamage());
                    items.get(i).setHealth(item.getHealth());
                    break;
                } 
            }
            
        }
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

    public Item getItemByIndex(int index){
        return items.get(index);
    }

    public Item getSelectedItem(){
        return items.get(selectedSlot);
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

    public void reset(){
        items.clear();
        for (int i = 0; i < 46; i++) {
            items.add(new Item());
        }
    }

}
