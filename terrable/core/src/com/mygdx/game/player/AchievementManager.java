package com.mygdx.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

public class AchievementManager {
    private static final String ACHIEVEMENTS_PREFS = "achievements";
    
    private Array<Achievement> achievements;
    private Preferences prefs;
    
    public AchievementManager() {
        achievements = new Array<>();
        prefs = Gdx.app.getPreferences(ACHIEVEMENTS_PREFS);
        
        // Create and add achievements to the list
        achievements.add(new Achievement("First Achievement", "You unlocked your first achievement!"));
        achievements.add(new Achievement("Build a rocketship", "Build a rocket ship and fly to the moon."));
        
        // Load the unlocked status of each achievement from preferences
        for (Achievement achievement : achievements) {
            boolean unlocked = prefs.getBoolean(achievement.getName(), false);
            achievement.setUnlocked(unlocked);
        }
    }
    
    public void unlockAchievement(String name) {
        // Find the achievement with the given name and set its unlocked status to true
        for (Achievement achievement : achievements) {
            if (achievement.getName().equals(name)) {
                achievement.setUnlocked(true);
                prefs.putBoolean(name, true);
                prefs.flush();
                break;
            }
        }
    }
    
    public Array<Achievement> getAchievements() {
        return achievements;
    }
}