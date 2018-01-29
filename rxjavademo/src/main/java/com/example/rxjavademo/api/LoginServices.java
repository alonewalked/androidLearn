package com.example.rxjavademo.api;

import com.example.rxjavademo.model.LoginResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tj on 2017/10/17.
 */

public interface LoginServices {

    @GET("v1/user/login")
    Observable<LoginResponse> login(@Query("username") String username,
                                    @Query("password") String password);
}
