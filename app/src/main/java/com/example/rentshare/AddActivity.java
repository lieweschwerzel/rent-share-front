package com.example.rentshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rentshare.model.Advert;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddActivity extends AppCompatActivity {
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private Button saveButton;
    private EditText editTitle, editDescription, editprice;
    private String URL = "http://192.168.1.105:3010/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        saveButton = findViewById(R.id.saveButtonView);
        editTitle = findViewById(R.id.editTitleView);
        editDescription = findViewById(R.id.editDecriptionView);
        editprice = findViewById(R.id.editPriceView);


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
                    Toast.makeText(AddActivity.this, ""+t.getMessage(), Toast.LENGTH_LONG);
          }
            });

        }));


    }

//
//    private void createPost() {
////        Map<String, String> fields = new HashMap<>();
////        fields.put("name", "New Name");
////        fields.put("email", "New email");
//       new User("Susan", "Suus@n.nl");
//        Call<Void> call = jsonPlaceHolderApi.createAdvert(advert);
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//
//                if (!response.isSuccessful()) {
//
//                    return;
//                }
//                Toast.makeText(AddActivity.this, "It worked", Toast.LENGTH_SHORT).show();
////                getUsers();
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(AddActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
////                textViewResult.setText("FAILURE " + t.getMessage() + "\n\n");
////                getUsers();
//        });
//    }
}
