package com.moonhythe.songle.Downloader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.moonhythe.songle.Parser.PlacemarkerParser;
import com.moonhythe.songle.Logic.GameInfo;
import com.moonhythe.songle.Structure.Lyrics;
import com.moonhythe.songle.Structure.Placemark;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 04/11/17.
 */

public class DownloadMap extends AsyncTask<String, Void, List<Placemark>> {

    private static final String TAG = DownloadMap.class.getSimpleName();
    private Context context;
    GameInfo gameInfoManager;
    int combo;
    Lyrics lyrics;

    public DownloadMap(Context context, GameInfo manager, int combo) {
        Log.i(TAG, "Constructor for downloading for combo " + combo);
        this.context = context;
        this.gameInfoManager = manager;
        this.combo = combo;
        this.lyrics = gameInfoManager.getLyrics();
    }

    @Override
    protected List<Placemark> doInBackground(String... urls){
        Log.i(TAG, "Starting background job");
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
        PlacemarkerParser xmlParser = new PlacemarkerParser();
        List<Placemark> result = new ArrayList<>();

        try {
            stream = downloadUrl(urlString);
            result =  xmlParser.parse(stream);
        } finally {
            try {stream.close();}
            catch (IOException e) {}
            catch (Exception e) {}
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
    protected void onPostExecute(List<Placemark> placemarks) {
        //Attach words to plcemarks
        if(placemarks == null){
            Log.i(TAG, "Placemark are null");
        } else {
            int row;
            int column;
            for(Placemark placemark : placemarks){
                row = Integer.parseInt(placemark.getName().split(":")[0]) - 1;
                column = Integer.parseInt(placemark.getName().split(":")[1]);
                placemark.setWord(lyrics.getLyrics().get(row)[column]);
            }
        }
        // Send back the placemarks
        gameInfoManager.onComboSetup(combo, placemarks);
    }
}
