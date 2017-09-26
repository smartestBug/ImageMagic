package com.msemyak.imagemagic.mvp.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msemyak.imagemagic.R;
import com.msemyak.imagemagic.mvp.model.local.Image;
import com.msemyak.imagemagic.utils.GlideApp;

import java.util.List;


public class RVAdapterImages extends RecyclerView.Adapter<RVAdapterImages.myViewHolder> {

    private List<Image> imagesList;
    private Context context;

    public RVAdapterImages(List<Image> imagesList, Context context) {
        this.imagesList = imagesList;
        this.context = context;
    }

    public void setNewData(List<Image> imagesList) {
        this.imagesList = imagesList;
    }

    @Override
    public RVAdapterImages.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_image_item, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {

        Image imageForDataBind = imagesList.get(position);

        GlideApp.with(context)
                .load(imageForDataBind.getSmallImagePath())
                .centerCrop()
                .into(holder.ivImage);
        holder.tvWeather.setText(imageForDataBind.getParameters().getWeather());
        holder.tvAddress.setText(imageForDataBind.getParameters().getAddress());

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivImage;
        public TextView tvWeather;
        public TextView tvAddress;

        public myViewHolder(View v) {
            super(v);
            ivImage = (ImageView) v.findViewById(R.id.ivImage);
            tvWeather = (TextView) v.findViewById(R.id.tvWeather);
            tvAddress = (TextView) v.findViewById(R.id.tvAddress);
        }

    }

}


