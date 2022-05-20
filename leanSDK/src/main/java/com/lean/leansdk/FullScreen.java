package com.lean.leansdk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class FullScreen extends AppCompatActivity {

    private WebView webView;


    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent myIntent = getIntent(); // gets the previously created intent
        String baseUrl = myIntent.getStringExtra("baseUrl");
        String url = myIntent.getStringExtra("url");
        setContentView(R.layout.activity_fullscreen);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        webView = findViewById(R.id.webView);
        webView.addJavascriptInterface(new WebAppInterface(this, baseUrl), "Android");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        Log.d("INFO", baseUrl + url);
        webView.loadUrl(baseUrl + url);
        webView.clearView();
    }



    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}