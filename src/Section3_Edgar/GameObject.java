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
public abstract class GameObject implements CollisionActions{
    
    protected int positionX;
    protected int positionY;
    protected int width;
    protected int height;
    protected boolean colliding = false;
    protected boolean isActive = true;
    protected Rectangle edges;

    public GameObject() {
    this.positionX = 0;
    this.positionY = 0;
    this.width = 50;
    this.height = 50;
    this.edges = new Rectangle(positionX, positionY, width, height);
    }

    public GameObject(int positionX, int positionY, int width, int height) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
        this.edges = new Rectangle(positionX, positionY, width, height);
    }
    
    @Override
    public Rectangle getEdges(){
        return edges;
    }  
    public Rectangle getBounds(){
        return edges;
    }
    
    @Override
    public void resolveCollision(){
    } 
    
    public abstract void draw(Graphics g);
    public abstract void update();
    
    //getters and setters
    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isColliding() {
        return colliding;
    }

    public boolean isIsActive() {
        return isActive;
    }
    
    public void setPosition(int positionX, int positionY){
        this.positionX = positionX;
        this.positionY = positionY;
        this.edges.setLocation(positionX, positionY);
    }

    public void setColliding(boolean colliding) {   
        this.colliding = colliding;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
  
}
