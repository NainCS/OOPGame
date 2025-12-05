/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Section2_Lance;

import Section1_Sam.Player;
import Section3_Edgar.Fire;
import Section3_Edgar.NavController;
import Section3_Edgar.Trees;
import Section3_Edgar.WaterSpray;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Lance
 */
public class GamePanel extends javax.swing.JPanel implements ActionListener, KeyListener {

    private NavController navigator;
    private int levelNumber;

    // Game objects
    private Player player;
    private ArrayList<Fire> fires;
    private ArrayList<Trees> trees;
    private WaterSpray waterSpray;
    
    // Key movement booleans
    private boolean up, down, left, right;
    private String facing = "RIGHT";

    private Level level;
    private CollisionDetector detector = new CollisionDetector();

    // HUD & Game variables
    private Timer frameTimer;         
    private Timer oneSecondTimer;     
    private int score = 0;
    private int lives;
    private int water;
    private int waterUsed = 0;
    private int sessionFires;
    private int timeRemaining;
    private int firesExtinguished = 0;
    private int livesLost;
    private int timeUsed = 0;
    private String currentUser;

    

    public GamePanel(NavController navigator, Level level, String username) {
        this.navigator = navigator;
        this.level = level;
        this.currentUser = username;
        this.levelNumber = level.getLevelNumber();


        fires = new ArrayList<>();
        trees = new ArrayList<>();

        initComponents(); 
        setupGame();       
        setupTimers();   

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
    }

    // Game setup
    private void setupGame() {

        // Player stays same
        player = new Player();
        player.setPosition(200, 300);

        waterSpray = new WaterSpray(100, 20, 1, 100, 0, 0, 20, 20);

        // Loads hud values from the Level class
        this.timeRemaining = (int) level.getGameTimer();
        this.water = level.getWater();
        this.lives = level.getLives();

        // Loads objects
        for (Object obj : level.getObjects()) {

            if (obj instanceof Fire f)
                fires.add(f);

            if (obj instanceof Trees t)
                trees.add(t);
        }

        level.setTotalFires(fires.size());
    }
    
    // Timer setups
    private void setupTimers() {

        frameTimer = new Timer(16, this);
        frameTimer.start();

        oneSecondTimer = new Timer(1000, e -> {
            timeRemaining--;
            timerLBL.setText("TIME: " + timeRemaining);

            if (timeRemaining <= 0) {
                levelFailed("Time ran out!");
            }
        });
        oneSecondTimer.start();
    }

    public void startGame() {
        frameTimer.start();
        oneSecondTimer.start();
    }
    
    public void resumeGame() {
    if (frameTimer != null && !frameTimer.isRunning()) {
        frameTimer.start();
    }
    if (oneSecondTimer != null && !oneSecondTimer.isRunning()) {
        oneSecondTimer.start();
    }

    // Request focus to the window to make sure that player inputs will work
    this.setFocusable(true);
    this.requestFocusInWindow();
    SwingUtilities.invokeLater(() -> this.requestFocusInWindow());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updatePlayerMovement();
        updateGameObjects();
        checkCollisions();
        updateHUD();
        repaint();
    }

    private void updatePlayerMovement() {
        
        int oldX = player.getPositionX();
        int oldY = player.getPositionY();

        if (up) player.moveUp();
        if (down) player.moveDown();
        if (left) player.moveLeft();
        if (right) player.moveRight();

        player.update();

        // Blocks movement through trees
        for (Trees t : trees) {
            if (detector.checkCollision(player, t)) {
                player.setPosition(oldX, oldY);
                break;
            }
        }

        // Makes the player lose a life when colliding with fire and then reduces their total lives by 1
        for (Fire f : fires) {
            if (!f.isExtinguished() && detector.checkCollision(player, f)) {

                lives--;
                livesLost++;

                if (lives <= 0) {
                    levelFailed("You lost all your lives!");
                }

                player.setPosition(200, 300);
                break;
            }
        }
    }

    private void updateGameObjects() {
        for (Fire f : fires) f.update();
        for (Trees t : trees) t.update();
        waterSpray.update();
    }

    private void updateHUD() {
        livesLBL.setText("LIVES: " + lives);
        waterLBL.setText("WATER: " + water);
        scoreLBL.setText("SCORE: " + score);
        levelLBL.setText("LEVEL: " + levelNumber);
    }

    // Collision related methods
    private void checkCollisions() {

        // Water extinguishing fire relation
        if (waterSpray.getActive()) {
            for (Fire f : fires) {

                if (!f.isExtinguished() && detector.checkCollision(waterSpray, f)) {

                    f.setExtinguished(true);
                    score += 10;

                    level.setExtinguishedFires(level.getExtinguishedFires() + 1);
                    firesExtinguished++;
                }
            }
        }

        // Makes it so if you run out of water, you lose the game
        if (water <= 0) {
            levelFailed("You ran out of water!");
            return;
        }

        // The level is completed when all fires are extinguished
        if (level.getExtinguishedFires() >= level.getTotalFires()) {
            levelComplete();
        }
    }
    
    private void sprayWater() {
        if (water <= 0) {
            levelFailed("You ran out of water!");
            return;
        }

        water -= 1;
        waterUsed++;

        int px = player.getPositionX();
        int py = player.getPositionY();
        int pw = player.getWidth();
        int ph = player.getHeight();
        int sw = waterSpray.getWidth();
        int sh = waterSpray.getHeight();

        int sx = px;
        int sy = py;

        switch (facing) {
            case "UP" -> {
                sx = px + pw/2 - sw/2;
                sy = py - sh - 10;
            }
            case "DOWN" -> {
                sx = px + pw/2 - sw/2;
                sy = py + ph + 10;
            }
            case "LEFT" -> {
                sx = px - sw - 10;
                sy = py + ph/2 - sh/2;
            }
            case "RIGHT" -> {
                sx = px + pw + 10;
                sy = py + ph/2 - sh/2;
            }
        }

        waterSpray.setPosition(sx, sy);
        waterSpray.setActive(true);
    }
    
    // Level Management Section
    private void levelComplete() {
        frameTimer.stop();
        oneSecondTimer.stop();

        JOptionPane.showMessageDialog(
                this,
                "Level Complete!\nPress OK to continue.",
                "Level Complete",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Update session stats
        int timeUsed = (int) level.getGameTimer() - timeRemaining;
        if (timeUsed < 0) timeUsed = 0;

        navigator.updateSessionStats(score, timeUsed, firesExtinguished);

        navigator.levelCompleted(level.getLevelNumber());

        navigator.startNextLevel();
    }

    private void levelFailed(String reason) {

        frameTimer.stop();
        oneSecondTimer.stop();

        JOptionPane.showMessageDialog(
                this,
                reason,
                "Level Failed",
                JOptionPane.ERROR_MESSAGE
        );

        // Will update the current player's session stats even if they fail a level
        int timeUsed = (int) level.getGameTimer() - timeRemaining;
        if (timeUsed < 0) timeUsed = 0;

        navigator.updateSessionStats(score, timeUsed, firesExtinguished);

        JOptionPane.showMessageDialog(this,
        "Game Over. Returning to the main menu.",
        "Your game sesssion has ended.",
        JOptionPane.INFORMATION_MESSAGE);

        // Send to results panel (end of session)
        navigator.switchScreen("MainMenu");
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background colour
        g.setColor(new Color(80, 150, 80));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draws the objects
        player.draw(g);
        waterSpray.draw(g);

        for (Fire f : fires) f.draw(g);
        for (Trees t : trees) t.draw(g);
    }

    // User input section for keys pressed etc.
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                up = true;
                facing = "UP";
            }
            case KeyEvent.VK_S -> {
                down = true;
                facing = "DOWN";
            }
            case KeyEvent.VK_A -> {
                left = true;
                facing = "LEFT";
            }
            case KeyEvent.VK_D -> {
                right = true;
                facing = "RIGHT";
            }

            case KeyEvent.VK_SPACE -> sprayWater();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = false;
            case KeyEvent.VK_S -> down = false;
            case KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_D -> right = false;

            case KeyEvent.VK_SPACE -> waterSpray.setActive(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { 
    
    }
    
    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getWater() {
        return water;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public int getLevelNumber() {
        return level.getLevelNumber();
    }

    public int getWaterUsed() {
        return waterUsed;
    }
    
    public int getFiresExtinguished() {
        return firesExtinguished;
    }
    
    public int getLivesLost() {
        return livesLost;
    }
    
    public int getTimeUsed() {
        return timeUsed;
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pauseBTN = new javax.swing.JButton();
        levelLBL = new javax.swing.JLabel();
        livesLBL = new javax.swing.JLabel();
        waterLBL = new javax.swing.JLabel();
        timerLBL = new javax.swing.JLabel();
        scoreLBL = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1920, 1080));

        pauseBTN.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        pauseBTN.setText("PAUSE");
        pauseBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseBTNActionPerformed(evt);
            }
        });

        levelLBL.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        levelLBL.setText("LEVEL:");
        levelLBL.setToolTipText("");

        livesLBL.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        livesLBL.setText("LIVES:");

        waterLBL.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        waterLBL.setText("WATER LEFT:");

        timerLBL.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        timerLBL.setText("TIME LEFT:");

        scoreLBL.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        scoreLBL.setText("SCORE:");
        scoreLBL.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pauseBTN)
                .addGap(305, 305, 305)
                .addComponent(livesLBL)
                .addGap(128, 128, 128)
                .addComponent(waterLBL)
                .addGap(112, 112, 112)
                .addComponent(timerLBL)
                .addGap(145, 145, 145)
                .addComponent(scoreLBL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 233, Short.MAX_VALUE)
                .addComponent(levelLBL)
                .addGap(113, 113, 113))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pauseBTN))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(timerLBL)
                            .addComponent(waterLBL)
                            .addComponent(livesLBL)
                            .addComponent(scoreLBL)
                            .addComponent(levelLBL))))
                .addContainerGap(995, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void pauseBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseBTNActionPerformed
        // TODO add your handling code here:
        frameTimer.stop();
        oneSecondTimer.stop();
        navigator.switchScreen("PauseMenu");
    }//GEN-LAST:event_pauseBTNActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel levelLBL;
    private javax.swing.JLabel livesLBL;
    private javax.swing.JButton pauseBTN;
    private javax.swing.JLabel scoreLBL;
    private javax.swing.JLabel timerLBL;
    private javax.swing.JLabel waterLBL;
    // End of variables declaration//GEN-END:variables
}
