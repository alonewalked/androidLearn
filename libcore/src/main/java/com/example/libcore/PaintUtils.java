package com.example.libcore;

import android.content.Context;

/**
 * Created by tj on 2017/10/29.
 */

public class PaintUtils {

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(Context context,float spValue) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }
}
