package com.example.rxjavademo.base;

import android.content.Context;

import com.example.rxjavademo.progress.ObserverOnNextListener;
import com.example.rxjavademo.progress.ProgressObserver;

import io.reactivex.Observable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tj on 2017/10/16.
 */

public class BaseModel<T> {

    /**
     * 封装线程管理和订阅的过程
     * @param context
     * @param observable
     * @param listener
     */
    public void Subscribe (Context context,
                           final Observable observable,
                           ObserverOnNextListener<T>
                                   listener) {
        final Observer<T> observer = new ProgressObserver<T>(context, listener);

        observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
