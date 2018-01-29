package com.example.uikit.banner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by tj on 2017/12/15.
 * Base Banner Adapter
 */

public abstract class BaseBannerAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<String> urlList;
    protected Context context;

    public BaseBannerAdapter(List<String> urlList, Context context) {
        this.urlList = urlList;
        this.context = context;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return createCustomViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.bindCustomViewHolder((VH)holder, position);
    }

    @Override
    public int getItemCount() {
        return urlList.size() < 2 ? 1 : Integer.MAX_VALUE;
    }

    /**
     * custom view holder
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract VH createCustomViewHolder(ViewGroup parent, int viewType);


    /**
     * bind custom view holder
     * @param holder
     * @param position
     */
    protected abstract void bindCustomViewHolder(VH holder, int position);



}
