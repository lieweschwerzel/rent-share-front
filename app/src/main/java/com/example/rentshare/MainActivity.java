package com.example.rentshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentshare.model.Advert;
import com.example.rentshare.service.JsonPlaceHolderApi;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    private EditText searchText;
    private Button searchButton, addButton, deleteButton;
    private String URL = "http://192.168.1.105:8080/rest/advert/";
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private RecyclerView mRecyclerView;
    private static String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsIiwiZXhwIjoxNTkwNjIyMzIwLCJpYXQiOjE1OTA2MDQzMjB9.HDDfqAwzoVIrEP2uJ75Gd9YtG5hXCYQ-wz8wcwgXHUPuWPUy1JQr1Y84IINJUrkcLRX0w7LEtCDTM2Wujri88A";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);
        searchButton = findViewById(R.id.searchButton);
        addButton = findViewById(R.id.addButton);
        searchText = findViewById(R.id.searchText);
        deleteButton = findViewById(R.id.deleteButton);
        mRecyclerView = (RecyclerView) findViewById(R.id.advertRecycler);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getAdverts();

        //Search Button
        searchButton.setOnClickListener((view -> {
            String searchTitle = searchText.getText().toString();
            Call<List<Advert>> call = jsonPlaceHolderApi.search(searchTitle);

            call.enqueue(new Callback<List<Advert>>() {
                @Override
                public void onResponse(Call<List<Advert>> call, retrofit2.Response<List<Advert>> response) {
                    if (!response.isSuccessful()) {
                        if (response.code() == 404) {
                            textViewResult.setText("wel iets invoeren");
                        } else {
                            textViewResult.setText("" + response.code());
                        }
                        return;
                    }

                    textViewResult.setText("");
                    List<Advert> adverts = response.body();
                   
                        AdvertAdapter mAdapter = new AdvertAdapter(getApplicationContext(), adverts);
                        mRecyclerView.setAdapter(mAdapter);
                   
                    if (adverts.isEmpty()) {
                        textViewResult.setText("Niets gevonden op " + searchTitle);
                    }

                    for (Advert advert : adverts) {
                        String content = "";
                        content += "ID: " + advert.getId() + "\n";
                        content += "Title: " + advert.getTitle() + "\n";
                        content += "Description: " + advert.getDescription() + "\n\n";
                        textViewResult.append(content);
                    }
                }

                @Override
                public void onFailure(Call<List<Advert>> call, Throwable t) {
                    textViewResult.setText(t.getMessage());
                }
            });
        }));

        //Add Button
        addButton.setOnClickListener((view -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        }));



        // Delete Button
        deleteButton.setOnClickListener((view -> {
            Call<ResponseBody> call = jsonPlaceHolderApi.deleteAll("Bearer "+ token);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "createPost Code: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(MainActivity.this, "It worked  " + response.toString(), Toast.LENGTH_SHORT).show();
                    textViewResult.setText("");
                    getAdverts();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG);
                }
            });
        }));

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.advertRecycler);
        mRecyclerView.setHasFixedSize(true);


        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));


    }



    private void getAdverts() {
        Call<List<Advert>> call = jsonPlaceHolderApi.getAdverts();

        call.enqueue(new Callback<List<Advert>>() {
            @Override
            public void onResponse(Call<List<Advert>> call, retrofit2.Response<List<Advert>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("code: " + response.code());
                    return;
                }
                List<Advert> adverts = response.body();
                AdvertAdapter mAdapter = new AdvertAdapter(getApplicationContext(), adverts);
                mRecyclerView.setAdapter(mAdapter);

                for (Advert advert : adverts) {
                    String content = "";
                    content += "ID: " + advert.getId() + "\n";
                    content += "Title: " + advert.getTitle() + "\n";
                    content += "Description: " + advert.getDescription() + "\n\n";
                    textViewResult.append(content);
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
                textViewResult.setText(t.getMessage());
            }
        });
    }

}
