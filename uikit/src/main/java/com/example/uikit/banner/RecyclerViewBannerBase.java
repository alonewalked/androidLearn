package com.example.uikit.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.libcore.PaintUtils;
import com.example.uikit.R;
import com.example.uikit.banner.adapter.BaseBannerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tj on 2017/12/15.
 * Recycler Banner base
 */

public abstract class RecyclerViewBannerBase<L extends RecyclerView.LayoutManager, A extends BaseBannerAdapter>
        extends FrameLayout {

    ///////////////////////////////////////////////////////////////////////////
    // self variable
    protected int autoPlayDuration;//刷新间隔时间

    protected boolean showIndicator;//是否显示指示器
    protected RecyclerView indicatorContainer;
    protected Drawable mSelectedDrawable;
    protected Drawable mUnselectedDrawable;
    protected IndicatorAdapter indicatorAdapter;
    protected int indicatorMargin;//指示器间距

    protected RecyclerView mRecyclerView;
    protected A adapter;
    protected L mLayoutManager;

    protected int WHAT_AUTO_PLAY = 1000;

    protected boolean hasInit;
    protected int bannerSize = 1;
    protected int currentIndex;
    protected boolean isPlaying;

    protected boolean isAutoPlaying;
    protected List<String> tempUrlList = new ArrayList<>();


    /////////////////////////////////////////////////////////////////////////// self variable end

    public RecyclerViewBannerBase(@NonNull Context context) {
        this(context, null);
    }

    public RecyclerViewBannerBase(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewBannerBase(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPlaying(false);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setPlaying(true);
                break;
        }
        try {
            return super.dispatchTouchEvent(ev);
        }
        catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return super.onTouchEvent(event);
        }
        catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPlaying(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setPlaying(false);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            setPlaying(true);
        } else {
            setPlaying(false);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // setter && getter

    /**
     * 设置轮播间隔时间
     *
     * @param millisecond 时间毫秒
     */
    public void setIndicatorInterval(int millisecond) {
        this.autoPlayDuration = millisecond;
    }

    /**
     * 设置是否自动播放（上锁）
     *
     * @param playing 开始播放
     */
    protected synchronized void setPlaying(boolean playing) {
        if (isAutoPlaying && hasInit) {
            if (!isPlaying && playing && adapter != null && adapter.getItemCount() > 2) {
                mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration);
                isPlaying = true;
            } else if (isPlaying && !playing) {
                mHandler.removeMessages(WHAT_AUTO_PLAY);
                isPlaying = false;
            }
        }
    }

    /**
     * 设置是否禁止滚动播放
     */
    public void setAutoPlaying(boolean isAutoPlaying) {
        this.isAutoPlaying = isAutoPlaying;
        setPlaying(this.isAutoPlaying);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setShowIndicator(boolean showIndicator) {
        this.showIndicator = showIndicator;
        indicatorContainer.setVisibility(showIndicator ? VISIBLE : GONE);
    }

    /**
     * 设置轮播数据集
     */
    public void initBannerImageView(@NonNull List<String> newList, OnBannerItemClickListener onBannerItemClickListener) {
        //解决recyclerView嵌套问题
        if (compareListDifferent(newList, tempUrlList)) {
            hasInit = false;
            setVisibility(VISIBLE);
            setPlaying(false);
            adapter = getAdapter(getContext(), newList, onBannerItemClickListener);
            mRecyclerView.setAdapter(adapter);
            tempUrlList = newList;
            bannerSize = tempUrlList.size();
            if (bannerSize > 1) {
                indicatorContainer.setVisibility(VISIBLE);
                currentIndex = bannerSize * 10000;
                mRecyclerView.scrollToPosition(currentIndex);
                indicatorAdapter.notifyDataSetChanged();
                setPlaying(true);
            } else {
                indicatorContainer.setVisibility(GONE);
                currentIndex = 0;
            }
            hasInit = true;
        }
        if (!showIndicator) {
            indicatorContainer.setVisibility(GONE);
        }
    }

    /**
     * 设置轮播数据集
     */
    public void initBannerImageView(@NonNull List<String> newList) {
        initBannerImageView(newList, null);
    }
    /////////////////////////////////////////////////////////////////////////// setter && getter end

    ///////////////////////////////////////////////////////////////////////////
    // self methods

    /**
     * init view
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {
        this.initAttr(context, attrs);

        if (null == mSelectedDrawable) {
            // 绘制默认选中
            GradientDrawable selectedGradientDrawable = new GradientDrawable();
            selectedGradientDrawable.setShape(GradientDrawable.OVAL);
            selectedGradientDrawable.setColor(getColor(R.color.colorAccent));
            selectedGradientDrawable.setSize(
                    PaintUtils.dp2px(getContext(), 5), PaintUtils.dp2px(getContext(), 5));
            selectedGradientDrawable.setCornerRadius(PaintUtils.dp2px(getContext(), 5) / 2);
            mSelectedDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
        }
        if (null == mUnselectedDrawable) {
            //绘制默认未选中状态图形
            GradientDrawable unSelectedGradientDrawable = new GradientDrawable();
            unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
            unSelectedGradientDrawable.setColor(getColor(R.color.colorPrimaryDark));
            unSelectedGradientDrawable.setSize(
                    PaintUtils.dp2px(getContext(), 5), PaintUtils.dp2px(getContext(), 5));
            unSelectedGradientDrawable.setCornerRadius(PaintUtils.dp2px(getContext(), 5) / 2);
            mUnselectedDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                onBannerScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                onBannerScrollStateChanged(recyclerView, newState);

            }
        });
    }

    /**
     * init attribute
     * @param context
     * @param attrs
     */
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewBannerBase);
        showIndicator = a.getBoolean(R.styleable.RecyclerViewBannerBase_showIndicator, true);
        autoPlayDuration = a.getInt(R.styleable.RecyclerViewBannerBase_interval, 4000);
        isAutoPlaying = a.getBoolean(R.styleable.RecyclerViewBannerBase_autoPlaying, true);
        mSelectedDrawable = a.getDrawable(R.styleable.RecyclerViewBannerBase_indicatorSelectedSrc);
        mUnselectedDrawable = a.getDrawable(R.styleable.RecyclerViewBannerBase_indicatorUnselectedSrc);

        indicatorMargin = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorSpace, PaintUtils.dp2px(getContext(),4));
        int marginLeft = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginLeft, PaintUtils.dp2px(getContext(),16));
        int marginRight = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginRight, PaintUtils.dp2px(getContext(),0));
        int marginBottom = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginBottom, PaintUtils.dp2px(getContext(),11));
        int g = a.getInt(R.styleable.RecyclerViewBannerBase_indicatorGravity, 0);
        int gravity;
        if (g == 0) {
            gravity = GravityCompat.START;
        } else if (g == 2) {
            gravity = GravityCompat.END;
        } else {
            gravity = Gravity.CENTER;
        }
        int o = a.getInt(R.styleable.RecyclerViewBannerBase_orientation, 0);
        int orientation = 0;
        if (o == 0) {
            orientation = LinearLayoutManager.HORIZONTAL;
        } else if (o == 1) {
            orientation = LinearLayoutManager.VERTICAL;
        }
        a.recycle();

        //recyclerView部分
        mRecyclerView = new RecyclerView(context);
        new PagerSnapHelper().attachToRecyclerView(mRecyclerView);
        mLayoutManager = getLayoutManager(context, orientation);
        mRecyclerView.setLayoutManager(mLayoutManager);

        LayoutParams vpLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mRecyclerView, vpLayoutParams);
        //指示器部分
        indicatorContainer = new RecyclerView(context);

        LinearLayoutManager indicatorLayoutManager = new LinearLayoutManager(context, orientation, false);
        indicatorContainer.setLayoutManager(indicatorLayoutManager);
        indicatorAdapter = new IndicatorAdapter();
        indicatorContainer.setAdapter(indicatorAdapter);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | gravity;
        params.setMargins(marginLeft, 0, marginRight, marginBottom);
        addView(indicatorContainer, params);
        if (!showIndicator) {
            indicatorContainer.setVisibility(GONE);
        }
    }

    /**
     * on banner scroller
     * @param recyclerView
     * @param dx
     * @param dy
     */
    protected void onBannerScrolled(RecyclerView recyclerView, int dx, int dy) {}


    /**
     * on banner scroll state changed
     * @param recyclerView
     * @param newState
     */
    protected  void onBannerScrollStateChanged(RecyclerView recyclerView, int newState){}

    /**
     * get layout manager
     * @param context
     * @param orientation
     * @return
     */
    protected abstract L getLayoutManager(Context context, int orientation);

    /**
     * get adapter
     * @param context
     * @param list
     * @param onBannerItemClickListener
     * @return
     */
    protected abstract A getAdapter(Context context, List<String> list, OnBannerItemClickListener onBannerItemClickListener);


    /**
     * 改变导航的指示点
     */
    protected synchronized void refreshIndicator() {
        if (showIndicator && bannerSize > 1) {
            indicatorAdapter.setPosition(currentIndex % bannerSize);
            indicatorAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取颜色
     */
    protected int getColor(@ColorRes int color) {
        return ContextCompat.getColor(getContext(), color);
    }

    /**
     * compare list different
     * @param newTabList
     * @param oldTabList
     * @return
     */
    protected boolean compareListDifferent(List<String> newTabList, List<String> oldTabList) {
        if (oldTabList == null || oldTabList.isEmpty())
            return true;
        if (newTabList.size() != oldTabList.size())
            return true;
        for (int i = 0; i < newTabList.size(); i++) {
            if (TextUtils.isEmpty(newTabList.get(i)))
                return true;
            if (!newTabList.get(i).equals(oldTabList.get(i))) {
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////// self methods end



    ///////////////////////////////////////////////////////////////////////////
    // self class

    /**
     * interface for banner item click
     */
    public interface OnBannerItemClickListener {
        /**
         * on item click
         * @param position
         */
        void onItemClick(int position);
    }


    protected class IndicatorAdapter extends RecyclerView.Adapter {

        int currentPosition = 0;

        public void setPosition(int currentPosition) {
            this.currentPosition = currentPosition;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            ImageView bannerPoint = new ImageView(getContext());
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(indicatorMargin, indicatorMargin, indicatorMargin, indicatorMargin);
            bannerPoint.setLayoutParams(lp);
            return new RecyclerView.ViewHolder(bannerPoint) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView bannerPoint = (ImageView) holder.itemView;
            bannerPoint.setImageDrawable(currentPosition == position ? mSelectedDrawable : mUnselectedDrawable);

        }

        @Override
        public int getItemCount() {
            return bannerSize;
        }
    }

    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_AUTO_PLAY) {
                mRecyclerView.smoothScrollToPosition(++currentIndex);
                refreshIndicator();
                mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration);

            }
            return false;
        }
    });

    /////////////////////////////////////////////////////////////////////////// self class end
}
