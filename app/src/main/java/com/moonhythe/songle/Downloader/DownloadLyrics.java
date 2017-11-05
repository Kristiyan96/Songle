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

    @Override
    protected Lyrics doInBackground(String... urls) {
        Log.i(TAG, "Started doInBackground");
        Lyrics fail = null;
        try {
            return loadTxtFromNetwork(urls[0]);
        } catch (IOException e) {
            Log.i(TAG, "IOException at loadingXmlFromNetwork");
            return fail;   // TO DO handle exception
        } catch (XmlPullParserException e) {
            Log.i(TAG, "XMLPullParserException at loadingXml");
            return fail;   // TO DO handle exception
        }
    }

    private Lyrics loadTxtFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        LyricsParser txtParser = new LyricsParser();
        Lyrics result = null;

        try {
            stream = downloadUrl(urlString);
            result = txtParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return result;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }
}
