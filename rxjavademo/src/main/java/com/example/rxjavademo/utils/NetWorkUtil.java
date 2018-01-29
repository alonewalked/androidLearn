package com.example.rxjavademo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by tj on 2017/10/16.
 */

public class NetWorkUtil {
    public static boolean isNetConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return true;
//                if (info.getState() == NetworkInfo.State.CONNECTED) {
//
//                }
            }
        }
        return false;
    }
}
