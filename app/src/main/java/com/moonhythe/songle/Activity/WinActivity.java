package com.moonhythe.songle.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.moonhythe.songle.R;
import com.moonhythe.songle.Structure.Badge;
import com.moonhythe.songle.Structure.Preference;

import java.time.LocalTime;

public class WinActivity extends AppCompatActivity {

    private static final String TAG = WinActivity.class.getSimpleName();

    private String song_number, song_artist, song_title, song_url, badge_type, artist_title, badge_text_str;
    private int total_time;


    private TextView badge_info, badge_text;
    private ImageView badge_image;
    private Button back_to_main_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get layout elements
        badge_image = (ImageView) findViewById(R.id.badge_img);
        badge_text = (TextView) findViewById(R.id.badge_text);
        badge_info = (TextView) findViewById(R.id.badge_info);
        back_to_main_btn = (Button) findViewById(R.id.back_to_main);
        // TODO: Button to youtube video


        // Get song info
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            song_number = bundle.getString("song_number");
            song_artist = bundle.getString("song_artist");
            song_title  = bundle.getString("song_title");
            song_url    = bundle.getString("song_url");
            total_time  = bundle.getInt("total_time");
        } else{
            song_number = "";
            song_artist = "unknown";
            song_title  = "unknown";
            song_url    = "http://www.youtube.com";
            total_time  = 0;
        }

        // Set badge info
        artist_title = song_artist + " - " + song_title;
        LocalTime timeOfDay = LocalTime.ofSecondOfDay(total_time);
        String time = timeOfDay.toString();
        if(time.substring(0,2).equals("00")) time = time.substring(3);  // Remove hours if unnecessary

        if(total_time<=600){ // Gold
            badge_image.setImageResource(R.drawable.gold);
            badge_type = "Gold";
        } else if(total_time<=900){ // Silver
            badge_image.setImageResource(R.drawable.silver);
            badge_type = "Silver";
        } else {  // Bronze
            badge_image.setImageResource(R.drawable.bronze);
            badge_type = "Bronze";
        }

        badge_text_str = "You earned a " + badge_type + " Badge!";

        badge_text.setText(badge_text_str);
        badge_info.setText("You guessed " + artist_title + " in " + time + ".");

        // TODO: Set button to youtube

        // Button listeners

        back_to_main_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(WinActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        // TODO: Listener for button to youtube

        // Add badge to sharedpreferences
        Badge badge = new Badge(time, badge_type, artist_title, badge_text_str);
        Preference.addBadge(this, "badges", badge);
    }
}
