package com.example.rentshare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentshare.model.User;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    private String URL = "http://192.168.1.105:8080/rest/users/";
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

//        getUsers();
        createPost();


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
                    textViewResult.setText("createPost Code: " + response.code() +"\n\n");
                    return;
                }
                Toast.makeText(MainActivity.this, "It worked", Toast.LENGTH_SHORT).show();
                getUsers();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textViewResult.setText("FAILURE " + t.getMessage()+"\n\n");
                getUsers();
            }
        });
    }
}
