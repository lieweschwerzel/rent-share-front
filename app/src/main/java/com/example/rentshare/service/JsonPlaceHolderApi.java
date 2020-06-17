package com.example.rentshare.service;

import com.example.rentshare.model.Advert;
import com.example.rentshare.model.Bid;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("advert/all")
    Call<List<Advert>> getAdverts(@Header("Authorization") String authToken);

    @POST("advert/save")
    Call<Void> createAdvert(@Body Advert advert, @Header("Authorization") String authToken);


    @GET("advert/search/title/{title}")
    Call<List<Advert>> searchAdvert(@Path("title") String search, @Header("Authorization") String authToken);

    @GET("advert/search/{userId}")
    Call<List<Advert>> getAdvertsByUserId(@Path("userId") Long userId);

    @GET("advert/delete")
        Call<ResponseBody> deleteAll(@Header("Authorization") String authToken);

    @GET("bid/all")
    Call<List<Bid>> getBids(@Header("Authorization") String authToken);

    @POST("bid/save")
    Call<Void> createBid(@Body Bid bid, @Header("Authorization") String authToken);

    @GET("bid/search/{username}")
    Call<List<Bid>> getBidsByUsername(@Path("username") String username);

    @GET("bid/search/advertId/{advertId}")
    Call<List<Bid>> getBidsByAdvertId(@Path("advertId") int advertId, @Header("Authorization") String authToken);

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
