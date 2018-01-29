package com.example.artdemo.viewdemo.view;

import com.example.artdemo.R;
import com.example.artdemo.viewdemo.listener.CustomGestureListener;
import com.example.artdemo.viewdemo.listener.IViewOnGesture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by tj on 2018/1/12.
 */

public class ZoomFragment extends Fragment {

    private static final String TAG = "ZoomFragment";

    ImageView mImageView;
    Button mBtnScroll;
    GestureDetector detector;
    CustomGestureListener mCustomGestureListener;
    //初始的图片资源
    Bitmap bitmap;
    //定义图片的宽、高
    int width, height;
    //定义最后位置
    float mLastX = 0;
    float mLastY = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_zoomer, container, false);
        this.initView(rootView);
        this.initEvent(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mImageView = (ImageView) rootView.findViewById(R.id.img_zoom);
        mBtnScroll = (Button) rootView.findViewById(R.id.img_scroll);
        // set Anime
        setAnimation(mImageView);
        //获取被缩放的原图片
        bitmap = BitmapFactory.decodeResource(
                this.getResources(),R.drawable.a
        );
        //获得位图宽
        width = bitmap.getWidth();
        //获得位图高
        height = bitmap.getHeight();
    }

    private void initEvent(View rootView) {
        mCustomGestureListener = new CustomGestureListener(new IViewOnGesture() {
            @Override
            public void doOnFling() {
                BitmapDrawable tmp = (BitmapDrawable)mImageView.getDrawable();
                //如狗图片还未收回，先强制收回该图片
                if(!tmp.getBitmap().isRecycled())
                {
                    tmp.getBitmap().recycle();
                }
                //根据原始位图和Matrix创建新图片
                Bitmap bitmap2 = Bitmap.createBitmap(bitmap,0,0,width, height,mCustomGestureListener.getMatrix(), true);
                //显示新的位图
                ZoomFragment.this.mImageView.setImageBitmap(bitmap2);
            }
        });
        //创建手势检测器
        detector = new GestureDetector(getActivity(), mCustomGestureListener);

        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return true;
            }
        });

        mBtnScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:

                        int x = (int) motionEvent.getRawX();
                        int y = (int) motionEvent.getRawY();

                        if (mLastX == 0) {
                            mLastX = x;
                        }
                        if (mLastY == 0) {
                            mLastY = y;
                        }

                        float deltaX = x - mLastX;
                        float deltaY = y - mLastY;
                        Log.d(TAG, "viewdeom___deltaX: " + deltaX + "deltaY: " + deltaY);
                        float tX = /* view.getTranslationX() +*/ deltaX;
                        float tY = view.getTranslationY() +  deltaY;
                        Log.d(TAG, "viewdeom___tX: " + tX + "tY: " + tY);
                        view.setTranslationX(tX);
                        view.setTranslationY(tY);

                        mLastY = x;
                        mLastY = y;

                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    private void setAnimation(View view) {
        // 步骤1:创建 需要设置动画的 视图View
        Animation translateAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.view_animation);
        // 步骤2:创建 动画对象 并传入设置的动画效果xml文件
        view.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // reset handler
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void resetLayout(View view) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.width += 100;
        params.height += 100;
        params.leftMargin += 100;
        view.requestLayout();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ZoomFragment.this.resetLayout(mImageView);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
