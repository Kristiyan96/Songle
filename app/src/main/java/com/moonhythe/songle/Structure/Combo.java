package com.moonhythe.songle.Structure;

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

    // Set up combo
    public Combo(int combo, List<Placemark> placemarks) {
        this.combo = combo;
        this.placemarks = placemarks;

        collected_words = 0;

        switch (combo) {
            case 1:
                goal_collected_words = 3;
                seconds_lasting = 120;
                break;
            case 2:
                goal_collected_words = 4;
                seconds_lasting = 180;
                break;
            case 3:
                goal_collected_words = 5;
                seconds_lasting = 240;
                break;
            case 4:
                goal_collected_words = 6;
                seconds_lasting = 300;
                break;
            case 5:
                goal_collected_words = 10;
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

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public int getCollected_words() {
        return collected_words;
    }

    public void setCollected_words(int collected_words) {
        this.collected_words = collected_words;
    }

    public int getGoal_collected_words() {
        return goal_collected_words;
    }

    public int getSeconds_lasting() {
        return seconds_lasting;
    }

    public List<Placemark> getPlacemarks() {
        return placemarks;
    }

    public void setPlacemarks(List<Placemark> placemarks) {
        this.placemarks = placemarks;
    }
}
