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

// Free use tree sprite sources: 
// https://bdragon1727.itch.io/pixel-tree-all

public class Trees extends GameObject {
    private String typeOfTree;

    private BufferedImage oakTreeSprite;
    private BufferedImage pineTreeSprite;

    public Trees(int x, int y) {
        super(x, y, 80, 120);
        this.typeOfTree = "Oak";
        loadSprites();
    }

    private void loadSprites() {
        try {
            // Loads the tree sprites from the same package directory
            oakTreeSprite = ImageIO.read(getClass().getResource("oaktree.png"));
            pineTreeSprite = ImageIO.read(getClass().getResource("pinetree.png"));
        } catch (IOException e) {
            System.out.println("Error loading tree sprite: " + e.getMessage());
        }
    }

    @Override
    public void update() {
        this.edges.setLocation(positionX, positionY);
    }

    @Override
    public void draw(Graphics g) {
        if (isActive) {
            BufferedImage sprite = null;
            switch (typeOfTree.toLowerCase()) {
                case "oak":
                    sprite = oakTreeSprite;
                    break;
                case "pine":
                    sprite = pineTreeSprite;
                    break;
            }

            if (sprite != null) {
                g.drawImage(sprite, positionX, positionY, width, height, null);
            }
        }
    }

    public String getTypeOfTree() {
        return typeOfTree;
    }

    public void setTypeOfTree(String typeOfTree) {
        this.typeOfTree = typeOfTree;
    }
}
