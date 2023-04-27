package com.mygdx.game.mobs;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.map.Biome;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Element;
import com.mygdx.game.player.Player;

public class MobManager {
    public static void Update(ArrayList<Mob> mobs, Block[][] mapArray, Player player, int mapSizeX, int mapSizeY,
            int volume,
            Texture kivimiesTexture, Texture batTexture, TextureRegion[][] chickenTexture, Texture slimeTexture,
            Sound mobSpawnSound, Sound mobScreamSound, Sound batScreamSound, Sound batSpawnSound, Sound slimeSound, TextureRegion[][] cowTexture, TextureRegion[][] sandratTexture, Sound sandratSqueakSound, TextureRegion[][] camelTexture, TextureRegion[][] sharkTexture, Sound sharkBiteSound) {

        // mob spawner
        Random rand = new Random();
        if (mobs.size() < 15 && rand.nextInt(500) < 2) {

            int spawn = rand.nextInt(7)+1;
            int startX = (int) (((player.getX()) - 1400) / 25) + (mapSizeX / 2);
            int startY = (int) (mapSizeY / 2 - ((player.getY() + 500) / 25));
            ArrayList<int[]> noCollisionList = new ArrayList<>();

            if (startX < 2) {startX = 2;}
            if (startX > mapSizeX - 113) {startX = mapSizeX - 113;}
            if (startY < 2) {startY = 2;}
            if (startY > mapSizeY - 71) {startY = mapSizeY - 71;}

            switch (spawn) {
                case 1:
                    for (int x = startX; x < startX + 112; x++) {
                        for (int y = startY; y < startY + 70; y++) {
                            if (mapArray[x][y].isCollision() == false && mapArray[x][y-1].isCollision() == false && mapArray[x][y-1].getPermanent() > 0 && mapArray[x][y-1].getElement() != Element.WATER3 &&
                                (mapArray[x][y].getPosX() < player.getX() - 700 || mapArray[x][y].getPosX() > player.getX() + 700) 
                                && (mapArray[x][y].getPosY() < player.getY() - 300 || mapArray[x][y].getPosY() > player.getY() + 500)) {
                                int[] pos = {x, y};
                                noCollisionList.add(pos);
                            }
                        }
                    }
                    if (noCollisionList.size() > 0){
                        int randIndex = rand.nextInt(noCollisionList.size());
                        int spawnX = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosX();
                        int spawnY = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosY();
                        mobs.add(new Mob(spawnX, spawnY, kivimiesTexture, mobScreamSound));
                       // mobSpawnSound.play(volume / 2000f);
                    }

                    break;
                case 2:
                    for (int x = startX; x < startX + 112; x++) {
                        for (int y = startY; y < startY + 70; y++) {
                            if (mapArray[x][y].isCollision() == false && mapArray[x][y-1].getPermanent() > 0 && mapArray[x][y-1].getElement() != Element.WATER3 &&
                                (mapArray[x][y].getPosX() < player.getX() - 700 || mapArray[x][y].getPosX() > player.getX() + 700) 
                                && (mapArray[x][y].getPosY() < player.getY() - 300 || mapArray[x][y].getPosY() > player.getY() + 500)) {
                                int[] pos = {x, y};
                                noCollisionList.add(pos);
                            }
                        }
                    }
                    if (noCollisionList.size() > 0){
                        int randIndex = rand.nextInt(noCollisionList.size());
                        int spawnX = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosX();
                        int spawnY = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosY();
                        mobs.add(new Bat(spawnX, spawnY, batTexture, batScreamSound));
                        batSpawnSound.play(volume / 200f);
                    }

                    break;
                case 3:
                    for (int x = startX; x < startX + 112; x++) {
                        for (int y = startY; y < startY + 70; y++) {
                            if (mapArray[x][y+1].isCollision() == true && mapArray[x][y].isCollision() == false && mapArray[x+1][y].isCollision() == false && mapArray[x][y].getPermanent() == 0 && mapArray[x][y].getBiome() == Biome.FOREST || mapArray[x][y].getBiome() == Biome.MOUNTAIN &&
                                (mapArray[x][y].getPosX() < player.getX() - 800 || mapArray[x][y].getPosX() > player.getX() + 800
                                || mapArray[x][y].getPosY() < player.getY() - 300 || mapArray[x][y].getPosY() > player.getY() + 500)) {
                                int[] pos = {x, y};
                                noCollisionList.add(pos);
                            }
                        }
                    }
                    if (noCollisionList.size() > 0){
                        int randIndex = rand.nextInt(noCollisionList.size());
                        int spawnX = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosX();
                        int spawnY = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosY();
                        mobs.add(new Chicken(spawnX, spawnY, chickenTexture));
                    }

                    break;
                case 4:
                    for (int x = startX; x < startX + 112; x++) {
                        for (int y = startY; y < startY + 70; y++) {
                            if (mapArray[x][y+1].isCollision() == true && mapArray[x][y].isCollision() == false && mapArray[x+1][y].isCollision() == false && mapArray[x][y].getPermanent() == 0 &&
                                (mapArray[x][y].getPosX() < player.getX() - 800 || mapArray[x][y].getPosX() > player.getX() + 800
                                || mapArray[x][y].getPosY() < player.getY() - 300 || mapArray[x][y].getPosY() > player.getY() + 500)) {
                                int[] pos = {x, y};
                                noCollisionList.add(pos);
                            }
                        }
                    }
                    if (noCollisionList.size() > 0){
                        int randIndex = rand.nextInt(noCollisionList.size());
                        int spawnX = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosX();
                        int spawnY = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosY();
                        mobs.add(new Slime(spawnX, spawnY, slimeTexture, slimeSound));
                    }

                    break;
                case 5:
                    for (int x = startX; x < startX + 112; x++) {
                        for (int y = startY; y < startY + 70; y++) {
                            if ((mapArray[x][y+1].isCollision() == true || mapArray[x+1][y+1].isCollision() == true) && mapArray[x][y].isCollision() == false && mapArray[x][y-1].isCollision() == false && mapArray[x+1][y].isCollision() == false && mapArray[x+1][y-1].isCollision() == false && mapArray[x][y].getPermanent() == 0 && mapArray[x][y].getBiome() == Biome.FOREST || mapArray[x][y].getBiome() == Biome.MOUNTAIN &&
                                (mapArray[x][y].getPosX() < player.getX() - 800 || mapArray[x][y].getPosX() > player.getX() + 800
                                || mapArray[x][y].getPosY() < player.getY() - 300 || mapArray[x][y].getPosY() > player.getY() + 500)) {
                                int[] pos = {x, y};
                                noCollisionList.add(pos);
                            }
                        }
                    }
                    if (noCollisionList.size() > 0){
                        int randIndex = rand.nextInt(noCollisionList.size());
                        int spawnX = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosX();
                        int spawnY = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosY();
                        mobs.add(new Cow(spawnX, spawnY, cowTexture));
                    }

                    break;
                case 6:
                for (int x = startX; x < startX + 112; x++) {
                    for (int y = startY; y < startY + 70; y++) {
                        if (mapArray[x][y+1].isCollision() == true && mapArray[x][y].isCollision() == false && mapArray[x+1][y].isCollision() == false && mapArray[x][y].getPermanent() == 0 && mapArray[x][y].getBiome() == Biome.DESERT &&
                            (mapArray[x][y].getPosX() < player.getX() - 800 || mapArray[x][y].getPosX() > player.getX() + 800
                            || mapArray[x][y].getPosY() < player.getY() - 300 || mapArray[x][y].getPosY() > player.getY() + 500)) {
                            int[] pos = {x, y};
                            noCollisionList.add(pos);
                        }
                    }
                }
                if (noCollisionList.size() > 0){
                    int randIndex = rand.nextInt(noCollisionList.size());
                    int spawnX = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosX();
                    int spawnY = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosY();
                    mobs.add(new SandRat(spawnX, spawnY, sandratTexture));
                }

                break;
                case 7:
                for (int x = startX; x < startX + 112; x++) {
                    for (int y = startY; y < startY + 70; y++) {
                        if ((mapArray[x][y+1].isCollision() == true || mapArray[x+1][y+1].isCollision() == true) && mapArray[x][y].isCollision() == false && mapArray[x][y-1].isCollision() == false && mapArray[x+1][y].isCollision() == false && mapArray[x+1][y-1].isCollision() == false && mapArray[x][y].getPermanent() == 0 && mapArray[x][y].getBiome() == Biome.DESERT &&
                            (mapArray[x][y].getPosX() < player.getX() - 800 || mapArray[x][y].getPosX() > player.getX() + 800
                            || mapArray[x][y].getPosY() < player.getY() - 300 || mapArray[x][y].getPosY() > player.getY() + 500)) {
                            int[] pos = {x, y};
                            noCollisionList.add(pos);
                        }
                    }
                }
                if (noCollisionList.size() > 0){
                    int randIndex = rand.nextInt(noCollisionList.size());
                    int spawnX = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosX();
                    int spawnY = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosY();
                    mobs.add(new Camel(spawnX, spawnY, camelTexture));
                } 
                    break;
                case 8:
                for (int x = startX; x < startX + 112; x++) {
                    for (int y = startY; y < startY + 70; y++) {
                        if ((mapArray[x][y+1].isCollision() == true || mapArray[x+1][y+1].isCollision() == true) && mapArray[x][y].isCollision() == false && mapArray[x][y-1].isCollision() == false && mapArray[x+1][y].isCollision() == false && mapArray[x+1][y-1].isCollision() == false && mapArray[x][y].getPermanent() == 0 && mapArray[x][y].getBiome() == Biome.DESERT &&
                            (mapArray[x][y].getPosX() < player.getX() - 800 || mapArray[x][y].getPosX() > player.getX() + 800
                            || mapArray[x][y].getPosY() < player.getY() - 300 || mapArray[x][y].getPosY() > player.getY() + 500)) {
                            int[] pos = {x, y};
                            noCollisionList.add(pos);
                        }
                    }
                }
                if (noCollisionList.size() > 0){
                    int randIndex = rand.nextInt(noCollisionList.size());
                    int spawnX = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosX();
                    int spawnY = mapArray[noCollisionList.get(randIndex)[0]][noCollisionList.get(randIndex)[1]].getPosY();
                    mobs.add(new Camel(spawnX, spawnY, camelTexture));
                } 
                    break;
        }
        }

        // mob despawner
        if (mobs.size() > 0) {
            for (int i = 0; i < mobs.size(); i++) {
                Mob thisMob = mobs.get(i);
                if (thisMob.getMobPosX() > player.getX() + 2000 || thisMob.getMobPosY() > player.getY() + 1300) {
                    mobs.remove(i);
                } else if (thisMob.getMobPosX() < player.getX() - 2000 || thisMob.getMobPosY() < player.getY() - 1300) {
                    mobs.remove(i);
                }
            }
        }
    }
}
