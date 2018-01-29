package com.example.artdemo.viewdemo.view;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by tj on 2018/1/15.
 */

public class ScrollerLayout extends ViewGroup {
    private final static String TAG = "ScrollerLayout";

    // 内部scroller
    private Scroller mScroller;
    // 最小平移距离
    private int mTouchSlop;

    // 坐标
    private float mXDown;
    private float mXMove;
    // 上次触发ACTION_MOVE事件时的屏幕坐标
    private float mXLastMove;

    // 边界
    private int leftBorder;
    private int rightBorder;

    public ScrollerLayout(Context context) {
        this(context, null);
    }

    public ScrollerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化scroller
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 为ScrollerLayout中的每一个子控件测量大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        // has changed?
        int childCount = getChildCount();
        if (b) {
            for (int idx = 0; idx < childCount; idx ++) {
                View childView = getChildAt(idx);
                childView.layout(
                        idx * childView.getMeasuredWidth(),
                        0,
                        (idx+1) * childView.getMeasuredWidth(),
                        childView.getMeasuredHeight());
            }
            leftBorder = getChildAt(0).getLeft();
            rightBorder = getChildAt(childCount -1).getRight();
        }
    }

    // 拦截 touch event
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "scroller___intercept touch event: MotionEvent.ACTION_DOWN");
                mXDown = ev.getRawX();
                mXLastMove = mXDown;
                return false;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "scroller___intercept touch event: MotionEvent.ACTION_MOVE");
                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                mXLastMove = mXMove;
                // 移动大于最小位移
                if (diff > mTouchSlop) {
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "scroller___touch event: MotionEvent.ACTION_DOWN");
                return false;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "scroller___touch event: MotionEvent.ACTION_MOVE");
                mXMove = event.getRawX();
                int scrolledX = (int) (mXLastMove - mXMove);
                // 可以左滑动
                if (getScrollX() + scrolledX < leftBorder) {
                    scrollTo(leftBorder, 0);
                    return true;
                }
                // 向右滑动
                else if (getScrollX() + getWidth() + scrolledX > rightBorder){
                    scrollTo(rightBorder - getWidth(), 0);
                    return true;
                }
                scrollBy(scrolledX, 0);
                mXLastMove = mXMove;
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "scroller___touch event: MotionEvent.ACTION_UP");
                // 1. 抬起手指， 判断滑动目标控件
                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                int dx = targetIndex * getWidth() - getScrollX();
                // 2. startScroll
                mScroller.startScroll(getScrollX(), 0, dx, 0);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    // 3. 平滑滚动处理
    @Override
    public void computeScroll() {
        // 发生位移
        if (null != mScroller && mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
