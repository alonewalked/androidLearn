package com.example.rxjavademo.presenter;

import com.example.rxjavademo.api.LocationApi;
import com.example.rxjavademo.contract.MainContract;
import com.example.rxjavademo.progress.ObserverOnNextListener;

/**
 * Created by tj on 2017/10/16.
 */

public class MainPresenter extends MainContract.Presenter {

    private static final String TAG = "MainPresenter";

    public MainPresenter(MainContract.View view) {
        super(view);
    }

    @Override
    public void getLocation(String city) {
        mModel.Subscribe(mContext,
                LocationApi.getLocationService().getLocation(city),
                new ObserverOnNextListener() {

                    @Override
                    public void onNext(Object o) {
                        getView().result(o+"");
                    }
                });
    }
}
