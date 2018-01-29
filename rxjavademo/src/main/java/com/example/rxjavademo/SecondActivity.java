package com.example.rxjavademo;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.example.rxjavademo.adapter.ImageAdapter;
import com.example.rxjavademo.base.BaseActivity;
import com.example.rxjavademo.base.BaseApplication;
import com.example.rxjavademo.contract.ImageConstract;
import com.example.rxjavademo.model.ImageModel;
import com.example.rxjavademo.presenter.ImagePresenter;

import butterknife.BindView;

public class SecondActivity extends BaseActivity<ImagePresenter> implements ImageConstract.View {

    private static final String TAG = "SecondActivity";
    private ImageAdapter imageAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ImagePresenter(this);
    }

    @Override
    public void init() {
        mPresenter.getImages("风景", 0 ,15);
    }

    @Override
    public void result(ImageModel s) {
        ImageModel imageModel = (ImageModel) s;
        Log.d(TAG, "image response result —— " +s.toString());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager
                .VERTICAL));
        imageAdapter = new ImageAdapter(BaseApplication.getContext(),imageModel.getItemModels());
        recyclerView.setAdapter(imageAdapter);
    }
}
