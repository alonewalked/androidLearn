package com.example.mixerframework.loadstatus;

import android.view.View;

/**
 * Created by tj on 2017/10/25.
 */

public interface ILoadingView {
    /**
     * 加载中页面显示设置
     */

    void show();
    void hide();
    View getView();
}
