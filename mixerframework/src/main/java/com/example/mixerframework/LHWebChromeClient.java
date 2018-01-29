package com.example.mixerframework;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import org.json.JSONException;

/**
 * Created by tj on 2017/10/25.
 */

public class LHWebChromeClient extends WebChromeClient {

    private static final String TAG = "LHWebChromeClient";
    protected final SystemWebViewEngine parentEngine;

    public LHWebChromeClient(SystemWebViewEngine parentEngine) {
        this.parentEngine = parentEngine;
    }

    @Override
    public boolean onJsPrompt(WebView view,
                              String url,
                              String message,
                              String defaultValue,
                              JsPromptResult result) {
        try {
            parentEngine.handleJsPrompt(message);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        result.confirm();
        return true;
    }


}
