package com.mygdx.game.map;

import static com.mygdx.game.map.elements.*;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Player;
import com.mygdx.game.mobs.Mob;

public class Map {

    Block[][] mapArray;

    
    private Texture textures;
    private Texture kivimiesTexture;
    private TextureRegion[][] blockTextures;
    ArrayList<Mob> mobs = new ArrayList<>();

    private int mapSizeX; // map size in blocks
    private int mapSizeY; // map size in blocks

    private Sound mobSpawnSound;

    public Map(int sizeX, int sizeY) {
        this.mapSizeX = sizeX;
        this.mapSizeY = sizeY;

        textures = new Texture("tileset2.png");
        kivimiesTexture = new Texture("kaapo.png");

        mobSpawnSound = Gdx.audio.newSound(Gdx.files.internal("sounds/moaiSpawnSound.mp3"));
        
        blockTextures = TextureRegion.split(textures, 25, 25); 
        

    }

    // MAP GENERATION
    public void GenerateNewMap(Player player) {


        mapArray = new Block[mapSizeX][mapSizeY];
        
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
                        mapArray[x][y-1] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-1) * 25, WOOD, false);
                        mapArray[x][y-2] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-2) * 25, WOOD, false);
                        mapArray[x][y-3] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-3) * 25, LEAVES, false);
                        mapArray[x][y-4] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-4) * 25, LEAVES, false);
                        mapArray[x-1][y-3] = new Block((-mapSizeX  * 25 / 2) + (x-1) * 25,(mapSizeY  * 25/ 2) - (y-3) * 25, LEAVES,  false);
                        mapArray[x+1][y-3] = new Block((-mapSizeX  * 25 / 2) + (x+1) * 25,(mapSizeY  * 25/ 2) - (y-3) * 25, LEAVES, false);
                        mapArray[x-1][y-4] = new Block((-mapSizeX  * 25 / 2) + (x-1) * 25,(mapSizeY  * 25/ 2) - (y-4) * 25, LEAVES, false);
                        mapArray[x+1][y-4] = new Block((-mapSizeX  * 25 / 2) + (x+1) * 25,(mapSizeY  * 25/ 2) - (y-4) * 25, LEAVES, false);
                        mapArray[x-2][y-4] = new Block((-mapSizeX  * 25 / 2) + (x-2) * 25,(mapSizeY  * 25/ 2) - (y-4) * 25, LEAVES, false);
                        mapArray[x+2][y-4] = new Block((-mapSizeX  * 25 / 2) + (x+2) * 25,(mapSizeY  * 25/ 2) - (y-4) * 25, LEAVES, false);
                        mapArray[x][y-5] = new Block((-mapSizeX  * 25 / 2) + (x) * 25,(mapSizeY  * 25/ 2) - (y-5) * 25, LEAVES, false);
                        mapArray[x-1][y-5] = new Block((-mapSizeX  * 25 / 2) + (x-1) * 25,(mapSizeY  * 25/ 2) - (y-5) * 25, LEAVES, false);
                        mapArray[x+1][y-5] = new Block((-mapSizeX  * 25 / 2) + (x+1) * 25,(mapSizeY  * 25/ 2) - (y-5) * 25, LEAVES, false);
                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, GROUND, true);
                    }else{
                        if(rand.nextInt(100) < 60) {
                            mapArray[x][y-1] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-1) * 25, TALLGRASS, false);    
                        }
                        if(rand.nextInt(100) < 10){
                            mapArray[x][y-1] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-1) * 25, REDFLOWER, false);
                        }
                        // GENERATE GRASS BLOCK
                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, GRASS, true);
                    }
                } else if ( y > height + 15){ //only stone and ores from depth 15
                    int oreChance = rand.nextInt(1000);
                    if ( oreChance < 5 ) { // 0.5% chance for diamond
                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, DIAMOND, true);
                    } else if ( oreChance < 55 ) { // 5% chance for iron
                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, IRON, true);
                    } else if ( oreChance < 160 ) { // 10% chance for coal
                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, COAL, true);
                    } else { // 84.5% chance for stone
                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, STONE, true);
                    }    
                } else if ( y > height + 3){
                    if (rand.nextInt(100) < 66 ) {//66% chance for stone, else spawning ground from depth 3
                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, STONE, true);  
                    } else {
                        mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, GROUND, true);
                    }
                } else if ( y > height){
                    mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, GROUND, true);
                }
                else if(mapArray[x][y] == null){
                    mapArray[x][y] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - y * 25, EMPTY, false);
                    mapArray[x][y].setBrightness(0.5f);
                }
            }
            // RADOMIZE NEXT ROWS HEIGHT
            if ( x % (rand.nextInt(4) + 2) == 0){
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
        Random rand = new Random();
        UpdateLighting(player);

        for (Mob mob: mobs){ 
            mob.Update(this, batch, player);
        }
        if(rand.nextInt(1000)<2) {
            Boolean direction = rand.nextBoolean();
            if(direction) {
                mobs.add(new Mob(player.getX() + 500, player.getY() + 200, kivimiesTexture));
                mobSpawnSound.play(0.01f);
            } else {
                mobs.add(new Mob(player.getX() - 500, player.getY() + 200, kivimiesTexture));
                mobSpawnSound.play(0.01f);
            }
        }
        if(mobs.size() > 0) {
            for(int i = 0; i < mobs.size(); i++) {
                Mob thisMob = mobs.get(i);
                if(thisMob.getMobPosX() - player.getX() >= 1000) {
                    mobs.remove(i);
                } else if(thisMob.getMobPosX() - player.getX() <= -1000) {
                    mobs.remove(i);
                }
            }
        }
        for (Block[] row : mapArray){ 
            if (row[0].getPosX() > player.getX() - 1000 && row[0].getPosX() < player.getX() + 1000){ // LOOP ONLY VERTICAL ROWS THAT ARE INSIDE VISIBLE AREA
                for (Block block: row){

                    batch.setColor(1, 1, 1, 1);

                    if (block.getPosY() > player.getY() - 1000 && block.getPosY() < player.getY() + 1000 ){ // DRAW BLOCK ONLY IF INSIDE SCREEN

                        // DRAW BACKROUND TEXTURE IF BLOCK IS PERMANENT
                        if(block.getPermanent() == GROUND || block.getPermanent() == GRASS){
                            batch.setColor(0.5f, 0.7f, 0.7f, 1);
                            batch.draw(blockTextures[0][0], block.getPosX(), block.getPosY());
                        } else if(block.getPermanent() == STONE) {
                            batch.setColor(0.6f, 0.6f, 0.6f, 1);
                            batch.draw(blockTextures[0][5], block.getPosX(), block.getPosY());
                        }


                        if (block.getElement() != EMPTY)  {
                            batch.setColor(block.getBrightness(),block.getBrightness(),block.getBrightness(),1f);
                        
                            // DRAW CORRECT TEXTURE BASED ON BLOCKS ELEMENT
                            batch.draw(blockTextures[0][block.getElement()-1], block.getPosX(), block.getPosY(), 25 , 25); // blockTextures[ROW][COLUMN]
    
                            batch.setColor(1,1,1,1);
                            block.setBrightness(0f);
                        }                     

                    }      
                    
                }
            }
        }

    }

    public void UpdateLighting(Player player){

             for (int x = 0; x < mapArray.length; x++){
            if (mapArray[x][0].getPosX() > player.getX() - 1200 && mapArray[x][0].getPosX() < player.getX() + 1200 && x > 5 && x < mapSizeX - 5) {
                for (int y = 0; y < mapArray[x].length; y++){
                    if (mapArray[x][y].isCollision() && mapArray[x][y].getPosY() > player.getY() - 1200 && mapArray[x][y].getPosY() < player.getY() + 1200 && y > 5 && y < mapSizeY - 5) {

                        if(!mapArray[x-1][y].isCollision() || !mapArray[x+1][y].isCollision() || !mapArray[x][y-1].isCollision() || !mapArray[x][y+1].isCollision() ){

                            mapArray[x][y-2].setBrightness(0.1f);
                            mapArray[x-1][y-2].setBrightness(0.1f);
                            mapArray[x-2][y-2].setBrightness(0.1f);
                            mapArray[x+1][y-2].setBrightness(0.1f);
                            mapArray[x+2][y-2].setBrightness(0.1f);

                            mapArray[x-2][y-1].setBrightness(0.1f);
                            mapArray[x+2][y-1].setBrightness(0.1f);

                            mapArray[x-2][y].setBrightness(0.1f);
                            mapArray[x+2][y].setBrightness(0.1f);

                            mapArray[x-2][y+1].setBrightness(0.1f);
                            mapArray[x+2][y+1].setBrightness(0.1f);

                            mapArray[x][y+2].setBrightness(0.1f);
                            mapArray[x-1][y+2].setBrightness(0.1f);
                            mapArray[x-2][y+2].setBrightness(0.1f);
                            mapArray[x+1][y+2].setBrightness(0.1f);
                            mapArray[x+2][y+2].setBrightness(0.1f);

                        }
                    
                    }
                }
            }
        }

        for (int x = 0; x < mapArray.length; x++){
            if (mapArray[x][0].getPosX() > player.getX() - 1200 && mapArray[x][0].getPosX() < player.getX() + 1200 && x > 5 && x < mapSizeX - 5) {
                for (int y = 0; y < mapArray[x].length; y++){
                    if (mapArray[x][y].isCollision() && mapArray[x][y].getPosY() > player.getY() - 1200 && mapArray[x][y].getPosY() < player.getY() + 1200 && y > 5 && y < mapSizeY - 5) {

                        if(!mapArray[x-1][y].isCollision() || !mapArray[x+1][y].isCollision() || !mapArray[x][y-1].isCollision() || !mapArray[x][y+1].isCollision() ){
                            mapArray[x][y-1].setBrightness(0.3f);
                            mapArray[x+1][y-1].setBrightness(0.3f);
                            mapArray[x-1][y-1].setBrightness(0.3f);
                            mapArray[x+1][y].setBrightness(0.3f);
                            mapArray[x-1][y].setBrightness(0.3f);
                            mapArray[x-1][y+1].setBrightness(0.3f);
                            mapArray[x][y+1].setBrightness(0.3f);
                            mapArray[x+1][y+1].setBrightness(0.3f);

                        }
                    
                    }
                }
            }
        }

        for (int x = 0; x < mapArray.length; x++){
            if (mapArray[x][0].getPosX() > player.getX() - 1200 && mapArray[x][0].getPosX() < player.getX() + 1200) {
                for (int y = 0; y < mapArray[x].length; y++){
                    if (mapArray[x][y].getElement() != EMPTY && mapArray[x][y].getPosY() > player.getY() - 1200 && mapArray[x][y].getPosY() < player.getY() + 1200 && x > 3 && x < mapSizeX-3 && y > 3 && y < mapSizeY-3) {

                        if(!mapArray[x-1][y].isCollision() || !mapArray[x+1][y].isCollision() || !mapArray[x][y-1].isCollision() || !mapArray[x][y+1].isCollision() || !mapArray[x][y].isCollision()){
                            mapArray[x][y].setBrightness(1f);
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


    public void dispose(){
        mapArray = null;
    }
}