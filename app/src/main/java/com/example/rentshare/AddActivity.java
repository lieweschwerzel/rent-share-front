package com.example.rentshare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rentshare.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddActivity extends AppCompatActivity {
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private Button saveButton;
    private EditText editName, editEmail;
    private String URL = "http://192.168.1.105:8080/rest/users/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        saveButton = findViewById(R.id.saveButton);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        saveButton.setOnClickListener((view -> {
            String name = editName.getText().toString();
            String email = editEmail.getText().toString();

            User user = new User(name, email);
            System.out.println(user.getName().toString());
            Call<Void> call = jsonPlaceHolderApi.createUser(user);
//            createPost();
//            Call<List<User>> call = jsonPlaceHolderApi.createUser(user);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (!response.isSuccessful()) {
                       Toast.makeText(AddActivity.this, "createPost Code: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(AddActivity.this, "It worked" + response.toString(), Toast.LENGTH_SHORT).show();
//                    getUsers();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
//                    textViewResult.setText("FAILURE " + t.getMessage() + "\n\n");
                    Toast.makeText(AddActivity.this, ""+t.getMessage(), Toast.LENGTH_LONG);
//                    getUsers();
                }
            });

        }));


    }


    private void createPost() {
//        Map<String, String> fields = new HashMap<>();
//        fields.put("name", "New Name");
//        fields.put("email", "New email");
        User user = new User("Susan", "Suus@n.nl");
        Call<Void> call = jsonPlaceHolderApi.createUser(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (!response.isSuccessful()) {

                    return;
                }
                Toast.makeText(AddActivity.this, "It worked", Toast.LENGTH_SHORT).show();
//                getUsers();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
//                textViewResult.setText("FAILURE " + t.getMessage() + "\n\n");
//                getUsers();
        });
    }
}
