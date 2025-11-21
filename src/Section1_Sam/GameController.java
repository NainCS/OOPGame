/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Section1_Sam; //Sook Ying Sam

/**
 *
 * @author Sam SY
 */
public class GameController {
    private MainFrame mainFrame;
    private Player player;
    private ScoreManager scoreManager;

    public GameController(MainFrame frame) {
        this.mainFrame = frame;
        player = new Player();
        scoreManager = new ScoreManager();
    }
    
    public void startGame(){
        mainFrame.showScreen("Game");
    }
    
    public void showResults(){
        mainFrame.showScreen("Result");
    }
    
}
