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
    private CardLayout cardLayout;
    private JPanel mainPanel;
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
    
    private int sessionScore = 0;
    private int sessionTime = 0;
    private int sessionFires = 0;
    private int sessionLevelsCompleted = 0;
    private int sessionWaterUsed = 0;
    
    
    private String currentUserName = "";
    public void setCurrentUser(String username) {
        this.currentUserName = username;
    }

    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainFrame.class.getName());

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        setTitle("Firefigher Game");
        setSize(1920,1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        setContentPane(mainPanel);
        
        levelManager = new LevelManager();
        gameController = new GameController(this);
        mainMenuPanel = new MainMenuPanel(this);
        gameStatsPanel = new GameStatsPanel(this);
        resultStatsPanel = new ResultStatsPanel(this);
        levelSelectPanel = new LevelSelectPanel(this);
        pauseMenuPanel = new PauseMenuPanel(this);
        settingsPanel = new SettingsPanel(this);
        
        mainPanel.add(mainMenuPanel, "MainMenu");
        mainPanel.add(gameStatsPanel, "GameStats");
        mainPanel.add(resultStatsPanel, "ResultStats");
        mainPanel.add(levelSelectPanel, "LevelSelect");
        mainPanel.add(pauseMenuPanel, "PauseMenu");
        mainPanel.add(settingsPanel, "Settings");
        
        
        setVisible(true);
        showScreen("MainMenu");
    }
    
    // This method gets called when a level is chosen by the user
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
            
        if (levelNumber == 1) {
            sessionScore = 0;
            sessionTime = 0;
            sessionFires = 0;
            sessionLevelsCompleted = 0;
        }

    }
    
    @Override
    public void updateSessionStats(int score, int time, int fires) {
        sessionScore += score;
        sessionTime += time;
        sessionFires += fires;
    }


    @Override
    public void levelCompleted(int lvl) {
        sessionLevelsCompleted++;
    }

    
    @Override
    public void startNextLevel() {
        int next = currentLevel + 1;

        if (next > 3) {

            // Save the lifetime stats for GameStatsPanel and PlayerStats
            saveLifetimeStats(currentUserName);
            
            resultStatsPanel.showSessionStats(
                currentUserName,
                sessionScore,
                sessionTime,
                sessionFires,
                sessionLevelsCompleted,
                sessionWaterUsed
            );

            JOptionPane.showMessageDialog(
                null,
                "Congratulations! You beat all levels!\n" +
                "Your progress has been saved.\n" +
                "Return to the Main Menu to view your stats.",
                "Game Complete",
                JOptionPane.INFORMATION_MESSAGE
            );

            switchScreen("MainMenu");
            return;
        }

        startLevel(next);
    }
    
    private void saveLifetimeStats(String username) {
        try {
            FileWriter fw = new FileWriter(username + "_stats.txt");
            PrintWriter out = new PrintWriter(fw);

            out.println("CompletedLevels:" + sessionLevelsCompleted);
            out.println("FiresExtinguished:" + sessionFires);
            out.println("TotalScore:" + sessionScore);
            out.println("BestTime:" + sessionTime);

            out.close();

        } catch (IOException e) {
            System.out.println("Error writing player stats: " + e.getMessage());
        }

        // Appending to the user's game history logs
        try {
            FileWriter fw = new FileWriter(username + "_games.txt", true);
            PrintWriter out = new PrintWriter(fw);

            out.println(sessionScore + "," + sessionTime + ",WIN");

            out.close();

        } catch (IOException e) {
            System.out.println("Error writing game history: " + e.getMessage());
        }
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
    public void showResultsStatsFromGame(String username, int score, int time, boolean won) {

        // Save game result to the user history
        resultStatsPanel.recordGameResult(username, score, time, won);

        // Shows the lifetime stats screen
        switchScreen("ResultStats");
    }

    
    private void saveFinalSessionToFile(String user, int totalScore, int totalLives, int totalWater){
        try (PrintWriter writer = new PrintWriter(new FileWriter(user + "_session.txt", true))) {

            writer.println("FinalSessionScore=" + totalScore);
            writer.println("FinalSessionLives=" + totalLives);
            writer.println("FinalSessionWater=" + totalWater);
            writer.println("----");

        } catch (IOException e) {
            System.out.println("Error saving session stats: " + e.getMessage());
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
