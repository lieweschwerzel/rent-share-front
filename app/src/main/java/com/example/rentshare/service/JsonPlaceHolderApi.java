package com.example.rentshare.service;

import com.example.rentshare.model.Advert;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("/rest/advert/all")
    Call<List<Advert>> getAdverts();

    @POST("/rest/advert/save")
    Call<Void> createAdvert(@Body Advert advert);

    @GET("/rest/advert/search/{title}")
    Call<List<Advert>> search(@Path("title") String search);

    @GET("/rest/advert/delete")
    Call<ResponseBody> deleteAll(@Header("Authorization") String authToken);

//    @GET("delete/{id}")
//    Call<List<User>> search(@Path("name") String search);


//    @FormUrlEncoded
//    @POST("/load")
//    Call<User> createUser(
//            @Field("name") String name,
//            @Field("email") String email
//    );

//    @FormUrlEncoded
//    @POST("load")
//    Call<User> createUser(@FieldMap Map<String, String> fields);
}