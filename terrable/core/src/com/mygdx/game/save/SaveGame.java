package com.mygdx.game.save;

import java.io.*;
import java.util.ArrayList;

import com.mygdx.game.InventorySlot;
import com.mygdx.game.Player;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;

public class SaveGame {

    public static void Save(Block[][] mapArray, Player player) {
        if (player.getPlayerHealth() > 0) {
            try {
                // Create file writer and buffered writer
                FileWriter fw = new FileWriter("terrable/assets/saves/savegame.trbl");
                BufferedWriter bw = new BufferedWriter(fw);
                // Write player information to file as plain text
                bw.write(player.getX() + "," + player.getY() + "," +
                player.getGravity() + "," + player.getPlayerHealth());
                bw.newLine();
                for (InventorySlot slot : player.getInventory()) {
                    bw.write(slot.getElement() + "," + slot.getAmount() + "," + slot.getDamage() + "," +
                            slot.isWeapon() + "," + slot.isFood() + "," + slot.isResource() + "," + slot.getHealth());
                    bw.newLine();
                }
                // Write map array to file as plain text
                for (int i = 0; i < mapArray.length; i++) {
                    for (int j = 0; j < mapArray[i].length; j++) {
                        bw.write(mapArray[i][j].getPosX() + "," + mapArray[i][j].getPosY() + "," +
                                mapArray[i][j].getElement() + "," + mapArray[i][j].isCollision() + "," +
                                mapArray[i][j].getPermanent() + "," + mapArray[i][j].brightnessLevel);
                        bw.newLine();
                    }
                }

                // Close writers
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Cant save game when dead.");
        }
    }

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
                ArrayList<InventorySlot> inventory = new ArrayList<>();
                for (int i = 0; i < 46; i++){
                    String[] inventoryTokens = br.readLine().split(",");
                    InventorySlot slot = new InventorySlot();
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
                        //mapArray[i][j] = new Block(posX, posY, element, collision, permanent);
                    }
                }



                // Close readers
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Can't load game when dead.");
        }
        //map.setMapArray(mapArray);

    }


}
