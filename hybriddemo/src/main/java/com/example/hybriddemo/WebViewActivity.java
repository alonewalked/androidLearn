package com.example.hybriddemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mixerframework.INVOKE;
import com.example.mixerframework.LHConstant;
import com.example.mixerframework.LHPreferences;
import com.example.mixerframework.WebViewFragment;
import com.example.mixerframework.base.LoadingErrorView;
import com.example.mixerframework.base.LoadingView;
import com.example.mixerframework.base.WebViewBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class WebViewActivity extends Activity {
    private WebViewFragment webviewFragment;
    private TextView tvTitle;
    private String mUrl = "";
    private TextView rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mUrl = getIntent().getStringExtra(LHConstant.KEY_INTENT_EXTRA_URL);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        rightButton = (TextView) findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String jsStr = "javascript:window.changeBackground('red')";
                webviewFragment.loadingJs(jsStr);


            }
        });
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webviewFragment = (WebViewFragment) getFragmentManager().findFragmentById(R.id.fg_webview);

        // 设置参数
        LHPreferences preferences = new LHPreferences();
        preferences.setLoadingView(new LoadingView(this));
        preferences.setLoadingErrorView(new LoadingErrorView(this));
        preferences.set("AppendUserAgent", "hitehybird");
        if (true) {
            preferences.set(LHConstant.IS_DEBUG_MODE, true);
        }


        webviewFragment.setArguments(preferences, new FunctionSet1());

        // 启动加载
        webviewFragment.loadUrl(mUrl);
    }

    public class FunctionSet1 {
        @INVOKE("popToast")
        public void popToast(JSONObject paras) {
            String para1 =  paras.optString("para1");
            Toast.makeText(WebViewActivity.this, para1, Toast.LENGTH_SHORT).show();
        }

        /**
         * 打开新的webview
         */
        @INVOKE("openWebView")
        public void openWebView(JSONObject paras) throws JSONException {
            String newUrl = paras.getString("url");

            Intent intent = new Intent(WebViewActivity.this, WebViewActivity.class);
            intent.putExtra(LHConstant.KEY_INTENT_EXTRA_URL, newUrl);

            startActivity(intent);

            if (paras.getBoolean("closeCurrent")) {
                finish();
            }
        }

        @INVOKE("refresh")
        public void refreshWebView(JSONObject paras) {
            webviewFragment.loadUrl(mUrl);
        }

        @INVOKE("closeCurrentActivity")
        public void closeCurrentActivity(JSONObject paras) {
            finish();
        }

    }
}
