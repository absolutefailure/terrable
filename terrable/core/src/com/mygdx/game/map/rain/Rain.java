package com.mygdx.game.map.rain;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.map.Block;
import com.mygdx.game.map.Element;
import com.mygdx.game.player.Player;

public class Rain {
    private float rainTimer = 0;
    private float newRaindropTimer = 0;

    private boolean isRaining = false;
    private Texture rainTexture;
    private TextureRegion[][] rainTextureRegions;
    private ArrayList<Raindrop> rainDropList = new ArrayList<>();
    private int wind = 0;
    private Random rand;

    public Rain() {
        rainTexture = new Texture("raindrop.png");
        rainTextureRegions = TextureRegion.split(rainTexture, 6, 6);
        rand = new Random();
        rainTimer = rand.nextInt(10000);
    }

    public void Update(Player player, Block[][] mapArray, int mapSizeX, int mapSizeY,
            Batch batch, float delta) {

        rainTimer -= 1f * delta;
        newRaindropTimer -= 1 * delta;
        if (rainTimer < 0 && !isRaining) {
            isRaining = true;
            rainTimer = rand.nextInt(9000) + 1000;
            wind = rand.nextInt(900) - 400;
        } else if (rainTimer < 0) {
            isRaining = false;
            rainTimer = rand.nextInt(9000) + 1000;
        }
        if (!isRaining && rainTimer > 0 && rainTimer < 200 && newRaindropTimer < 0) {
            if (rainDropList.size() < 2000) {
                rainDropList.add(new Raindrop(rainTextureRegions,
                        (int) player.getX() + rand.nextInt(2500) - 1250 + (-wind), (int) player.getY() + 1000));
                newRaindropTimer = 2;
            }
        }

        if (isRaining && newRaindropTimer < 0) {
            if (rainTimer > 200) {
                if (rainDropList.size() < 2000) {
                    for (int i = 0; i < 5; i++) {
                        rainDropList.add(new Raindrop(rainTextureRegions,
                                (int) player.getX() + rand.nextInt(2500) - 1250 + (-wind), (int) player.getY() + 1000));
                    }
                    newRaindropTimer = 1;
                }
            } else {
                if (rainDropList.size() < 2000) {
                    rainDropList.add(new Raindrop(rainTextureRegions,
                            (int) player.getX() + rand.nextInt(2500) - 1250 + (-wind), (int) player.getY() + 1000));
                }
            }

        }

        for (int i = 0; i < rainDropList.size(); i++) {
            Raindrop raindrop = rainDropList.get(i);
            int raindropX = (int) (((raindrop.getX()) - 50) / 25) + (mapSizeX / 2);
            int raindropY = (int) (mapSizeY / 2 - ((raindrop.getY() + 50) / 25));
            if (raindropY < 0) {
                raindropY = 0;
            }
            if (raindropY > mapSizeY - 5) {
                raindropY = mapSizeY - 5;
            }
            if (raindropX < 0) {
                raindropX = 0;
            }
            if (raindropX > mapSizeX - 5) {
                raindropX = mapSizeX - 5;
            }
            raindrop.Update(batch, wind / 100f, delta);
            for (int x = raindropX; x < raindropX + 3; x++) {
                for (int y = raindropY; y < raindropY + 4; y++) {
                    if ((mapArray[x][y].isCollision() || mapArray[x][y].getElement() == Element.LEAVES)
                            && raindrop.getX() >= mapArray[x][y].getPosX()
                            && raindrop.getX() <= mapArray[x][y].getPosX() + mapArray[x][y].getBLOCKSIZE()
                            && raindrop.getY() >= mapArray[x][y].getPosY()
                            && raindrop.getY() <= mapArray[x][y].getPosY() + mapArray[x][y].getBLOCKSIZE()) {
                        raindrop.setY(mapArray[x][y].getPosY() + 25);
                        raindrop.setOnGround(true);
                        break;
                    }
                }
            }
            if (raindrop.isOnGround() && raindrop.getTimer() > 50) {
                rainDropList.remove(i);

            }
        }
    }

    public float getRainTimer() {
        return rainTimer;
    }

    public void setRainTimer(float rainTimer) {
        this.rainTimer = rainTimer;
    }

    public boolean isRaining() {
        return isRaining;
    }

    public void setRaining(boolean isRaining) {
        this.isRaining = isRaining;
    }

}
