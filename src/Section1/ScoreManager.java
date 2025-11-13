/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Section1;

import java.util.ArrayList;

/**
 *
 * @author Sam SY
 */
public class ScoreManager {
    ArrayList<Integer> scores = new ArrayList<>();
    
    public void addScore(int s){
        scores.add(s);
    }
    
    public void resetScores(){
        scores.clear();
    }
    
    public ArrayList<Integer> getTopScores(){
        return scores;
    }
}
