package com.vegaraju.anand.earthquakerescue;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Anand on 18-01-2018.
 */

public class MapView extends AppCompatActivity implements OnMapReadyCallback {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        Intent i = getIntent();
        Double latd = i.getDoubleExtra("latitude", 0);
        Double longd = i.getDoubleExtra("longitude", 0);
        String title = i.getStringExtra("title");
        LatLng loc = new LatLng(latd, longd);
        googleMap.addMarker(new MarkerOptions().position(loc)
                .title(title));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

    }

}
