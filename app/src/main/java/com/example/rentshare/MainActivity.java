package com.example.rentshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rentshare.model.Advert;
import com.example.rentshare.service.JsonPlaceHolderApi;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private EditText searchText;
    private Button searchButton, addButton, deleteButton;
    private String URL = getString(R.string.server);
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private RecyclerView mRecyclerView;
    private static String token = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.searchButton);
        addButton = findViewById(R.id.addButton);
        searchText = findViewById(R.id.searchText);
        deleteButton = findViewById(R.id.deleteButton);
        mRecyclerView = (RecyclerView) findViewById(R.id.advertRecycler);

        Intent intentToken = getIntent();
        token = intentToken.getExtras().getString("token");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getAdverts();

        //Search Button
        searchButton.setOnClickListener((view -> {
            searchAdverts();
        }));

        // Delete Button
        deleteButton.setOnClickListener((view -> {
            deleteAll();
        }));

        //Add Button
        addButton.setOnClickListener((view -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            intent.putExtra("token", token);
            startActivity(intent);
        }));

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.advertRecycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
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
        String searchTitle = searchText.getText().toString();
        Call<List<Advert>> call = jsonPlaceHolderApi.search(searchTitle);

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

//                textViewResult.setText("");
                List<Advert> adverts = response.body();

                AdvertAdapter mAdapter = new AdvertAdapter(getApplicationContext(), adverts);
                mRecyclerView.setAdapter(mAdapter);

                if (adverts.isEmpty()) {
//                    textViewResult.setText("Niets gevonden op " + searchTitle);
                }

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
                AdvertAdapter mAdapter = new AdvertAdapter(getApplicationContext(), adverts);
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

}
