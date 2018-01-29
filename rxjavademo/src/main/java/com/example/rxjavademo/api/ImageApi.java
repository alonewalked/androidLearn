package com.example.rxjavademo.api;

import com.example.rxjavademo.base.BaseApi;

/**
 * Created by tj on 2017/10/16.
 */

public class ImageApi {

    private String baseUrl = "http://image.so.com/";

    private volatile static ImageServices imageService;

    public static ImageServices getImageService() {
        if (imageService == null) {
            synchronized (ImageApi.class) {
                if (imageService == null) {
                    new ImageApi();
                }
            }
        }
        return imageService;
    }

    private ImageApi() {
        BaseApi baseApi = new BaseApi();
        imageService = baseApi.getRetrofit(baseUrl).create(ImageServices.class);
    }
}
