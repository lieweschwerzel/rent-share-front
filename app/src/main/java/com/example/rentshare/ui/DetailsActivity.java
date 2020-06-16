package com.example.rentshare.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rentshare.R;

import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

public class DetailsActivity extends AppCompatActivity {
    LocalDateTime now;
    LocalDateTime expirationDate;
    private TextView timerText, adOwnerTextView;
    private CountDownTimer countDownTimer;
    int hoursLeft;
    int minutesLeft;
    int secondsLeft;
    volatile long millisecondsLeft;
    private static LocalDateTime createdOn = null;

    private TextView title, description, price;
    private ImageView image;
    private String userName;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advert_details_layout);

        timerText = findViewById(R.id.timerTextDetails);
        adOwnerTextView = findViewById(R.id.adOwnerDetailsview);
        title = findViewById(R.id.titleDetailview);
        description = findViewById(R.id.descriptionDetail);
        price = findViewById(R.id.priceDetail);
        image = findViewById(R.id.imageDetail);

        Intent intent = getIntent();
        String newTitle = intent.getExtras().getString("title");
        String newDescription = intent.getExtras().getString("description");
        String adOwner = intent.getExtras().getString("adowner");
        Long newPrice = intent.getExtras().getLong("price");
        Glide.with(getApplicationContext()).load(intent.getExtras().getString("imageUrl")).into(image);
        createdOn = LocalDateTime.parse(intent.getExtras().getString("createdOn"));

//        LocalDateTime createdOn = LocalDateTime.parse("2020-06-17T03:05:05.409");

        title.setText(newTitle);
        description.setText(newDescription);
        price.setText(newPrice + "€ per dag");
        adOwnerTextView.setText("Verhuurd door: "+adOwner);


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

    public void directReservation(View view) {
//        Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
//        intent.putExtra("username", userName);
//        intent.putExtra("token", token);
        finish();
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

        timerText.setText(timeLeft);

    }
}
