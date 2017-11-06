package com.moonhythe.songle.Downloader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.moonhythe.songle.Parser.SongParser;
import com.moonhythe.songle.Logic.GameInfo;
import com.moonhythe.songle.Structure.Preference;
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
    GameInfo gameInfoManager;

    public DownloadSong(Context context, GameInfo manager) {
        this.context = context;
        this.gameInfoManager = manager;
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
        SongParser xmlParser = new SongParser(context);
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
        // song is a randomly chosen song from the ones that have not been guessed
        // Now we have to download the lyrics and markers
        String song_number = song.getNumber();
        //Save the song as last played
        Preference.setSharedPreferenceString(context, "last_played", song_number);
        //Alert GameInfo that the song is downloaded
        gameInfoManager.onSongDownloaded(song);
    }
}
