/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Section1_Sam; //Sook Ying Sam


import Section3_Edgar.GameObject;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
/**
 *
 * @author Sam SY
 */
public class Player extends GameObject implements Controllable, Scorable, Displayable{
    private String name;
    private int waterSpray;
    private int lives;
    private int score;
    public int speed = 5;

    public Player(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    
    public Player() {
        waterSpray = 10;
        lives = 3;
        score = 0;

        this.width = 50;
        this.height = 50;
        this.positionX = 200;
        this.positionY = 300;

        this.edges = new Rectangle(positionX, positionY, width, height);
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
    public void draw(Graphics g){
        g.setColor(Color.BLUE);
        g.fillRect(positionX, positionY, width, height);
    }
    @Override
    public void update(){
        edges.setLocation(positionX, positionY);
    }
    @Override
    public void moveUp() {
        positionY -= speed;
    }
    @Override
    public void moveDown() {
        positionY += speed;
    }
    @Override
    public void moveLeft() {
        positionX -= speed;
    }
    @Override
    public void moveRight() {
        positionX += speed;
    }
    @Override
    public void action(){
        sprayWater();
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
