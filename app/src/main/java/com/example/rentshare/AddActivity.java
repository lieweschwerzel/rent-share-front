package com.example.rentshare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rentshare.model.Advert;
import com.example.rentshare.model.Login;
import com.example.rentshare.model.User;
import com.example.rentshare.service.JsonPlaceHolderApi;
import com.example.rentshare.service.UserClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddActivity extends AppCompatActivity {
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private Button saveButton, cameraButton;
    private EditText editTitle, editDescription, editprice;
    private ImageView imageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath;
    private String URL = "http://192.168.1.105:8080/";

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        saveButton = findViewById(R.id.saveButtonView);
        cameraButton = findViewById(R.id.cameraButtonView);
        editTitle = findViewById(R.id.editTitleView);
        editDescription = findViewById(R.id.editDecriptionView);
        editprice = findViewById(R.id.editPriceView);
        imageView = findViewById(R.id.cameraImageView);

        Retrofit retrofit2 = builder.build();

        jsonPlaceHolderApi = retrofit2.create(JsonPlaceHolderApi.class);

        saveButton.setOnClickListener((view -> {
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();
            long price = Long.parseLong((editprice.getText().toString()));


            Advert advert = new Advert(title, description, price, "www.k");
            System.out.println(advert.getTitle().toString());
            Call<Void> call = jsonPlaceHolderApi.createAdvert(advert);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (!response.isSuccessful()) {
                        Toast.makeText(AddActivity.this, "error code: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(AddActivity.this, "It worked" + response.toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AddActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG);
                }
            });

        }));

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dispatchTakePictureIntent();
//                login();
                getAll();
            }
        });



    }

    private static String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsIiwiZXhwIjoxNTkwNjIyMzIwLCJpYXQiOjE1OTA2MDQzMjB9.HDDfqAwzoVIrEP2uJ75Gd9YtG5hXCYQ-wz8wcwgXHUPuWPUy1JQr1Y84IINJUrkcLRX0w7LEtCDTM2Wujri88A";

    private void login(){
        Login login = new Login("l", "l");
        Call<User> call = userClient.login(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    Toast.makeText(AddActivity.this, "USER BESTAAT en heeft token \n "+ response.body().getToken() , Toast.LENGTH_SHORT).show();
                    System.out.println(response.body().getUsername());
                    System.out.println(response.body().getToken());
                    token = response.body().getToken();
                }else{
                    Toast.makeText(AddActivity.this, "error!!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AddActivity.this, "USER BESTAAT NIET!!!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getAll(){
        Call<ResponseBody> call = userClient.getAll2("Bearer "+ token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(AddActivity.this, "USER BESTAAT en heeft token \n "+ response.body() , Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(AddActivity.this, "error!!" + response.body(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                System.out.println("liewe " + ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
