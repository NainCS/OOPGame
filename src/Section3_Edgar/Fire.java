/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Section3_Edgar;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 *
 * @author Edgar Camacho
 */

// Code credit to RyiSnow for loading sprite game objects
// Video: https://www.youtube.com/watch?v=wT9uNGzMEM4

// Free use fire intensity sprite sources:
// https://devkidd.itch.io/pixel-fire-asset-pack

public class Fire extends GameObject {
    private int intensity;
    private boolean extinguished;
    
    // Sprite images for 3 intensities
    private BufferedImage intensity1Sprite;
    private BufferedImage intensity2Sprite;
    private BufferedImage intensity3Sprite;
    
    public Fire() {
        super();
        this.intensity = 1;
        this.extinguished = false;
        loadSprites();
    }
    
    public Fire(int x, int y) {
        super(x, y, 50, 80);
        this.intensity = 1;
        this.extinguished = false;
        loadSprites();
    }

    public Fire(int intensity, int positionX, int positionY, int witdh, int height, boolean extinguished) {
        super(positionX, positionY, witdh, height);
        this.intensity = intensity;
        this.extinguished = extinguished;
        loadSprites();
    }
    
    private void loadSprites() {
        try {
            intensity1Sprite = ImageIO.read(getClass().getResource("fireintensity1.png"));
            intensity2Sprite = ImageIO.read(getClass().getResource("fireintensity2.png"));
            intensity3Sprite = ImageIO.read(getClass().getResource("fireintensity3.png"));
        } catch (IOException e) {
            System.out.println("Fire sprites missing: " + e.getMessage());
        }
    }
    
    @Override
    public void resolveCollision() {
        if (colliding && !extinguished) {
            setIntensity(intensity - 1);
            setColliding(false);
        }
    }
    
    public boolean collideWith(GameObject other) {
        return this.edges.intersects(other.getEdges());
    }

    //water putting out the fire
    public void extinguish(int waterStrength) {
        if (!extinguished) {
            setIntensity(intensity - waterStrength);
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
        } else if (intensity > 3) {
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
    public void update() {
        if (!extinguished && isActive) {
            if (Math.random() < 0.005 && intensity < 3) {
                setIntensity(intensity + 1);
            }
            this.edges.setLocation(positionX, positionY);
        }
    }    
    
    @Override 
    public void draw(Graphics g) {
        if (!extinguished && isActive) {
            Graphics2D g2d = (Graphics2D) g;
            
            // Draws the sprite based on intensity
            BufferedImage sprite = null;
            switch (intensity) {
                case 1: sprite = intensity1Sprite; break;
                case 2: sprite = intensity2Sprite; break;
                case 3: sprite = intensity3Sprite; break;
            }
            
            if (sprite != null) {
                g2d.drawImage(sprite, positionX, positionY, width, height, null);
            }
        }
    }
}
