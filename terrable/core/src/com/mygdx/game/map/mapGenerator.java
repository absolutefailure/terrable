package com.mygdx.game.map;

import java.util.Random;

import com.mygdx.game.player.Player;

import static com.mygdx.game.map.Element.*;

public class mapGenerator {
    public static void newMap(Block[][] mapArray, Player player, int mapSizeX, int mapSizeY){
        
        
        
        int height = mapSizeY/2;

        Random rand = new Random();
        
        /*[
            [1,1,1,1,1,0,0,0,0,0],
            [1,1,1,1,1,0,0,0,0,0],
            [1,1,1,1,1,0,0,0,0,0],
            [1,1,1,1,1,0,0,0,0,0],
            [1,1,1,1,1,0,0,0,0,0],
            [1,1,1,1,1,0,0,0,0,0],
            [1,1,1,1,1,0,0,0,0,0],
        ]
        */
        

        for (int x = 0; x < mapSizeX; x++){
            if (x == mapSizeX / 2){ // SET PLAYER POSITION TO THE CENTER OF THE MAP
                player.setX(0);
                player.setY((mapSizeY/2-height) * 25 + 60);
            }
            for (int y = 0; y < mapSizeY; y++ ){
                if ( y == height){
                    if (rand.nextInt(15) == 1 && x > 5 && x < mapSizeX-5){ // GENERATE TREE
                        int treeHeight = rand.nextInt(5) + 3;
                        for (int t = 1; t < treeHeight; t++){
                            mapArray[x][y-t] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-t) * 25, WOOD, false, EMPTY);
                        }
                        
                        mapArray[x][y-treeHeight] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-treeHeight) * 25, LEAVES, false, EMPTY);
                        mapArray[x][y-treeHeight-1] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-treeHeight-1) * 25, LEAVES, false, EMPTY);
                        mapArray[x-1][y-treeHeight] = new Block((-mapSizeX  * 25 / 2) + (x-1) * 25,(mapSizeY  * 25/ 2) - (y-treeHeight) * 25, LEAVES,  false, EMPTY);
                        mapArray[x+1][y-treeHeight] = new Block((-mapSizeX  * 25 / 2) + (x+1) * 25,(mapSizeY  * 25/ 2) - (y-treeHeight) * 25, LEAVES, false, EMPTY);
                        mapArray[x-1][y-treeHeight-1] = new Block((-mapSizeX  * 25 / 2) + (x-1) * 25,(mapSizeY  * 25/ 2) - (y-treeHeight-1) * 25, LEAVES, false, EMPTY);
                        mapArray[x+1][y-treeHeight-1] = new Block((-mapSizeX  * 25 / 2) + (x+1) * 25,(mapSizeY  * 25/ 2) - (y-treeHeight-1) * 25, LEAVES, false, EMPTY);
                        mapArray[x-2][y-treeHeight-1] = new Block((-mapSizeX  * 25 / 2) + (x-2) * 25,(mapSizeY  * 25/ 2) - (y-treeHeight-1) * 25, LEAVES, false, EMPTY);
                        mapArray[x+2][y-treeHeight-1] = new Block((-mapSizeX  * 25 / 2) + (x+2) * 25,(mapSizeY  * 25/ 2) - (y-treeHeight-1) * 25, LEAVES, false, EMPTY);
                        mapArray[x][y-treeHeight-2] = new Block((-mapSizeX  * 25 / 2) + (x) * 25,(mapSizeY  * 25/ 2) - (y-treeHeight-2) * 25, LEAVES, false, EMPTY);
                        mapArray[x-1][y-treeHeight-2] = new Block((-mapSizeX  * 25 / 2) + (x-1) * 25,(mapSizeY  * 25/ 2) - (y-treeHeight-2) * 25, LEAVES, false, EMPTY);
                        mapArray[x+1][y-treeHeight-2] = new Block((-mapSizeX  * 25 / 2) + (x+1) * 25,(mapSizeY  * 25/ 2) - (y-treeHeight-2) * 25, LEAVES, false, EMPTY);

                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, GROUND, true, EMPTY);
                        
                    }else{
                        if(rand.nextInt(100) < 60) {
                            mapArray[x][y-1] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-1) * 25, TALLGRASS, false, EMPTY);    
                        }
                        if(rand.nextInt(100) < 10){
                            mapArray[x][y-1] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-1) * 25, REDFLOWER, false, EMPTY);
                        }
                        // GENERATE GRASS BLOCK
                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, GRASS, true, EMPTY);
                    }
                } else if ( y > height + 15){ //only stone and ores from depth 15
                    int oreChance = rand.nextInt(1000);
                    int block = 6;

                    // Change created block to an ore based on oreChance (default is stone)
                    if ( oreChance < 2 ){
                        block = 9;
                    } else if ( oreChance < 11){
                        block = 8;
                    }else if (oreChance < 20) {
                        block = 7;
                    }else if (oreChance == 69) {
                        block = 0;
                    }

                    if (oreChance > 660 
                    && x > 15 
                    && x < mapSizeX -15 
                    && y < mapSizeY - 5 
                    && y > height + 20) {
                        int caveRandomX = rand.nextInt(6);
                        int caveRandomY = rand.nextInt(3);
                        int caveY = caveRandomY + 1;
                        int caveX = caveRandomX + 3;

                        for (int i = 0; i <= caveX; i++){
                            if (mapArray[x - 2][y].getElement() == EMPTY 
                            || mapArray[x][y - 1].getElement() == EMPTY ){
                                for (int a = 0; a <= caveY; a++){
                                    mapArray[x - i][y - a] = new Block((-mapSizeX  * 25 / 2) + (x - i) * 25,(mapSizeY  * 25/ 2) - (y - a) * 25, EMPTY, false, STONE);
                                }
                            }
                        }
                    }

                    if (block != 6 && x > 1 && x < mapSizeX -1 && block != 0){
                        int vein = rand.nextInt(100);

                        // Determine the size of the ore vein from 1 to 4 blocks
                        if (vein <= 25){
                            mapArray[x - 1][y] = new Block((-mapSizeX  * 25 / 2) + (x - 1) * 25,(mapSizeY  * 25/ 2) - y * 25, block, true, STONE);
                            mapArray[x - 1][y - 1] = new Block((-mapSizeX  * 25 / 2) + (x - 1) * 25,(mapSizeY  * 25/ 2) - (y - 1) * 25, block, true, STONE);
                            mapArray[x][y - 1] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y - 1) * 25, block, true, STONE);
                            mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y) * 25, block, true, STONE);
                        }else  if (vein <= 50){
                            mapArray[x - 1][y] = new Block((-mapSizeX  * 25 / 2) + (x - 1) * 25,(mapSizeY  * 25/ 2) - y * 25, block, true, STONE);
                            mapArray[x][y - 1] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y - 1) * 25, block, true, STONE);
                            mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y) * 25, block, true, STONE);
                        }else  if (vein <= 75){
                            mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, block, true, STONE);
                            mapArray[x][y - 1] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y - 1) * 25, block, true, STONE);
                        }else{
                            mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, block, true, STONE);
                        }
                    } else if (block == 0 
                    && x > 3 
                    && x < mapSizeX - 3 
                    && y < mapSizeY -1
                    && y > height + 20) {
                        mapArray[x - 1][y] = new Block((-mapSizeX  * 25 / 2) + (x - 1) * 25,(mapSizeY  * 25/ 2) - y * 25, block, false, STONE);
                        mapArray[x - 1][y - 1] = new Block((-mapSizeX  * 25 / 2) + (x - 1) * 25,(mapSizeY  * 25/ 2) - (y - 1) * 25, block, false, STONE);
                        mapArray[x - 1][y - 2] = new Block((-mapSizeX  * 25 / 2) + (x - 1) * 25,(mapSizeY  * 25/ 2) - (y - 2) * 25, block, false, STONE);
                        mapArray[x - 1][y - 3] = new Block((-mapSizeX  * 25 / 2) + (x - 1) * 25,(mapSizeY  * 25/ 2) - (y - 3) * 25, block, false, STONE);
                        mapArray[x - 2][y - 3] = new Block((-mapSizeX  * 25 / 2) + (x - 2) * 25,(mapSizeY  * 25/ 2) - (y - 3) * 25, block, false, STONE);
                        mapArray[x - 2][y - 2] = new Block((-mapSizeX  * 25 / 2) + (x - 2) * 25,(mapSizeY  * 25/ 2) - (y - 2) * 25, block, false, STONE);
                        mapArray[x - 2][y - 1] = new Block((-mapSizeX  * 25 / 2) + (x - 2) * 25,(mapSizeY  * 25/ 2) - (y - 1) * 25, block, false, STONE);
                        mapArray[x - 3][y -2] = new Block((-mapSizeX  * 25 / 2) + (x - 3) * 25,(mapSizeY  * 25/ 2) - (y -2) * 25, block, false, STONE);
                        mapArray[x - 3][y -1] = new Block((-mapSizeX  * 25 / 2) + (x - 3) * 25,(mapSizeY  * 25/ 2) - (y -1) * 25, block, false, STONE);
                        mapArray[x][y - 2] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y - 2) * 25, block, false, STONE);
                        mapArray[x][y - 1] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y - 1) * 25, block, false, STONE);
                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y) * 25, block, false, STONE);
                    } else {
                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, STONE, true, STONE);
                    }

                } else if ( y > height + 2){
                    if (rand.nextInt(100) < 66 ) {//66% chance for stone, else spawning ground from depth 3
                        if (y > height + 3){
                            mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, STONE, true, STONE);
                        }else{
                            mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, GROUND, true, GROUND);
                        }
                          
                    } else {
                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, GROUND, true, GROUND);
                    }
                } else if ( y > height){
                    if(y > height + 10){
                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, GROUND, true, GROUND);
                    }else{
                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, GROUND, true, EMPTY);
                    }
                    
                }
                else if(mapArray[x][y] == null){
                    mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, EMPTY, false, EMPTY);
                    mapArray[x][y].setBrightness(0.5f);
                }
            }
            // RADOMIZE NEXT ROWS HEIGHT
            if ( x % (rand.nextInt(4) + 2) == 0){
                height += rand.nextInt(3) -1;
            }
            if (height > 175) height = 175;
            if (height < 125) height = 125;
        }
        for (int i = 0; i < 5000; i++){
            boolean isAirBlock = true;
            int start = 9999;
            for(int j = 0; j < 300; j++){
                if (isAirBlock && mapArray[i][j].isCollision()){
                    isAirBlock = false;
                    start = j;
                }
                mapArray[i][j].brightnessLevel = 0f;
                

                if (j > start && 0.01f*(j-start) < 1f){
                    mapArray[i][j].brightnessLevel = 0.01f*(j-start);
                }else if(j > start ){
                    mapArray[i][j].brightnessLevel = 1f;
                }
            }
        }
    }
}