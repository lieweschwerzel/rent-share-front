package com.example.rentshare.service;

import com.example.rentshare.model.Advert;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("advert/all")
    Call<List<Advert>> getAdverts(@Header("Authorization") String authToken);

    @POST("advert/save")
    Call<Void> createAdvert(@Body Advert advert, @Header("Authorization") String authToken);

    @GET("advert/search/{title}")
    Call<List<Advert>> search(@Path("title") String search);

    @GET("advert/search/{userId}")
    Call<List<Advert>> getAdvertsByUserId(@Path("userId") Long userId);

    @GET("advert/delete")
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

    @Multipart
    @POST("images/upload_image.php")
    Call<ServerResponse> upload(
            @Header("Authorization") String authorization,
            @PartMap Map<String, RequestBody> map
    );
}
