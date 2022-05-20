package com.lean.leansdk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class FullScreen extends AppCompatActivity {

    private WebView webView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        webView = findViewById(R.id.webView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        webSettings.setJavaScriptEnabled(true);
        Intent myIntent = getIntent(); // gets the previously created intent
        String url = myIntent.getStringExtra("url");
        webView.loadUrl("http://192.168.4.111:3000/" + url);
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