package com.example.rxjavademo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.rxjavademo.base.BaseActivity;
import com.example.rxjavademo.contract.MainContract;
import com.example.rxjavademo.presenter.MainPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View{

    private static final String TAG = "MainActivity";

    @BindView(R.id.main_text)
    TextView text;
    @BindView(R.id.main_click)
    Button click;
    @BindView(R.id.main_test_rxjava)
    Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MainPresenter(this);
    }

    @Override
    public void init() {
        mPresenter.getLocation("北京市");
    }


    @Override
    public void result(String s) {
        text.setText(s);
    }

    @OnClick(R.id.main_click)
    public void onViewClicked() {
        startActivity(new Intent(this, SecondActivity.class));
    }

    @OnClick(R.id.main_test_rxjava)
    public void onTestClicked() {
        startActivity(new Intent(this, TestActivity.class));
    }
}
