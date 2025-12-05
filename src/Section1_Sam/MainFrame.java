/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Section1_Sam; //Sook Ying Sam

import Section2_Lance.GamePanel;
import Section2_Lance.Level;
import Section2_Lance.LevelManager;
import Section2_Lance.LevelSelectPanel;
import Section2_Lance.PauseMenuPanel;
import Section2_Lance.SettingsPanel;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import Section3_Edgar.GameStatsPanel;
import Section3_Edgar.NavController;
import Section3_Edgar.ResultStatsPanel;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

/**
 *
 * @author Sam SY
 */
public class MainFrame extends javax.swing.JFrame implements NavController{
    private CardLayout cardLayout; //switching between screen
    private JPanel mainPanel; //holds all screens
    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;
    private GameController gameController;
    private GameStatsPanel gameStatsPanel;
    private ResultStatsPanel resultStatsPanel;
    private LevelSelectPanel levelSelectPanel;
    private PauseMenuPanel pauseMenuPanel;
    private SettingsPanel settingsPanel;
    
    private int currentLevel = 1;
    private LevelManager levelManager;
    
    private String currentUserName = "";
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainFrame.class.getName());

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        setTitle("Firefigher Game");
        setSize(1920,1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closing the window ends the program
        setLocationRelativeTo(null); //centers the window on screen
        
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        setContentPane(mainPanel); //tells that mainPanel is the main display area
        
        levelManager = new LevelManager();
        gameController = new GameController(this); //create the controller
        mainMenuPanel = new MainMenuPanel(this);
        gameStatsPanel = new GameStatsPanel(this);
        resultStatsPanel = new ResultStatsPanel(this);
        levelSelectPanel = new LevelSelectPanel(this);
        pauseMenuPanel = new PauseMenuPanel(this);
        settingsPanel = new SettingsPanel(this);
        
       //add all screen to CardLayout
        mainPanel.add(mainMenuPanel, "MainMenu");
        mainPanel.add(gameStatsPanel, "GameStats");
        mainPanel.add(resultStatsPanel, "ResultStats");
        mainPanel.add(gamePanel, "Game");
        mainPanel.add(levelSelectPanel, "LevelSelect");
        mainPanel.add(pauseMenuPanel, "PauseMenu");
        mainPanel.add(settingsPanel, "Settings");
        
        
        setVisible(true);
        showScreen("MainMenu"); //show the main menu first
    }
    
    // Called when user picks a level
    public void startLevel(int levelNumber) {

        currentLevel = levelNumber;

        Level level = levelManager.loadLevelFile("src/Section2_Lance/Level" + levelNumber + ".txt");

        if (gamePanel != null)
            mainPanel.remove(gamePanel);

        gamePanel = new GamePanel(this, level, currentUserName);

        mainPanel.add(gamePanel, "Game");
        gamePanel.startGame();

        switchScreen("Game");
        gamePanel.requestFocusInWindow();
    }
    
    public void startNextLevel() {
        int next = currentLevel + 1;

        if (next > 3) { // assuming you have 3 levels
            JOptionPane.showMessageDialog(null, "All levels complete!");
            switchScreen("MainMenu");
            return;
        }

        startLevel(next);
    }
    
    public void levelFinished(int levelNumber) {
        this.currentLevel = levelNumber;  
    }

    @Override
    public void switchScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }
    
    public void showScreen(String name){
        cardLayout.show(mainPanel, name);
    }
    
    public void showResultsStats(String playerName) {
        this.currentUserName = playerName;
        resultStatsPanel.loadPlayerStats(playerName);
        showScreen("ResultStats");
    }
    
    public GameController getGameController(){
        return gameController;
    }
    
    public GamePanel getGamePanel() {
        return gamePanel;
    }
    
    public ResultStatsPanel getResultStatsPanel() {
        return resultStatsPanel;
    }
    
   
    
    @Override
    public void showResultsStatsFromGame(
            String username,
            int level,
            int score,
            int lives,
            int water,
            int experience,
            int time,
            String result) {
        
        this.currentUserName = username;

        // Update Edgar's ResultStatsPanel
        resultStatsPanel.updatePlayerStats(
                username,
                level,
                score,
                lives,
                water,
                experience
        );
        
        showScreen("ResultStats");
    }
    
    private int calculateExperience(int score,
                                    int lives,
                                    int water,
                                    int timeRemaining,
                                    int level) {

        int exp = 0;
        exp += score / 10;
        exp += lives * 5;
        exp += timeRemaining / 2;
        if (water > 50) exp += 10;
        exp += (level - 1) * 10;

        // clamp 0–100
        if (exp < 0) exp = 0;
        if (exp > 100) exp = 100;

        return exp;
    }
    
    private void appendGameResult(String username, int score, int time, String result){
        try (PrintWriter writer = new PrintWriter(new FileWriter(username + "_games.txt", true))) {
            writer.println(score + ","+time+","+result);
        }catch(IOException e){
            System.out.println("Error saving game result: "+e.getMessage());
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
