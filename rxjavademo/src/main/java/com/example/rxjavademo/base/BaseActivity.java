package com.example.rxjavademo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by tj on 2017/10/16.
 */

public abstract class BaseActivity <T extends BasePresenter> extends AppCompatActivity{
    public T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initPresenter();
        if (mPresenter != null) {
            mPresenter.mContext = this;
        }

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // abstract

    /**
     * get layout id
     * @return
     */
    public abstract int getLayoutId();

    /**
     * init presenter
     */
    public abstract void initPresenter();

    /**
     * init activity
     */
    public abstract void init();

    /////////////////////////////////////////////////////////////////////////// abstract end


}
