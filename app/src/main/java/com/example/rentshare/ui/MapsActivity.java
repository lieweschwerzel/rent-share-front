package com.example.rentshare.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.rentshare.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intentToken = getIntent();
        latitude = intentToken.getExtras().getDouble("latitude");
        longitude = intentToken.getExtras().getDouble("longitude");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng test = new LatLng(51.835397, 4.140444);
        googleMap.addMarker(new MarkerOptions()
                .position(test)
                .title("Mijn huis")).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(test, 16));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void toMail(View view) {
        Intent intent = new Intent(MapsActivity.this, MailActivity.class);
        startActivity(intent);
    }
}
