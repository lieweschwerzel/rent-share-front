package com.example.rentshare.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentshare.R;
import com.example.rentshare.model.Advert;
import com.example.rentshare.service.JsonPlaceHolderApi;

import com.bumptech.glide.Glide;

import org.joda.time.LocalDateTime;

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
    private TextView userNameText;
    private ImageView imageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String currentPhotoPath = null;
    private static String token = null;
    private static String userName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        String URL = this.getResources().getString(R.string.server);
        saveButton = findViewById(R.id.saveButtonView);
        cameraButton = findViewById(R.id.cameraButtonView);
        editTitle = findViewById(R.id.editTitleView);
        editDescription = findViewById(R.id.editDecriptionView);
        editprice = findViewById(R.id.editPriceView);
        imageView = findViewById(R.id.cameraImageView);
        userNameText = findViewById(R.id.advertOwnerIUserNameAdd);


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create());

        Intent intentToken = getIntent();
        token = intentToken.getExtras().getString("token");
        userName = intentToken.getExtras().getString("username");

        Retrofit retrofit = builder.build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        userNameText.setText(userName);

        saveButton.setOnClickListener(view -> saveAdvert());
        cameraButton.setOnClickListener(view -> dispatchTakePictureIntent());
    }

    private void saveAdvert() {
        String title = editTitle.getText().toString();
        String description = editDescription.getText().toString();
        long price = Long.parseLong((editprice.getText().toString()));
        String createdOn = LocalDateTime.now().toString();
        System.out.println(createdOn);


        Advert advert = new Advert(title, description, price, currentPhotoPath, createdOn);

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
                intent.putExtra("token", token);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG);
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
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Glide.with(this).load(currentPhotoPath).into(imageView);
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
        System.out.println(currentPhotoPath.toString());
        return image;
    }
}
