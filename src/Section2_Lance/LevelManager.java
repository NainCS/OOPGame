/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Section2_Lance;
import Section3_Edgar.Fire;
import Section3_Edgar.Trees;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 *
 * @author Lance Wilde
 */
public class LevelManager implements GamePlayActions {

    private Level currentLevel;

    public LevelManager() {}

    /**
     * Loads a level from a TXT file and builds the Level object.
     */
    public Level loadLevelFile(String filename) {
        
        Level level = new Level();
        level.setLevelNumber(extractNumberFromFilename(filename));
        List<Object> objects = level.getObjects();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty() || line.startsWith("//")) continue;

                // LEVEL SETTINGS
                
                if (line.startsWith("TIME=")) {
                    level.setGameTimer(Integer.parseInt(line.substring(5)));
                    continue;
                }
                if (line.startsWith("WATER=")) {
                    level.setWater(Integer.parseInt(line.substring(6)));
                    continue;
                }
                if (line.startsWith("LIVES=")) {
                    level.setLives(Integer.parseInt(line.substring(6)));
                    continue;
                }

                // Fires

                if (line.startsWith("FIRE")) {
                    String[] p = line.split(" ");
                    int x = Integer.parseInt(p[1]);
                    int y = Integer.parseInt(p[2]);
                    objects.add(new Fire(x, y));
                    continue;
                }


                // Trees
                
                if (line.startsWith("TREE")) {
                    String[] p = line.split(" ");
                    int x = Integer.parseInt(p[1]);
                    int y = Integer.parseInt(p[2]);
                    objects.add(new Trees(x, y));
                    continue;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        currentLevel = level;
        return level;
    }

    //  GamePlayActions interface methods

    private int extractNumberFromFilename(String name) {
        try {
            return Integer.parseInt(name.replaceAll("\\D", ""));
        } catch (Exception e) {
            return 1;
        }
    }
    
    @Override
    public void startLevel(int levelNumber) {
    }

    @Override
    public void pauseGame() {
    }

    @Override
    public void resumeGame() {
    }

    @Override
    public void restartLevel() {
        if (currentLevel != null) {
            currentLevel.setExtinguishedFires(0);
            currentLevel.setFailed(false);
            currentLevel.setCompleted(false);
        }
    }

    @Override
    public boolean isLevelComplete() {
        return currentLevel != null && currentLevel.isCompleted();
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }
}
