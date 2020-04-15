package com.example.rentshare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rentshare.R;
import com.example.rentshare.model.Advert;

import java.util.List;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.ViewHolder> {

        private List<Advert> mAdverts;


    public AdvertAdapter(List<Advert> mAdverts) {
            this.mAdverts = mAdverts;
        }

        @NonNull
        @Override
        public AdvertAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.advertisement_layout, viewGroup, false);

            AdvertAdapter.ViewHolder viewHolder = new AdvertAdapter.ViewHolder(view);
            return viewHolder;
        }

    @Override
    public void onBindViewHolder(@NonNull AdvertAdapter.ViewHolder viewHolder, int i) {
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

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView title;
            TextView description;
            TextView price;
            ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.titleText);
                description = itemView.findViewById(R.id.descriptionText);
                price = itemView.findViewById(R.id.priceText);
                image = itemView.findViewById(R.id.advertisementImage);
            }
        }
}
