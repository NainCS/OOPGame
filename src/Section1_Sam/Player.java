/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Section1_Sam; //Sook Ying Sam


import Section3_Edgar.GameObject;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 *
 * @author Sam SY
 */

// Code credit to RyiSnow for loading sprite game objects
// Video: https://www.youtube.com/watch?v=wT9uNGzMEM4

// Free use firefighter sprite source: 
// https://laredgames.itch.io/char-fireman

public class Player extends GameObject implements Controllable, Scorable, Displayable{
    private String name;
    private int waterSpray;
    private int lives;
    private int score;
    public int speed = 5;
    
    private BufferedImage playerSprite;

    public Player(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    // Sets some default values for the current player
    public Player() {
        waterSpray = 10;
        lives = 3;
        score = 0;

        this.width = 50;
        this.height = 70;
        this.positionX = 200;
        this.positionY = 300;

        this.edges = new Rectangle(positionX, positionY, width, height);
        loadSprite();
}
    
    public boolean sprayWater(){
        if (waterSpray > 0){
            waterSpray--;
            return true;
        }
        return false;
    }
    
    public void refillWater(){
        waterSpray = waterSpray;
    }
    
    public void loseLife(){
        lives--;
    }
    
    public void gainLife(){
        lives++;
    }
    
    
    @Override
    public void draw(Graphics g){
        if (isActive && playerSprite != null) {
            g.drawImage(playerSprite, positionX, positionY, width, height, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(positionX, positionY, width, height);
        }
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
    
    private void loadSprite(){
        try{
            playerSprite = ImageIO.read(getClass().getResource("firefighter.png"));
        }catch (IOException e){
            System.out.println("Player sprite load failed: " + e.getMessage());
        }
    }
}
