package com.mygdx.game.save;

import java.io.*;

import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;
import com.mygdx.game.player.Item;
import com.mygdx.game.player.Player;

public class SaveGame {
    
    public static void Save(Map map, Player player) {
        Block[][] mapArray = map.getMapArray();
        if (player.getPlayerHealth() > 0) {
            try {
                // Create file writer and buffered writer
                FileWriter fw = new FileWriter("terrable/assets/saves/savegame.trbl");
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
                        bw.write(mapArray[i][j].getPosX() + "," + mapArray[i][j].getPosY() + "," +
                                mapArray[i][j].getElement() + "," + mapArray[i][j].isCollision() + "," +
                                mapArray[i][j].getPermanent() + "," + mapArray[i][j].brightnessLevel);
                        bw.newLine();
                    }
                }
                bw.write("" + map.getClock());
                bw.newLine();
                bw.write("" + map.getTimeShift());
                bw.newLine();
                bw.write("" + map.getRainTimer());
                bw.newLine();
                bw.write("" + map.isRaining());
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
}
