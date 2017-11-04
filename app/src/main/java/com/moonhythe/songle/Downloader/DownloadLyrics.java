package com.moonhythe.songle.Downloader;

import android.os.AsyncTask;
import android.util.Log;

import com.moonhythe.songle.Parser.LyricsParser;
import com.moonhythe.songle.Structure.Lyrics;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kris on 04/11/17.
 */

public class DownloadLyrics extends AsyncTask<String, Void, Lyrics> {

    private static final String TAG = DownloadLyrics.class.getSimpleName();
    private String song_number = null;
    private String baseUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + song_number + "/words.txt";

    @Override
    protected Lyrics doInBackground(String... urls) {
        Lyrics fail = null;
        try {
            return loadXmlFromNetwork(urls[0]);
        } catch (IOException e) {
            Log.i(TAG, "IOException at loadingXmlFromNetwork");
            return fail;   // TO DO handle exception
        } catch (XmlPullParserException e) {
            Log.i(TAG, "XMLPullParserException at loadingXml");
            return fail;   // TO DO handle exception
        }
    }

    private Lyrics loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        LyricsParser xmlParser = new LyricsParser();
        Lyrics lyrics = null;
        Log.i(TAG, "Begin loadXmlFromNetwork");

        try {
            stream = downloadUrl(urlString);
            lyrics = xmlParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        Log.i(TAG, "Lyrics downloaded and parsed");
        return lyrics;
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
        Log.i(TAG, "Lyrics downloaded");
        return conn.getInputStream();
    }
}
