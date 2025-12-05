/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Section3_Edgar;

/**
 *
 * @author Edgar Camacho
 */
public interface NavController {
    public void switchScreen(String screenName);
    public void showResultsStatsFromGame(String username, int score, int time, boolean won);             
    public void startNextLevel();
    public void updateSessionStats(int score, int time, int fires);
    public void levelCompleted(int levelNumber);
    }
