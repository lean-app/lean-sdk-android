package com.lean.leansdk;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

public class CardFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String url = requireArguments().getString("url");
        String baseUrl = requireArguments().getString("baseUrl");

        View rootView = inflater.inflate(R.layout.fragment_card, container, true);

        WebView view = (WebView) rootView.findViewById(R.id.webView);

        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        view.setWebViewClient(new WebViewClient());
        WebSettings webSettings = view.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }        view.setWebViewClient(new WebViewClient());
        view.addJavascriptInterface(new WebAppInterface(this.getContext(), baseUrl), "Android");
        view.loadUrl(baseUrl + url);

        return rootView ;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}