package com.moonhythe.songle.Logic;

import android.content.Context;

import com.moonhythe.songle.Structure.Combo;

/**
 * Created by kris on 06/11/17.
 */

public class GameLogic {
    Combo current_combo;
    Context context;
    GameInfo info;

    public GameLogic(Context context, GameInfo info) {
        this.context = context;
        this.info = info;
        this.current_combo = info.getCombo(1);
        setupNewCombo();
    }

    public void setupNewCombo(){
        // When there is a new combo
        // - The map design changes
        // - The placemarkers change
        // - The current combo text field changes
        // - A new counter starts
        // - The goal sentence changes
        switch (current_combo.getCombo()) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                break;
        }
    }

    public void onCurrentComboChange(){
        // When the current combo changes
        // - Reprint the goal
        // - Add collected placemarks to the GameInfo

    }
}
