package com.mygdx.game;

import java.util.List;
import static com.mygdx.game.map.elements.*;

public class Crafting {

    public static InventorySlot craft(List<InventorySlot> resources) {
        InventorySlot result = null;

        // LADDER
        result = craftLadder(resources);
        if (result != null) {
            return result;
        }

        // GRASS BLOCK
        result = craftGrassBlock(resources);
        if (result != null) {
            return result;
        }

        // PLANKS
        result = craftPlank(resources);
        if (result != null) {
            return result;
        }

        return null;
    }

    private static InventorySlot craftLadder(List<InventorySlot> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == PLANKS
                && resources.get(1).getElement() == EMPTY
                && resources.get(2).getElement() == PLANKS
                && resources.get(3).getElement() == PLANKS
                && resources.get(4).getElement() == PLANKS
                && resources.get(5).getElement() == PLANKS
                && resources.get(6).getElement() == PLANKS
                && resources.get(7).getElement() == EMPTY
                && resources.get(8).getElement() == PLANKS) {
            int amount = 32;
            for (InventorySlot slot : resources) {
                if (slot.getElement() == PLANKS && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }
            // check for max slot size
            if (amount > 8) {
                amount = 8;
            }
            amount *= 4;
            // create new item/s
            InventorySlot item = new InventorySlot();
            item.setElement(LADDER);
            item.setAmount(amount);
            item.setRemoveAmount(amount / 4);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static InventorySlot craftGrassBlock(List<InventorySlot> resources) {
        int isGround = 0;
        int isTallGrass = 0;
        // check slots for correct resources
        for (InventorySlot slot : resources) {
            if (slot.getElement() == GROUND) {
                isGround++;
            }
            if (slot.getElement() == TALLGRASS) {
                isTallGrass++;
            }
            // return null if slots have something else than ground or tall grass
            if (slot.getElement() != GROUND && slot.getElement() != TALLGRASS && slot.getElement() != EMPTY) {
                return null;
            }
        }

        if (isGround == 1 && isTallGrass == 1) {
            int amount = 32;
            for (InventorySlot slot : resources) {
                if ((slot.getElement() == GROUND || slot.getElement() == TALLGRASS) && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }
            // create new item/s
            InventorySlot item = new InventorySlot();
            item.setElement(GRASS);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static InventorySlot craftPlank(List<InventorySlot> resources) {
        int isWood = 0;
        // check slots for correct resources
        for (InventorySlot slot : resources) {
            if (slot.getElement() == WOOD) {
                isWood++;
            }
            if (slot.getElement() != WOOD && slot.getElement() != EMPTY) {
                return null;
            }
        }

        if (isWood == 1) {
            int amount = 32;
            for (InventorySlot slot : resources) {
                if ((slot.getElement() == WOOD) && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }
            // check for max slot size
            if (amount > 8) {
                amount = 8;
            }
            amount *= 4;
            // create new item/s
            InventorySlot item = new InventorySlot();
            item.setElement(PLANKS);
            item.setAmount(amount);
            item.setRemoveAmount(amount / 4);
            return item;
        }
        // return null if resources are not correct
        return null;
    }
}
