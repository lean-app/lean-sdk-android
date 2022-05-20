package com.lean.leansdk;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class Lean extends AppCompatActivity {

    private String token;

    private String baseUrl = "http://192.168.4.111:3000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void viewCardInfo(Context context, String externalToken) {
        try {
            token = externalToken;
            String cookieString = "auth=" + token + " ;path=/";
            CookieManager.getInstance().setCookie(baseUrl, cookieString, (res) -> {
                Intent intent = new Intent(context, Card.class);
                intent.putExtra("url", "initial/signup");
                context.startActivity(intent);
            });
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }

    }

    public void viewOnboarding(Context context) {
        Intent intent = new Intent(context, FullScreen.class);
        context.startActivity(intent);
    }

}
