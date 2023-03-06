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
import com.mygdx.game.mobs.Bat;
import com.mygdx.game.mobs.Chicken;
import com.mygdx.game.mobs.Mob;
import com.mygdx.game.mobs.Slime;

public class Map {

    Block[][] mapArray;

    
    private Texture textures;
    private Texture kivimiesTexture;
    private Texture batTexture;
    private Texture chickenTexture;
    private Texture slimeTexture;
    private TextureRegion[][] blockTextures;
    ArrayList<Mob> mobs = new ArrayList<>();

    private int mapSizeX; // map size in blocks
    private int mapSizeY; // map size in blocks

    private Sound mobSpawnSound;
    private Sound batSpawnSound;
    private Sound batScreamSound;
    private Sound slimeSound;
    private Sound mobScreamSound;

    public Map(int sizeX, int sizeY) {
        this.mapSizeX = sizeX;
        this.mapSizeY = sizeY;

        textures = new Texture("tileset2.png");
        kivimiesTexture = new Texture("kaapo.png");
        batTexture = new Texture("bat.png");
        chickenTexture = new Texture("chicken.png");
        slimeTexture = new Texture("slime.png");

        mobSpawnSound = Gdx.audio.newSound(Gdx.files.internal("sounds/moaiSpawnSound.mp3"));
        batSpawnSound = Gdx.audio.newSound(Gdx.files.internal("sounds/batSpawnSound.mp3"));

        batScreamSound = Gdx.audio.newSound(Gdx.files.internal("sounds/batScreamSound.mp3"));
        slimeSound =  Gdx.audio.newSound(Gdx.files.internal("sounds/slimeSound.mp3"));
        mobScreamSound =  Gdx.audio.newSound(Gdx.files.internal("sounds/mobScreamSound.mp3"));
        
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
                        mapArray[x][y-1] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-1) * 25, WOOD, false, EMPTY);
                        mapArray[x][y-2] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-2) * 25, WOOD, false, EMPTY);
                        mapArray[x][y-3] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-3) * 25, LEAVES, false, EMPTY);
                        mapArray[x][y-4] = new Block((-mapSizeX  * 25 / 2) + x * 25,(mapSizeY  * 25/ 2) - (y-4) * 25, LEAVES, false, EMPTY);
                        mapArray[x-1][y-3] = new Block((-mapSizeX  * 25 / 2) + (x-1) * 25,(mapSizeY  * 25/ 2) - (y-3) * 25, LEAVES,  false, EMPTY);
                        mapArray[x+1][y-3] = new Block((-mapSizeX  * 25 / 2) + (x+1) * 25,(mapSizeY  * 25/ 2) - (y-3) * 25, LEAVES, false, EMPTY);
                        mapArray[x-1][y-4] = new Block((-mapSizeX  * 25 / 2) + (x-1) * 25,(mapSizeY  * 25/ 2) - (y-4) * 25, LEAVES, false, EMPTY);
                        mapArray[x+1][y-4] = new Block((-mapSizeX  * 25 / 2) + (x+1) * 25,(mapSizeY  * 25/ 2) - (y-4) * 25, LEAVES, false, EMPTY);
                        mapArray[x-2][y-4] = new Block((-mapSizeX  * 25 / 2) + (x-2) * 25,(mapSizeY  * 25/ 2) - (y-4) * 25, LEAVES, false, EMPTY);
                        mapArray[x+2][y-4] = new Block((-mapSizeX  * 25 / 2) + (x+2) * 25,(mapSizeY  * 25/ 2) - (y-4) * 25, LEAVES, false, EMPTY);
                        mapArray[x][y-5] = new Block((-mapSizeX  * 25 / 2) + (x) * 25,(mapSizeY  * 25/ 2) - (y-5) * 25, LEAVES, false, EMPTY);
                        mapArray[x-1][y-5] = new Block((-mapSizeX  * 25 / 2) + (x-1) * 25,(mapSizeY  * 25/ 2) - (y-5) * 25, LEAVES, false, EMPTY);
                        mapArray[x+1][y-5] = new Block((-mapSizeX  * 25 / 2) + (x+1) * 25,(mapSizeY  * 25/ 2) - (y-5) * 25, LEAVES, false, EMPTY);
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
                    if ( oreChance < 5 ){
                        block = 9;
                    } else if ( oreChance < 20 ){
                        block = 8;
                    }else if ( oreChance < 50 ) {
                        block = 7;
                    }else if ( oreChance < 51 ) {
                        block = 0;
                    }

                    if (oreChance > 660 && x > 15 && x < mapSizeX -15 && y < mapSizeY - 5) {
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
                    } else if (block == 0 && x > 3 && x < mapSizeX - 3) {
                        mapArray[x - 1][y] = new Block((-mapSizeX  * 25 / 2) + (x - 1) * 25,(mapSizeY  * 25/ 2) - y * 25, block, false, STONE);
                        mapArray[x - 1][y - 1] = new Block((-mapSizeX  * 25 / 2) + (x - 1) * 25,(mapSizeY  * 25/ 2) - (y - 1) * 25, block, false, STONE);
                        mapArray[x - 1][y - 2] = new Block((-mapSizeX  * 25 / 2) + (x - 1) * 25,(mapSizeY  * 25/ 2) - (y - 2) * 25, block, false, STONE);
                        mapArray[x - 2][y] = new Block((-mapSizeX  * 25 / 2) + (x - 2) * 25,(mapSizeY  * 25/ 2) - y * 25, block, false, STONE);
                        mapArray[x - 2][y - 1] = new Block((-mapSizeX  * 25 / 2) + (x - 2) * 25,(mapSizeY  * 25/ 2) - (y - 1) * 25, block, false, STONE);
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
            if (height > 200) height = 200;
            if (height < 100) height = 100;
        }
    }

    // UPDATE MAP
    public void Update() {
         
    }

   // DRAW MAP
   public void Draw(Batch batch, Player player, int volume){

        Random rand = new Random();
        UpdateLighting(player);

        int startBlockX = (int)(player.getX() / 25 - 1800 / 25 / 2) +(mapSizeX/2);
        int endBlockX = (startBlockX + 1800 / 25) ;
        for (int x = startBlockX; x < endBlockX; x++){ 
            
            for (int y = 0; y < mapArray[x].length; y++){
                Block block = mapArray[x][y];
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


        // Update all mobs
        for (Mob mob: mobs){ 
            mob.Update(this, batch, player, volume);
        }

        // mob spawner
        if(mobs.size() < 10 && rand.nextInt(500)<2) {
            int direction = rand.nextInt(2) * 2 - 1;
            int spawn = rand.nextInt(4) + 1;

            switch (spawn) {
                case 1:  
                    mobs.add(new Mob(player.getX() + (1000 * direction), player.getY() + 200, kivimiesTexture, mobScreamSound));
                    mobSpawnSound.play(volume/2000f);
                    break;
                case 2:  
                    mobs.add(new Chicken(player.getX() + (1000 * direction), player.getY() + 200, chickenTexture));
                    break;
                case 3:  
                    mobs.add(new Bat(player.getX() + (1000 * direction), player.getY() + 200, batTexture, batScreamSound));
                    batSpawnSound.play(volume/200f);
                    break;
                case 4:  
                    mobs.add(new Slime(player.getX() + (1000 * direction), player.getY() + 200, slimeTexture, slimeSound));
                    break;
                default:
                    break;
            }
            
        }

        // mob despawner
        if(mobs.size() > 0) {
            for(int i = 0; i < mobs.size(); i++) {
                Mob thisMob = mobs.get(i);
                if(thisMob.getMobPosX() > player.getX() + 1500 || thisMob.getMobPosY() > player.getY() + 1000) {
                    mobs.remove(i);
                } else if(thisMob.getMobPosX() < player.getX() - 1500 || thisMob.getMobPosY() < player.getY() - 1000) {
                    mobs.remove(i);
                }
            }
        }




    }

    public void UpdateLighting(Player player){

        int startBlockX = (int)(player.getX() / 25 - 1800 / 25 / 2) +(mapSizeX/2);
        int endBlockX = (startBlockX + 1800 / 25) ;

        for (int x = startBlockX; x < endBlockX; x++){
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

        for (int x = startBlockX; x < endBlockX; x++){
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

        for (int x = startBlockX; x < endBlockX; x++){
            if (mapArray[x][0].getPosX() > player.getX() - 1200 && mapArray[x][0].getPosX() < player.getX() + 1200) {
                for (int y = 0; y < mapArray[x].length; y++){
                    if (mapArray[x][y].getElement() != EMPTY && mapArray[x][y].getPosY() > player.getY() - 1200 && mapArray[x][y].getPosY() < player.getY() + 1200 && x > 3 && x < mapSizeX-3 && y > 3 && y < mapSizeY-3) {

                        if(!mapArray[x-1][y].isCollision() || !mapArray[x+1][y].isCollision() || !mapArray[x][y-1].isCollision() || !mapArray[x][y+1].isCollision() || !mapArray[x][y].isCollision()){
                            if(mapArray[x][y].isCollision() || mapArray[x][y].getElement() == REDFLOWER || mapArray[x][y].getElement() == TALLGRASS || mapArray[x][y].getElement() == LADDER){
                                mapArray[x][y].setBrightness(1f);
                            }else{
                                mapArray[x][y].setBrightness(0.7f);
                            }
                            
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
        mobs.clear();
    }
}