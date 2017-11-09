package com.moonhythe.songle.Structure;

import com.moonhythe.songle.GameManager.GameData;
import com.moonhythe.songle.GameManager.GameLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 04/11/17.
 */

public class Combo {

    private static final String TAG = Combo.class.getSimpleName();
    private int combo;
    private int collected_words;
    private int goal_collected_words;
    private int seconds_lasting;
    private List<Placemark> placemarks = new ArrayList<Placemark>();
    private GameLogic game;
    private GameData data;

    // Set up combo
    public Combo(int combo, List<Placemark> placemarks, GameData game_data, GameLogic game_logic) {
        this.combo = combo;
        this.placemarks = placemarks;
        this.game = game_logic;
        this.data = game_data;

        collected_words = 0;

        switch (combo) {
            case 1:
                goal_collected_words = 5;
                seconds_lasting = 120;
                break;
            case 2:
                goal_collected_words = 8;
                seconds_lasting = 180;
                break;
            case 3:
                goal_collected_words = 12;
                seconds_lasting = 240;
                break;
            case 4:
                goal_collected_words = 16;
                seconds_lasting = 300;
                break;
            case 5:
                goal_collected_words = 30;
                seconds_lasting = 600;
                break;
            default:
                goal_collected_words = 20;
                seconds_lasting = 2;
                break;
        }
    }

    public int getCombo() {
        return combo;
    }

    public int getCollected_words() {
        return collected_words;
    }

    public void setCollected_words(int collected_words) {
        this.collected_words = collected_words;
        if(collected_words>=goal_collected_words){
            data.incrementCombo();
        }
    }

    public int getGoal_collected_words() {
        return goal_collected_words;
    }

    public int getSeconds_lasting() {
        return seconds_lasting;
    }

    public void setSeconds_lasting(int seconds_lasting) {
        this.seconds_lasting = seconds_lasting;
        if(seconds_lasting<=0){
            data.resetCombo();
        }
    }

    public List<Placemark> getPlacemarks() {
        return placemarks;
    }

    public void clearMap(){
        for(Placemark placemark : placemarks){
            placemark.deleteMarker();
        }
    }
}
