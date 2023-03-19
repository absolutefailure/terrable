package com.mygdx.game.map.rain;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Raindrop {
    private TextureRegion[][] textures;
    private float x;
    private float y;
    private boolean onGround = false;

    private float timer = 0;


    public Raindrop(TextureRegion[][] tex, float x, float y){
        this.textures = tex;
        this.x = x;
        this.y = y;
    }

    public void Update(Batch b, float wind, float delta){
        if (!onGround){
            y -= 10 * delta;
            x += wind * delta;
        }else{
            timer += 1f * delta;
        }
        if (timer == 0){
            b.draw(textures[0][0], x-5, y);
        }else if (timer > 0 && timer < 15){
            b.draw(textures[0][1], x-5, y);
        }else if(timer >= 15 && timer <30){
            b.draw(textures[0][2], x-5, y);
        }else if(timer >= 45){
            b.draw(textures[0][3], x-5, y);
        }
        
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    public float getTimer() {
        return timer;
    }
    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
