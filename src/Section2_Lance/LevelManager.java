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
 * @author Lance
 */

// Code credit to KaarinGaming for the developement of this class
// Video: https://www.youtube.com/watch?v=et5JeT-ESKk

public class LevelManager implements GamePlayActions {

    private Level currentLevel;

    public LevelManager() {}

    // Loads the level from a text file, which values are edited for
    // the level layout and is parsed to be compatible 
    public Level loadLevelFile(String filename){
        
        Level level = new Level();
        level.setLevelNumber(extractNumberFromFilename(filename));
        List<Object> objects = level.getObjects();

        // File gets closed after it is read
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;

            while ((line = reader.readLine()) != null){

                // Parsing Section
                // removes leading/trailing whitespace and empty lines
                line = line.trim();
                // Skips comments for better reading
                if (line.isEmpty() || line.startsWith("//")) continue;

                // Level settings syntax          
                
                if (line.startsWith("TIME=")){
                    level.setGameTimer(Integer.parseInt(line.substring(5)));
                    continue;
                }
                if (line.startsWith("WATER=")){
                    level.setWater(Integer.parseInt(line.substring(6)));
                    continue;
                }
                if (line.startsWith("LIVES=")){
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

        }catch (Exception e){
            e.printStackTrace();
        }
        currentLevel = level;
        return level;
    }

    //  GamePlayActions interface methods

    // Gets the current level number from the text file name
    private int extractNumberFromFilename(String name){
        try{
            String filenameOnly = new java.io.File(name).getName();
            // "\\D" gets rid of all non digits in the file name
            String digits = filenameOnly.replaceAll("\\D", "");   
            return Integer.parseInt(digits);
        } catch (Exception e) {
            System.out.println("Parse failed");
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
    public boolean isLevelComplete() {
        return currentLevel != null && currentLevel.isCompleted();
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }
}
