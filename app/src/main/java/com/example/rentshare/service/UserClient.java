package com.example.rentshare.service;

import com.example.rentshare.model.User;
import com.example.rentshare.model.Login;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {

    @POST("authenticate")
    Call<User> login(@Body Login login);

    @GET("hello")
    Call<ResponseBody> getAll2(@Header("Authorization") String authToken);

}
