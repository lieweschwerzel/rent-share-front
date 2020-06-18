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
import com.example.rentshare.model.Bid;
import com.example.rentshare.service.JsonPlaceHolderApi;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static String userName;
    static Long advertId;
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
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    Retrofit retrofit;

    private TextView title, description, price, highestBidTextView;
    private ImageView image;
    Button okBtn;
    private static double highestBid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advert_details_layout);
        String URL = getString(R.string.server);

        okBtn = findViewById(R.id.buttonMailToMain);
        getSupportActionBar().setTitle("Advertentie details");
        timerText = findViewById(R.id.timerTextDetails);
        adOwnerTextView = findViewById(R.id.adOwnerDetailsview);
        title = findViewById(R.id.titleDetailview);
        description = findViewById(R.id.descriptionDetail);
        price = findViewById(R.id.priceDetailView);
        image = findViewById(R.id.imageDetail);
        highestBidTextView = findViewById(R.id.hoogsteBodDetailView2);

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
        Toast.makeText(this, advertId.toString(), Toast.LENGTH_SHORT).show();
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

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getHighestBid();
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

    private void getHighestBid() {
        Call<List<Bid>> call = jsonPlaceHolderApi.getBidsByAdvertId(advertId, "Bearer " + token);

        call.enqueue(new Callback<List<Bid>>() {
            @Override
            public void onResponse(Call<List<Bid>> call, retrofit2.Response<List<Bid>> response) {
                if (!response.isSuccessful()) {
//                    textView.setText("code: " + response.code());
                    return;
                }

                List<Bid> bids = response.body();
                if (bids != null && !bids.isEmpty()) {
                    highestBid = response.body().get(0).getAmount();
                    if (highestBid == 0){
                        highestBidTextView.setText("geen biedingen");
                    } else
                        highestBidTextView.setText(String.valueOf(highestBid)+"€");
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<List<Bid>> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
            }
        });
    }
}
