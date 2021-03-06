package com.example.rentshare.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentshare.R;
import com.example.rentshare.model.Bid;
import com.example.rentshare.service.JsonPlaceHolderApi;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BidActivity extends AppCompatActivity {

    String username;
    static Long advertId;
    static String token;
    static double highestBid = 0;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    Retrofit retrofit;
    private EditText amountText;
    private double amount;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);
        textView = findViewById(R.id.textViewBid);
        getSupportActionBar().setTitle("Doe een bod");
        String URL = getString(R.string.server);

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        advertId = intent.getExtras().getLong("advertId");
        token = intent.getExtras().getString("token");
        System.out.println("token:  " + token);

        amountText = findViewById(R.id.bidAmountText);

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getBids();
    }

    private void getBids() {
        Call<List<Bid>> call = jsonPlaceHolderApi.getBidsByAdvertId(advertId, "Bearer " + token);

        call.enqueue(new Callback<List<Bid>>() {
            @Override
            public void onResponse(Call<List<Bid>> call, retrofit2.Response<List<Bid>> response) {
                if (!response.isSuccessful()) {
                    textView.setText("code: " + response.code());
                    return;
                }

                List<Bid> bids = response.body();
                if (bids != null && !bids.isEmpty()) {
                    textView.setText("");
                    for (Bid bid : bids) {
                        if (highestBid <= bid.getAmount()) {
                            highestBid = bid.getAmount();
//                            System.out.println("bid  " +highestBid);
                        }
                        String content = "";
                        content += "Gebruiker: " + bid.getUsername() + "\n";
                        content += "Bod: " + bid.getAmount() +"€"+ "\n\n";
//                        content += "Date: " + bid.getCreatedOn() + "\n\n";
                        textView.append(content);
                    }
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

    public void onClickSaveBid(View view) {

        String amountString = amountText.getText().toString();
        if (!TextUtils.isEmpty(amountString)) {
            amount = Double.parseDouble((amountText.getText().toString()));
        } else {
            Toast.makeText(this, "Voer een bedrag in", Toast.LENGTH_SHORT).show();
        }
        if (Double.parseDouble((amountText.getText().toString())) <= highestBid) {
            Toast.makeText(this, "Voer een bedrag hoger dan hoogste bod in", Toast.LENGTH_SHORT).show();
            return;
        }

        Bid bid = new Bid(username, advertId, amount, LocalDateTime.now().toString());

        Call<Void> call = jsonPlaceHolderApi.createBid(bid, "Bearer " + token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(BidActivity.this, "error code: " + response.code(), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(BidActivity.this, "Je bod van " + "€" + amount + " is geplaatst", Toast.LENGTH_LONG).show();
                amountText.setText("");
                getBids();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(BidActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
