package com.mygdx.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.ArrayList;

public class AchievementManager {
    private static final String ACHIEVEMENTS_PREFS = "achievements";
    
    private static ArrayList<Achievement> achievements = new ArrayList<>();
    private Preferences prefs;
    
    public AchievementManager() {
        prefs = Gdx.app.getPreferences(ACHIEVEMENTS_PREFS);
        
        // Create and add achievements to the list
        achievements.add(new Achievement("TIMBER", "Gather a log from a tree."));
        achievements.add(new Achievement("Toy or tool?", "Craft a wooden pickaxe."));
        achievements.add(new Achievement("Rock solid", "Mine a stone with a pickaxe."));
        achievements.add(new Achievement("Stone age", "Craft a stone pickaxe."));
        achievements.add(new Achievement("Let there be light!", "Craft a torch."));
        achievements.add(new Achievement("Ironworks", "Smelt iron in a furnace."));
        achievements.add(new Achievement("To the moon", "Build a rocketship."));
        
        // Load the unlocked status of each achievement from preferences
        /*for (Achievement achievement : achievements) {
            boolean unlocked = prefs.getBoolean(achievement.getName(), false);
            achievement.setUnlocked(unlocked);
        }*/
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
    
    public static ArrayList<Achievement> getAchievements() {
        return achievements;
    }
}