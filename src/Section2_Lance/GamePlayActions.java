/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Section2_Lance;

/**
 *
 * @author lwild_oeg6x3t
 */
public interface GamePlayActions {
    void startLevel(int levelNumber);
    void pauseGame();
    void resumeGame();
    void restartLevel();
    boolean isLevelComplete();
    
}
