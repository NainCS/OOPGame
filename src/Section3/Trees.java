/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Section3;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
/**
 *
 * @author Edgar Camacho
 */
public class Trees extends GameObject{
    private String typeOfTree;
    private boolean flammable;
    private int durability;
    private boolean onFire;

    public Trees() {
        super();
        this.typeOfTree = "Oak";
        this.flammable = true;
        this.durability = 100;
        this.onFire = false;
    }

    public Trees(String typeOfTree, boolean flammable, int durability, boolean onFire, int positionX, int positionY, int witdh, int height) {
        super(positionX, positionY, witdh, height);
        this.typeOfTree = typeOfTree;
        this.flammable = flammable;
        this.durability = durability;
        this.onFire = false;
    }
    
    @Override
    public void resolveCollision(){
        if (colliding) {
            System.out.println("Tree is colliding with another sprite");
        }
    }
    
    public boolean collideWith(GameObject other) {
        return this.edges.intersects(other.getEdges());
    }
    
    //fire touches the tree
    public void lightUp(){
        if (flammable && !onFire){
            setOnFire(true);
        }      
    }
    
    //water in contact a burning tree
    public void extinguish(){
        if (onFire) {
            setOnFire(false);
        }
    }

    public String getTypeOfTree() {
        return typeOfTree;
    }

    public boolean isFlammable() {
        return flammable;
    }

    public int getDurability() {
        return durability;
    }

    public boolean isOnFire() {
        return onFire;
    }

    public void setTypeOfTree(String typeOfTree) {
        this.typeOfTree = typeOfTree;
    }

    public void setFlammable(boolean flammable) {
        this.flammable = flammable;
    }

    public void setDurability(int durability) {
        this.durability = durability;
        if (durability <= 0) {
            setIsActive(false);
        }
    }

    public void setOnFire(boolean onFire) {
        if (flammable) {
            this.onFire = onFire;
            if (onFire) {
                setColliding(true);   //spread fires
            }
        }
    }
    
    @Override
    public void update(){
        if (onFire && isActive) {
            durability -= 1;
            if (durability <= 0) {
                setIsActive(false);
            }
        }
        this.edges.setLocation(positionX, positionY);
    }
    
    @Override
    public void draw(Graphics g){
        if (isActive) {
            if (onFire) {
                g.setColor(Color.RED);
            }else {
                switch (typeOfTree.toLowerCase()){
                    case "Oak":
                        g.setColor(new Color(0, 100, 0));
                        break;
                    case "Pine":
                        g.setColor(new Color(34, 139, 30));
                        break;
                    case "Palm":
                        g.setColor(new Color(50, 200, 50));
                        break;
                    default:
                        g.setColor(Color.GREEN);
                }
            }
            
            // Draw trunk 
            g.setColor(new Color(140, 70, 20)); // Brown
            g.fillRect(positionX + width/3, positionY + height/2, width/3, height/2);
            
            // Draw foliage
            if (onFire) {
                g.setColor(Color.ORANGE); 
            } else {
                switch (typeOfTree.toLowerCase()) {
                    case "oak":
                        g.setColor(new Color(0, 150, 0));
                        break;
                    case "pine":
                        g.setColor(new Color(0, 180, 0));
                        break;
                    case "palm":
                        g.setColor(new Color(100, 255, 100));
                        break;
                    default:
                        g.setColor(new Color(0, 200, 0));
                }
            }
            g.fillOval(positionX, positionY, width, height/2);
            
            if (onFire) {
                g.setColor(Color.RED);
                int barWidth = (int)(width * (durability / 100.0));
                g.fillRect(positionX, positionY - 10, barWidth, 5);
                
                g.setColor(Color.BLACK);
                g.drawRect(positionX, positionY - 10, width, 5);
            }
        }
    }
    
    
}
