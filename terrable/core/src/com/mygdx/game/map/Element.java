package com.mygdx.game.map;

import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

public final class Element {

    public static final int EMPTY = 0;
    public static final int GROUND = 1;
    public static final int GRASS = 2;
    public static final int WOOD = 3;
    public static final int LEAVES = 4;
    public static final int LADDER = 5;
    public static final int STONE = 6;
    public static final int COAL = 7;
    public static final int IRON = 8;
    public static final int DIAMOND = 9;
    public static final int TALLGRASS = 10;
    public static final int REDFLOWER = 11;
    public static final int PLANKS = 12;
    public static final int COALITEM = 13;
    public static final int DIAMONDITEM = 14;
    public static final int WOODPICKAXE = 15;
    public static final int STONEPICKAXE = 16;
    public static final int IRONPICKAXE = 17;
    public static final int DIAMONDPICKAXE = 18;
    public static final int WOODAXE = 19;
    public static final int STONEAXE = 20;
    public static final int IRONAXE = 21;
    public static final int DIAMONDAXE = 22;
    public static final int IRONINGOT = 23;
    public static final int DOOR = 24;
    public static final int DOOR1 = 25;
    public static final int DOOR2 = 26;
    public static final int STICK = 27;
    public static final int TORCH = 28;
    public static final int FEATHER = 29;
    public static final int SLIMEBALL = 30;
    public static final int STONEBACKGROUND = 31;
    public static final int DIRTBACKGROUND = 32;
    public static final int FURNACE = 33;
    public static final int FURNACE2 = 34;
    public static final int RAWSTEAK = 35;
    public static final int STEAK = 36;
    public static final int RAWCHICKENMEAT = 37;
    public static final int CHICKENMEAT = 38;
    public static final int SAND = 39;
    public static final int CACTUS = 40;
    public static final int SANDSTONE = 41;
    public static final int GLASS = 42;
    public static final int SANDBACKGROUND = 43;
    public static final int SANDSTONEBACKGROUND = 44;
    public static final int WATER1 = 45;
    public static final int WATER2 = 46;
    public static final int WATER3 = 47;
    public static final int WATER4 = 48;
    public static final int WATER5 = 49;
    public static final int IRONPLATE = 50;
    public static final int LWING = 51;
    public static final int RWING = 52;
    public static final int SPACESHIPBODY = 53;
    public static final int COCKPIT = 54;
    public static final int ENGINE = 55;
    public static final int ROCKET = 56;
    public static final int BLUEPRINT = 57;
    public static final int OIL = 58;
    public static final int BUCKET = 59;
    public static final int OILBUCKET = 60;
    public static final int COPPER = 61;
    public static final int COPPERINGOT = 62;
    public static final int COPPERWIRE = 63;


    public static final Map<Integer, String> elementNames;
    
    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0,"");
        map.put(1, "Dirt");
        map.put(2, "Grass");
        map.put(3, "Wood");
        map.put(4, "Leaves");
        map.put(5, "Ladder");
        map.put(6, "Stone");
        map.put(7, "Coal");
        map.put(8, "Iron ore");
        map.put(9, "Diamond ore");
        map.put(10, "Tall grass");
        map.put(11, "Red flower");
        map.put(12, "Wooden Planks");
        map.put(13, "Piece of coal");
        map.put(14, "Diamond");
        map.put(15, "Stone pick-axe");
        map.put(16, "Wooden pick-axe");
        map.put(17, "Iron pick-axe");
        map.put(18, "Diamond pick-axe");
        map.put(19, "Wooden axe");
        map.put(20, "Stone axe");
        map.put(21, "Iron axe");
        map.put(22, "Diamond axe");
        map.put(23, "Iron ingot");
        map.put(24, "Door");
        map.put(25, "Door");
        map.put(26, "Door");
        map.put(27, "Stick");
        map.put(28, "Torch");
        map.put(29, "Feather");
        map.put(30, "Ball of slime");
        map.put(31, "");
        map.put(32, "");
        map.put(33, "Furnace");
        map.put(34, "Furnace");
        map.put(35, "Raw Steak");
        map.put(36, "Steak");
        map.put(37, "Raw Chicken");
        map.put(38, "Chicken");
        map.put(39, "Sand");
        map.put(40, "Cactus");
        map.put(41, "Sandstone");
        map.put(42, "Glass");
        map.put(43, "");
        map.put(44, "");
        map.put(45, "Water");
        map.put(46, "Water");
        map.put(47, "Water");
        map.put(48, "Water");
        map.put(49, "Water");
        map.put(50, "Iron Plate");
        map.put(51, "Left Wing");
        map.put(52, "Right Wing");
        map.put(53, "Space Ship Body");
        map.put(54, "Cockpit");
        map.put(55, "Engine");
        map.put(56, "Rocket");
        map.put(57, "Blueprint");
        map.put(58, "Oil");
        map.put(59, "Bucket");
        map.put(60, "Bucket of oil");
        map.put(61, "Copper Ore");
        map.put(62, "Copper Ingot");
        map.put(63, "Copper Wire");

        elementNames = Collections.unmodifiableMap(map);
    }

    public static final Map<Integer, Integer> BLOCKMAXHP;
    
    static {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(0,0);
        map.put(1, 50);
        map.put(2, 50);
        map.put(3, 100);
        map.put(4, 25);
        map.put(5, 25);
        map.put(6, 200);
        map.put(7, 200);
        map.put(8, 250);
        map.put(9, 300);
        map.put(10, 10);
        map.put(11, 25);
        map.put(12, 75);
        map.put(13, 0);
        map.put(14, 0);
        map.put(15, 0);
        map.put(16, 0);
        map.put(17, 0);
        map.put(18, 0);
        map.put(19, 0);
        map.put(20, 0);
        map.put(21, 0);
        map.put(22, 0);
        map.put(23, 0);
        map.put(24, 75);
        map.put(25, 75);
        map.put(26, 75);
        map.put(27, 0);
        map.put(28, 10);
        map.put(29, 0);
        map.put(30, 0);
        map.put(31, 0);
        map.put(32, 0);
        map.put(33, 200);
        map.put(34, 200);
        map.put(35, 0);
        map.put(36, 0);
        map.put(37, 0);
        map.put(38, 0);
        map.put(39, 40);
        map.put(40, 30);
        map.put(41, 200);
        map.put(42, 25);
        map.put(43, 0);
        map.put(44, 0);
        map.put(45, 0);
        map.put(46, 0);
        map.put(47, 0);
        map.put(48, 0);
        map.put(49, 0);
        map.put(50, 0);
        map.put(51, 0);
        map.put(52, 0);
        map.put(53, 0);
        map.put(54, 0);
        map.put(55, 0);
        map.put(56, 0);
        map.put(57, 0);
        map.put(58, 0);
        map.put(59, 0);
        map.put(60, 0);
        map.put(61, 250);
        map.put(62, 0);
        map.put(63, 0);

        BLOCKMAXHP = Collections.unmodifiableMap(map);
    }

}
