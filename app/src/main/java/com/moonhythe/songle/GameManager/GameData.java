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
                current_combo = getCombo(1);
                game = new GameLogic(context, this, mMap);
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
    }

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

    public void waitForAllCombos(){
        combos_downloaded++;
        if(combos_downloaded==5){
            setupGame(3);
        }
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
        current_combo.setCollected_words(current_combo.getCollected_words()+1);
    }

    public Combo getCurrent_combo(){
        return current_combo;
    }

    public void incrementCombo(){
        Log.i(TAG, "Incrementing combo");
        // Clear previous combo marks before adding the new one
        current_combo.clearMap();
        if(current_combo.getCombo()==5){
            current_combo = getCombo(5);
        }else{
            current_combo = getCombo(current_combo.getCombo()+1);
        }
        Log.i(TAG, "Current combo updated");
        game.setupNewCombo();
        Log.i(TAG, "New combo setted up");
    }
    public void resetCombo(){
        // Clear previous combo marks before adding the new one
        current_combo.clearMap();
        current_combo = getCombo(1);
        game.setupNewCombo();
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

    public void guessSong(String song_title){
        if(song.getTitle()==song_title){
            // Jump to congratulations screen
        }else{
            game.showMessage("Wrong", "That's not the song title, Try again.");
        }
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

    public int getTotal_time_seconds() {
        return total_time_seconds;
    }

    public void setTotal_time_seconds(int total_time_seconds) {
        this.total_time_seconds = total_time_seconds;
    }

}
