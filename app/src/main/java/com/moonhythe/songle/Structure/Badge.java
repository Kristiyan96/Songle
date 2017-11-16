package com.moonhythe.songle.Structure;

import android.content.Context;
import android.util.Log;

import java.time.LocalTime;

import static com.moonhythe.songle.R.id.total_time;

/**
 * Created by kris on 15/11/17.
 */

public class Badge {
    private String badge, artist_title, badge_text, total_time;

    public Badge(String total_time, String badge, String artist_title, String badge_text) {
        this.total_time = total_time;
        this.badge = badge;
        this.artist_title = artist_title;
        this.badge_text = badge_text;
    }

    public Badge(){}

    public String getTotal_time() {
        return "Time: " + total_time;
    }

    public String getBadge() {
        return badge;
    }

    public String getArtist_title() {
        return artist_title;
    }

    public String getBadge_text() {
        return badge_text;
    }
}
