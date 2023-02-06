package com.mygdx.game.map;

import static com.mygdx.game.map.elements.*;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.Player;

public class Map {

    Block[][] mapArray;

    private Texture mudTexture;
    private Texture grassTexture;
    private Texture woodTexture;
    private Texture leavesTexture;
    private Texture ladderTexture;

    private int mapSizeX; // map size in blocks
    private int mapSizeY; // map size in blocks


    public Map(int sizeX, int sizeY) {
        this.mapSizeX = sizeX;
        this.mapSizeY = sizeY;

        mapArray = new Block[mapSizeX][mapSizeY];
        mudTexture = new Texture("mud.png");
        grassTexture = new Texture("grass.png");
        woodTexture = new Texture("wood.png");
        leavesTexture = new Texture("leaves.png");
        ladderTexture = new Texture("ladder.png");


    }

    // MAP GENERATION
    public void GenerateNewMap(Player player) {

        
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

        

        for (int i = 0; i < mapSizeX; i++){
            if (i == mapSizeX / 2){ // SET PLAYER POSITION TO THE CENTER OF THE MAP
                player.setX(0);
                player.setY((mapSizeY/2-height) * 25 + 60);
            }
            for (int j = 0; j < mapSizeY; j++ ){
                if ( j == height){
                    if (rand.nextInt(15) == 1 && i > 5 && i < mapSizeX-5){ // GENERATE TREE
                        mapArray[i][j-1] = new Block((-mapSizeX  * 25 / 2) + i * 25,(mapSizeY  * 25/ 2) - (j-1) * 25, WOOD, false);
                        mapArray[i][j-2] = new Block((-mapSizeX  * 25 / 2) + i * 25,(mapSizeY  * 25/ 2) - (j-2) * 25, WOOD, false);
                        mapArray[i][j-3] = new Block((-mapSizeX  * 25 / 2) + i * 25,(mapSizeY  * 25/ 2) - (j-3) * 25, LEAVES, false);
                        mapArray[i][j-4] = new Block((-mapSizeX  * 25 / 2) + i * 25,(mapSizeY  * 25/ 2) - (j-4) * 25, LEAVES, false);
                        mapArray[i-1][j-3] = new Block((-mapSizeX  * 25 / 2) + (i-1) * 25,(mapSizeY  * 25/ 2) - (j-3) * 25, LEAVES,  false);
                        mapArray[i+1][j-3] = new Block((-mapSizeX  * 25 / 2) + (i+1) * 25,(mapSizeY  * 25/ 2) - (j-3) * 25, LEAVES, false);
                        mapArray[i-1][j-4] = new Block((-mapSizeX  * 25 / 2) + (i-1) * 25,(mapSizeY  * 25/ 2) - (j-4) * 25, LEAVES, false);
                        mapArray[i+1][j-4] = new Block((-mapSizeX  * 25 / 2) + (i+1) * 25,(mapSizeY  * 25/ 2) - (j-4) * 25, LEAVES, false);
                        mapArray[i-2][j-4] = new Block((-mapSizeX  * 25 / 2) + (i-2) * 25,(mapSizeY  * 25/ 2) - (j-4) * 25, LEAVES, false);
                        mapArray[i+2][j-4] = new Block((-mapSizeX  * 25 / 2) + (i+2) * 25,(mapSizeY  * 25/ 2) - (j-4) * 25, LEAVES, false);
                        mapArray[i][j-5] = new Block((-mapSizeX  * 25 / 2) + (i) * 25,(mapSizeY  * 25/ 2) - (j-5) * 25, LEAVES, false);
                        mapArray[i-1][j-5] = new Block((-mapSizeX  * 25 / 2) + (i-1) * 25,(mapSizeY  * 25/ 2) - (j-5) * 25, LEAVES, false);
                        mapArray[i+1][j-5] = new Block((-mapSizeX  * 25 / 2) + (i+1) * 25,(mapSizeY  * 25/ 2) - (j-5) * 25, LEAVES, false);
                        mapArray[i][j] = new Block((-mapSizeX  * 25 / 2) + i * 25,(mapSizeY  * 25/ 2) - j * 25, GROUND, true);
                    } else{ // GENERATE GRASS BLOCK
                        mapArray[i][j] = new Block((-mapSizeX  * 25 / 2) + i * 25,(mapSizeY  * 25/ 2) - j * 25, GRASS, true);
                    }                   
                }else if ( j > height){
                    mapArray[i][j] = new Block((-mapSizeX  * 25 / 2) + i * 25,(mapSizeY  * 25/ 2) - j * 25, GROUND, true);
                } else if(mapArray[i][j] == null){
                    mapArray[i][j] = new Block((-mapSizeX  * 25 / 2) + i * 25,(mapSizeY  * 25/ 2) - j * 25, EMPTY, false);
                }
            }
            // RADOMIZE NEXT ROWS HEIGHT
            if ( i % (rand.nextInt(4) + 2) == 0){
                height += rand.nextInt(3) -1;
            }
            if (height > 120) height = 120;
            if (height < 80) height = 80;
        }
    }

    // UPDATE MAP
    public void Update() {

    }

   // DRAW MAP
   public void Draw(Batch batch, Player player){

        UpdateLighting(player);


        for (Block[] row : mapArray){ 
            if (row[0].getPosX() > player.getX() - 1300 && row[0].getPosX() < player.getX() + 1300){ // LOOP ONLY VERTICAL ROWS THAT ARE INSIDE VISIBLE AREA
                for (Block block: row){ 

                    if(block.isPermanent()){
                        batch.setColor(0.5f, 0.7f, 0.7f, 1);
                        batch.draw(mudTexture, block.getPosX(), block.getPosY());
                        // batch.setColor(0.7f, 0.7f, 0.7f, 1);
                    }
                    batch.setColor(1, 1, 1, 1);

                    if (block.getPosY() > player.getY() - 1300 && block.getPosY() < player.getY() + 1300 && block.getElement() != EMPTY){ // DRAW BLOCK ONLY IF INSIDE SCREEN
                        
                        // DRAW CORRECT TEXTURE BASED ON BLOCKS ELEMENT
                        batch.setColor(block.getBrightness(),block.getBrightness(),block.getBrightness(),1f);
                        switch (block.getElement()){             
                            case(GROUND):
                                batch.draw(mudTexture, block.getPosX(), block.getPosY(), 25, 25);
                                break;
                            case(GRASS):
                                batch.draw(grassTexture, block.getPosX(), block.getPosY(), 25, 25);
                                break;
                            case(WOOD):
                                batch.draw(woodTexture, block.getPosX(), block.getPosY(), 25, 25);
                                break;
                            case(LEAVES):
                                batch.draw(leavesTexture, block.getPosX(), block.getPosY(), 25, 25);  
                                break;
                            case(LADDER):
                                batch.draw(ladderTexture, block.getPosX(), block.getPosY(), 25, 25);
                                break;
                        }
                        batch.setColor(1,1,1,1);
                        block.setBrightness(0f);
                    }      
                    
                }
            }
        }

    }

    public void UpdateLighting(Player player){

             for (int i = 0; i < mapArray.length; i++){
            if (mapArray[i][0].getPosX() > player.getX() - 1200 && mapArray[i][0].getPosX() < player.getX() + 1200 && i > 5 && i < mapSizeX - 5) {
                for (int j = 0; j < mapArray[i].length; j++){
                    if (mapArray[i][j].isCollision() && mapArray[i][j].getPosY() > player.getY() - 1200 && mapArray[i][j].getPosY() < player.getY() + 1200 && j > 5 && j < mapSizeY - 5) {

                        if(!mapArray[i-1][j].isCollision() || !mapArray[i+1][j].isCollision() || !mapArray[i][j-1].isCollision() || !mapArray[i][j+1].isCollision() ){

                            mapArray[i][j-2].setBrightness(0.1f);
                            mapArray[i-1][j-2].setBrightness(0.1f);
                            mapArray[i-2][j-2].setBrightness(0.1f);
                            mapArray[i+1][j-2].setBrightness(0.1f);
                            mapArray[i+2][j-2].setBrightness(0.1f);

                            mapArray[i-2][j-1].setBrightness(0.1f);
                            mapArray[i+2][j-1].setBrightness(0.1f);

                            mapArray[i-2][j].setBrightness(0.1f);
                            mapArray[i+2][j].setBrightness(0.1f);

                            mapArray[i-2][j+1].setBrightness(0.1f);
                            mapArray[i+2][j+1].setBrightness(0.1f);

                            mapArray[i][j+2].setBrightness(0.1f);
                            mapArray[i-1][j+2].setBrightness(0.1f);
                            mapArray[i-2][j+2].setBrightness(0.1f);
                            mapArray[i+1][j+2].setBrightness(0.1f);
                            mapArray[i+2][j+2].setBrightness(0.1f);

                        }
                    
                    }
                }
            }
        }

        for (int i = 0; i < mapArray.length; i++){
            if (mapArray[i][0].getPosX() > player.getX() - 1200 && mapArray[i][0].getPosX() < player.getX() + 1200 && i > 5 && i < mapSizeX - 5) {
                for (int j = 0; j < mapArray[i].length; j++){
                    if (mapArray[i][j].isCollision() && mapArray[i][j].getPosY() > player.getY() - 1200 && mapArray[i][j].getPosY() < player.getY() + 1200 && j > 5 && j < mapSizeY - 5) {

                        if(!mapArray[i-1][j].isCollision() || !mapArray[i+1][j].isCollision() || !mapArray[i][j-1].isCollision() || !mapArray[i][j+1].isCollision() ){
                            mapArray[i][j-1].setBrightness(0.3f);
                            mapArray[i+1][j-1].setBrightness(0.3f);
                            mapArray[i-1][j-1].setBrightness(0.3f);
                            mapArray[i+1][j].setBrightness(0.3f);
                            mapArray[i-1][j].setBrightness(0.3f);
                            mapArray[i-1][j+1].setBrightness(0.3f);
                            mapArray[i][j+1].setBrightness(0.3f);
                            mapArray[i+1][j+1].setBrightness(0.3f);

                        }
                    
                    }
                }
            }
        }

        for (int i = 0; i < mapArray.length; i++){
            if (mapArray[i][0].getPosX() > player.getX() - 1200 && mapArray[i][0].getPosX() < player.getX() + 1200) {
                for (int j = 0; j < mapArray[i].length; j++){
                    if (mapArray[i][j].getElement() != EMPTY && mapArray[i][j].getPosY() > player.getY() - 1200 && mapArray[i][j].getPosY() < player.getY() + 1200 && i > 3 && i < mapSizeX-3 && j > 3 && j < mapSizeY-3) {

                        if(!mapArray[i-1][j].isCollision() || !mapArray[i+1][j].isCollision() || !mapArray[i][j-1].isCollision() || !mapArray[i][j+1].isCollision() || !mapArray[i][j].isCollision()){
                            mapArray[i][j].setBrightness(1f);
                        }
                    }
                }
            }
        }
    }





    public Block[][] getMapArray() {
        return mapArray;
    }

    public void setMapArray(Block[][] mapArray) {
        this.mapArray = mapArray;
    }

    public int getMapSizeX() {
        return mapSizeX;
    }

    public void setMapSizeX(int mapSizeX) {
        this.mapSizeX = mapSizeX;
    }

    public int getMapSizeY() {
        return mapSizeY;
    }

    public void setMapSizeY(int mapSizeY) {
        this.mapSizeY = mapSizeY;
    }

}
