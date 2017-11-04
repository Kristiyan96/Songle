package com.moonhythe.songle.Downloader;

import android.os.AsyncTask;
import android.util.Log;

import com.moonhythe.songle.Parser.PlacemarkerParser;
import com.moonhythe.songle.Structure.Placemark;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by kris on 04/11/17.
 */

public class DownloadMap extends AsyncTask<String, Void, List<Placemark>> {

    private static final String TAG = DownloadMap.class.getSimpleName();

    private String song_number;
    private String combo;
    String baseUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + song_number + "/map" + combo + ".kml";

    @Override
    protected List<Placemark> doInBackground(String... urls){
        List<Placemark> fail = null;
        try {
            return loadXmlFromNetwork(urls[0]);
        } catch (IOException e) {
            Log.i(TAG, "IOException at loadingXmlFromNetwork");
            return fail;
        } catch (XmlPullParserException e) {
            Log.i(TAG, "XMLPullParserException at loadingXml");
            return fail;
        }
    }

    private List<Placemark> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        PlacemarkerParser xmlParser = new PlacemarkerParser();
        List<Placemark> placemarks = null;

        Log.i(TAG, "Begin loadXmlFromNetwork");

        try {
            stream = downloadUrl(urlString);
            placemarks = xmlParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        Log.i(TAG, "Markers downloaded and parsed");
        return placemarks;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        Log.i(TAG, "Markers downloaded");
        return conn.getInputStream();
    }
}
