/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Section1;

/**
 *
 * @author Sam SY
 */
public class Player implements Controllable, Scorable, Displayable{
    int waterCapacity;
    int maxWaterCapacity;
    int lives;
    int score;
    
    public Player(){
        waterCapacity = 10;
        maxWaterCapacity = 10;
        lives = 3;
        score = 0;
    }
    
    public boolean sprayWater(){
        if (waterCapacity > 0){
            waterCapacity--;
            return true;
        }
        return false;
    }
    
    public void moveUp(){waterCapacity = maxWaterCapacity;}
    public void loseLife(){lives--;}
    public void gainLife(){lives++;}
    
    @Override
    public void moveUp(){
    }
    @Override
    public void moveDown(){
    }
    @Override
    public void moveRight(){
    }
    @Override
    public void moveLeft(){
    }
    @Override
    public void action(){
    }
    @Override
    public int getScore(){
        return score;
    }
    @Override
    public void addScore(int points){
        score += points;
    }
    @Override
    public void resetScore(){
        score = 0;
    }
    @Override
    public void updateDisplay(){
        System.out.println("Player Display Update");
    }
}
