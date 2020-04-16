package com.example.rentshare;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rentshare.model.Advert;

import java.util.List;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.AdvertViewholder> {

        private List<Advert> mAdverts;
        private Context context;


    public AdvertAdapter(Context context, List<Advert> mAdverts) {
            this.context = context;
            this.mAdverts = mAdverts;
        }

        @NonNull
        @Override
        public AdvertViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.advertisement_layout, viewGroup, false);

            AdvertViewholder viewHolder = new AdvertViewholder(view);
            return viewHolder;
        }

    @Override
    public void onBindViewHolder(@NonNull AdvertViewholder viewHolder, int i) {
        Advert advert = mAdverts.get(i);
        viewHolder.title.setText(advert.getTitle());
        viewHolder.description.setText(advert.getDescription());
        viewHolder.price.setText(advert.getPrice() + "");
        viewHolder.image.setImageResource(R.drawable.bezem);
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
                title = itemView.findViewById(R.id.titleText);
                description = itemView.findViewById(R.id.descriptionText);
                price = itemView.findViewById(R.id.priceText);
                image = itemView.findViewById(R.id.advertisementImage);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Advert onClickedAdvert = mAdverts.get(position);
                        Intent intent = new Intent(context, ResultActivity.class);
                        intent.putExtra("title", onClickedAdvert.getTitle());
                        intent.putExtra("description", onClickedAdvert.getDescription());
                        intent.putExtra("price", onClickedAdvert.getPrice());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }
        }
}