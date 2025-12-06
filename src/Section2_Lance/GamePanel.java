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

    // Imported NavController from section 3 as a way to switch between screens
    private NavController navigator;
    private int levelNumber;

    // Game objects
    private Player player;
    private ArrayList<Fire> fires;
    private ArrayList<Trees> trees;
    private WaterSpray waterSpray;
    
    // Key movement booleans that detect what direction to shoot the water spray
    // based on the last held key i.e. for WASD "W" = up "D" = right etc.
    private boolean up, down, left, right;
    private String facing = "RIGHT";

    private Level level;
    private CollisionDetector detector = new CollisionDetector();

    // hud and game variables
    private Timer frameTimer;
    private Timer oneSecondTimer;     
    private int score = 0;
    private int lives;
    private int water;
    private int waterUsed = 0;
    private int timeRemaining;
    private int firesExtinguished = 0;
    private int livesLost;
    private int timeUsed = 0;
    private String currentUser;
    private int sessionWaterUsed = 0;
    
    /**
     * Creates new form GamePanel
     */
    public GamePanel(NavController navigator, Level level, String username) {
        this.navigator = navigator;
        this.level = level;
        this.currentUser = username;
        this.levelNumber = level.getLevelNumber();

        // Lists of all the fires and trees used for the level
        fires = new ArrayList<>();
        trees = new ArrayList<>();

        initComponents();
        // Calls the setupGame method and timers once
        // all screen components have shown up on screen
        setupGame();       
        setupTimers();   

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
    }
    
    // Game setup
    private void setupGame(){

        // Creates the player and their spawn point
        player = new Player();
        player.setPosition(200, 300); // spawn point
        
        // Creates the water spray dimensions and settings
        waterSpray = new WaterSpray(120, 20, 1, 100, 0, 0, 20, 20);

        // Loads hud values from the Level class
        this.timeRemaining = (int) level.getGameTimer();
        this.water = level.getWater();
        this.lives = level.getLives();

        // Loads tree and fire objects from the lists
        for (Object obj : level.getObjects()) {

            if (obj instanceof Fire f)
                fires.add(f);

            if (obj instanceof Trees t)
                trees.add(t);
        }
        
        // This is used as a conditioner for winning the level
        // after extinguishing all fires
        level.setTotalFires(fires.size());
    }
    
    // Timer setups
    // There are two timers - frameTimer and oneSecondTimer:
    
    // frameTimer makes it so that the main game loop runs at 60fps
    // as this is usually standard for most modern games and keeps
    // gameplay smooth for the users.
    
    // oneSecondTimer is a timer for the time remaining in levels
    
    private void setupTimers(){

        frameTimer = new Timer(1000 / 60, this);
        frameTimer.start();
        
        // Every 1000ms, the time remaining for the level will reduce by 1 second
        oneSecondTimer = new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                timeRemaining--;
                timerLBL.setText("TIME: " + timeRemaining);

                // If the timer hits 0, run levelFailed method
                if (timeRemaining <= 0) {
                    levelFailed("Level failed, you have ran out of time!");
                }
            }
        });
        oneSecondTimer.start();
    }

    // Starts the timers when startGame method is called
    public void startGame(){
        frameTimer.start();
        oneSecondTimer.start();
    }
    
    // Method used to resume the game after it is paused
    // both timers are stopped, and then resumed after pressing
    // the resume button
    public void resumeGame(){
    if (frameTimer != null && !frameTimer.isRunning()){
        frameTimer.start();
    }
    if (oneSecondTimer != null && !oneSecondTimer.isRunning()){
        oneSecondTimer.start();
    }

    // Requests focus to the window to make sure that player inputs will work
    // Upon game testing user keyboard inputs were not detected when switching from
    // the MainMenuPanel to the GamePanel. The fix was to make it so that the 
    // GamePanel is forced to be in focus.
    // Code reference:
    // https://docs.oracle.com/javase/tutorial/uiswing/misc/focus.html
    this.setFocusable(true);
    this.requestFocusInWindow();
    SwingUtilities.invokeLater(() -> this.requestFocusInWindow());
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        // This is the main game loop method
        // This is called for every single frame ~60fps
        // This will call methods and check for their conditions
        // every frame so that the gameplay is responsive and 
        // so related areas are constantly updated
        updatePlayerMovement();
        updateGameObjects();
        checkCollisions();
        updateHUD();
        repaint();
    }

    // Handles the player movement for each frame
    private void updatePlayerMovement(){   
        // Gets the x and y position of the player so that if 
        // an object gets in the way (trees) it will block movement
        int oldX = player.getPositionX();
        int oldY = player.getPositionY();
        
        // Moves the player based on booleans set in
        // the keyPressed/keyReleased methods
        if (up) player.moveUp();
        if (down) player.moveDown();
        if (left) player.moveLeft();
        if (right) player.moveRight();
        player.update();

        // Blocks movement through trees by reverting the player
        // to the position of collision
        for (Trees t: trees){
            if (detector.checkCollision(player, t)){
                player.setPosition(oldX, oldY);
                break;
            }
        }

        // Makes the player lose a life when colliding with fire and then reduces their total lives by 1
        for (Fire f: fires){
            if (!f.isExtinguished() && detector.checkCollision(player, f)){
                lives--;
                livesLost++;

                if (lives <= 0){
                    levelFailed("You lost all your lives!");
                }
                
                // Spawns the player back at the spawn point
                player.setPosition(200, 300);
                break;
            }
        }
    }

    // Method to update the fire and tree objects
    // as well as waterSpray every frame
    private void updateGameObjects(){
        for (Fire f: fires) f.update();
        for (Trees t: trees) t.update();
        waterSpray.update();
    }

    // Updates the hud values based on what happens
    // in game so the user is kept updated during gameplay
    private void updateHUD(){
        livesLBL.setText("LIVES: " + lives);
        waterLBL.setText("WATER: " + (water - 1));
        scoreLBL.setText("SCORE: " + score);
        levelLBL.setText("LEVEL: " + levelNumber);
    }

    // Collision related methods which are checked every frame
    private void checkCollisions(){

        // Water extinguishing fire event handler
        // When the player sprays water, it will check if 
        // there is was a collision with a fire object and
        // will add 10 points to the score as well as adding to
        // the firesExtinguished counter used for the stat panels
        if (waterSpray.getActive()){
            for (Fire f: fires){
                if (!f.isExtinguished() && detector.checkCollision(waterSpray, f)){
                    f.setExtinguished(true);
                    score += 10;
                    level.setExtinguishedFires(level.getExtinguishedFires() + 1);
                    firesExtinguished++;
                }
            }
        }

        // Makes it so if you run out of water, you lose the game
        if (water <= 0){
            levelFailed("You ran out of water!");
            return;
        }

        // The level is completed when all fires are extinguished
        if (level.getExtinguishedFires() >= level.getTotalFires()){
            levelComplete();
        }
    }
    
    // Method for the spray water game feature
    private void sprayWater(){
        if (water <= 0){
            levelFailed("You ran out of water!");
            return;
        }

        // everytime the player uses waterSpray, it will
        // reduce their total water limit for the level
        // waterused will be used in the stat panels
        water -= 1;
        sessionWaterUsed++;
  

        // Setting up to get the players position
        // size p for player, s for size as well as x y positions
        // and width height values
        int px = player.getPositionX();
        int py = player.getPositionY();
        int pw = player.getWidth();
        int ph = player.getHeight();
        int sw = waterSpray.getWidth();
        int sh = waterSpray.getHeight();
        
        // This makes the water spray start on the current player
        // position but we want to adjust it so that it sprays
        // in the direction the user is facing based on their last
        // held key
        int sx = px;
        int sy = py;

        // Positions the spray in front of player, 
        // based on the direction they are facing
        if ("UP".equals(facing)) {
            // math to center it horizontally up from the player
            // same applies to all below other directions
            sx = px + pw / 2 - sw / 2;
            sy = py - sh - 10;
        } else if ("DOWN".equals(facing)) {
            sx = px + pw / 2 - sw / 2;
            sy = py + ph + 10;
            
        } else if ("LEFT".equals(facing)) {
            sx = px - sw - 10;
            sy = py + ph / 2 - sh / 2;
            
        } else if ("RIGHT".equals(facing)) {
            sx = px + pw + 10;
            sy = py + ph / 2 - sh / 2;
        }

        waterSpray.setPosition(sx, sy);
        waterSpray.setActive(true);
    }
    
    // Level Management Section
    private void levelComplete() {
        // Stops timers when the level has been completed
        // and will show a prompt window to show the user this
        // upon pressing ok to the prompt, it will send them to the next level
        frameTimer.stop();
        oneSecondTimer.stop();

        JOptionPane.showMessageDialog(
                this,
                "Level has been completed!\nPress OK to continue.",
                "Level Complete",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Update session stats for the time used
        int timeUsed = (int) level.getGameTimer()-timeRemaining;
        if (timeUsed < 0){
            timeUsed = 0;
        }
        navigator.updateSessionStats(score, timeUsed, firesExtinguished);
        // Updates NavController current level and starts next level
        navigator.levelCompleted(level.getLevelNumber());
        navigator.startNextLevel();
    }

    // Method called when the level is failed based on multiple
    // conditions such as all water used, all lives lost and time
    // ran out with appropriate output messages
    private void levelFailed(String reason){
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
        if (timeUsed < 0){
            timeUsed = 0;
        }

        navigator.updateSessionStats(score, timeUsed, firesExtinguished);

        // Shows the user they have failed
        JOptionPane.showMessageDialog(this,
        "Game Over. Returning you to the main menu.",
        "Your game session has ended.",
        JOptionPane.INFORMATION_MESSAGE);

        // Goes back to the main menu
        navigator.switchScreen("MainMenu");
    }
    
    // Draws the game screen every frame to update all game objects
    // from events that have happened.
    // This again happens every frame
    @Override
    protected void paintComponent(Graphics g){
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
    // I have set the default keyindings to WASD
    // usually standard for games
    @Override
    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()){
            case KeyEvent.VK_W:
                up = true;
                facing = "UP";
                break;
            case KeyEvent.VK_S:
                down = true;
                facing = "DOWN";
                break;
            case KeyEvent.VK_A:
                left = true;
                facing = "LEFT";
                break;
            case KeyEvent.VK_D:
                right = true;
                facing = "RIGHT";
                break;
            case KeyEvent.VK_SPACE:
                // Space bar is default to spray the water
                sprayWater();
                break;
            default:
                // Any other key inputs do nothing
                break;
        }
    }

    // Handles events related to when keys are released
    @Override
    public void keyReleased(KeyEvent e){
        switch (e.getKeyCode()){
            case KeyEvent.VK_W:
                up = false;
                break;
            case KeyEvent.VK_S:
                down = false;
                break;
            case KeyEvent.VK_A:
                left = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
            case KeyEvent.VK_SPACE:
                waterSpray.setActive(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e){ 
    }
    
    // Setters and getters
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

        pauseBTN.setBackground(new java.awt.Color(0, 92, 59));
        pauseBTN.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        pauseBTN.setForeground(new java.awt.Color(255, 255, 255));
        pauseBTN.setText("PAUSE");
        pauseBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseBTNActionPerformed(evt);
            }
        });

        levelLBL.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        levelLBL.setForeground(new java.awt.Color(255, 255, 255));
        levelLBL.setText("LEVEL:");
        levelLBL.setToolTipText("");

        livesLBL.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        livesLBL.setForeground(new java.awt.Color(255, 255, 255));
        livesLBL.setText("LIVES:");

        waterLBL.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        waterLBL.setForeground(new java.awt.Color(255, 255, 255));
        waterLBL.setText("WATER LEFT:");

        timerLBL.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        timerLBL.setForeground(new java.awt.Color(255, 255, 255));
        timerLBL.setText("TIME LEFT:");

        scoreLBL.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        scoreLBL.setForeground(new java.awt.Color(255, 255, 255));
        scoreLBL.setText("SCORE:");
        scoreLBL.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pauseBTN)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                .addComponent(levelLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92)
                .addComponent(livesLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98)
                .addComponent(waterLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(timerLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(scoreLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pauseBTN)
                    .addComponent(livesLBL)
                    .addComponent(waterLBL)
                    .addComponent(timerLBL)
                    .addComponent(scoreLBL)
                    .addComponent(levelLBL))
                .addContainerGap(1019, Short.MAX_VALUE))
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
