/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Section3_Edgar;

/**
 *
 * @author Edgar Camacho
 */
public class PlayerStats {
    String username;
    int totalGames;
    int totalScore;
    int bestScore;
    int bestTime;
    int firesExtinguished;
    int completedLevels;
    
    public int getAverage(){
        return totalGames > 0 ? totalScore / totalGames : 0;
    }
}
