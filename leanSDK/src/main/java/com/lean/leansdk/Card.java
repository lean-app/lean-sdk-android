package com.lean.leansdk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Card extends AppCompatActivity {

    private WebView webView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Intent myIntent = getIntent(); // gets the previously created intent
        String baseUrl = myIntent.getStringExtra("baseUrl");
        String url = myIntent.getStringExtra("url");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new WebAppInterface(this, baseUrl), "Android");
        webView.loadUrl(baseUrl + url);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }
}