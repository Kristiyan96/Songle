package com.moonhythe.songle.Downloader;

import android.os.AsyncTask;

import com.moonhythe.songle.Structure.Placemark;

import java.util.Map;

/**
 * Created by kris on 04/11/17.
 */

public class DownloadMap extends AsyncTask<String, Void, Placemark> {

    private static final String TAG = DownloadMap.class.getSimpleName();

    private String song_number;
    private String combo;
    String baseUrl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + song_number + "/map" + combo + ".kml";
}
