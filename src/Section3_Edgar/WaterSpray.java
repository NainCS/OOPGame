/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Section3_Edgar;

import java.awt.*;
/**
 *
 * @author Edgar Camacho
 */
public class WaterSpray extends GameObject{
    private int range;
    private int duration;
    private int strength;
    private boolean active;
    private int waterAmount;

    public WaterSpray() {
        super();
        this.range = 50;
        this.duration = 60;
        this.strength = 1;
        this.active = false;
        this.waterAmount = 100;
    }
    
    public WaterSpray(int x, int y) {
        super(x, y, 20, 20);
        this.range = 80;
        this.duration = 30;
        this.strength = 1;
        this.active = true;
        this.waterAmount = 100;
    }

    public WaterSpray(int range, int duration, int strength, int waterAmount, int positionX, int positionY, int witdh, int height) {
        super(positionX, positionY, witdh, height);
        this.range = range;
        this.duration = duration;
        this.strength = strength;
        this.active = false;
        this.waterAmount = waterAmount;
    }
    
    @Override
    public void resolveCollision(){
        if (colliding) {
            setColliding(false);
        }
    }
    
    public boolean collideWith(GameObject other){
        if (!active)return false;
    
        Rectangle sprayArea = new Rectangle(
                positionX - range /2,
                positionY - range /2,
                width + range,
                height + range
        );       
        return sprayArea.intersects(other.getEdges());
    }
   
    public void active(){
        if (waterAmount > 0 && !active) {
            this.active = true;
            this.setIsActive(true);
            this.waterAmount = Math.max(0, waterAmount - 5);
        }
    }
    
    public void stopSpraying(){
        this.active = false;
        this.waterAmount = 0;
        this.setIsActive(false);
    }
    
    public void refillWater(){
        this.waterAmount = 100;
    }
    
    public boolean canSpray(){
        if (waterAmount > 0 && !active) {
            return true;
        }
        return false;
    }
    
    public boolean isSprayingWater(){
        if (active && !isActive) {
            return true;
        }
        return false;
    }

    //getters and setters
    public int getRange() {
        return range;
    }

    public int getDuration() {
        return duration;
    }

    public int getStrength() {
        return strength;
    }

    public boolean getActive() {
        return active;
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setActive(boolean active) {
        this.active = active;
        this.setIsActive(active);
        if (active){
            this.duration = 20;
        }else{
            this.duration = 0;
        }
    }

    public void setWaterAmount(int waterAmount) {
        this.waterAmount = waterAmount;
    }
    
    public Rectangle getSprayArea() {
        // Area of effect for water spraying
        return new Rectangle(
            positionX - range / 2,
            positionY - range / 2,
            width + range,
            height + range
        );
    }
    
    @Override
     public void update() {
        this.edges.setLocation(positionX, positionY);

        if (active && isActive) {
            duration--;
            if (duration <= 0) {
                stopSpraying();
            }
        }
    }
     
    @Override
    public void draw(Graphics g) {
        if (active && isActive) {
            // Draw water spray effect
            g.setColor(new Color(100, 150, 255, 150)); 
            
            // spray area
            int spraySize = range;
            g.fillOval(
                positionX - spraySize/2 + width/2, 
                positionY - spraySize/2 + height/2, 
                spraySize, 
                spraySize
            );
        }
        
//        //draw water tank
//        g.setColor(Color.BLUE);
//        g.fillRect(positionX, positionY, width, height);
//        
//        // water level
//        g.setColor(Color.CYAN);
//        int waterHeight = (int)(height * (waterAmount / 100.0));
//        g.fillRect(positionX, positionY + (height - waterHeight), width, waterHeight);
//        
//        //tank border
//        g.setColor(Color.BLACK);
//        g.drawRect(positionX, positionY, width, height);
    }
    
}
