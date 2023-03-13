package com.mygdx.game.map;

import static com.mygdx.game.map.elements.*;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
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
    public static ArrayList<Mob> mobs = new ArrayList<>();
    private Texture sunTexture;
    // private Texture moonTexture;
    private int mapSizeX; // map size in blocks
    private int mapSizeY; // map size in blocks

    private Sound mobSpawnSound;
    private Sound batSpawnSound;
    private Sound batScreamSound;
    private Sound slimeSound;
    private Sound mobScreamSound;
    
    private int rainTimer = 0;
    private int clock = 0;
    private Boolean timeShift = false;
    private boolean isRaining = false;
    private Texture rainTexture;
    private TextureRegion[][] rainTextureRegions;
    private ArrayList<Raindrop> rainDropList = new ArrayList<>();
    private int wind = 0;
    private float dayTime = 10000;

    public Map(int sizeX, int sizeY) {
        this.mapSizeX = sizeX;
        this.mapSizeY = sizeY;

        textures = new Texture("tileset2.png");
        kivimiesTexture = new Texture("kaapo.png");
        batTexture = new Texture("bat.png");
        chickenTexture = new Texture("chicken.png");
        slimeTexture = new Texture("slime.png");
        sunTexture = new Texture("sun.png");
        // moonTexture = new Texture("moon.png");
        mobSpawnSound = Gdx.audio.newSound(Gdx.files.internal("sounds/moaiSpawnSound.mp3"));
        batSpawnSound = Gdx.audio.newSound(Gdx.files.internal("sounds/batSpawnSound.mp3"));

        batScreamSound = Gdx.audio.newSound(Gdx.files.internal("sounds/batScreamSound.mp3"));
        slimeSound =  Gdx.audio.newSound(Gdx.files.internal("sounds/slimeSound.mp3"));
        mobScreamSound =  Gdx.audio.newSound(Gdx.files.internal("sounds/mobScreamSound.mp3"));
        
        blockTextures = TextureRegion.split(textures, 25, 25); 
        
        rainTexture = new Texture("raindrop.png");
        rainTextureRegions = TextureRegion.split(rainTexture,6,6);

    }

    // MAP GENERATION
    public void GenerateNewMap(Player player) {


        mapArray = new Block[mapSizeX][mapSizeY];
        
        int height = mapSizeY/2;

        Random rand = new Random();
        rainTimer = rand.nextInt(10000);
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
            if (height > 200) height = 200;
            if (height < 100) height = 100;
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

    // UPDATE MAP
    public void Update() {
         
    }

   // DRAW MAP
   public void Draw(Batch batch, Player player, int volume){
        Random rand = new Random();
        float dayBrightness = 1000f/clock;
        if(dayBrightness >= 1) {
            dayBrightness = 1;
        }
        System.out.println(dayBrightness);
        float red = (clock/dayTime)/3;
        float green = (clock/dayTime)/2;
        float blue = clock/dayTime;
        ScreenUtils.clear(red, green, blue, 1);
        rainTimer--;
        if(!timeShift){
            if(clock < dayTime) {
                clock++;
                batch.draw(sunTexture, player.getX()-800, -(2500)+(clock)/2);
            } else {
                timeShift = true;
            }
        } else {
            if(clock > 0) {
                batch.draw(sunTexture, player.getX()-800, -(2500)+(dayTime-(dayTime-clock))/2);
                clock--;
            } else {
                timeShift = false;
            }
        }
        if (rainTimer < 0 && !isRaining){
            isRaining = true;
            rainTimer = rand.nextInt(9000)+1000;
            wind = rand.nextInt(900)-400;
        }else if(rainTimer < 0){
            isRaining = false;
            rainTimer = rand.nextInt(9000)+1000;
        }

        if (!isRaining && rainTimer > 0 && rainTimer < 200){
            if (rainDropList.size() < 2000 && rainTimer % 2 == 0){
                rainDropList.add(new Raindrop(rainTextureRegions, (int)player.getX() + rand.nextInt(2500)-1250+(-wind), (int)player.getY()+1000));
            }
        }

        if (isRaining){
            if (rainTimer > 200){
                wind += rand.nextInt(9)-4;
                if (wind < -500){wind = -500;}
                if(wind > 500){wind = 500;}
                if (rainDropList.size() < 2000){
                    for (int i = 0; i < 5; i++){
                        rainDropList.add(new Raindrop(rainTextureRegions, (int)player.getX() + rand.nextInt(2500)-1250+(-wind), (int)player.getY()+1000));
                    }
                }
            }else{
                if (rainDropList.size() < 2000 && rainTimer % 2 == 0){
                    rainDropList.add(new Raindrop(rainTextureRegions, (int)player.getX() + rand.nextInt(2500)-1250+(-wind), (int)player.getY()+1000));
                }
            }

        }
        for (int i = 0; i < rainDropList.size(); i++){
            Raindrop raindrop = rainDropList.get(i);
            int raindropX = (int)(((raindrop.getX())-50) / 25) +(mapSizeX/2);
            int raindropY = (int)(mapSizeY/2 - ((raindrop.getY()+50) / 25)) ;
            if (raindropY < 0){raindropY = 0;}
            if(raindropY > mapSizeY-5){raindropY = mapSizeY-5;}
            if (raindropX < 0){raindropX = 0;}
            if(raindropX > mapSizeX-5){raindropX = mapSizeX-5;}
            raindrop.Update(batch, wind/100f);
            for (int x = raindropX; x < raindropX + 3; x++){
                for (int y = raindropY; y < raindropY+4; y++){
                    if ((mapArray[x][y].isCollision() || mapArray[x][y].getElement() == LEAVES) && raindrop.getX() >= mapArray[x][y].getPosX()
                    && raindrop.getX() <= mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                    && raindrop.getY() >= mapArray[x][y].getPosY()
                    && raindrop.getY() <= mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {
                        raindrop.setY(mapArray[x][y].getPosY()+25);
                        raindrop.setOnGround(true);
                        break;
                    }
                }
            }
            if (raindrop.isOnGround() && raindrop.getTimer() > 50){
                rainDropList.remove(i);

            }
        }
        
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
                        batch.setColor(block.getBrightness()-block.brightnessLevel, block.getBrightness()-block.brightnessLevel, block.getBrightness()-block.brightnessLevel, 1f);
                        batch.draw(blockTextures[0][31], block.getPosX(), block.getPosY());
                    } else if(block.getPermanent() == STONE) {
                        batch.setColor(block.getBrightness()-block.brightnessLevel, block.getBrightness()-block.brightnessLevel, block.getBrightness()-block.brightnessLevel, 1f);
                        batch.draw(blockTextures[0][30], block.getPosX(), block.getPosY());
                    }


                    if (block.getElement() != EMPTY)  {
                        batch.setColor(block.getBrightness()-dayBrightness, block.getBrightness()-dayBrightness, block.getBrightness()-dayBrightness, 1f);
                    
                        // DRAW CORRECT TEXTURE BASED ON BLOCKS ELEMENT
                        batch.draw(blockTextures[0][block.getElement()-1], block.getPosX(), block.getPosY(), 25 , 25); // blockTextures[ROW][COLUMN]

                        batch.setColor(1,1,1,1);
                    } 
                    block.setBrightness(0f);                    

                }      
                
            }
            
        }





        // Update all mobs
        for (Mob mob: mobs){ 
            mob.Update(this, batch, player, volume);
        }
        
        
        
         // mob spawner
     if(mobs.size() < 10 && rand.nextInt(500)<2) {
        //int direction = rand.nextInt(2) * 2 - 1;
        int spawn = rand.nextInt(4) + 1;
        int mobSpawnX = (int)(((player.getX())- 1500) / 25) +(mapSizeX/2);
        int mobSpawnY = (int)(mapSizeY/2 - ((player.getY()+1500) / 25)) ;
        ArrayList<Integer> noCollisionXList = new ArrayList<>();
        ArrayList<Integer> noCollisionYList = new ArrayList<>();

        
        for (int x = mobSpawnX; x < mobSpawnX + 100; x++){
            for (int y = mobSpawnY; y < mobSpawnY + 50; y++){
                if (mapArray[x][y].isCollision() == false) {
                    noCollisionXList.add(x);
                    noCollisionYList.add(y);
                    }
                }
            }

        switch (spawn) { 
            case 1:  
                int a = rand.nextInt(noCollisionXList.size() -1);
                int mobX = mapArray[noCollisionXList.get(a)][noCollisionYList.get(a)].getPosX();
                int mobY = mapArray[noCollisionXList.get(a)][noCollisionYList.get(a)].getPosY();
                mobs.add(new Mob( mobX, mobY, kivimiesTexture, mobScreamSound));
                mobSpawnSound.play(volume/2000f);
                noCollisionXList.clear();
                noCollisionYList.clear();
                break;
            case 2:  
                int b = rand.nextInt(noCollisionXList.size() -1);
                int batX = mapArray[noCollisionXList.get(b)][noCollisionYList.get(b)].getPosX();
                int batY = mapArray[noCollisionXList.get(b)][noCollisionYList.get(b)].getPosY();
                mobs.add(new Bat(batX , batY, batTexture, batScreamSound));
                batSpawnSound.play(volume/200f);
                noCollisionXList.clear();
                noCollisionYList.clear();
                break;
            case 3:  
                int c = rand.nextInt(noCollisionXList.size() -1);
                int chikX = mapArray[noCollisionXList.get(c)][noCollisionYList.get(c)].getPosX();
                int chikY = mapArray[noCollisionXList.get(c)][noCollisionYList.get(c)].getPosY();
                mobs.add(new Chicken(chikX , chikY, chickenTexture));
                noCollisionXList.clear();
                noCollisionYList.clear();
                break;
            case 4: 
                int s = rand.nextInt(noCollisionXList.size() -1);
                int slimX = mapArray[noCollisionXList.get(s)][noCollisionYList.get(s)].getPosX();
                int slimY = mapArray[noCollisionXList.get(s)][noCollisionYList.get(s)].getPosY();
                mobs.add(new Slime(slimX , slimY, slimeTexture, slimeSound));
                noCollisionXList.clear();
                noCollisionYList.clear();
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
                                mapArray[x][y].setBrightness(0.8f);
                            } else{
                                mapArray[x][y].setBrightness(0.7f);
                            }
                            
                        }
                    }
                }
            }
        }

        // Torches increase brightness around them
        for (int x = startBlockX; x < endBlockX; x++){
            if (mapArray[x][0].getPosX() > player.getX() - 1200 && mapArray[x][0].getPosX() < player.getX() + 1200) {
                for (int y = 0; y < mapArray[x].length; y++){
                    if (mapArray[x][y].getElement() == TORCH){
                        mapArray[x][y].setCollision(false);
                        float brightness = 1.1f;
                        mapArray[x][y].setBrightness(mapArray[x][y].getBrightness() + brightness);
                        for (int i = 1; i<9; i++){
                            brightness = 1.1f;
                            brightness = brightness - (i*0.1f);
                            mapArray[x-i][y].setBrightness(mapArray[x-i][y].getBrightness() + brightness);
                            mapArray[x][y-i].setBrightness(mapArray[x][y-i].getBrightness() + brightness);
                            mapArray[x+i][y].setBrightness(mapArray[x+i][y].getBrightness() + brightness);
                            mapArray[x][y+i].setBrightness(mapArray[x][y+i].getBrightness() + brightness);
                        }

                        for (int i = 2; i<8; i++){
                           brightness = 1.1f;
                            brightness = brightness - (i*0.1f);
                            
                            mapArray[x-i][y +1].setBrightness(mapArray[x-i][y +1].getBrightness() + brightness);
                            mapArray[x +1][y-i].setBrightness(mapArray[x +1][y-i].getBrightness() + brightness);
                            mapArray[x+i][y +1].setBrightness(mapArray[x+i][y +1].getBrightness() + brightness);
                            mapArray[x +1][y+i].setBrightness(mapArray[x +1][y+i].getBrightness() + brightness);

                            mapArray[x-i][y -1].setBrightness(mapArray[x-i][y -1].getBrightness() + brightness);
                            mapArray[x -1][y-i].setBrightness(mapArray[x -1][y-i].getBrightness() + brightness);
                            mapArray[x+i][y -1].setBrightness(mapArray[x+i][y -1].getBrightness() + brightness);
                            mapArray[x -1][y+i].setBrightness(mapArray[x -1][y+i].getBrightness() + brightness);
                        }

                        for (int i = 3; i<7; i++){
                            brightness = 1.1f;
                            brightness = brightness - (i*0.1f);

                            mapArray[x-i][y +2].setBrightness(mapArray[x-i][y +2].getBrightness() + brightness);
                            mapArray[x +2][y-i].setBrightness(mapArray[x +2][y-i].getBrightness() + brightness);
                            mapArray[x+i][y +2].setBrightness(mapArray[x+i][y +2].getBrightness() + brightness);
                            mapArray[x +2][y+i].setBrightness(mapArray[x +2][y+i].getBrightness() + brightness);

                            mapArray[x-i][y -2].setBrightness(mapArray[x-i][y -2].getBrightness() + brightness);
                            mapArray[x -2][y-i].setBrightness(mapArray[x -2][y-i].getBrightness() + brightness);
                            mapArray[x+i][y -2].setBrightness(mapArray[x+i][y -2].getBrightness() + brightness);
                            mapArray[x -2][y+i].setBrightness(mapArray[x -2][y+i].getBrightness() + brightness);
                        }

                        for (int i = 4; i<6; i++){
                            brightness = 1.1f;
                            brightness = brightness - (i*0.1f);

                            mapArray[x-i][y +3].setBrightness(mapArray[x-i][y +3].getBrightness() + brightness);
                            mapArray[x +3][y-i].setBrightness(mapArray[x +3][y-i].getBrightness() + brightness);
                            mapArray[x+i][y +3].setBrightness(mapArray[x+i][y +3].getBrightness() + brightness);
                            mapArray[x +3][y+i].setBrightness(mapArray[x -3][y+i].getBrightness() + brightness);

                            mapArray[x-i][y -3].setBrightness(mapArray[x-i][y -3].getBrightness() + brightness);
                            mapArray[x -3][y-i].setBrightness(mapArray[x -3][y-i].getBrightness() + brightness);
                            mapArray[x+i][y -3].setBrightness(mapArray[x+i][y -3].getBrightness() + brightness);
                            mapArray[x -3][y+i].setBrightness(mapArray[x -3][y+i].getBrightness() + brightness);
                        }

                        for (int i = 1; i<5; i++){
                            brightness = 1.1f;
                            brightness = brightness - (i*0.1f);

                            mapArray[x+i][y+i].setBrightness(mapArray[x+i][y+i].getBrightness() + brightness);
                            mapArray[x+i][y-i].setBrightness(mapArray[x+i][y-i].getBrightness() + brightness);
                            mapArray[x-i][y+i].setBrightness(mapArray[x-i][y+i].getBrightness() + brightness);
                            mapArray[x-i][y-i].setBrightness(mapArray[x-i][y-i].getBrightness() + brightness);
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
    public void clearMap(){
        mobs.clear();
    }

    public static ArrayList<Mob> getMobs() {
        return mobs;
    }

    public void dispose(){
        mapArray = null;
        mobs.clear();
    }
}