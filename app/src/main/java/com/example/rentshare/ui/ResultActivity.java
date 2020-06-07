package com.example.rentshare.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rentshare.R;

public class ResultActivity extends AppCompatActivity {

    private TextView title, description, price;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advert_details_layout);


        title = findViewById(R.id.titleDetail);
        description = findViewById(R.id.descriptionDetail);
        price = findViewById(R.id.priceDetail);
        image = findViewById(R.id.imageDetail);


        Intent intent = getIntent();
        String newTitle = intent.getExtras().getString("title");
        String newDescription = intent.getExtras().getString("description");
        Long newPrice = intent.getExtras().getLong("price");
        Glide.with(getApplicationContext()).load(intent.getExtras().getString("imageUrl")).into(image);

        title.setText(newTitle);
        description.setText(newDescription);
        price.setText(newPrice + "€ per dag");
    }
}
