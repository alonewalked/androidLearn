package com.example.rxjavademo.contract;

import com.example.rxjavademo.base.BasePresenter;
import com.example.rxjavademo.base.BaseView;
import com.example.rxjavademo.model.LoginResponse;

/**
 * Created by tj on 2017/10/17.
 */

public interface LoginConstract {

    interface View extends BaseView {
        void result(LoginResponse s);
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void doLogin(String username,String password);
    }
}
