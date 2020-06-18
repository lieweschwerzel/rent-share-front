package com.example.rentshare.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rentshare.R;
import com.example.rentshare.model.Bid;
import com.example.rentshare.model.Login;
import com.example.rentshare.model.User;
import com.example.rentshare.service.JsonPlaceHolderApi;
import com.example.rentshare.service.UserClient;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    private String token;
    private String userName;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    Retrofit retrofit;
    private User user;
    TextView textView;
    EditText editStreet, editStreetNumber, editZipCode;
    private UserClient userClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String URL = getString(R.string.server);

        textView = findViewById(R.id.userNameProfileText);
        editStreet = findViewById(R.id.addressStreetProfileEdit);
        editStreetNumber = findViewById(R.id.addressNumberProfileEdit);
        editZipCode = findViewById(R.id.zipcodeProfileEdit);

        Intent intentToken = getIntent();
        token = intentToken.getExtras().getString("token");
        userName = intentToken.getExtras().getString("username");

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getUserDetails();


    }

    private void getUserDetails() {
        Call<User> call = jsonPlaceHolderApi.getUserDetailsByUsername(userName, "Bearer " + token);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                user = response.body();
                System.out.println("test "+user.toString());
                textView.setText(userName);
                editStreet.setText(user.getStreetName());
                editStreetNumber.setText(String.valueOf(user.getHouseNumber()));
                editZipCode.setText(user.getZipcode());

            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }

        });
    }

    public void onClickSaveDetails(View view){
        System.out.println("hi");
        String addressStreet = editStreetNumber.getText().toString();
        int addressNumber = Integer.parseInt(editStreetNumber.getText().toString());
        String zipcode = editZipCode.getText().toString();


        Login login = new Login(userName, zipcode, addressStreet, addressNumber);
        Call<User> call = userClient.updateUser(login);

    }
}
