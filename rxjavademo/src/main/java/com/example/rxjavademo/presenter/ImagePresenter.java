package com.example.rxjavademo.presenter;

import com.example.rxjavademo.api.ImageApi;
import com.example.rxjavademo.contract.ImageConstract;
import com.example.rxjavademo.model.ImageModel;
import com.example.rxjavademo.progress.ObserverOnNextListener;

/**
 * Created by tj on 2017/10/16.
 */

public class ImagePresenter extends ImageConstract.Presenter {

    private static final String TAG = "ImagePresenter";

    public ImagePresenter(ImageConstract.View view) {
        super(view);
    }

    @Override
    public void getImages(String query, int startIndex, int endIndex) {
        mModel.Subscribe(mContext,
                ImageApi.getImageService().getImageData(query, startIndex, endIndex),
                new ObserverOnNextListener() {
                    @Override
                    public void onNext(Object o) {
                        getView().result((ImageModel) o);
                    }
                });
    }
}
