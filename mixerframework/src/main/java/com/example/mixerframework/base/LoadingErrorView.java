package com.example.mixerframework.base;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mixerframework.R;
import com.example.mixerframework.loadstatus.ILoadingErrorView;

/**
 * Created by tj on 2017/10/25.
 */

public class LoadingErrorView extends LinearLayout implements ILoadingErrorView {
    public LoadingErrorView(Context context) {
        super(context);
        View.inflate(context, R.layout.widget_loading_error_view, this);
    }

    @Override
    public void show() {
        this.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        this.setVisibility(View.GONE);
    }

    @Override
    public View getView() {
        return this;
    }

}
