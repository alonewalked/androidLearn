package com.example.rxjavademo.contract;

import com.example.rxjavademo.base.BasePresenter;
import com.example.rxjavademo.base.BaseView;

/**
 * Created by tj on 2017/10/16.
 */

public interface MainContract {

    interface View extends BaseView {
        void result(String s);
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void getLocation(String city);
    }
}
