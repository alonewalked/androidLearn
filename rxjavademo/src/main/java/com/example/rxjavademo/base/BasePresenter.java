package com.example.rxjavademo.base;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by tj on 2017/10/16.
 */

public class BasePresenter<T extends BaseView> {
    protected WeakReference<T> mView;
    protected BaseModel mModel = new BaseModel();
    protected Context mContext;

    public BasePresenter(T view) {
        mView = new WeakReference<T>(view);
    }

    public T getView() {
        return mView.get();
    }

    public void onDestroy() {
        if (mView != null) {
            mView.clear();
            mView = null;
        }
        if (mModel != null) {
            mModel = null;
        }
    }
}
