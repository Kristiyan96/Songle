package com.moonhythe.songle.Logic;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.moonhythe.songle.Downloader.DownloadLyrics;
import com.moonhythe.songle.Downloader.DownloadMap;
import com.moonhythe.songle.Downloader.DownloadSong;
import com.moonhythe.songle.R;
import com.moonhythe.songle.Structure.Combo;
import com.moonhythe.songle.Structure.Lyrics;
import com.moonhythe.songle.Structure.Placemark;
import com.moonhythe.songle.Structure.Preference;
import com.moonhythe.songle.Structure.Song;

import java.util.List;

/**
 * Created by kris on 05/11/17.
 */

public class GameInfo extends Activity {

    private static final String TAG = GameInfo.class.getSimpleName();
    Context context = null;

    Song song;
    Lyrics lyrics;
    Combo combo_1, combo_2, combo_3, combo_4, combo_5;

    public GameInfo(Context context) {
        Log.i(TAG, "GameInfo created");
        this.context = context;
        setupGame(0);
    }

    public void setupGame(int step){
        Log.i(TAG, "Enter setupGame with step " + step);
        switch(step) {
            case 0:
                downloadSong();
                break;
            case 1:
                downloadLyrics();
                break;
            case 2:
                setupCombo();
                break;
            case 3:
                startGame();
        }
    }

    public void downloadSong(){
        // Pick, download and parse a song
        String stringUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml";
        new DownloadSong(context, this).execute(stringUrl);
    }

    public void onSongDownloaded(Song song){
        Log.i(TAG, "Song downloaded " + song.getNumber());
        this.song = song;

        setupGame(1);
    }

    public void downloadLyrics(){
        // Get last_played song from shared preferences
        String song_number = Preference.getSharedPreferenceString(context, "last_played", "01");
        String stringUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + song_number + "/words.txt";

        new DownloadLyrics(context, this).execute(stringUrl);
    }

    public void onLyricsDownloaded(Lyrics lyrics){
        this.lyrics = lyrics;

        setupGame(2);
    }

    public void setupCombo(){
        String stringUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + song.getNumber() + "/map";
        for(int i=1;i<=5;i++){
            stringUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + song.getNumber() + "/map";
            stringUrl+= (i + ".kml");
            new DownloadMap(context, this, i).execute(stringUrl);
        }
        Log.i(TAG, "Exiting iteration");

    }

    public void onComboSetup(int which_combo, List<Placemark> placemarks){
        if(placemarks == null) {
            Log.i(TAG, "No placemarks received.");
        }
        switch (which_combo){
            case 1:
                combo_1 = new Combo(1, placemarks);
                break;
            case 2:
                combo_2 = new Combo(2, placemarks);
                break;
            case 3:
                combo_3 = new Combo(3, placemarks);
                break;
            case 4:
                combo_4 = new Combo(4, placemarks);
                break;
            case 5:
                combo_5 = new Combo(5, placemarks);
                break;
        }
        setupGame(3);
    }

    public void startGame(){
        TextView txtView = (TextView) ((Activity)context).findViewById(R.id.combo);
        txtView.setText("1");
    }

    public Lyrics getLyrics() {
        return lyrics;
    }

    public Combo getCombo(int comb) {
        switch(comb){
            case 1:
                return combo_1;
            case 2:
                return combo_2;
            case 3:
                return combo_3;
            case 4:
                return combo_4;
            case 5:
                return combo_5;
            default:
                return combo_1;
        }
    }
}
