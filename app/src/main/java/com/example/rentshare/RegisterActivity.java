package com.example.rentshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    EditText editUserName, editPassword, editZipcode, editAddressStreet ,editAddressNumber;
    Button registerButton;
    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editUserName = findViewById(R.id.userNameRegisterText);
        editPassword = findViewById(R.id.firstPasswordRegisterText);
        editZipcode = findViewById(R.id.zipCodeRegisterText);
        editAddressStreet = findViewById(R.id.addressStreetRegisterText);
        editAddressNumber = findViewById(R.id.addressNumberRegisterText);
        registerButton = findViewById(R.id.registerBtn);
        String URL = this.getResources().getString(R.string.server);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserClient userClient = retrofit.create(UserClient.class);

        registerButton.setOnClickListener(view -> {
            // register new user with the given information
            String userName = editUserName.getText().toString();
            String password = editPassword.getText().toString();
            String zipcode = editZipcode.getText().toString();
            String addressStreet = editAddressStreet.getText().toString();
            int addressNumber = Integer.parseInt(editAddressNumber.getText().toString());

            Login login = new Login(userName, password, zipcode, addressStreet, addressNumber);
            Call<User> call = userClient.register(login);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Call<User> call2 = userClient.login(login);
                        call2.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call2, Response<User> response) {
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
                            public void onFailure(Call<User> call2, Throwable t) {
                                Toast.makeText(RegisterActivity.this, "USER BESTAAT NIET!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "error!!", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "USER BESTAAT NIET!!!", Toast.LENGTH_SHORT).show();
                }
            });
        });



    }

//    public void registerUser() {
//        // register new user with the given information
//        String user = editUserName.getText().toString();
//        String password = editPassword.getText().toString();
//
//        Login login = new Login(user, password);
//        Call<User> call = userClient.register(login);
//
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()) {
//                    loginUser();
//                } else {
//                    Toast.makeText(RegisterActivity.this, "error!!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(RegisterActivity.this, "USER BESTAAT NIET!!!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    public void loginUser() {
//        String user = editUserName.getText().toString();
//        String password = editPassword.getText().toString();
//        Login login = new Login(user, password);
//
//    }

}
