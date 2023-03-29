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
    public static final int STONEPICKAXE = 15;
    public static final int WOODPICKAXE = 16;
    public static final int IRONPICKAXE = 17;
    public static final int DIAMONDPICKAXE = 18;
    public static final int STONEAXE = 19;
    public static final int WOODAXE = 20;
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
        map.put(19, "Stone axe");
        map.put(20, "Wooden axe");
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
        
        elementNames = Collections.unmodifiableMap(map);
    }

}
