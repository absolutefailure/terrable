package com.mygdx.game.save;

import com.mygdx.game.map.Block;
import com.mygdx.game.map.Element;
import com.mygdx.game.map.Map;
import com.mygdx.game.player.Achievement;
import com.mygdx.game.player.Item;
import com.mygdx.game.player.Player;
import com.mygdx.game.screens.MainMenuScreen;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class SaveGame {
    private static final int maxFileCount = 4;
    private static int saveCounter = 1;

    public static void Save(Map map, Player player) {
   
        File folder = new File("terrable/assets/saves");
        String fileNamePrefix = "savegame_";
        String fileNameSuffix = ".trbl";

        int saveNumber = MainMenuScreen.saveNumber;
        Block[][] mapArray = map.getMapArray();
        
        try {


            // Create file writer and buffered writer
            FileWriter fw = new FileWriter(new File(folder, fileNamePrefix + "0" + saveNumber+fileNameSuffix));
            BufferedWriter bw = new BufferedWriter(fw);
            // Write player information to file as plain text
            bw.write(player.getX() + "," + player.getY() + "," +
                    player.getGravity() + "," + player.getPlayerHealth());
            bw.newLine();
            for (Item slot : player.getInventory()) {
                bw.write(slot.getElement() + "," + slot.getAmount() + "," + slot.getDamage() + "," +
                        slot.isWeapon() + "," + slot.isFood() + "," + slot.isResource() + "," + slot.getHealth());
                bw.newLine();
            }
            // Write map array to file as plain text
            for (int i = 0; i < mapArray.length; i++) {
                for (int j = 0; j < mapArray[i].length; j++) {
                    if(mapArray[i][j].getElement() == Element.FURNACE || mapArray[i][j].getElement() == Element.FURNACE2){
                        bw.write(mapArray[i][j].getPosX() + "," + mapArray[i][j].getPosY() + "," +
                        mapArray[i][j].getElement() + "," + mapArray[i][j].isCollision() + "," +
                        mapArray[i][j].getPermanent() + "," + mapArray[i][j].brightnessLevel + "," + mapArray[i][j].getBiome() + 
                        "," + mapArray[i][j].getFurnaceSlot1().getElement() + "," + mapArray[i][j].getFurnaceSlot1().getAmount() + "," + mapArray[i][j].getFurnaceSlot1().getDamage() + "," +
                        mapArray[i][j].getFurnaceSlot1().isWeapon() + "," + mapArray[i][j].getFurnaceSlot1().isFood() + "," + mapArray[i][j].getFurnaceSlot1().isResource() + "," + mapArray[i][j].getFurnaceSlot1().getHealth() + 
                        "," + mapArray[i][j].getFurnaceSlot2().getElement() + "," + mapArray[i][j].getFurnaceSlot2().getAmount() + "," + mapArray[i][j].getFurnaceSlot2().getDamage() + "," +
                        mapArray[i][j].getFurnaceSlot2().isWeapon() + "," + mapArray[i][j].getFurnaceSlot2().isFood() + "," + mapArray[i][j].getFurnaceSlot2().isResource() + "," + mapArray[i][j].getFurnaceSlot2().getHealth() + 
                        "," + mapArray[i][j].getFurnaceSlot3().getElement() + "," + mapArray[i][j].getFurnaceSlot3().getAmount() + "," + mapArray[i][j].getFurnaceSlot3().getDamage() + "," +
                        mapArray[i][j].getFurnaceSlot3().isWeapon() + "," + mapArray[i][j].getFurnaceSlot3().isFood() + "," + mapArray[i][j].getFurnaceSlot3().isResource() + "," + mapArray[i][j].getFurnaceSlot3().getHealth());
                        bw.newLine();
                    }else{
                        bw.write(mapArray[i][j].getPosX() + "," + mapArray[i][j].getPosY() + "," +
                        mapArray[i][j].getElement() + "," + mapArray[i][j].isCollision() + "," +
                        mapArray[i][j].getPermanent() + "," + mapArray[i][j].brightnessLevel + "," + mapArray[i][j].getBiome());
                        bw.newLine();
                    }

                }
            }
            bw.write("" + map.getClock());
            bw.newLine();
            bw.write("" + map.getClock2());
            bw.newLine();
            bw.write("" + map.getTimeShift());
            bw.newLine();
            bw.write("" + map.getRainTimer());
            bw.newLine();
            bw.write("" + map.isRaining());
            bw.newLine();
            ArrayList<Achievement> achievements = player.getInventoryObject().getAchievementManager().getAchievements2();
            for (Achievement achievement: achievements){
                bw.write(achievement.getName() + "," + achievement.isUnlocked());
                bw.newLine();
            }
            ArrayList<Integer> discoveredItems = player.getInventoryObject().getDiscoveredItems();
            for (Integer integer: discoveredItems){
                bw.write(integer + ",");
            }
            // Close writers
            bw.close();
            fw.close();
            System.out.println("Saved game to file: " + fileNamePrefix + "0" + saveNumber+fileNameSuffix);
        } catch (IOException e) {
            System.err.println("Error saving game to file: " + e.getMessage());
        }
   

        // Increment save counter
        saveCounter++;
        if (saveCounter > maxFileCount) {
            saveCounter = 1;
        }
    }

  
}