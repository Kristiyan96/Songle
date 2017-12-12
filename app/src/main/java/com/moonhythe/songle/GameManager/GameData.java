package com.moonhythe.songle.GameManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.moonhythe.songle.Activity.SurrenderActivity;
import com.moonhythe.songle.Activity.WinActivity;
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
    private static Context context = null;

    private int combos_downloaded=0;
    private int total_time_seconds = 0;

    private Song song;
    private Lyrics lyrics;
    private Combo combo_1, combo_2, combo_3, combo_4, combo_5, current_combo;
    private GameLogic game;
    private GoogleMap mMap;
    private Boolean continue_game = false;
    private List<Placemark> picked_placemarks = new ArrayList<Placemark>();

    /**
     * Game setup
     */

    // Constructor for Data Manager of current session
    public GameData(Context context, GoogleMap mMap, Boolean continue_game) {
        Log.i(TAG, "GameData created");
        this.context = context;
        this.continue_game = continue_game;
        this.mMap = mMap;
        Preference.setSharedPreferenceBoolean(context, "can_continue", true);
        setupGame(0);
    }

    // Downloading the resources for the played song and creating the Logic Manager
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
                if(continue_game) applyGameState();  // Get previous state if continue game
                else current_combo = getCombo(1);    // Else set up a new combo
                game = new GameLogic(context, this, mMap);
        }
    }

    /**
     * Downloaders
     */

    public void downloadSong(){
        // Pick, download and parse a song
        String stringUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml";
        new DownloadSong(context, this).execute(stringUrl);
    }

    public void downloadLyrics(){
        // Get last_played song from shared preferences
        String stringUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + song.getNumber() + "/words.txt";
        new DownloadLyrics(context, this).execute(stringUrl);
    }

    public void setupCombo(){
        String stringUrl;
        for(int i=1;i<=5;i++){
            stringUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + song.getNumber() + "/map";
            stringUrl+= (i + ".kml");
            new DownloadMap(context, this, i).execute(stringUrl);
        }
    }

    /**
     * Callbacks
     */

    public void onComboSetup(int which_combo, List<Placemark> placemarks){
        switch (which_combo){
            case 1:
                combo_1 = new Combo(1, placemarks, this, game);
                break;
            case 2:
                combo_2 = new Combo(2, placemarks, this, game);
                break;
            case 3:
                combo_3 = new Combo(3, placemarks, this, game);
                break;
            case 4:
                combo_4 = new Combo(4, placemarks, this, game);
                break;
            case 5:
                combo_5 = new Combo(5, placemarks, this, game);
                break;
        }
        waitForAllCombos();
    }

    public void onSongDownloaded(Song song){
        this.song = song;
        setupGame(1);
    }

    public void onLyricsDownloaded(Lyrics lyrics){
        this.lyrics = lyrics;
        setupGame(2);
    }

    public void updateLocation(Location location){
        if(game != null) game.onLocationChanged(location);
        else setupGame(0); // setting the game if a new session started
    }

    /**
     * Getters
     */

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
    public Lyrics getLyrics() {
        return lyrics;
    }

    public Combo getCombo(int comb) {
        // send back a copy of the original saved combo
        switch(comb){
            case 1:
                return new Combo(1, combo_1.getPlacemarks(), this, game);
            case 2:
                return new Combo(2, combo_2.getPlacemarks(), this, game);
            case 3:
                return new Combo(3, combo_3.getPlacemarks(), this, game);
            case 4:
                return new Combo(4, combo_4.getPlacemarks(), this, game);
            case 5:
                return new Combo(5, combo_5.getPlacemarks(), this, game);
            default:
                return new Combo(1, combo_1.getPlacemarks(), this, game);
        }
    }

    public Boolean getContinue_game() {
        return continue_game;
    }

    public int getTotal_time_seconds() {
        return total_time_seconds;
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

    /**
     * Setters
     */

    public void setTotal_time_seconds(int total_time_seconds) {
        this.total_time_seconds = total_time_seconds;
    }

    public void saveGameState(){
        Preference.setSharedPreferenceString(context, "song_number", song.getNumber());
        Preference.setSharedPreferenceInt(context, "current_combo_number", current_combo.getCombo());
        Preference.setSharedPreferenceInt(context, "current_combo_time", current_combo.getSeconds_lasting());
        Preference.setSharedPreferenceInt(context, "current_combo_words_picked", current_combo.getCollected_words());
        Preference.setSharedPreferenceString(context, "picked_placemarks", savePickedPlacemarks());
        Preference.setSharedPreferenceInt(context, "current_total_time", getTotal_time_seconds());
    }

    public void applyGameState(){
        setTotal_time_seconds(Preference.getSharedPreferenceInt(context, "current_total_time", 0));
        importPickedPlacemarks();
        current_combo = getCombo(Preference.getSharedPreferenceInt(context, "current_combo_number", 1));
        current_combo.setSeconds_lasting(Preference.getSharedPreferenceInt(context, "current_combo_time", 120));
        current_combo.setCollected_words(Preference.getSharedPreferenceInt(context, "current_combo_words_picked", 0));
    }

    public void importPickedPlacemarks(){
        for(String i : Preference.getSharedPreferenceString(context, "picked_placemarks", "").split(" ")){
            for(Placemark k : combo_5.getPlacemarks()){
                if(k.getName().equals(i)) picked_placemarks.add(k);
            }
        }
    }

    public String savePickedPlacemarks(){
        String picked = "";
        for(Placemark placemark : picked_placemarks){
            picked += placemark.getName() + " ";
        }
        return picked;
    }

    public void incrementCombo(){
        // Clear previous combo marks before adding the new one
        current_combo.clearMap();
        if(current_combo.getCombo()==5){
            current_combo = getCombo(5);
        }else{
            current_combo = getCombo(current_combo.getCombo()+1);
        }
        game.setupNewCombo();
    }
    public void resetCombo(){
        // Clear previous combo marks before adding the new one
        current_combo.clearMap();
        current_combo = getCombo(1);
        game.setupNewCombo();
    }

    public void addPickedPlacemark(Placemark placemark){
        picked_placemarks.add(placemark);
        current_combo.setCollected_words(current_combo.getCollected_words()+1);
    }

    /**
     * Logic functions
     */

    // Waiting for all combos to download before continuing to next step in set up
    public void waitForAllCombos(){
        combos_downloaded++;
        if(combos_downloaded==5){
            setupGame(3);
        }
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

    public Intent putSongInfo(Intent intent){
        intent.putExtra("song_number", song.getNumber());
        intent.putExtra("song_title", song.getTitle());
        intent.putExtra("song_artist", song.getArtist());
        intent.putExtra("song_url", song.getLink());
        intent.putExtra("total_time", total_time_seconds);

        return intent;
    }

    public void surrender(){
        // No continue game
        Preference.setSharedPreferenceBoolean(context, "can_continue", false);
        // Jump to SurrenderActivity
        Intent intent = new Intent();
        intent = putSongInfo(intent);
        intent.setClass(context, SurrenderActivity.class);
        context.startActivity(intent);
    }

    public void guessSong(String song_title){
        if(closeEnough(song.getTitle(),song_title)){
            // No continue game
            Preference.setSharedPreferenceBoolean(context, "can_continue", false);
            // Jump to congratulations screen
            Intent intent = new Intent();
            intent = putSongInfo(intent);
            intent.setClass(context, WinActivity.class);
            context.startActivity(intent);
        }else{
            game.showMessage("Wrong", "That's not the song title, Try again.");
        }
    }

    public Boolean closeEnough(String str1, String str2){
        str1 = str1.replaceAll("[^a-zA-Z]", "").toLowerCase();
        str2 = str2.replaceAll("[^a-zA-Z]", "").toLowerCase();
        CharSequence str1_charS = str1;
        CharSequence str2_charS = str2;
        int treshold = 1 + str2.length()/5;
        int distance = levenshteinDistance(str1_charS, str2_charS);
        if(distance<=treshold) return true;
        else return false;
    }

    public int levenshteinDistance (CharSequence lhs, CharSequence rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        for (int i = 0; i < len0; i++) cost[i] = i;
        for (int j = 1; j < len1; j++) {
            newcost[0] = j;

            for(int i = 1; i < len0; i++) {
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;

                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            int[] swap = cost; cost = newcost; newcost = swap;
        }

        return cost[len0 - 1];
    }

}
