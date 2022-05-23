package com.lean.leansdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class WebAppInterface {
    Context mContext;
    String baseUrl;
    String styleScript;
    private final EventFuncInterface onEvent;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c, String receivedBaseUrl, EventFuncInterface onEvent, String styleScript) {
        mContext = c;
        baseUrl = receivedBaseUrl;
        this.onEvent = onEvent;
        this.styleScript = styleScript;
    }

    private void handleEvent(String message) {
      if (onEvent != null) {
        onEvent.operation(message);
      }
    }
    /** Show a toast from the web page */
    @JavascriptInterface
    public void navigate(String message) {
      handleEvent(message);
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                Log.d("INFO", message);
                Intent intent = new Intent(mContext, FullScreen.class);
                intent.putExtra("baseUrl", baseUrl);
                intent.putExtra("styleScript", styleScript);
                if (message.equals("navigate-signup")) {
                    intent.putExtra("url", "onboarding");
                } else if (message.equals("navigate-transactions")) {
                    intent.putExtra("url", "transactions");
                } else if (message.equals("navigate-account")) {
                    intent.putExtra("url", "account");
                } else if (message.equals("navigate-card")) {
                    intent.putExtra("url", "card");
                } else {
                   return;
                }
                mContext.startActivity(intent);
            });
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }

    }

    @JavascriptInterface
    public void dismiss() {
        handleEvent("dismiss");
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    ((Activity) mContext).finish();
                }
            });
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());

        }

    }

    @JavascriptInterface
    public void completeOnboarding() {
        handleEvent("complete-onboarding");
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    ((Activity) mContext).finish();

                }
            });

        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }

    }
}
