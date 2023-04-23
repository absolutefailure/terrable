package com.mygdx.game.player;

import java.util.List;
import static com.mygdx.game.map.Element.*;

public class Crafting {

    public static Item craft(List<Item> resources) {
        Item result = null;

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

        //STONE PICKAXE
        result = craftStonePickaxe(resources);
        if (result != null) {
            return result;
        }

        //DOOR
        result = craftDoor(resources);
        if (result != null) {
            return result;
        }

        //WOOD PICKAXE
        result = craftWoodPickaxe(resources);
        if (result != null) {
            return result;
        }

        //IRON PICKAXE
        result = craftIronPickaxe(resources);
        if (result != null) {
            return result;
        }

        //DIAMOND PICKAXE
        result = craftDiamondPickaxe(resources);
        if (result != null) {
            return result;
        }

        
        //STONE AXE
        result = craftStoneAxe(resources);
        if (result != null) {
            return result;
        }

        //WOOD AXE
        result = craftWoodAxe(resources);
        if (result != null) {
            return result;
        }

        //IRON AXE
        result = craftIronAxe(resources);
        if (result != null) {
            return result;
        }

        //DIAMOND AXE
        result = craftDiamondAxe(resources);
        if (result != null) {
            return result;
        }

        //IRON INGOT (VERY TEMPORARY BEROFE SMELTING MECHANIC)
/*         result = craftIronIngot(resources);
        if (result != null) {
            return result;
        } */

        //STICK
        result = craftStick(resources);
        if (result != null) {
            return result;
        }

        //TORCH
        result = craftTorch(resources);
        if (result != null) {
            return result;
        }

        //FURNACE
        result = craftFurnace(resources);
        if (result != null) {
            return result;
        }

        //IRON PLATE
        result = craftIronPlate(resources);
        if (result != null) {
            return result;
        }

        //LEFT WING
        result = craftLWing(resources);
        if (result != null) {
            return result;
        }

        //RIGHT WING
        result = craftRWing(resources);
        if (result != null) {
            return result;
        }

        //SPACE SHIP BODY
        result = craftSpaceShipBody(resources);
        if (result != null) {
            return result;
        }

        //COCKPIT
        result = craftCockpit(resources);
        if (result != null) {
            return result;
        }

        //ENGINE
        result = craftEngine(resources);
        if (result != null) {
            return result;
        }

        //ROCKET
        result = craftRocket(resources);
        if (result != null) {
            return result;
        }

        //BUCKET
        result = craftBucket(resources);
        if (result != null) {
            return result;
        }

        //COPPER WIRE
        result = craftCopperWire(resources);
        if (result != null) {
            return result;
        }

        //SLIME PUDDING
        result = craftSlimePudding(resources);
        if (result != null) {
            return result;
        }

        //DOWSING ROD
        result = craftDowsingRod(resources);
        if (result != null) {
            return result;
        }

        return null;
    }

    private static Item craftLadder(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == STICK
                && resources.get(1).getElement() == EMPTY
                && resources.get(2).getElement() == STICK
                && resources.get(3).getElement() == STICK
                && resources.get(4).getElement() == STICK
                && resources.get(5).getElement() == STICK
                && resources.get(6).getElement() == STICK
                && resources.get(7).getElement() == EMPTY
                && resources.get(8).getElement() == STICK) {
            int amount = 32;
            for (Item slot : resources) {
                if (slot.getElement() == STICK && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }
            // check for max slot size
            if (amount > 8) {
                amount = 8;
            }
            amount *= 4;
            // create new item/s
            Item item = new Item();
            item.setElement(LADDER);
            item.setAmount(amount);
            item.setRemoveAmount(amount / 4);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftGrassBlock(List<Item> resources) {
        int isGround = 0;
        int isTallGrass = 0;
        // check slots for correct resources
        for (Item slot : resources) {
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
            for (Item slot : resources) {
                if ((slot.getElement() == GROUND || slot.getElement() == TALLGRASS) && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }
            // create new item/s
            Item item = new Item();
            item.setElement(GRASS);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftPlank(List<Item> resources) {
        int isWood = 0;
        // check slots for correct resources
        for (Item slot : resources) {
            if (slot.getElement() == WOOD) {
                isWood++;
            }
            if (slot.getElement() != WOOD && slot.getElement() != EMPTY) {
                return null;
            }
        }

        if (isWood == 1) {
            int amount = 32;
            for (Item slot : resources) {
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
            Item item = new Item();
            item.setElement(PLANKS);
            item.setAmount(amount);
            item.setRemoveAmount(amount / 4);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftStonePickaxe(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == STICK
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == STICK
                && resources.get(5).getElement() == EMPTY
                && resources.get(6).getElement() == STONE
                && resources.get(7).getElement() == STONE
                && resources.get(8).getElement() == STONE) {
            int amount = 1;
           
            for (Item slot : resources) {
                    if ((slot.getElement() == STICK || slot.getElement() == STONE) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            Item item = new Item();
            item.setElement(STONEPICKAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(25);
            item.setHealth(50);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftWoodPickaxe(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == STICK
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == STICK
                && resources.get(5).getElement() == EMPTY
                && resources.get(6).getElement() == WOOD
                && resources.get(7).getElement() == WOOD
                && resources.get(8).getElement() == WOOD) {
            int amount = 1;
           
            for (Item slot : resources) {
                    if ((slot.getElement() == STICK || slot.getElement() == WOOD) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            Item item = new Item();
            item.setElement(WOODPICKAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(10);
            item.setHealth(25);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftIronPickaxe(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == STICK
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == STICK
                && resources.get(5).getElement() == EMPTY
                && resources.get(6).getElement() == IRONINGOT
                && resources.get(7).getElement() == IRONINGOT
                && resources.get(8).getElement() == IRONINGOT) {
            int amount = 1;
           
            for (Item slot : resources) {
                    if ((slot.getElement() == STICK || slot.getElement() == IRONINGOT) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            Item item = new Item();
            item.setElement(IRONPICKAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(50);
            item.setHealth(150);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftDiamondPickaxe(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == STICK
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == STICK
                && resources.get(5).getElement() == EMPTY
                && resources.get(6).getElement() == DIAMONDITEM
                && resources.get(7).getElement() == DIAMONDITEM
                && resources.get(8).getElement() == DIAMONDITEM) {
            int amount = 1;
           
            for (Item slot : resources) {
                    if ((slot.getElement() == STICK || slot.getElement() == DIAMONDITEM) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            Item item = new Item();
            item.setElement(DIAMONDPICKAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(75);
            item.setHealth(300);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftStoneAxe(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == STICK
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == STICK
                && resources.get(5).getElement() == STONE
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == STONE
                && resources.get(8).getElement() == STONE) {
            int amount = 1;
           
            for (Item slot : resources) {
                    if ((slot.getElement() == STICK || slot.getElement() == STONE) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            Item item = new Item();
            item.setElement(STONEAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(25);
            item.setHealth(50);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftWoodAxe(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == STICK
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == STICK
                && resources.get(5).getElement() == WOOD
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == WOOD
                && resources.get(8).getElement() == WOOD) {
            int amount = 1;
           
            for (Item slot : resources) {
                    if ((slot.getElement() == STICK || slot.getElement() == WOOD) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            Item item = new Item();
            item.setElement(WOODAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(10);
            item.setHealth(25);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftIronAxe(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == STICK
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == STICK
                && resources.get(5).getElement() == IRONINGOT
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == IRONINGOT
                && resources.get(8).getElement() == IRONINGOT) {
            int amount = 1;
           
            for (Item slot : resources) {
                    if ((slot.getElement() == STICK || slot.getElement() == IRONINGOT) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            Item item = new Item();
            item.setElement(IRONAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(50);
            item.setHealth(150);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftDiamondAxe(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == STICK
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == STICK
                && resources.get(5).getElement() == DIAMONDITEM
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == DIAMONDITEM
                && resources.get(8).getElement() == DIAMONDITEM) {
            int amount = 1;
           
            for (Item slot : resources) {
                    if ((slot.getElement() == STICK || slot.getElement() == DIAMONDITEM) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            Item item = new Item();
            item.setElement(DIAMONDAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(75);
            item.setHealth(300);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

/*     private static Item craftIronIngot(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == IRON
                && resources.get(1).getElement() == IRON
                && resources.get(2).getElement() == IRON
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == EMPTY
                && resources.get(5).getElement() == EMPTY
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == EMPTY
                && resources.get(8).getElement() == EMPTY) {
            int amount = 32;
           
            for (Item slot : resources) {
                    if ((slot.getElement() == IRON) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            Item item = new Item();
            item.setElement(IRONINGOT);
            item.setAmount(amount);
            item.setResource(true);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    } */

    private static Item craftDoor(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == PLANKS
                && resources.get(1).getElement() == PLANKS
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == PLANKS
                && resources.get(4).getElement() == PLANKS
                && resources.get(5).getElement() == EMPTY
                && resources.get(6).getElement() == PLANKS
                && resources.get(7).getElement() == PLANKS
                && resources.get(8).getElement() == EMPTY) {
            int amount = 32;
            for (Item slot : resources) {
                if (slot.getElement() == PLANKS && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }
            // check for max slot size
            if (amount > 8) {
                amount = 8;
            }
            amount *= 1;
            // create new item/s
            Item item = new Item();
            item.setElement(DOOR);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftStick(List<Item> resources) {
        int isPlanks = 0;
        // check slots for correct resources
        for (Item slot : resources) {
            if (slot.getElement() == PLANKS) {
                isPlanks++;
            }
            if (slot.getElement() != PLANKS && slot.getElement() != EMPTY) {
                return null;
            }
        }

        if (isPlanks == 1) {
            int amount = 32;
            for (Item slot : resources) {
                if ((slot.getElement() == PLANKS) && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }
            // check for max slot size
            if (amount > 8) {
                amount = 8;
            }
            amount *= 4;
            // create new item/s
            Item item = new Item();
            item.setElement(STICK);
            item.setAmount(amount);
            item.setResource(true);
            item.setRemoveAmount(amount / 4);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftTorch(List<Item> resources) {
        int stickAmount = 0;
        int coalItemAmount = 0;
        // check slots for correct resources
        if ((resources.get(0).getElement() == STICK
            && resources.get(3).getElement() == COALITEM) ||
            (resources.get(1).getElement() == STICK
            && resources.get(4).getElement() == COALITEM) ||
            (resources.get(2).getElement() == STICK
            && resources.get(5).getElement() == COALITEM) ||
            (resources.get(3).getElement() == STICK
            && resources.get(6).getElement() == COALITEM) ||
            (resources.get(4).getElement() == STICK
            && resources.get(7).getElement() == COALITEM) ||
            (resources.get(5).getElement() == STICK
            && resources.get(8).getElement() == COALITEM) 
               ) {
            int amount = 32;
            for(Item slot: resources){
                // return null if slots have something else than ground or tall grass
                if (slot.getElement() != STICK && slot.getElement() != COALITEM && slot.getElement() != EMPTY) {
                    return null;
                }
                if(slot.getElement() == STICK){
                    stickAmount++;
                    if(stickAmount > 1){
                        return null;
                    }
                }
                if(slot.getElement() == COALITEM){
                    coalItemAmount++;
                    if(coalItemAmount > 1){
                        return null;
                    }
                }
            }

            for (Item slot : resources) {
                    if ((slot.getElement() == STICK || slot.getElement() == COALITEM) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }
            // check for max slot size
            if (amount > 8) {
            amount = 8;
            }
            amount *= 4;
            // create new item/s
            Item item = new Item();
            item.setElement(TORCH);
            item.setAmount(amount);
            item.setRemoveAmount(amount / 4);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftFurnace(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == STONE
                && resources.get(1).getElement() == STONE
                && resources.get(2).getElement() == STONE
                && resources.get(3).getElement() == STONE
                && resources.get(4).getElement() == EMPTY
                && resources.get(5).getElement() == STONE
                && resources.get(6).getElement() == STONE
                && resources.get(7).getElement() == STONE
                && resources.get(8).getElement() == STONE) {
            int amount = 32;
            for (Item slot : resources) {
                if (slot.getElement() == STONE && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }
            // create new item/s
            Item item = new Item();
            item.setElement(FURNACE);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftIronPlate(List<Item> resources) {
        int ironIngotAmount = 0;
        // check slots for correct resources
        if ((resources.get(0).getElement() == IRONINGOT
            && resources.get(1).getElement() == IRONINGOT
            && resources.get(3).getElement() == IRONINGOT
            && resources.get(4).getElement() == IRONINGOT) ||
            (resources.get(1).getElement() == IRONINGOT
            && resources.get(2).getElement() == IRONINGOT
            && resources.get(4).getElement() == IRONINGOT
            && resources.get(5).getElement() == IRONINGOT) ||
            (resources.get(3).getElement() == IRONINGOT
            && resources.get(4).getElement() == IRONINGOT
            && resources.get(6).getElement() == IRONINGOT
            && resources.get(7).getElement() == IRONINGOT)||
            (resources.get(4).getElement() == IRONINGOT
            && resources.get(5).getElement() == IRONINGOT
            && resources.get(7).getElement() == IRONINGOT
            && resources.get(8).getElement() == IRONINGOT)
            ) {
            int amount = 32;
            for(Item slot: resources){
                // return null if slots have something else than ground or tall grass
                if (slot.getElement() != IRONINGOT && slot.getElement() != EMPTY) {
                    return null;
                }
                if(slot.getElement() == IRONINGOT){
                    ironIngotAmount++;
                    if(ironIngotAmount > 4){
                        return null;
                    }
                }
            }

            for (Item slot : resources) {
                    if (slot.getElement() == IRONINGOT  && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }
            // check for max slot size
            if (amount > 8) {
            amount = 8;
            }
            amount *= 4;
            // create new item/s
            Item item = new Item();
            item.setElement(IRONPLATE);
            item.setResource(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount / 4);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftLWing(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == IRONPLATE
                && resources.get(1).getElement() == IRONPLATE
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == IRONPLATE
                && resources.get(5).getElement() == IRONPLATE
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == EMPTY
                && resources.get(8).getElement() == EMPTY) {
            int amount = 32;
            for (Item slot : resources) {
                if (slot.getElement() == IRONPLATE && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }
            // create new item/s
            Item item = new Item();
            item.setElement(LWING);
            item.setResource(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftRWing(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == IRONPLATE
                && resources.get(2).getElement() == IRONPLATE
                && resources.get(3).getElement() == IRONPLATE
                && resources.get(4).getElement() == IRONPLATE
                && resources.get(5).getElement() == EMPTY
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == EMPTY
                && resources.get(8).getElement() == EMPTY) {
            int amount = 32;
            for (Item slot : resources) {
                if (slot.getElement() == IRONPLATE && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }
            // create new item/s
            Item item = new Item();
            item.setElement(RWING);
            item.setResource(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftSpaceShipBody(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == IRONPLATE
                && resources.get(1).getElement() == IRONPLATE
                && resources.get(2).getElement() == IRONPLATE
                && resources.get(3).getElement() == IRONPLATE
                && resources.get(4).getElement() == EMPTY
                && resources.get(5).getElement() == IRONPLATE
                && resources.get(6).getElement() == IRONPLATE
                && resources.get(7).getElement() == IRONPLATE
                && resources.get(8).getElement() == IRONPLATE) {
            int amount = 32;
            for (Item slot : resources) {
                if (slot.getElement() == IRONPLATE && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }
            // create new item/s
            Item item = new Item();
            item.setElement(SPACESHIPBODY);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftCockpit(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == IRONPLATE
                && resources.get(1).getElement() == IRONPLATE
                && resources.get(2).getElement() == IRONPLATE
                && resources.get(3).getElement() == IRONPLATE
                && resources.get(4).getElement() == GLASS
                && resources.get(5).getElement() == IRONPLATE
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == IRONPLATE
                && resources.get(8).getElement() == EMPTY) {
            int amount = 32;
            for (Item slot : resources) {
                if ((slot.getElement() == IRONPLATE || slot.getElement() == GLASS) && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }
            // create new item/s
            Item item = new Item();
            item.setElement(COCKPIT);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftEngine(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == IRONPLATE
                && resources.get(1).getElement() == IRONPLATE
                && resources.get(2).getElement() == IRONPLATE
                && resources.get(3).getElement() == COPPERWIRE
                && resources.get(4).getElement() == OILBUCKET
                && resources.get(5).getElement() == COPPERWIRE
                && resources.get(6).getElement() == IRONPLATE
                && resources.get(7).getElement() == IRONPLATE
                && resources.get(8).getElement() == IRONPLATE) {
            int amount = 32;
            for (Item slot : resources) {
                if ((slot.getElement() == IRONPLATE  || slot.getElement() == COPPERWIRE || slot.getElement() == OIL) && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }
            // create new item/s
            Item item = new Item();
            item.setElement(ENGINE);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftRocket(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == LWING
                && resources.get(1).getElement() == ENGINE
                && resources.get(2).getElement() == RWING
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == SPACESHIPBODY
                && resources.get(5).getElement() == EMPTY
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == COCKPIT
                && resources.get(8).getElement() == EMPTY) {
            int amount = 1;

            // create new item/s
            Item item = new Item();
            item.setElement(ROCKET);
            item.setResource(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }


    private static Item craftBucket(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == IRONINGOT
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == IRONINGOT
                && resources.get(4).getElement() == EMPTY
                && resources.get(5).getElement() == IRONINGOT
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == EMPTY
                && resources.get(8).getElement() == EMPTY) {
            int amount = 1;

            // create new item/s
            Item item = new Item();
            item.setElement(BUCKET);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftCopperWire(List<Item> resources) {
        int isCopper = 0;
        // check slots for correct resources
        for (Item slot : resources) {
            if (slot.getElement() == COPPERINGOT) {
                isCopper++;
            }
            if (slot.getElement() != COPPERINGOT && slot.getElement() != EMPTY) {
                return null;
            }
        }

        if (isCopper == 1) {
            int amount = 32;
            for (Item slot : resources) {
                if ((slot.getElement() == COPPERINGOT) && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }
            // check for max slot size
            if (amount > 8) {
                amount = 8;
            }
            amount *= 2;
            // create new item/s
            Item item = new Item();
            item.setElement(COPPERWIRE);
            item.setAmount(amount);
            item.setRemoveAmount(amount / 2);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftSlimePudding(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == GLASS
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == SLIMEBALL
                && resources.get(5).getElement() == EMPTY
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == EMPTY
                && resources.get(8).getElement() == EMPTY) {
            int amount = 1;

            // create new item/s
            Item item = new Item();
            item.setElement(SLIMEPUDDING);
            item.setFood(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static Item craftDowsingRod(List<Item> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == STICK
                && resources.get(1).getElement() == EMPTY
                && resources.get(2).getElement() == STICK
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == STICK
                && resources.get(5).getElement() == EMPTY
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == EMPTY
                && resources.get(8).getElement() == EMPTY) {
            int amount = 1;

            for (Item slot : resources) {
                if ((slot.getElement() == STICK) && amount > slot.getAmount()) {
                    amount = slot.getAmount();
                }
            }

            amount *= 1;
            // create new item/s
            Item item = new Item();
            item.setElement(DOWSINGROD);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }
}
