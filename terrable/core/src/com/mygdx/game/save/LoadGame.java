package com.mygdx.game.save;

import java.io.*;
import java.util.ArrayList;

import com.mygdx.game.map.Block;
import com.mygdx.game.map.Element;
import com.mygdx.game.map.Map;
import com.mygdx.game.player.Item;
import com.mygdx.game.player.Player;

public class LoadGame {
    public static void Load(Map map, Player player) {

        if (player.getPlayerHealth() > 0) {
            try {
                Block[][] mapArray = map.getMapArray();
                // Create file reader and buffered reader
                FileReader fr = new FileReader("terrable/assets/saves/savegame.trbl");
                BufferedReader br = new BufferedReader(fr);

                // Read player data from file and construct new player object
                String[] playerTokens = br.readLine().split(",");
                float playerX = Float.parseFloat(playerTokens[0]);
                float playerY = Float.parseFloat(playerTokens[1]);
                float gravity = Float.parseFloat(playerTokens[2]);
                int playerHealth = Integer.parseInt(playerTokens[3]);
                player.setX(playerX);
                player.setY(playerY);
                player.setGravity(gravity);
                player.setPlayerHealth(playerHealth);

                // Load player inventory from file
                ArrayList<Item> inventory = new ArrayList<>();
                for (int i = 0; i < 46; i++) {
                    String[] inventoryTokens = br.readLine().split(",");
                    Item slot = new Item();
                    int element = Integer.parseInt(inventoryTokens[0]);
                    int amount = Integer.parseInt(inventoryTokens[1]);
                    int damage = Integer.parseInt(inventoryTokens[2]);
                    boolean isWeapon = Boolean.parseBoolean(inventoryTokens[3]);
                    boolean isFood = Boolean.parseBoolean(inventoryTokens[4]);
                    boolean isResource = Boolean.parseBoolean(inventoryTokens[5]);
                    int itemHealth = Integer.parseInt(inventoryTokens[6]);
                    slot.setElement(element);
                    slot.setAmount(amount);
                    slot.setDamage(damage);
                    slot.setWeapon(isWeapon);
                    slot.setFood(isFood);
                    slot.setResource(isResource);
                    slot.setHealth(itemHealth);
                    inventory.add(slot);
                }
                player.setInventory(inventory);
                // Determine number of rows and columns
                int numRows = 5000;
                int numCols = 300;

                // Read map data from file
                for (int i = 0; i < numRows; i++) {
                    for (int j = 0; j < numCols; j++) {
                        String[] tokens = br.readLine().split(",");
                        int posX = Integer.parseInt(tokens[0]);
                        int posY = Integer.parseInt(tokens[1]);
                        int element = Integer.parseInt(tokens[2]);
                        boolean collision = Boolean.parseBoolean(tokens[3]);
                        int permanent = Integer.parseInt(tokens[4]);
                        float brightnessLevel = Float.parseFloat(tokens[5]);
                        mapArray[i][j].setPosX(posX);
                        mapArray[i][j].setPosY(posY);
                        mapArray[i][j].setElement(element);
                        mapArray[i][j].setCollision(collision);
                        mapArray[i][j].setPermanent(permanent);
                        mapArray[i][j].brightnessLevel = brightnessLevel;
                        if (mapArray[i][j].getElement() == Element.FURNACE){
                            mapArray[i][j].setFurnaceSlot1(new Item());
                            mapArray[i][j].setFurnaceSlot2(new Item());
                            mapArray[i][j].setFurnaceSlot3(new Item());
                        }
                        // mapArray[i][j] = new Block(posX, posY, element, collision, permanent);
                    }
                }

                map.setClock(Float.parseFloat(br.readLine()));
                map.setTimeShift(Boolean.parseBoolean(br.readLine()));
                map.setRainTimer(Float.parseFloat(br.readLine()));
                map.setRaining(Boolean.parseBoolean(br.readLine()));
                // Close readers
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Can't load game when dead.");
        }
        // map.setMapArray(mapArray);

    }
}
