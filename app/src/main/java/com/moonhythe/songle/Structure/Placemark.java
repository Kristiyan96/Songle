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

    public Placemark() {
    }

    public Placemark(String name, String description, String styleUrl, LatLng point) {
        this.name = name;
        this.description = description;
        this.styleUrl = styleUrl;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStyleUrl() {
        return styleUrl;
    }

    public void setStyleUrl(String styleUrl) {
        this.styleUrl = styleUrl;
    }

    public LatLng getPoint() {
        return point;
    }

    public void setPoint(LatLng point) {
        this.point = point;
    }
}
