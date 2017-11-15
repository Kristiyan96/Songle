package com.moonhythe.songle.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.moonhythe.songle.R;
import com.moonhythe.songle.Structure.Badge;
import com.moonhythe.songle.Structure.Preference;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = HistoryActivity.class.getSimpleName();
    private List<Badge> badges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        badges = parseBadges();
    }

    public List<Badge> parseBadges(){
        String[] current_songs  = Preference.getSharedPreferenceString(this, "badge_songs", "").split("|");
        String[] current_times  = Preference.getSharedPreferenceString(this, "badge_times", "").split("|");
        String[] current_badges = Preference.getSharedPreferenceString(this, "badge_badges", "").split("|");

        List<Badge> badges_list = new ArrayList<Badge>();
        String badge, song;
        int time;
        // Check for errors during splitting
        if(current_badges.length == current_songs.length && current_badges.length == current_times.length){
            // Parse badges
            for(int i=0;i<current_badges.length; i++){
                time = Integer.parseInt(current_times[i]);
                badge = current_badges[i];
                song = current_songs[i];
                badges_list.add(new Badge(time, badge, song));
            }
        }

        return badges_list;
    }
}
