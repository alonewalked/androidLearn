package com.example.mixerframework.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mixerframework.LHConstant;
import com.example.mixerframework.R;
import com.example.mixerframework.WebViewFragment;

public class WebViewBaseActivity extends AppCompatActivity {

    private WebViewFragment webviewFragment;
    private String mUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_base);

        mUrl = getIntent().getStringExtra(LHConstant.KEY_INTENT_EXTRA_URL);

        webviewFragment = (WebViewFragment) getFragmentManager().findFragmentById(R.id.fg_webview);

    }
}
