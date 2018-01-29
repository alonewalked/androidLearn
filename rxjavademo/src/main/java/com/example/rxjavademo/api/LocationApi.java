package com.example.rxjavademo.api;

import com.example.rxjavademo.base.BaseApi;

/**
 * Created by tj on 2017/10/16.
 */

public class LocationApi {

    private String baseUrl = "http://gc.ditu.aliyun.com/";

    private volatile static LocationService locationService;

    public static LocationService getLocationService() {
        if (locationService == null) {
            synchronized (LocationApi.class) {
                if (locationService == null) {
                    new LocationApi();
                }
            }
        }
        return locationService;
    }

    private LocationApi() {
        BaseApi baseApi = new BaseApi();
        locationService = baseApi.getRetrofit(baseUrl).create(LocationService.class);
    }
}
