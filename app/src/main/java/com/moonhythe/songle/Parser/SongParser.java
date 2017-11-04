package com.moonhythe.songle.Parser;

import android.util.Log;
import android.util.Xml;

import com.moonhythe.songle.Structure.Song;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kris on 03/11/17.
 * Parses the info for a specified by the constructor song
 */

public class SongParser {

    private static final String TAG = SongParser.class.getSimpleName();
    private static final String ns = null;
    // TODO: Pull this song_number from the shared preferences
    String song_number = "04";

    public Song parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private Song readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        Song song = null;
        parser.require(XmlPullParser.START_TAG, ns, "Songs");
        // Iterate all songs
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Song")) {
                song = readSong(parser);
                Log.i(TAG, song.getArtist());
                if(song.getNumber().equals(song_number)) {
                    return song;
                }
            } else {
                skip(parser);
            }
        }
        // if song not found
        return null;
    }

    private Song readSong(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Song");
        Song song = new Song();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag_name = parser.getName();
            switch (tag_name) {
                case "Number":
                    song.setNumber(readXml(parser, "Number"));
                    break;
                case "Artist":
                    song.setArtist(readXml(parser, "Artist"));
                    break;
                case "Title":
                    song.setTitle(readXml(parser, "Title"));
                    break;
                case "Link":
                    song.setLink(readXml(parser, "Link"));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        Log.i(TAG, song.getArtist());
        return song;
    }

    private String readXml(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String str = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return str;
    }

    private String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
