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

public class LoginActivity extends AppCompatActivity {
    private Button loginButton, goToRegisterButton;
    private EditText usernameText, passwordText;
    private static String URL = "http://192.168.1.105:8080";
    private static String token;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.build();
    UserClient userClient = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameText = findViewById(R.id.editUsernameText);
        passwordText = findViewById(R.id.editPasswordText);
        loginButton = findViewById(R.id.loginBtn);
        goToRegisterButton = findViewById(R.id.toRegisterBtn);

        goToRegisterButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            loginUser();
        });
    }
    // try to login with the given credentials
    // if successful redirect to the homepage
    // if not show toast with error message
    public void loginUser() {
    String user = usernameText.getText().toString();
    String password = passwordText.getText().toString();
        Login login = new Login(user, password);
        Call<User> call = userClient.login(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    token = response.body().getToken();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("token", token);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "error!!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "USER BESTAAT NIET!!!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void ToRegisterActivity(View view) {
        Intent intent = new Intent();
        // redirect to registerActivity
    }

}






