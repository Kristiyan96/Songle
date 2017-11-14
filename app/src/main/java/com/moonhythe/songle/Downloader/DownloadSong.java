package com.moonhythe.songle.Downloader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.moonhythe.songle.GameManager.GameData;
import com.moonhythe.songle.Parser.SongParser;
import com.moonhythe.songle.Structure.Song;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kris on 03/11/17.
 * Downloads a previously predefined song chosen at random
 */

public class DownloadSong extends AsyncTask<String, Void, Song> {

    private static final String TAG = DownloadSong.class.getSimpleName();
    private Context context;
    GameData dataManager;

    public DownloadSong(Context context, GameData dataManager) {
        this.context = context;
        this.dataManager = dataManager;
    }

    @Override
    protected Song doInBackground(String... urls) {
        Song fail = null;
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

    private Song loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        SongParser xmlParser = new SongParser(context, dataManager);
        Song result = null;

        try {
            stream = downloadUrl(urlString);
            result = xmlParser.parse(stream);
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
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }

    @Override
    protected void onPostExecute(Song song) {
        // Song has been downloaded, alert game data manager for the progress
        dataManager.onSongDownloaded(song);
    }
}
