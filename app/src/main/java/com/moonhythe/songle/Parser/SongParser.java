package com.moonhythe.songle.Parser;

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

    static private String TAG = SongParser.class.getSimpleName();
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
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Song")) {
                song = readSong(parser);
            } else {
                skip(parser);
            }
        }
        return song;
    }

    private Song readSong(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Song");
        String number = null;
        String artist = "Unnamed";
        String title = "Unnamed";
        String link = "No link";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag_name = parser.getName();
            if (tag_name.equals("Number")) {
                number = readString(parser, "Number");
                // check if the read song is theone we are looking for
                // if not, skip reading forward and go to the next song
                if (number != song_number) {
                    continue;
                }
            } else if (tag_name.equals("Artist")) {
                artist = readString(parser, "Artist");
            } else if (tag_name.equals("Title")) {
                title = readString(parser, "Title");
            } else if (tag_name.equals("Link")) {
                link = readString(parser, "Link");
            } else {
                skip(parser);
            }
        }
        return new Song(number, artist, title, link);
    }

    private String readString(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
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
