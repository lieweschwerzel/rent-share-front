package com.example.rentshare.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.rentshare.R;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    public void toAdvancedSearch(View view) {
        // redirect to advancedSearchActivity
    }

    public void search(View view) {
        // redirect to resultActivity
    }
}
