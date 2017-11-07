package com.moonhythe.songle.GameManager;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import com.moonhythe.songle.Structure.Combo;
import com.moonhythe.songle.Structure.Placemark;

import java.util.List;

import static com.moonhythe.songle.R.id.combo;
import static com.moonhythe.songle.R.id.combo_quest;
import static com.moonhythe.songle.R.id.combo_time;
import static com.moonhythe.songle.R.id.total_time;

/**
 * Created by kris on 06/11/17.
 */

public class GameLogic {

    private static final String TAG = GameLogic.class.getSimpleName();

    Combo current_combo;
    Context context;
    GameData data;
    TextView combo_text, total_time_text, combo_quest_text, combo_time_text;
    List<Placemark> placemarkers;

    // Timer
    Handler total_time_h = new Handler();
    Handler combo_time_h = new Handler();
    int delay = 1000; //every second
    Runnable runnable;
    int seconds = 0;
    int minutes = 0;

    // Game logic constructor
    // Setting all needed default variables
    public GameLogic(Context context, GameData data) {
        this.context = context;
        this.data = data;
        this.current_combo = data.getCombo(1);

        combo_text = (TextView) ((Activity)context).findViewById(combo);
        total_time_text = (TextView) ((Activity)context).findViewById(total_time);
        combo_quest_text = (TextView) ((Activity)context).findViewById(combo_quest);
        combo_time_text = (TextView) ((Activity)context).findViewById(combo_time);
        setupNewCombo();
    }

    public void setupNewCombo(){
        // When there is a new combo
        // - The map design changes
        // - The placemarkers change
        setCurrentComboMarkers();
        // - The current combo text field changes
        setComboText();
        // - A new counter starts
        data.setCombo_time_seconds(current_combo.getSeconds_lasting());
        startTimers();

        // All other changes
        onCurrentComboChange();
    }

    public void onCurrentComboChange(){
        // When the current combo changes
        // - Reprint the goal
        // - Add collected placemarks to the GameData

        // - The goal sentence changes
        setComboQuestText();
    }

    public void setCurrentComboMarkers(){
        placemarkers = data.getCombo(current_combo.getCombo()).getPlacemarks();

        // TODO: Place markers on map
    }


    /**
     *  TIMERS
     */

    public void startTimers(){
        startTotalTimer();
        startComboTimer();
    }

    public void pauseTimers(){
        pauseComboTimer();
        pauseTotalTimer();
    }

    // This way of creating a timer is not as accurate
    // but is easier to implement for time persistence through sessions
    public void startTotalTimer(){
        total_time_h.postDelayed(new Runnable() {
            public void run() {
                //Increments seconds by 1 every second
                seconds = data.getTotal_time_seconds()+1;
                data.setTotal_time_seconds(seconds);

                minutes = (int) seconds / 60;
                seconds %= 60;
                setTotal_time_text("" + String.format("%02d",minutes) + ":" + String.format("%02d", seconds));

                runnable=this;
                total_time_h.postDelayed(runnable, delay);
            }
        }, delay);
    }

    public void pauseTotalTimer(){
        total_time_h.removeCallbacks(runnable); //stop handler when activity not visible
    }

    public void startComboTimer(){
        combo_time_h.postDelayed(new Runnable() {
            public void run() {
                //Increments seconds by 1 every second
                seconds = data.getCombo_time_seconds()-1;
                data.setCombo_time_seconds(seconds);

                minutes = (int) seconds / 60;
                seconds %= 60;
                setCombo_time_text("" + String.format("%02d",minutes) + ":" + String.format("%02d", seconds));

                runnable=this;
                combo_time_h.postDelayed(runnable, delay);
            }
        }, delay);
    }

    public void pauseComboTimer(){
        combo_time_h.removeCallbacks(runnable); //stop handler when activity not visible
    }

    /**
     *  SETTERS AND GETTERS
     */

    public void setComboText(){
        combo_text.setText("Combo X" + current_combo.getCombo());
    }

    public void setComboQuestText(){
        combo_quest_text.setText("Collect " + (current_combo.getGoal_collected_words() - current_combo.getCollected_words()) + " more in");
    }

    public void setTotal_time_text(String time) {
        total_time_text.setText(time);
    }

    public void setCombo_time_text(String time){
        combo_time_text.setText(time);
    }
}
