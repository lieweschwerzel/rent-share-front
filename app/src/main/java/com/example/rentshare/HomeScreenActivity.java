package com.example.rentshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
