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
public class Fire extends GameObject{
    private int intensity;
    private boolean extinguished;

    public Fire() {
        super();
        this.intensity = 1;
        this.extinguished = false;
    }

    public Fire(int intensity, int positionX, int positionY, int witdh, int height, boolean extinguished) {
        super(positionX, positionY, witdh, height);
        this.intensity = intensity;
        this.extinguished = extinguished;
    }
    
    @Override
    public void resolveCollision(){
        if (colliding && !extinguished) {
            
            setIntensity(intensity - 1);
            
            setColliding(false);
        }
    }
    
    public boolean collideWith(GameObject other) {
        return this.edges.intersects(other.getEdges());
    }

    //water putting out the fire
    public void extinguish(int waterStrength){
        if (!extinguished) {
            setIntensity(intensity - waterStrength);
        }
    }
    
    public void spreadToTree(Trees tree){
        if (!extinguished && tree.isFlammable() && !tree.isOnFire()) {
            tree.setOnFire(true);
        }
    }
    
    
    public int getIntensity() {
        return intensity;
    }

    public boolean isExtinguished() {
        return extinguished;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
        if (intensity <= 0) {
            this.extinguished = true;
            setIsActive(false);
        }else if (intensity > 3) {
            this.intensity = 3;
        }
    }

    public void setExtinguished(boolean extinguished) {
        this.extinguished = extinguished;
        if (extinguished) {
            setIsActive(false);
            this.intensity = 0;
        }
    }
    
    @Override
    public void update(){
        if (!extinguished && isActive) {
            if (Math.random() < 0.005 && intensity < 3) {
                setIntensity(intensity + 1);
            }
            this.edges.setLocation(positionX, positionY);
        }
    }    
    
    @Override 
    public void draw(Graphics g){
        if (!extinguished && isActive) {
            switch (intensity){
                case 1:
                    g.setColor(Color.YELLOW);
                    break;
                case 2:
                    g.setColor(Color.ORANGE);
                    break;
                case 3:
                    g.setColor(Color.RED);
                    break;
                default:
                    g.setColor(Color.YELLOW);
            }
            
            g.fillRect(positionX, positionY, width, height);
            
        }
    }
    
}
