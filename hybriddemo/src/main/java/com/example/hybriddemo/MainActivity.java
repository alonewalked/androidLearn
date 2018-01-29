package com.example.hybriddemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mixerframework.LHConstant;

public class MainActivity extends AppCompatActivity {

    private String url = "http://baidu.com";

    private Button btnOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOpen = (Button) findViewById(R.id.btn_open);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.setClass(MainActivity.this, WebViewActivity.class);
                it.putExtra(LHConstant.KEY_INTENT_EXTRA_URL, url);
                startActivity(it);
            }
        });
    }
}
