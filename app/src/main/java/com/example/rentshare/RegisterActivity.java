package com.example.rentshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rentshare.model.Login;
import com.example.rentshare.model.User;
import com.example.rentshare.service.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    EditText editUserName, editPassword;
    Button registerButton;
    private String URL = getString(R.string.server);;
    private static String token;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();
    UserClient userClient = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editUserName = findViewById(R.id.editUserNameRegisterView);
        editPassword = findViewById(R.id.editPasswordRegisterView);
        registerButton = findViewById(R.id.registerBtn);

        registerButton.setOnClickListener(view -> {
            registerUser();
        });

    }

    public void registerUser() {
        // register new user with the given information
        String user = editUserName.getText().toString();
        String password = editPassword.getText().toString();

        Login login = new Login(user, password);
        Call<User> call = userClient.register(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    loginUser();
                } else {
                    Toast.makeText(RegisterActivity.this, "error!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "USER BESTAAT NIET!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginUser() {
        String user = editUserName.getText().toString();
        String password = editPassword.getText().toString();
        Login login = new Login(user, password);
        Call<User> call = userClient.login(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "USER heeft token \n" + response.body().getToken(), Toast.LENGTH_SHORT).show();
                    token = response.body().getToken();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("token", token);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "error!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "USER BESTAAT NIET!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
