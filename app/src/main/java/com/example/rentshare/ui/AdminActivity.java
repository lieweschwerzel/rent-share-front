package com.example.rentshare.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.rentshare.R;
import com.example.rentshare.service.JsonPlaceHolderApi;
import com.example.rentshare.service.UserClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminActivity extends AppCompatActivity {

    private String token;
    private String userName;
    Retrofit retrofit;
    UserClient userClient;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        String URL = getString(R.string.server);

        Intent intentToken = getIntent();
        token = intentToken.getExtras().getString("token");
        userName = intentToken.getExtras().getString("username");

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


    }


    public void onClickDeleteAll(View view) {
        deleteAll();
    }

    private void deleteAll() {
        Call<ResponseBody> call = jsonPlaceHolderApi.deleteAll("Bearer " + token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AdminActivity.this, "createPost Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(AdminActivity.this, "All adverts Deleted  " + response.toString(), Toast.LENGTH_SHORT).show();
//                getAdverts();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AdminActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG);
            }
        });
    }
}
