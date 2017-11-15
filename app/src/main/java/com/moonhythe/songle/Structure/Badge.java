package com.moonhythe.songle.Structure;

import android.content.Context;

/**
 * Created by kris on 15/11/17.
 */

public class Badge {
    private int total_time;
    private String badge, artist_title, badge_text;

    public Badge(int total_time, String badge, String artist_title) {
        this.total_time = total_time;
        this.badge = badge;
        this.artist_title = artist_title;

        switch(this.badge){
            case "Gold":
                badge_text = "You earned a Gold Badge!";
                break;
            case "Silver":
                badge_text = "You earned a Silver Badge!";
                break;
            default:
                badge_text = "Your earned a Bronze Badge!";
        }
    }

    public Badge(){

    }

    public void addNew(Context context, String song_info, int time){
        String badge;

        if(time<=600){
            badge = "Gold";
        } else if(time<=900){
            badge = "Silver";
        } else{
            badge = "Bronze";
        }

        // Getting
        String current_songs  = Preference.getSharedPreferenceString(context, "badge_songs", "") + "," + song_info;
        String current_times  = Preference.getSharedPreferenceString(context, "badge_times", "") + "," + time;
        String current_badges = Preference.getSharedPreferenceString(context, "badge_badges", "") + "," + badge;

        // Setting
        Preference.setSharedPreferenceString(context, "badge_songs", current_songs);
        Preference.setSharedPreferenceString(context, "badge_times", current_times);
        Preference.setSharedPreferenceString(context, "badge_badges", current_badges);
    }

    // TODO: Return in the correct format as a string
    public int getTotal_time() {
        return total_time;
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
