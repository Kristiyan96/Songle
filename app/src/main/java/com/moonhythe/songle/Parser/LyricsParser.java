package com.moonhythe.songle.Parser;

import android.util.Xml;

import com.moonhythe.songle.Structure.Lyrics;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kris on 04/11/17.
 */

public class LyricsParser {

    static private String TAG = LyricsParser.class.getSimpleName();
    private static final String ns = null;

    public Lyrics parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readText(parser);
        } finally {
            in.close();
        }
    }

    // TODO: Finish this method when testing
    private Lyrics readText(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        Lyrics result = null;
        String[] lines = null;
        int row = 0;
        if (parser.next() == XmlPullParser.TEXT) {
            lines[row] = parser.getText();
            parser.nextTag();
            row++;
        }
        return result;
    }
}
