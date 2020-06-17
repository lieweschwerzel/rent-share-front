package com.example.rentshare.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentshare.R;
import com.example.rentshare.model.Advert;
import com.example.rentshare.model.User;
import com.example.rentshare.service.JsonPlaceHolderApi;
import com.example.rentshare.service.UserClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private EditText searchText;
    private TextView gpsText;

    LocationManager locationManager;
    LocationListener locationListener;
    Double latitude;
    Double longitude;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private RecyclerView mRecyclerView;
    private static String token = null;
    private static String userName = null;
    Retrofit retrofit;
    UserClient userClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String URL = getString(R.string.server);
        searchText = findViewById(R.id.searchText);
        mRecyclerView = (RecyclerView) findViewById(R.id.advertRecycler);

        Intent intentToken = getIntent();
        token = intentToken.getExtras().getString("token");
        userName = intentToken.getExtras().getString("username");

        getSupportActionBar().setTitle("Rentshare: " + userName);

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getAdverts();
//        getUserId();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.advertRecycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    private void getUserId() {
        Call<Void> call3 = userClient.getUserId(userName);
        call3.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call3, Response<Void> response) {
                if (response.isSuccessful()) {
                    String userId = response.body().toString();
                    System.out.println(userId);
                } else {
                    Toast.makeText(MainActivity.this, "error!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> response, Throwable t) {
                Toast.makeText(MainActivity.this, "USER BESTAAT NIET!!!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void onClickAddButton(View view) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", userName);
        startActivity(intent);
    }

    public void onClickDeleteButton(View view) {
        deleteAll();
    }

    public void onClickSearchButton(View view) {
        searchAdverts();
    }

    private void deleteAll() {
        Call<ResponseBody> call = jsonPlaceHolderApi.deleteAll("Bearer "+ token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "createPost Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(MainActivity.this, "It worked  " + response.toString(), Toast.LENGTH_SHORT).show();
                getAdverts();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG);
            }
        });
    }

    private void searchAdverts() {
        if (searchText.getText().toString().isEmpty()){
            getAdverts();
//        getUserId();

            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.advertRecycler);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        String searchTitle = searchText.getText().toString();
		
        Call<List<Advert>> call = jsonPlaceHolderApi.searchAdvert(searchTitle, "Bearer "+ token);

        call.enqueue(new Callback<List<Advert>>() {
            @Override
            public void onResponse(Call<List<Advert>> call, retrofit2.Response<List<Advert>> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
//                        textViewResult.setText("wel iets invoeren");
                    } else {
//                        textViewResult.setText("" + response.code());
                    }
                    return;
                }

                List<Advert> adverts = response.body();

                AdvertAdapter mAdapter = new AdvertAdapter(getApplicationContext(), adverts, token, userName);

                mRecyclerView.setAdapter(mAdapter);


                for (Advert advert : adverts) {
                    String content = "";
                    content += "ID: " + advert.getId() + "\n";
                    content += "Title: " + advert.getTitle() + "\n";
                    content += "Description: " + advert.getDescription() + "\n\n";
//                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Advert>> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getAdverts() {
        Call<List<Advert>> call = jsonPlaceHolderApi.getAdverts("Bearer "+ token);

        call.enqueue(new Callback<List<Advert>>() {
            @Override
            public void onResponse(Call<List<Advert>> call, retrofit2.Response<List<Advert>> response) {
                if (!response.isSuccessful()) {
//                    textViewResult.setText("code: " + response.code());
                    return;
                }
                List<Advert> adverts = response.body();
                AdvertAdapter mAdapter = new AdvertAdapter(getApplicationContext(), adverts, token, userName);
                mRecyclerView.setAdapter(mAdapter);

                for (Advert advert : adverts) {
                    String content = "";
                    content += "ID: " + advert.getId() + "\n";
                    content += "Title: " + advert.getTitle() + "\n";
                    content += "Description: " + advert.getDescription() + "\n\n";
//                    textViewResult.append(content);
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
            public void onFailure(Call<List<Advert>> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settingsmenu:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.signoutmenu:
                signOut2();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void signOut2(){
        token = null;
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClickMail(View view) {
            Intent intent = new Intent(MainActivity.this, MailActivity.class);
            startActivity(intent);
    }

    public void onClickTimer(View view) {
        Intent intent = new Intent(MainActivity.this, TimerActivity.class);
        startActivity(intent);
    }

}
