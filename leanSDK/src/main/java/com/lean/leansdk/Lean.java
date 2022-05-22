package com.lean.leansdk;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

    private final String token;
    private final Context context;
    private String baseUrl;

    public Lean(Context context, String token, HashMap<String, String> options) {
        this.token = token;
        this.context = context;
        String env = options.get("environment");
        setBaseUrl(env);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View getCard() {

        WebView view = null;
        try {
            String cookieString = "auth=" + token + " ;path=/";
            CookieManager.getInstance().setCookie(baseUrl, cookieString);
            view = new WebView(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            view.setWebViewClient(new WebViewClient());
            WebSettings webSettings = view.getSettings();
            webSettings.setDomStorageEnabled(true);
            webSettings.setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }        view.setWebViewClient(new WebViewClient());
            view.addJavascriptInterface(new WebAppInterface(context, baseUrl), "Android");

            try {
                if (!isConfirmed(token)) {
                    view.loadUrl(baseUrl + "initial/signup");
                } else {
                    view.loadUrl(baseUrl + "initial/card");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
        return view;
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
