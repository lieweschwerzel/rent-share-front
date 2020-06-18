package com.example.rentshare.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rentshare.R;
import com.example.rentshare.model.Login;
import com.example.rentshare.model.User;
import com.example.rentshare.service.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameText, passwordText;
    private static String token;
    Retrofit.Builder builder;
    Retrofit retrofit;
    UserClient userClient;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String URL = this.getResources().getString(R.string.server);
        usernameText = findViewById(R.id.editUsernameText);
        passwordText = findViewById(R.id.editPasswordText);

        builder = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();
        userClient = retrofit.create(UserClient.class);

    }

    public void logIn(View view) {
        userName = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        System.out.println("test1: "+ userName);
        Login login = new Login(userName, password);

        if (!userName.equals("a")){
            System.out.println("test234234");

        }
        Call<User> call = userClient.login(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (userName.equals("admin")){
                        System.out.println("test "+userName);
                        token = response.body().getToken();
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        intent.putExtra("username", userName);
                        intent.putExtra("token", token);
                        startActivity(intent);
                    }else{
                        token = response.body().getToken();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username", userName);
                        intent.putExtra("token", token);
                        startActivity(intent);
                    }

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

    public void goToRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

}






