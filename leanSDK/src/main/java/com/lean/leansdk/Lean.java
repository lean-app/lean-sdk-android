package com.lean.leansdk;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.HashMap;

public class Lean extends AppCompatActivity {

    private String token;
    private String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void viewCardInfo(
            Context context,
            String externalToken,
            HashMap<String, String> options) {

        String env = options.get("environment");
        setBaseUrl(env);

        try {
            token = externalToken;
            String cookieString = "auth=" + token + " ;path=/";
            CookieManager.getInstance().setCookie(baseUrl, cookieString, (res) -> {
                Intent intent = new Intent(context, Card.class);
                intent.putExtra("baseUrl", baseUrl);
                try {
                    if (!isConfirmed(externalToken)) {
                        intent.putExtra("url", "initial/signup");
                    } else {
                        intent.putExtra("url", "initial/card");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                context.startActivity(intent);
            });
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
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
