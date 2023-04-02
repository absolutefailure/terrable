package com.mygdx.game.map;

import static com.mygdx.game.map.Element.*;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.map.rain.Rain;
import com.mygdx.game.mobs.Mob;
import com.mygdx.game.mobs.mobManager;
import com.mygdx.game.player.Player;

public class Map {

    Block[][] mapArray;


    private Texture textures;
    private Texture kivimiesTexture;
    private Texture batTexture;
    private Texture chickenTexture;
    private Texture slimeTexture;
    private Texture cowTexture;
    private TextureRegion[][] cowTextureRegions;
    private TextureRegion[][] chickenTextureRegions;
    private TextureRegion[][] blockTextures;
    public static ArrayList<Mob> mobs = new ArrayList<>();
    private Texture sunTexture;
    private Texture moonTexture;
    private int mapSizeX; // map size in blocks
    private int mapSizeY; // map size in blocks

    private Sound mobSpawnSound;
    private Sound batSpawnSound;
    private Sound batScreamSound;
    private Sound slimeSound;
    private Sound mobScreamSound;
    
    
    private float clock = 2000;
    private float clock2 = 2000;
    private Boolean timeShift = false;

    private float dayTime = 20000;
    private float nightTime = 6000;

    private Rain rain;

    public Map(int sizeX, int sizeY) {
        this.mapSizeX = sizeX;
        this.mapSizeY = sizeY;

        textures = new Texture("tileset2.png");
        kivimiesTexture = new Texture("kaapo.png");
        batTexture = new Texture("bat.png");
        chickenTexture = new Texture("chicken.png");
        slimeTexture = new Texture("slime.png");
        sunTexture = new Texture("sun.png");

        cowTexture = new Texture("cow.png");
        cowTextureRegions = TextureRegion.split(cowTexture, 50, 40);
        chickenTextureRegions = TextureRegion.split(chickenTexture, 16, 18);
        moonTexture = new Texture("moon.png");
        mobSpawnSound = Gdx.audio.newSound(Gdx.files.internal("sounds/moaiSpawnSound.mp3"));
        batSpawnSound = Gdx.audio.newSound(Gdx.files.internal("sounds/batSpawnSound.mp3"));

        batScreamSound = Gdx.audio.newSound(Gdx.files.internal("sounds/batScreamSound.mp3"));
        slimeSound =  Gdx.audio.newSound(Gdx.files.internal("sounds/slimeSound.mp3"));
        mobScreamSound =  Gdx.audio.newSound(Gdx.files.internal("sounds/mobScreamSound.mp3"));
        
        blockTextures = TextureRegion.split(textures, 25, 25); 
        
        rain = new Rain();

    }

    // MAP GENERATION
    public void GenerateNewMap(Player player) {

        mapArray = new Block[mapSizeX][mapSizeY];
        mapGenerator.newMap(mapArray,player,mapSizeX,mapSizeY);
    }

    // UPDATE MAP
    public void Update() {
         
    }

   // DRAW MAP
   public void Draw(Batch batch, Player player, int volume, float delta){
        
        float dayBrightness = 800f/clock;
        if(dayBrightness >= 0.7f) {
            dayBrightness = 0.7f;
        }
        float red = (clock/dayTime)/3*2.5f;
        float green = (clock/dayTime)/2*2.5f;
        float blue = clock/dayTime*2.5f;
        if (red > 0.2f){red = 0.2f;}
        if (green > 0.5f){green = 0.5f;}
        if (blue > 0.9f){blue = 0.9f;}
        ScreenUtils.clear(red, green, blue, 1);
        if(!timeShift){
            if(clock < dayTime) {
                clock += 1f * delta;
                batch.draw(sunTexture, (int)player.getX()-800, -(2500)+(clock)/2);
                if(clock2 > 10) {
                    clock2 -= 1f * delta;
                    batch.draw(moonTexture, (int)player.getX()-800, -(2500)+(nightTime-(nightTime-clock2))/2);
                }
            } else {
                timeShift = true;
            }
        } else {
            if(clock > 10) {
                batch.draw(sunTexture, (int)player.getX()-800, -(2500)+(dayTime-(dayTime-clock))/2);
                clock -= 1f * delta;
                if (clock < nightTime) {
                    if(clock2 < nightTime) {
                        clock2 += 1f * delta;
                        batch.draw(moonTexture, (int)player.getX()-800, -(2500)+(clock2)/2);
                    }
                }
            } else {
                timeShift = false;
            }
        }
    
        rain.Update(player, mapArray, mapSizeX, mapSizeY, batch, delta);

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
                        if (block.getBrightness() <= 0.1f){
                            batch.setColor(block.getBrightness()-block.brightnessLevel-(dayBrightness/7f), block.getBrightness()-block.brightnessLevel-(dayBrightness/7f), block.getBrightness()-block.brightnessLevel-(dayBrightness/7f), 1f);
                        }else if(block.getBrightness() <= 0.3f){
                            batch.setColor(block.getBrightness()-block.brightnessLevel-(dayBrightness/2f), block.getBrightness()-block.brightnessLevel-(dayBrightness/2f), block.getBrightness()-block.brightnessLevel-(dayBrightness/2f), 1f);
                        }else{
                            batch.setColor(block.getBrightness()-block.brightnessLevel-dayBrightness, block.getBrightness()-block.brightnessLevel-dayBrightness, block.getBrightness()-block.brightnessLevel-dayBrightness, 1f);
                        }
                        
                    
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
            mob.Update(this, batch, player, volume, delta);
        }
        //mob spawner/despawner
        mobManager.Update(mobs, mapArray, player, mapSizeX, mapSizeY, volume,
            kivimiesTexture, batTexture, chickenTextureRegions, slimeTexture,
            mobSpawnSound, mobScreamSound, batScreamSound,batSpawnSound,slimeSound,cowTextureRegions);






    }

    public void UpdateLighting(Player player){

        int startBlockX = (int)(player.getX() / 25 - 2000 / 25 / 2) +(mapSizeX/2);
        int endBlockX = (startBlockX + 2000 / 25) ;

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
    

    public Boolean getTimeShift() {
        return timeShift;
    }

    public void setTimeShift(Boolean timeShift) {
        this.timeShift = timeShift;
    }

    public boolean isRaining() {
        return rain.isRaining();
    }

    public void setRaining(boolean isRaining) {
        rain.setRaining(isRaining);
    }

    public float getRainTimer() {
        return rain.getRainTimer();
    }

    public void setRainTimer(float rainTimer) {
        rain.setRainTimer(rainTimer);;
    }

    public float getClock() {
        return clock;
    }

    public void setClock(float clock) {
        this.clock = clock;
    }

    public void reset(){
        mapArray = null;
        mobs.clear();
    }
}