/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Section1_Sam; //Sook Ying Sam

/**
 *
 * @author Sam SY
 */
public class Player implements Controllable, Scorable, Displayable{
    private String name;
    private int waterSpray;
    private int lives;
    private int score;

    public Player(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    
    public Player(){
        waterSpray = 10;
        lives = 3;
        score = 0;
    }
    
    public boolean sprayWater(){
        if (waterSpray > 0){
            waterSpray--;
            return true;
        }
        return false;
    }
    
    public void refillWater(){waterSpray = waterSpray;}
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
    
    public String printDetails(){
        return "Details: Player  " + name + " is ready.";
    }
}
