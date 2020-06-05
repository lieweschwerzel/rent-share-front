package com.example.rentshare;

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
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentshare.model.Advert;
import com.example.rentshare.service.JsonPlaceHolderApi;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private EditText searchText;
    private TextView gpsText;

    LocationManager locationManager;
    LocationListener locationListener;
    Double latitude;
    Double longitude;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private RecyclerView mRecyclerView;
    private static String token = null;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String URL = getString(R.string.server);
        searchText = findViewById(R.id.searchText);
        mRecyclerView = (RecyclerView) findViewById(R.id.advertRecycler);
        gpsText = findViewById(R.id.gpsTestText);

        Intent intentToken = getIntent();
        token = intentToken.getExtras().getString("token");

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getAdverts();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.advertRecycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    public void onClickAddButton(View view) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    public void onClickDeleteButton(View view) {
        deleteAll();
    }

    public void onClickSearchButton(View view) {
        searchAdverts();
    }

    private void deleteAll() {
        Call<ResponseBody> call = jsonPlaceHolderApi.deleteAll("Bearer "+ token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "createPost Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(MainActivity.this, "It worked  " + response.toString(), Toast.LENGTH_SHORT).show();
                getAdverts();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG);
            }
        });
    }

    private void searchAdverts() {
        String searchTitle = searchText.getText().toString();
        Call<List<Advert>> call = jsonPlaceHolderApi.search(searchTitle);

        call.enqueue(new Callback<List<Advert>>() {
            @Override
            public void onResponse(Call<List<Advert>> call, retrofit2.Response<List<Advert>> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
//                        textViewResult.setText("wel iets invoeren");
                    } else {
//                        textViewResult.setText("" + response.code());
                    }
                    return;
                }

//                textViewResult.setText("");
                List<Advert> adverts = response.body();

                AdvertAdapter mAdapter = new AdvertAdapter(getApplicationContext(), adverts);
                mRecyclerView.setAdapter(mAdapter);

                if (adverts.isEmpty()) {
//                    textViewResult.setText("Niets gevonden op " + searchTitle);
                }

                for (Advert advert : adverts) {
                    String content = "";
                    content += "ID: " + advert.getId() + "\n";
                    content += "Title: " + advert.getTitle() + "\n";
                    content += "Description: " + advert.getDescription() + "\n\n";
//                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Advert>> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getAdverts() {
        Call<List<Advert>> call = jsonPlaceHolderApi.getAdverts("Bearer "+ token);

        call.enqueue(new Callback<List<Advert>>() {
            @Override
            public void onResponse(Call<List<Advert>> call, retrofit2.Response<List<Advert>> response) {
                if (!response.isSuccessful()) {
//                    textViewResult.setText("code: " + response.code());
                    return;
                }
                List<Advert> adverts = response.body();
                AdvertAdapter mAdapter = new AdvertAdapter(getApplicationContext(), adverts);
                mRecyclerView.setAdapter(mAdapter);

                for (Advert advert : adverts) {
                    String content = "";
                    content += "ID: " + advert.getId() + "\n";
                    content += "Title: " + advert.getTitle() + "\n";
                    content += "Description: " + advert.getDescription() + "\n\n";
//                    textViewResult.append(content);
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<List<Advert>> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settingsmenu:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.signoutmenu:
                signOut2();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void signOut2(){
        token = null;
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClickGps(View view) {
        checkGpsPermissions();
    }

    public void checkGpsPermissions() {

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Toestemming is geweigerd
            // Alertdialog weergeven om toestemming te vragen gps te gebruiken
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Toestemming nodig")
                        .setMessage("Toestemming is nodig om GPS-functie te kunnen gebruiken")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
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
                ActivityCompat.requestPermissions(MainActivity.this,
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
        if (ContextCompat.checkSelfPermission(MainActivity.this,
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
                            Toast.makeText(MainActivity.this, "Geen locatie gevonden", Toast.LENGTH_LONG).show();
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
                        gpsText.setText(addresses.get(0).getLocality() + "\n"
                                + addresses.get(0).getAddressLine(0));
                        gpsText.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "over 5 seconden redirect naar maps", Toast.LENGTH_LONG).show();

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);
                        startActivity(intent);
                    }, 5000);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}
