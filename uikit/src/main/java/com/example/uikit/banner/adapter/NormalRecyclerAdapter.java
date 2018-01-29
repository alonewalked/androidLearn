package com.example.uikit.banner.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.uikit.banner.RecyclerViewBannerBase;

import java.util.List;

/**
 * Created by tj on 2017/12/15.
 * Normal Recycler Adapter
 */

public class NormalRecyclerAdapter extends BaseBannerAdapter<NormalRecyclerAdapter.NormalHolder> {

    private RecyclerViewBannerBase.OnBannerItemClickListener onBannerItemClickListener;

    public NormalRecyclerAdapter(List<String> urlList, Context context) {
        this(context, urlList, null);
    }

    public NormalRecyclerAdapter(Context context, List<String> urlList, RecyclerViewBannerBase.OnBannerItemClickListener onBannerItemClickListener) {
        super(urlList, context);
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    protected NormalRecyclerAdapter.NormalHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        return new NormalHolder(new ImageView(context));
    }

    @Override
    protected void bindCustomViewHolder(NormalHolder holder, final int position) {
        if (null == urlList || urlList.isEmpty()) {
            return;
        }
        String url = urlList.get(position % urlList.size());
        ImageView img = (ImageView) holder.itemView;
        Glide.with(context).load(url).into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerItemClickListener != null) {
                    onBannerItemClickListener.onItemClick(position % urlList.size());
                }
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // self class

    class NormalHolder extends RecyclerView.ViewHolder {
        ImageView bannerItem;

        NormalHolder(View itemView) {
            super(itemView);
            bannerItem = (ImageView) itemView;
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            bannerItem.setLayoutParams(params);
            bannerItem.setScaleType(ImageView.ScaleType.FIT_XY);

        }
    }
    /////////////////////////////////////////////////////////////////////////// self class end


}
