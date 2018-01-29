package com.example.rxjavademo.api;

import com.example.rxjavademo.model.City;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tj on 2017/10/16.
 */

public interface LocationService {

    @GET("geocoding")
    Observable<String> getLocation(@Query("a") String a);//获取的请求结果为String

    @GET("geocoding")
    Observable<City> getCity(@Query("a") String a);//获取的请求结果为实体类型

}
