package com.mygdx.game;


import com.badlogic.gdx.InputProcessor;



public class CustomInputProcessor implements InputProcessor {

    private int keyCode;
    private boolean scrolledUp;
    private boolean scrolledDown;

    @Override
    public boolean keyDown(int keycode) {
        keyCode = keycode;
        return false;
    }
    

    @Override
    public boolean keyUp(int keycode) {
        // Unimplemented method
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // Unimplemented method
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Unimplemented method
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Unimplemented method
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Unimplemented method
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // Unimplemented method
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (amountY < 0) {
            scrolledUp = true;
            scrolledDown = false;
        } else if (amountY > 0) {
            scrolledUp = false;
            scrolledDown = true;
        }
        return false;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public boolean wasScrolledUp() {
        boolean value = scrolledUp;
        scrolledUp = false;
        return value;
    }

    public boolean wasScrolledDown() {
        boolean value = scrolledDown;
        scrolledDown = false;
        return value;
    }
}
