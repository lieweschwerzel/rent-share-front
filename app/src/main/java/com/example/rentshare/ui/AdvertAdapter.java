package com.example.rentshare.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rentshare.R;
import com.example.rentshare.model.Advert;

import java.util.List;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.AdvertViewholder> {

        private List<Advert> mAdverts;
        private Context context;
        private String token;


    public AdvertAdapter(Context context, List<Advert> mAdverts, String token) {
            this.context = context;
            this.mAdverts = mAdverts;
            this.token = token;
        }

        @NonNull
        @Override
        public AdvertViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.cardview2, viewGroup, false);

            AdvertViewholder viewHolder = new AdvertViewholder(view);
            return viewHolder;
        }

    @Override
    public void onBindViewHolder(@NonNull AdvertViewholder viewHolder, int i) {
        Advert advert = mAdverts.get(i);
        viewHolder.title.setText(advert.getTitle());
        viewHolder.description.setText(advert.getDescription());
        viewHolder.price.setText(advert.getPrice() + "€");
//        viewHolder.image.setImageResource(R.drawable.bezem);
        Glide.with(context).load(advert.getImageUrl()).into(viewHolder.image);
    }

        @Override
        public int getItemCount() {
            return mAdverts.size();
        }

        public void swapList(List<Advert> newList) {
            mAdverts = newList;
            if (newList != null) {
                this.notifyDataSetChanged();
            }
        }

        public class AdvertViewholder extends RecyclerView.ViewHolder {

            TextView title;
            TextView description;
            TextView price;
            ImageView image;

            public AdvertViewholder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.titleTextCard2);
                description = itemView.findViewById(R.id.descriptionTextCard2);
                price = itemView.findViewById(R.id.priceTextCard2);
                image = itemView.findViewById(R.id.advertisementImageCard2);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Advert onClickedAdvert = mAdverts.get(position);
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("title", onClickedAdvert.getTitle());
                        intent.putExtra("description", onClickedAdvert.getDescription());
                        intent.putExtra("price", onClickedAdvert.getPrice());
                        intent.putExtra("imageUrl", onClickedAdvert.getImageUrl());
                        intent.putExtra("createdOn", onClickedAdvert.getCreatedOn());
                        intent.putExtra("adowner", onClickedAdvert.getAdvertOwner());
                        intent.putExtra("token", token);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }
        }
}
