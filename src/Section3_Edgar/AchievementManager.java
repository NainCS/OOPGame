/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Section3_Edgar;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Edgar Camacho
 */
public class AchievementManager {
    
    public ArrayList<Achievement> getAchievements(PlayerStats stats){
        ArrayList<Achievement> unlocked = new ArrayList<>();
        
        if (stats.totalGames >= 1){
            unlocked.add(new Achievement(
                    "FIRST_GAME",
                    "First_Game",
                    "Completed your first game."
            ));
        }
        
        
        if (stats.getAverage() >= 100) {
            unlocked.add(new Achievement(
                "GOOD_AVERAGE",
                "Nice Performance",
                "Get an average score of at least 100."
            ));
        }

        if (stats.bestScore >= 300) {
            unlocked.add(new Achievement(
                "HIGH_SCORE",
                "High Scorer",
                "Reached a score of 300 or more in a single game."
            ));
        }

        if (stats.bestTime > 0 && stats.bestTime <= 60) {
            unlocked.add(new Achievement(
                "SPEED_RUN",
                "Fast Player",
                "Finished a game in 60 seconds or less."
            ));
        }

        if (stats.firesExtinguished >= 20) {
            unlocked.add(new Achievement(
                "FIRE_HERO",
                "Firefighter Master",
                "Extinguished 10 or more fires."
            ));
        }

        return unlocked;
    }
}
