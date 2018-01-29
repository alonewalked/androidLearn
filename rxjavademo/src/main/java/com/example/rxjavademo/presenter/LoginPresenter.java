package com.example.rxjavademo.presenter;

import com.example.rxjavademo.api.LoginApi;
import com.example.rxjavademo.contract.LoginConstract;
import com.example.rxjavademo.model.LoginResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tj on 2017/10/17.
 */

public class LoginPresenter extends LoginConstract.Presenter {


    public LoginPresenter(LoginConstract.View view) {
        super(view);
    }

    @Override
    public void doLogin(String username, String password) {
        LoginApi.getLoginServices().login(username, password)
                .subscribeOn(Schedulers.io())               //在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求结果
                .subscribe(new Observer<LoginResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginResponse value) {
                        getView().result(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().result(new LoginResponse("登录失败", -1));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
