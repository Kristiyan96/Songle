package com.moonhythe.songle.Downloader;

import android.os.AsyncTask;
import android.util.Log;

import com.moonhythe.songle.Parser.SongXmlParser;
import com.moonhythe.songle.Structure.Song;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by kris on 03/11/17.
 */

public class DownloadSongList extends AsyncTask<String, Void, List<Song>> {

    private static final String TAG = DownloadSongList.class.getSimpleName();

    String baseUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml";


    @Override
    protected List<Song> doInBackground(String... urls) {
        List<Song> fail = null;
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

    private List<Song> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        SongXmlParser xmlParser = new SongXmlParser();
        List<Song> songs = null;
        String number = null;
        String artist = null;
        String title = null;
        String link = null;
        Log.i(TAG, "Begin loadXmlFromNetwork");

        try {
            stream = downloadUrl(urlString);
            songs = xmlParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        Log.i(TAG, "Songs downloaded and parsed");
        return songs;
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
        Log.i(TAG, "Songs downloaded");
        return conn.getInputStream();
    }
}
