package com.example.mixerframework.loadstatus;

import android.view.View;

/**
 * Created by tj on 2017/10/25.
 */

public interface ILoadingErrorView {

    /**
     * 页面加载失败
     */
    void show();
    void hide();
    View getView();
}
