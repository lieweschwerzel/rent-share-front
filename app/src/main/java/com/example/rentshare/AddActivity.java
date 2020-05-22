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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private String URL = "http://192.168.1.105:8080/rest/advert/";

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


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        saveButton.setOnClickListener((view -> {
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();
            Long price = Long.parseLong(editprice.getText().toString());


            Advert advert = new Advert(title, description, price, "www.k");
            System.out.println(advert.getTitle().toString());
            Call<Void> call = jsonPlaceHolderApi.createAdvert(advert);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (!response.isSuccessful()) {
                        Toast.makeText(AddActivity.this, "createPost Code: " + response.code(), Toast.LENGTH_LONG).show();
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

//        cameraButton.setOnClickListener((view -> {          openCameraIntent();     }));
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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
}
