package com.example.googlemapplot;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Deep1 on 3/27/2018.
 */

public class MapResponse {

    ArrayList<LatLng> points;
    String title;

    public ArrayList<LatLng> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<LatLng> points) {
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
