package com.example.rxjavademo.api;

import com.example.rxjavademo.base.BaseApi;

/**
 * Created by tj on 2017/10/17.
 */

public class LoginApi {
    private String baseUrl = "http://10.58.95.158:8080/";

    private volatile static LoginServices loginServices;

    public static LoginServices getLoginServices() {
        if (loginServices == null) {
            synchronized (LoginApi.class) {
                if (loginServices == null) {
                    new LoginApi();
                }
            }
        }
        return loginServices;
    }

    private LoginApi() {
        BaseApi baseApi = new BaseApi();
        loginServices = baseApi.getRetrofit(baseUrl).create(LoginServices.class);
    }
}
