package com.mygdx.game.player;

public class HudMessage {
    private static String message;

    public HudMessage(String message) {
        HudMessage.message = message;
    }

    public HudMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        HudMessage.message = message;
    }
}
