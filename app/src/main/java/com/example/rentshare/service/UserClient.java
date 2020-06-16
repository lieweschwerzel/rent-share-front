package com.example.rentshare.service;

import com.example.rentshare.model.User;
import com.example.rentshare.model.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserClient {

    @POST("authenticate")
    Call<User> login(@Body Login login);

    @POST("register")
    Call<User> register(@Body Login login);



    @GET("searchByUser/{username}")
    Call<Void> getUserId(@Path("username") String userName);
//                               @Header("Authorization") String authToken);

}
