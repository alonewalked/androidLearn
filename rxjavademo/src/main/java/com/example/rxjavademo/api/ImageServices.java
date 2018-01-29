package com.example.rxjavademo.api;

import com.example.rxjavademo.model.ImageModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tj on 2017/10/16.
 */

public interface ImageServices {

    @GET("j")
    Observable<ImageModel> getImageData(@Query("q") String q,
                                        @Query("sn") int sn,
                                        @Query("pn") int pn);
}
