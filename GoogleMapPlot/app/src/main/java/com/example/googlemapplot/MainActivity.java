package com.example.googlemapplot;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final String TAG = "demo";
    private GoogleMap mMap;
    private String line;
    private MapResponse mapResponse;
    private LatLngBounds.Builder bounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            InputStreamReader isr = new InputStreamReader(getResources().openRawResource(R.raw.trip));
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            Gson gson = new Gson();
            mapResponse = gson.fromJson(sb.toString(), MapResponse.class);
            Log.d(TAG, "onCreate: " + mapResponse.getPoints().size());

        } catch (IOException e) {
            e.printStackTrace();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");
        bounds = new LatLngBounds.Builder();

        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mapResponse.getPoints().get(0)));
        mMap.addMarker(new MarkerOptions()
                .position(mapResponse.getPoints().get(0))
                .title(mapResponse.title));
        for (int i = 0; i < mapResponse.getPoints().size() ; i++) {
            //Log.d("demo", "onMapReady: " + mapResponse.getPoints().size());
            bounds.include(mapResponse.getPoints().get(i));
            if (i == 0) {
                mMap.addMarker(new MarkerOptions().position(mapResponse.getPoints().get(i)));
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(mapResponse.getPoints().get(i), mapResponse.getPoints().get(i + 1))
                        .width(5)
                        .color(Color.RED));
            } else if (i == mapResponse.getPoints().size() - 1) {
                mMap.addMarker(new MarkerOptions()
                        .position(mapResponse.getPoints().get(i))
                        .title("End Point"));
            } else {
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(mapResponse.getPoints().get(i), mapResponse.getPoints().get(i + 1))
                        .width(5)
                        .color(Color.RED));
            }
        }
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 0));
            }
        });
    }
}