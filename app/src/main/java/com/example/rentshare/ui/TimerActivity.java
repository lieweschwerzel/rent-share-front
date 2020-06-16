package com.example.rentshare.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentshare.R;
import com.example.rentshare.model.Advert;
import com.example.rentshare.service.JsonPlaceHolderApi;

import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

public class TimerActivity extends AppCompatActivity {

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    LocalDateTime now;
    LocalDateTime expirationDate;
    private TextView timerText;
    private CountDownTimer countDownTimer;
    int hoursLeft;
    int minutesLeft;
    int secondsLeft;
    volatile long millisecondsLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerText = findViewById(R.id.timerTextDetails);

        //Advert advert = new Advert("advert", "description", 50, "http://", 100, "100", 5, LocalDateTime.now().toString(), 12);
        //jsonPlaceHolderApi.createAdvert(advert);

        now = LocalDateTime.now();
        LocalDateTime createdOn = LocalDateTime.parse("2020-06-17T03:05:05.409");
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
                    Toast.makeText(TimerActivity.this, "Deze advertentie is net verstreken", Toast.LENGTH_LONG).show();
                }
            }.start();
        }
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
