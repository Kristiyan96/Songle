package com.moonhythe.songle.Structure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by kris on 04/11/17.
 */

public class Placemark {

    static private String TAG = Placemark.class.getSimpleName();
    private String name = null;
    private String word = null;
    private String description = null;
    private String styleUrl = null;
    private LatLng point = null;

    public Placemark() {
    }

    public void putOnMap(Context context, GoogleMap mMap){
        switch(description){
            case "unclassified":
                mMap.addMarker(new MarkerOptions().position(point).title(description).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("unclassified",100,100, context))));
                break;
            case "boring":
                mMap.addMarker(new MarkerOptions().position(point).title(description).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("boring",100,100, context))));
                break;
            case "notboring":
                mMap.addMarker(new MarkerOptions().position(point).title(description).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("not_boring",100,100, context))));
                break;
            case "interesting":
                mMap.addMarker(new MarkerOptions().position(point).title(description).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("interesting",100,100, context))));
                break;
            case "veryinteresting":
                mMap.addMarker(new MarkerOptions().position(point).title(description).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("very_interesting",100,100, context))));
                break;
        }
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height, Context context){
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(),context.getResources().getIdentifier(iconName, "drawable", context.getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
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
