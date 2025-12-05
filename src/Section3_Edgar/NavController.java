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
    public void levelFinished(int levelNumber);
    public void showResultsStatsFromGame(
            String username,
            int level,
            int score,
            int lives,
            int water, 
            int experience,
            int time,
            String result);             
    }
