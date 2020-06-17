package com.example.rentshare.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rentshare.R;

public class BidActivity extends AppCompatActivity {

    String username;
    Long advertId;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        advertId = intent.getExtras().getLong("advertId");
        token = intent.getExtras().getString("token");
    }
}
