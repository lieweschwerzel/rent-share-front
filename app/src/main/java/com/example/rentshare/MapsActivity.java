package com.example.rentshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MapsActivity extends AppCompatActivity {

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intentToken = getIntent();
        latitude = intentToken.getExtras().getDouble("latitude");
        longitude = intentToken.getExtras().getDouble("longitude");
    }
}
