package com.example.rentshare.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentshare.R;
import com.example.rentshare.model.Advert;
import com.example.rentshare.model.ServerResponse;
import com.example.rentshare.service.ApiConfig;
import com.example.rentshare.service.AppConfig;
import com.example.rentshare.service.JsonPlaceHolderApi;

import com.bumptech.glide.Glide;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static String imageFileName = "";
    private String currentPhotoPath = null;
    private static String token = null;
    private static String userName = null;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private Button saveButton, cameraButton;
    private EditText editTitle, editDescription, editprice;
    private TextView userNameText;
    private ImageView imageView;
    LocationManager locationManager;
    LocationListener locationListener;
    Double latitude;
    Double longitude;
    private TextView locationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("Voeg een Advertentie toe");
        String URL = this.getResources().getString(R.string.server);
        saveButton = findViewById(R.id.saveButtonView);
        cameraButton = findViewById(R.id.cameraButtonView);
        editTitle = findViewById(R.id.editTitleView);
        editDescription = findViewById(R.id.editDecriptionView);
        editprice = findViewById(R.id.editPriceView);
        imageView = findViewById(R.id.cameraImageView);
        userNameText = findViewById(R.id.advertOwnerIUserNameAdd);
        locationText = findViewById(R.id.locationText);


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create());

        Intent intentToken = getIntent();
        token = intentToken.getExtras().getString("token");
        userName = intentToken.getExtras().getString("username");

        Retrofit retrofit = builder.build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        userNameText.setText("Welkom gebruiker: \n" + userName);

        saveButton.setOnClickListener(view -> saveAdvert());
        cameraButton.setOnClickListener(view -> dispatchTakePictureIntent());
    }

    private void saveAdvert() {

        if (latitude == null || longitude == null) {
            Toast.makeText(this, "Zorg ervoor dat je je locatie meegeeft", Toast.LENGTH_LONG).show();
        } else {
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();
            long price = Long.parseLong((editprice.getText().toString()));
            String createdOn = LocalDateTime.now().toString();
            String advertOwner = userName;
            String imageUrl = "https://rentshare.000webhostapp.com/uploads/" + imageFileName;
            System.out.println(createdOn);


            Advert advert = new Advert(title, description, price, imageUrl, createdOn, advertOwner, latitude, longitude);

            Call<Void> call = jsonPlaceHolderApi.createAdvert(advert, "Bearer " + token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (!response.isSuccessful()) {
                        Toast.makeText(AddActivity.this, "error code: " + response.code(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    Toast.makeText(AddActivity.this, "Advertentie toegevoegd", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("username", userName);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AddActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG);
                }
            });
        }
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
            uploadFile();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        imageFileName = image.getName();
        return image;
    }

    private void uploadFile() {
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(currentPhotoPath);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ServerResponse> call = getResponse.uploadFile(fileToUpload, filename);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
//                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
//                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }

    public void onClickGps(View view) {
        checkGpsPermissions();
    }

    public void checkGpsPermissions() {

        if (ContextCompat.checkSelfPermission(AddActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Toestemming is geweigerd
            // Alertdialog weergeven om toestemming te vragen gps te gebruiken
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Toestemming nodig")
                        .setMessage("Toestemming is nodig om GPS-functie te kunnen gebruiken")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(AddActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            }

                        })
                        .setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();

            } else {
                // vragen om toestemming
                ActivityCompat.requestPermissions(AddActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else
        // toestemming is gegeven
        {
            UseGps();
        }
    }

    private void UseGps() {
        if (ContextCompat.checkSelfPermission(AddActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // als GPS is uitgeschakeld vragen deze aan te zetten
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showAlertGpsDisabled();
            }

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        // stoppen met GPS gebruiken
                        locationManager.removeUpdates(locationListener);

                        UtilizeCoordinates();
                    } else {
                        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                            Toast.makeText(AddActivity.this, "Geen locatie gevonden", Toast.LENGTH_LONG).show();
                    }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // stoppen met GPS gebruiken
                    locationManager.removeUpdates(locationListener);
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                    // stoppen met GPS gebruiken
                    locationManager.removeUpdates(locationListener);
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void showAlertGpsDisabled() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Het lijkt erop dat GPS is uitgeschakeld, wil je GPS nu inschakelen?")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }

                })
                .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void UtilizeCoordinates() {

        try {
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);

            if (addresses.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Geen adres gevonden op basis van coordinaten", Toast.LENGTH_LONG).show();
            } else {
                locationText.setText(addresses.get(0).getLocality() + "\n"
                        + addresses.get(0).getAddressLine(0));
                locationText.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
