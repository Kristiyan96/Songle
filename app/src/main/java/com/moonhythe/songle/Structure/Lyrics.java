package com.moonhythe.songle.Structure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 04/11/17.
 */

public class Lyrics {

    static private String TAG = Lyrics.class.getSimpleName();
    private List<String[]> lyrics = new ArrayList<>();

    public Lyrics(List<String[]> lyrics) {
        this.lyrics = lyrics;
    }

    public List<String[]> getLyrics() {
        return lyrics;
    }

    public void setLyrics(List<String[]> lyrics) {
        this.lyrics = lyrics;
    }
}
