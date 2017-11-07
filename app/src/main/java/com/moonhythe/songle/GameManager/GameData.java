package com.moonhythe.songle.GameManager;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.moonhythe.songle.Downloader.DownloadLyrics;
import com.moonhythe.songle.Downloader.DownloadMap;
import com.moonhythe.songle.Downloader.DownloadSong;
import com.moonhythe.songle.Structure.Combo;
import com.moonhythe.songle.Structure.Lyrics;
import com.moonhythe.songle.Structure.Placemark;
import com.moonhythe.songle.Structure.Preference;
import com.moonhythe.songle.Structure.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 05/11/17.
 */

public class GameData extends Activity {

    private static final String TAG = GameData.class.getSimpleName();
    Context context = null;

    private int combos_downloaded=0;

    private Song song;
    private Lyrics lyrics;
    private Combo combo_1, combo_2, combo_3, combo_4, combo_5, current_combo;
    private GameLogic game;
    private GoogleMap mMap;
    private List<Placemark> picked_placemarks = new ArrayList<Placemark>();

    private int total_time_seconds = 0;
    private int combo_time_seconds = 0;

    public GameData(Context context, GoogleMap mMap) {
        Log.i(TAG, "GameData created");
        this.context = context;
        this.mMap = mMap;
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
                current_combo = getCombo(4);
                game = new GameLogic(context, this);
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
        waitForAllCombos();
    }

    public void waitForAllCombos(){
        combos_downloaded++;
        if(combos_downloaded==5){
            setupGame(3);
        }
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

    public int getTotal_time_seconds() {
        return total_time_seconds;
    }

    public void setTotal_time_seconds(int total_time_seconds) {
        this.total_time_seconds = total_time_seconds;
    }

    public int getCombo_time_seconds() {
        return combo_time_seconds;
    }

    public void setCombo_time_seconds(int combo_time_seconds) {
        this.combo_time_seconds = combo_time_seconds;
    }

    public GoogleMap getMap(){
        return mMap;
    }

    public void updateLocation(Location location){
        if(game != null) game.onLocationChanged(location);
    }

    public List<Placemark> getUnpickedPlacemarks(){
        List<Placemark> unpicked_placemarks = new ArrayList<Placemark>();

        if (picked_placemarks != null && current_combo.getPlacemarks() != null){
            Boolean found = false;
            for(Placemark placemark_i : current_combo.getPlacemarks()){
                for(Placemark placemark_k : picked_placemarks){
                    if(placemark_i.getName() == placemark_k.getName()) found = true;
                }
                if(!found) unpicked_placemarks.add(placemark_i);
                found = false;
            }
            return unpicked_placemarks;
        } else{
            return current_combo.getPlacemarks();
        }
    }

    public void addPickedPlacemark(Placemark placemark){
        picked_placemarks.add(placemark);
    }

    public Combo getCurrent_combo(){
        return current_combo;
    }

    public String getLyricsText(){
        String lyrics_text = "";
        List<String[]> lyrics_lines = lyrics.getLyrics();
        for(int i=0;i<lyrics_lines.size();i++){
            for(int k=1;k<lyrics_lines.get(i).length;k++){
                if(isWordOpened(i+1,k)) lyrics_text+=lyrics_lines.get(i)[k]+" ";
                else lyrics_text+= "....." + " ";
            }
            lyrics_text+="\n";
        }

        return lyrics_text;
    }

    public Boolean isWordOpened(int row, int column){
        String[] name;
        int num1, num2;
        if(picked_placemarks.size()==0) return false;
        else{
            for(Placemark placemark : picked_placemarks){
                name = placemark.getName().split(":");
                num1 = Integer.parseInt(name[0]);
                num2 = Integer.parseInt(name[1]);
                if(num1 == row && num2 == column) return true;
            }
        }
        return false;
    }

    //    public long getGame_start_time() {
//        return game_start_time;
//    }
//
//    public void setGame_start_time(long game_start_time) {
//        this.game_start_time = game_start_time;
//    }
//
//    public long getCombo_start_time() {
//        return combo_start_time;
//    }
//
//    public void setCombo_start_time(long combo_start_time) {
//        this.combo_start_time = combo_start_time;
//    }
}
