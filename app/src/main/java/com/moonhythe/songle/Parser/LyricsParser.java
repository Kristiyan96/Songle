package com.moonhythe.songle.Parser;

import com.moonhythe.songle.Structure.Lyrics;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 04/11/17.
 */

public class LyricsParser {

    static private String TAG = LyricsParser.class.getSimpleName();
    private static final String ns = null;

    public Lyrics parse(InputStream in) throws XmlPullParserException, IOException {
        Lyrics result;
        try {
            result = readText(in);
        } finally {
            in.close();
        }
        return result;
    }

    private Lyrics readText(InputStream in){
        List<String[]> lyrics = new ArrayList<String[]>();
        java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");

        while(s.hasNext()){
            lyrics.add(s.nextLine().split("\\s+"));
        }

        return new Lyrics(lyrics);
    }
}
