package com.example.rxjavademo.contract;

import com.example.rxjavademo.base.BasePresenter;
import com.example.rxjavademo.base.BaseView;
import com.example.rxjavademo.model.ImageModel;

/**
 * Created by tj on 2017/10/16.
 */

public interface ImageConstract {

    interface View extends BaseView {
        void result(ImageModel s);
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void getImages(String query,int startIndex,int endIndex);
    }
}
