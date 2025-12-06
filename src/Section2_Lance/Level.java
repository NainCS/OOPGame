/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Section2_Lance;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lance
 */

public class Level {
    private int levelNumber;
    private String name;
    private List<Object> objects; 
    private double gameTimer;
    private int totalFires;
    private int extinguishedFires;
    private boolean completed;
    private boolean failed;
    private int water;
    private int lives;

    public Level() {
        this.objects = new ArrayList<>();
    }

    public Level(int levelNumber, String name, double gameTimer, int totalFires, int extinguishedFires) {
        this();
        this.levelNumber = levelNumber;
        this.name = name;
        this.gameTimer = gameTimer;
        this.totalFires = totalFires;
        this.extinguishedFires = extinguishedFires;
    }

    // Setters and getters
    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGameTimer() {
        return gameTimer;
    }

    public void setGameTimer(double gameTimer) {
        this.gameTimer = gameTimer;
    }

    public int getTotalFires() {
        return totalFires;
    }

    public void setTotalFires(int totalFires) {
        this.totalFires = totalFires;
    }

    public int getExtinguishedFires() {
        return extinguishedFires;
    }

    public void setExtinguishedFires(int extinguishedFires) {
        this.extinguishedFires = extinguishedFires;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int w) {
        water = w;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int l) {
        lives = l;
    }
      
    // Checks if the level is complete
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // Checks if the level was failed
    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    // Adds game object to the level
    public void addObject(Object obj) { 
        this.objects.add(obj); 
    }
    
    // List of objects
    public List<Object> getObjects() {
        return objects;
    }

    // List of fires
    public int[] getFires() {
        return new int[0];
    }

    public Object getPlayer() {
        return null;
    }

    // Updates the level state over time and also checks the current
    // time left and overall game state with conditionals
    public void updateLevelState(double elapsedSeconds) {
        if (gameTimer > 0) {
            gameTimer -= elapsedSeconds;
            if (gameTimer <= 0) {
                failed = true;
            }
        }
        if (extinguishedFires >= totalFires) {
            completed = true;
        }
    }
}

