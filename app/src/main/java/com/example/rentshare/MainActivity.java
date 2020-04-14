package com.example.rentshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentshare.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    private EditText searchText;
    private Button searchButton, addButton, deleteButton;
    private String URL = "http://192.168.1.105:8080/rest/users/";
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);
        searchButton = findViewById(R.id.searchButton);
        addButton = findViewById(R.id.addButton);
        searchText = findViewById(R.id.searchText);
        deleteButton = findViewById(R.id.deleteButton);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getUsers();

        // Delete Button
        deleteButton.setOnClickListener((view -> {
            Call<Void> call = jsonPlaceHolderApi.deleteAll();

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "createPost Code: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(MainActivity.this, "It worked  " + response.toString(), Toast.LENGTH_SHORT).show();
                    textViewResult.setText("");
                    getUsers();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG);
                }
            });


        }));

        //Add Button
        addButton.setOnClickListener((view -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        }));

        //Search Button
        searchButton.setOnClickListener((view -> {
            String searchTitle = searchText.getText().toString();
            Call<List<User>> call = jsonPlaceHolderApi.search(searchTitle);

            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                    if (!response.isSuccessful()) {
                        if (response.code() == 404) {
                            textViewResult.setText("wel iets invoeren");
                        } else {
                            textViewResult.setText("" + response.code());
                        }
                        return;
                    }

                    textViewResult.setText("");
                    List<User> users = response.body();
                    if (users.isEmpty()) {
                        textViewResult.setText("Niets gevonden op " + searchTitle);
                    }

                    for (User user : users) {
                        String content = "";
                        content += "ID: " + user.getId() + "\n";
                        content += "Name: " + user.getName() + "\n";
                        content += "Email: " + user.getEmail() + "\n\n";
                        textViewResult.append(content);
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    textViewResult.setText(t.getMessage());
                }
            });
        }));
    }

    private void getUsers() {
        Call<List<User>> call = jsonPlaceHolderApi.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("code: " + response.code());
                    return;
                }
                List<User> users = response.body();

                for (User user : users) {
                    String content = "";
                    content += "ID: " + user.getId() + "\n";
                    content += "Name: " + user.getName() + "\n";
                    content += "Email: " + user.getEmail() + "\n\n";
                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void createPost() {
//        Map<String, String> fields = new HashMap<>();
//        fields.put("name", "New Name");
//        fields.put("email", "New email");
        User user = new User("Jantje", "jan@jan.nl");
        Call<Void> call = jsonPlaceHolderApi.createUser(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("createPost Code: " + response.code() + "\n\n");
                    return;
                }
                Toast.makeText(MainActivity.this, "It worked", Toast.LENGTH_SHORT).show();
                getUsers();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textViewResult.setText("FAILURE " + t.getMessage() + "\n\n");
                getUsers();
            }
        });
    }
}
