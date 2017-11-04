package com.moonhythe.songle.Parser;

import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;
import com.moonhythe.songle.Structure.Placemark;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 04/11/17.
 */

public class PlacemarkerParser {

    static private String TAG = PlacemarkerParser.class.getSimpleName();
    private static final String ns = null;

    public List<Placemark> parse(InputStream in) throws XmlPullParserException, IOException {
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

    private List<Placemark> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Placemark> placemarks = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "kml");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Placemark")) {
                placemarks.add(readPlacemark(parser));
            } else {
                skip(parser);
            }
        }
        return placemarks;
    }

    private Placemark readPlacemark(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Placemark");

        String name = null;
        String description = null;
        String styleUrl = null;
        LatLng point = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag_name = parser.getName();
            if (tag_name.equals("name")) {
                name = readString(parser);
            } else if (tag_name.equals("description")) {
                description = readString(parser);
            } else if (tag_name.equals("styleUrl")){
                styleUrl = readString(parser);
            } else if (tag_name.equals("Point")) {
                point = readPoint(parser);
            } else {
                skip(parser);
            }
        }
        return new Placemark(name, description, styleUrl, point);
    }

    private String readString(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
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

    private LatLng readPoint(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "Point");
        String coordinates = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("coordinates")) {
                coordinates = readText(parser);
            } else {
                skip(parser);
            }
        }

        String[] latlong = coordinates.split(",");
        double latitude = Double.parseDouble(latlong[1]);
        double longitude = Double.parseDouble(latlong[0]);
        return new LatLng(latitude,longitude);
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
