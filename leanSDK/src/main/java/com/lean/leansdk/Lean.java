package com.lean.leansdk;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.HashMap;

public class Lean extends AppCompatActivity {

    private String token;
    private String baseUrl;
    private Bundle bundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.CardFragment, CardFragment.class, bundle)
                    .commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View viewCardInfo(
            Context context,
            String externalToken,
            HashMap<String, String> options) {

        String env = options.get("environment");
        setBaseUrl(env);
        View rootView = findViewById(android.R.id.content).getRootView();

        try {
            token = externalToken;
            String cookieString = "auth=" + token + " ;path=/";
            CookieManager.getInstance().setCookie(baseUrl, cookieString, (res) -> {

                WebView view = (WebView) rootView.findViewById(R.id.webView);
                view.setWebViewClient(new WebViewClient());
                WebSettings webSettings = view.getSettings();
                webSettings.setDomStorageEnabled(true);
                webSettings.setJavaScriptEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    WebView.setWebContentsDebuggingEnabled(true);
                }        view.setWebViewClient(new WebViewClient());
                view.addJavascriptInterface(new WebAppInterface(context, baseUrl), "Android");

                try {
                    if (!isConfirmed(externalToken)) {
                        view.loadUrl(baseUrl + "initial/signup");
                    } else {
                        view.loadUrl(baseUrl + "initial/card");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
        return rootView;
    }

    public void viewOnboarding(Context context) {
        Intent intent = new Intent(context, FullScreen.class);
        intent.putExtra("baseUrl", baseUrl);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isConfirmed(String jwtToken) throws JSONException {
        String[] parts = jwtToken.split("\\.");
        JSONObject payload = new JSONObject(decode(parts[1]));
        String status = payload.getString("status");
        return status.equals("CONFIRMED");
    }

    private void setBaseUrl(String env) {
        if (env == null) {
            return;
        }
        switch (env) {
            case "STAGING":
                baseUrl = "https://sdk-web.staging.withlean.com/";
                break;
            case "SANDBOX":
                baseUrl = "https://sdk-web.sandbox.withlean.com/";
                break;
            case "PRODUCTION":
                baseUrl = "https://sdk-web.withlean.com/";
                break;
        }
    }
}
