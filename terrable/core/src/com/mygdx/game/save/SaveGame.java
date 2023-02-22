package com.mygdx.game.save;

import java.io.*;

import com.mygdx.game.Player;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Map;

public class SaveGame {

    public static void Save(Block[][] mapArray, Player player) {
        if (player.getPlayerHealth() > 0) {
            try {
                FileOutputStream fileOut = new FileOutputStream("terrable/assets/saves/savegame.trbl");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(mapArray);
                out.writeObject(player);
                out.writeObject(player.getInventory());
                out.close();
                fileOut.close();
                System.out.println("Map and player saved as savegame.trbl");
            } catch (IOException i) {
                i.printStackTrace();
            }
        } else {
            System.out.println("Cant save game when dead.");
        }
    }

    public static void Load(Map map, Player player) {
        Block[][] mapArray = null;
        try {
            FileInputStream fileIn = new FileInputStream("terrable/assets/saves/savegame.trbl");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            mapArray = (Block[][]) in.readObject();
            map.setMapArray(mapArray);
            Player p = (Player) in.readObject();
            player.setX(p.getX());
            player.setY(p.getY());
            player.setGravity(p.getGravity());
            player.setInventory(p.getInventory());
            player.setPlayerHealth(p.getPlayerHealth());
            in.close();
            fileIn.close();
            System.out.println("Map and player loaded from savegame.trbl");
        } catch (IOException i) {
            i.printStackTrace();
            map.GenerateNewMap(player);
        } catch (ClassNotFoundException c) {
            System.out.println("Map or player class not found");
            c.printStackTrace();
        }

    }

}
