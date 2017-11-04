package com.moonhythe.songle.Structure;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kris on 04/11/17.
 */

public class Placemark {

    static private String TAG = Placemark.class.getSimpleName();
    private String name = null;
    private String description = null;
    private String styleUrl = null;
    private LatLng point = null;

    public Placemark(String name, String description, String styleUrl, LatLng point) {
        this.name = name;
        this.description = description;
        this.styleUrl = styleUrl;
        this.point = point;
    }
}
