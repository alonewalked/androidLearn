package com.example.rxjavademo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rxjavademo.R;
import com.example.rxjavademo.model.ImageItemModel;

import java.util.ArrayList;

/**
 * Created by tj on 2017/10/16.
 */

public class ImageAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<ImageItemModel> mImageItems;

    public ImageAdapter(Context context, ArrayList<ImageItemModel> imageItems) {
        this.mContext = context;
        this.mImageItems = imageItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final String url = mImageItems.get(position).getImg();
        Glide.with(mContext)
                .load(url)
                .fitCenter()
                .override(mImageItems.get(position).getWidth(),mImageItems.get(position).getHeight())
                .placeholder(R.mipmap.ic_launcher)
                .into(((ViewHolder)holder).image);

        ((ViewHolder)holder).image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.img_item);
        }
    }
}
