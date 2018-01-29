package com.example.mixerframework;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by tj on 2017/10/25.
 */

public class LHWebView extends WebView {
    private boolean mIsDestroy = false;

    public LHWebView(Context context) {
        super(context);
        mIsDestroy = false;
    }

    public LHWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mIsDestroy = false;
    }

    @Override
    public void destroy() {
        super.destroy();
        mIsDestroy = true;
    }

    public boolean isDestroy() {
        return mIsDestroy;
    }
}
