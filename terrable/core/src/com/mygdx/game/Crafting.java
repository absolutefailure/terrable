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
        result = craftIronIngot(resources);
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

    private static InventorySlot craftStonePickaxe(List<InventorySlot> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == PLANKS
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == PLANKS
                && resources.get(5).getElement() == EMPTY
                && resources.get(6).getElement() == STONE
                && resources.get(7).getElement() == STONE
                && resources.get(8).getElement() == STONE) {
            int amount = 1;
           
            for (InventorySlot slot : resources) {
                    if ((slot.getElement() == PLANKS || slot.getElement() == STONE) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            InventorySlot item = new InventorySlot();
            item.setElement(STONEPICKAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(25);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static InventorySlot craftWoodPickaxe(List<InventorySlot> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == PLANKS
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == PLANKS
                && resources.get(5).getElement() == EMPTY
                && resources.get(6).getElement() == WOOD
                && resources.get(7).getElement() == WOOD
                && resources.get(8).getElement() == WOOD) {
            int amount = 1;
           
            for (InventorySlot slot : resources) {
                    if ((slot.getElement() == PLANKS || slot.getElement() == WOOD) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            InventorySlot item = new InventorySlot();
            item.setElement(WOODPICKAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(10);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static InventorySlot craftIronPickaxe(List<InventorySlot> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == PLANKS
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == PLANKS
                && resources.get(5).getElement() == EMPTY
                && resources.get(6).getElement() == IRONINGOT
                && resources.get(7).getElement() == IRONINGOT
                && resources.get(8).getElement() == IRONINGOT) {
            int amount = 1;
           
            for (InventorySlot slot : resources) {
                    if ((slot.getElement() == PLANKS || slot.getElement() == IRONINGOT) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            InventorySlot item = new InventorySlot();
            item.setElement(IRONPICKAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(50);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static InventorySlot craftDiamondPickaxe(List<InventorySlot> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == PLANKS
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == PLANKS
                && resources.get(5).getElement() == EMPTY
                && resources.get(6).getElement() == DIAMONDITEM
                && resources.get(7).getElement() == DIAMONDITEM
                && resources.get(8).getElement() == DIAMONDITEM) {
            int amount = 1;
           
            for (InventorySlot slot : resources) {
                    if ((slot.getElement() == PLANKS || slot.getElement() == DIAMONDITEM) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            InventorySlot item = new InventorySlot();
            item.setElement(DIAMONDAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(75);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static InventorySlot craftStoneAxe(List<InventorySlot> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == PLANKS
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == PLANKS
                && resources.get(5).getElement() == STONE
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == STONE
                && resources.get(8).getElement() == STONE) {
            int amount = 1;
           
            for (InventorySlot slot : resources) {
                    if ((slot.getElement() == PLANKS || slot.getElement() == STONE) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            InventorySlot item = new InventorySlot();
            item.setElement(STONEAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(25);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static InventorySlot craftWoodAxe(List<InventorySlot> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == PLANKS
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == PLANKS
                && resources.get(5).getElement() == WOOD
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == WOOD
                && resources.get(8).getElement() == WOOD) {
            int amount = 1;
           
            for (InventorySlot slot : resources) {
                    if ((slot.getElement() == PLANKS || slot.getElement() == WOOD) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            InventorySlot item = new InventorySlot();
            item.setElement(WOODAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(10);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static InventorySlot craftIronAxe(List<InventorySlot> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == PLANKS
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == PLANKS
                && resources.get(5).getElement() == IRONINGOT
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == IRONINGOT
                && resources.get(8).getElement() == IRONINGOT) {
            int amount = 1;
           
            for (InventorySlot slot : resources) {
                    if ((slot.getElement() == PLANKS || slot.getElement() == IRONINGOT) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            InventorySlot item = new InventorySlot();
            item.setElement(IRONAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(50);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static InventorySlot craftDiamondAxe(List<InventorySlot> resources) {
        // check slots for correct resources
        if (resources.get(0).getElement() == EMPTY
                && resources.get(1).getElement() == PLANKS
                && resources.get(2).getElement() == EMPTY
                && resources.get(3).getElement() == EMPTY
                && resources.get(4).getElement() == PLANKS
                && resources.get(5).getElement() == DIAMONDITEM
                && resources.get(6).getElement() == EMPTY
                && resources.get(7).getElement() == DIAMONDITEM
                && resources.get(8).getElement() == DIAMONDITEM) {
            int amount = 1;
           
            for (InventorySlot slot : resources) {
                    if ((slot.getElement() == PLANKS || slot.getElement() == DIAMONDITEM) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            InventorySlot item = new InventorySlot();
            item.setElement(DIAMONDAXE);
            item.setWeapon(true);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            item.setDamage(75);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static InventorySlot craftIronIngot(List<InventorySlot> resources) {
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
            int amount = 1;
           
            for (InventorySlot slot : resources) {
                    if ((slot.getElement() == IRON) && amount > slot.getAmount()) {
                        amount = slot.getAmount();
                    }
                }

            amount *= 1;
            // create new item/s
            InventorySlot item = new InventorySlot();
            item.setElement(IRONINGOT);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }

    private static InventorySlot craftDoor(List<InventorySlot> resources) {
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
            for (InventorySlot slot : resources) {
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
            InventorySlot item = new InventorySlot();
            item.setElement(DOOR);
            item.setAmount(amount);
            item.setRemoveAmount(amount);
            return item;
        }
        // return null if resources are not correct
        return null;
    }
}
