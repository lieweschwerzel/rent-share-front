package com.example.rentshare;

import com.example.rentshare.model.Advert;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("all")
    Call<List<Advert>> getAdverts();

    @POST("save")
    Call<Void> createAdvert(@Body Advert advert);

    @GET("search/{title}")
    Call<List<Advert>> search(@Path("title") String search);

    @GET("delete")
    Call<Void> deleteAll();

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
