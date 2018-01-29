package com.example.artdemo.viewdemo.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.graphics.Matrix;

/**
 * Created by tj on 2018/1/12.
 */

public class CustomGestureListener implements GestureDetector.OnGestureListener {

    private float currentScale = 1;
    //控制图片缩放的Matrx 对象
    Matrix matrix;
    IViewOnGesture mIViewOnGesture;

    public CustomGestureListener() {
        this(null);
    }

    public CustomGestureListener(IViewOnGesture iViewOnGesture) {
        mIViewOnGesture = iViewOnGesture;
        matrix = new Matrix();
    }

    public float getCurrentScale() {
        return currentScale;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    // 缩放
    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        // v1为velocityX, v2为velocityY
        v = v > 4000 ? 4000 : v;
        v = v1 > 4000 ? 4000 : v1;

        //根据手势的速度来计算缩放比，如果velocityX>0,放大图像，否则缩小图像
        currentScale += currentScale * v / 4000.0f;
        // 重置Matrix
        matrix.reset();
        //缩放Matrix
        matrix.setScale(currentScale,currentScale,160,200);
        setMatrix(matrix);

        if (null != mIViewOnGesture) {
            mIViewOnGesture.doOnFling();
        }

        return true;


    }
}
