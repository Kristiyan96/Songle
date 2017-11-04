package com.moonhythe.songle.Structure;

/**
 * Created by kris on 04/11/17.
 */

public class Combo {

    private static final String TAG = Combo.class.getSimpleName();
    private int combo;
    private int collected_words;
    private int goal_collected_words;
    private int seconds_lasting;

    // Set up combo
    public Combo(int combo) {
        this.combo = combo;

        collected_words = 0;

        switch (combo) {
            case 0:
                goal_collected_words = 3;
                seconds_lasting = 120;
                break;
            case 1:
                goal_collected_words = 4;
                seconds_lasting = 180;
                break;
            case 2:
                goal_collected_words = 5;
                seconds_lasting = 240;
                break;
            case 3:
                goal_collected_words = 6;
                seconds_lasting = 300;
                break;
            case 4:
                goal_collected_words = 10;
                seconds_lasting = 600;
                break;
            default:
                goal_collected_words = 20;
                seconds_lasting = 2;
                break;
        }
    }


}
