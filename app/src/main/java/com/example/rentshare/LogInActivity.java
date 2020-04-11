package com.example.rentshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void tryLogIn(View view) {
        // try to login with the given credentials
        // if successful redirect to the homepage
        // if not show toast with error message
    }

    public void ToRegisterActivity(View view) {
        Intent intent = new Intent();
        // redirect to registerActivity
    }
}
