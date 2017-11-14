package com.moonhythe.songle.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moonhythe.songle.R;
import com.moonhythe.songle.Structure.Preference;

public class SurrenderActivity extends AppCompatActivity {

    private static final String TAG = SurrenderActivity.class.getSimpleName();

    private String song_number, song_artist, song_title, song_url;
    private Button show_song_btn;
    private LinearLayout info;
    private TextView song_info;
    private boolean song_showed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surrender);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get layout elements
        show_song_btn = (Button) findViewById(R.id.show_song_btn);
        info = (LinearLayout) findViewById(R.id.song_info);
        song_info = (TextView) findViewById(R.id.song_title);

        // Get song info
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            song_number = bundle.getString("song_number");
            song_artist = bundle.getString("song_artist");
            song_title  = bundle.getString("song_title");
            song_url    = bundle.getString("song_url");
        } else{
            song_number = "";
            song_artist = "unknown";
            song_title  = "unknown";
            song_url    = "http://www.youtube.com";
        }

        // Set song info
        song_info.setText(song_artist + " - " + song_title);
        // TODO: make the image button a link to youtube video

        // Button listeners
        show_song_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(!song_showed){
                    // Player viewed song => add it to removed songs
                    String removed_songs = Preference.getSharedPreferenceString(SurrenderActivity.this, "removed_songs", "");
                    Preference.setSharedPreferenceString(SurrenderActivity.this, "removed_songs", removed_songs + " " + song_number);
                    info.setVisibility(View.VISIBLE);
                    song_showed = true;  // avoid adding a song multiple times
                }
            }
        });
    }
}
