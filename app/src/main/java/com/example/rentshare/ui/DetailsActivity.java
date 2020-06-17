package com.example.rentshare.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rentshare.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static String userName;
    private Long advertId;
    private static String token;
    private static String advertName;
    private static String adOwner;
    LocalDateTime now;
    LocalDateTime expirationDate;
    private TextView timerText, adOwnerTextView;
    private CountDownTimer countDownTimer;
    int hoursLeft;
    int minutesLeft;
    int secondsLeft;
    volatile long millisecondsLeft;
    private static LocalDateTime createdOn = null;
    double latitude;
    double longitude;

    private TextView title, description, price;
    private ImageView image;
    Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advert_details_layout);
        okBtn = findViewById(R.id.buttonMailToMain);

        timerText = findViewById(R.id.timerTextDetails);
        adOwnerTextView = findViewById(R.id.adOwnerDetailsview);
        title = findViewById(R.id.titleDetailview);
        description = findViewById(R.id.descriptionDetail);
        price = findViewById(R.id.priceDetail);
        image = findViewById(R.id.imageDetail);

        Intent intent = getIntent();
        advertName = intent.getExtras().getString("title");
        String newDescription = intent.getExtras().getString("description");
        adOwner = intent.getExtras().getString("adowner");
        latitude = intent.getExtras().getDouble("latitude");
        longitude = intent.getExtras().getDouble("longitude");
        Long newPrice = intent.getExtras().getLong("price");
        userName = intent.getExtras().getString("username");
        advertId = intent.getExtras().getLong("advertId");
        token = intent.getExtras().getString("token");
        Glide.with(getApplicationContext()).load(intent.getExtras().getString("imageUrl")).into(image);
        createdOn = LocalDateTime.parse(intent.getExtras().getString("createdOn"));

//        LocalDateTime createdOn = LocalDateTime.parse("2020-06-17T03:05:05.409");

        title.setText(advertName);
        description.setText(newDescription);
        price.setText("€" + newPrice + " per dag");
        adOwnerTextView.setText("Verhuurd door: " + adOwner);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        now = LocalDateTime.now();
        int duration = 24;
        expirationDate = createdOn.plusHours(duration);

        if (expirationDate.isBefore(now)) {
            Toast.makeText(this, "Deze advertentie is al verstreken", Toast.LENGTH_SHORT).show();
        } else {
            hoursLeft = Hours.hoursBetween(now, expirationDate).getHours();
            minutesLeft = Minutes.minutesBetween(now, expirationDate).getMinutes() - (hoursLeft * 60);
            secondsLeft = Seconds.secondsBetween(now, expirationDate).getSeconds() - ((minutesLeft * 60) + (hoursLeft * 3600));

            millisecondsLeft = Seconds.secondsBetween(now, expirationDate).getSeconds() * 1000;

            timerText.setVisibility(View.VISIBLE);

            countDownTimer = new CountDownTimer(millisecondsLeft, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    millisecondsLeft = millisUntilFinished;
                    refreshTimer();
                }

                @Override
                public void onFinish() {
                    Toast.makeText(DetailsActivity.this, "Deze advertentie is net verstreken", Toast.LENGTH_LONG).show();
                }
            }.start();
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Locatie adverteerder")).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void directReservation(View view) {
        Intent intent = new Intent(DetailsActivity.this, MailActivity.class);
        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        intent.putExtra("username", userName);
        intent.putExtra("adowner", adOwner);
        intent.putExtra("advertname", advertName);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    private void refreshTimer() {
        long hoursLeft = millisecondsLeft / 3600000;
        long minutesLeft = millisecondsLeft / 60000 - (hoursLeft * 60);
        long secondsLeft = millisecondsLeft / 1000 - ((minutesLeft * 60) + (hoursLeft * 3600));

        StringBuilder timeLeft = new StringBuilder();

        if (hoursLeft < 10) {
            timeLeft.append("0").append(hoursLeft);
        } else timeLeft.append(hoursLeft);
        timeLeft.append(":");
        if (minutesLeft < 10) {
            timeLeft.append("0").append(minutesLeft);
        } else timeLeft.append(minutesLeft);
        timeLeft.append(":");
        if (secondsLeft < 10) {
            timeLeft.append("0").append(secondsLeft);
        } else timeLeft.append(secondsLeft);

        timeLeft.append(" resterend");
        timerText.setText(timeLeft);
    }

    public void toBids(View view) {

        Intent intent = new Intent(DetailsActivity.this, BidActivity.class);
        intent.putExtra("username", userName);
        intent.putExtra("token", token);
        intent.putExtra("advertId", advertId);
        startActivity(intent);
    }
}
