package com.example.rxjavademo.progress;

import android.content.Context;
import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by tj on 2017/10/16.
 */

public class ProgressObserver<T> implements Observer<T>, ProgressCancelListener {

    private static final String TAG = "ProgressObserver";
    private ObserverOnNextListener listener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Context context;
    private Disposable d;

    public ProgressObserver(Context context, ObserverOnNextListener listener) {
        this.listener = listener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }


    @Override
    public void onCancelProgress() {
        //如果处于订阅状态，则取消订阅
        if (!d.isDisposed()) {
            d.dispose();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        Log.d(TAG, "onSubscribe: ");
        showProgressDialog();
    }

    @Override
    public void onNext(T value) {
        listener.onNext(value);
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        Log.e(TAG, "onError: ", e);
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
        Log.d(TAG, "onComplete: ");
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG)
                    .sendToTarget();
            mProgressDialogHandler = null;
        }
    }
}
