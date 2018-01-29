package com.example.artdemo.viewdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by tj on 2018/1/26.
 */

public class DragLayout extends ViewGroup {

    private static final String TAG = "DragLayout";

    private Scroller mSroller;//用于完成滚动的实例
    private int mTouchSlop;//判定为拖动的最小移动像素数

    private int leftBorder;//界面可滚动的左边界
    private int rightBorder;//界面可滚动的右边界

    private float mFirstX = 0;//第一次触碰屏幕的x坐标
    private float mFirstY = 0;//第一次触碰屏幕的y坐标
    private float mLastX = 0;//滑动屏幕时的x坐标

    private float mLastXIntercept = 0;//用于onInterceptTouchEvent中滑动屏幕的x坐标
    private float mLastYIntercept = 0;//用于onInterceptTouchEvent中滑动屏幕的y坐标


    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        // layout变动
        if (b) {
            int childCount = getChildCount();
            for (int idx = 0; idx < childCount; idx++) {
                View child = getChildAt(idx);
                child.layout(
                        idx * child.getMeasuredWidth(),
                        0,
                        ( i+1 ) * child.getMeasuredWidth(),
                        child.getMeasuredHeight());
            }
            // 初始化边界
            leftBorder = getChildAt(0).getLeft();
            rightBorder = getChildAt(childCount - 1).getRight();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "ev------->" + ev.getAction());
        Boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstX = ev.getRawX();
                mFirstY = ev.getRawY();
                mLastX = ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                mLastXIntercept = ev.getRawX();
                mLastYIntercept = ev.getRawY();
                float delX = Math.abs(mLastXIntercept - mFirstX);
                float dexY = Math.abs(mLastYIntercept - mFirstY);
                mFirstX = mLastXIntercept;
                if (getScrollX() + leftBorder < getWidth()) {//此时为第一个fragment的页面
                    if (delX > dexY) {
                        intercept = true;//x滑动距离大于y时，此时拦截子控件的事件
                    } else {
                        intercept = false;
                    }
                } else if (getScrollX() + leftBorder > getWidth() && getScrollX() + leftBorder <
                        2 * getWidth()) {//此时为第二个fragment的页面
                    intercept = false;
                } else {//此时是第三个fragment的页面
                    if (delX > dexY) {
                        intercept = true;//x滑动距离大于y时，此时拦截子控件的事件
                    } else {
                        intercept = false;
                    }
                }

                /*
                if (delX < mTouchSlop) {//如果滑动的最小距离小于toushSlop,那么不认为滑动，不拦截子控件的事件
                    intercept = false;
                } else {
                    intercept = true;
                }
                */

                break;
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "event------->" + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                mLastX = event.getRawX();
                int delX = (int) (mFirstX - mLastX);
                if (getScrollX() + delX < leftBorder) {
                    scrollTo(leftBorder, 0);
                    return true;
                } else if (getScrollX() + getWidth() + delX > rightBorder) {
                    scrollTo(rightBorder - getWidth(), 0);
                    return true;
                }
                scrollBy(delX, 0);
                mFirstX = mLastX;
                break;
            case MotionEvent.ACTION_UP:
                int index = (getScrollX() + getWidth() / 2) / getWidth();
                int dx = index * getWidth() - getScrollX();
                mSroller.startScroll(getScrollX(), 0, dx, 0);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (mSroller.computeScrollOffset()) {
            scrollTo(mSroller.getCurrX(), mSroller.getCurrY());
            invalidate();
        }
    }
}
