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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class LeanView extends WebView {

    private String token;
    private final Context context;
    private HashMap<String, Object> options;
    private HashMap<String, Object> theme;
    private String baseUrl;
    private final EventFuncInterface onEvent;

    public LeanView(Context context, String token, HashMap<String, Object> options, HashMap<String, Object> theme, EventFuncInterface onEvent) {
        super(context);
        this.token = token;
        this.context = context;
        this.onEvent = onEvent;
        this.options = options;
        this.theme = theme;
        setBaseUrl();
        load();
    }

    public void load() {
      if (token == null || baseUrl == null) return;
      try {
        String cookieString = "auth=" + token + " ;path=/";
        CookieManager.getInstance().setCookie(baseUrl, cookieString);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT));
        String styleScript = getStyleScript();
        this.setWebViewClient(new WebViewClient() {
          public void onPageFinished(WebView view, String url) {
            view.evaluateJavascript(getStyleScript(), null);
          }
        });
        WebSettings webSettings = this.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
          WebView.setWebContentsDebuggingEnabled(true);
        }
        this.addJavascriptInterface(new WebAppInterface(context, baseUrl, onEvent, styleScript), "Android");
        try {
          if (!isConfirmed(token)) {
            this.loadUrl(baseUrl + "initial/signup");
          } else {
            this.loadUrl(baseUrl + "initial/card");
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      } catch (Exception e) {
        System.out.println(e.toString());
      }
    }

    public void refreshStyles() {
      String styleScript = getStyleScript();
      this.evaluateJavascript(getStyleScript(), null);
      this.addJavascriptInterface(new WebAppInterface(context, baseUrl, onEvent, styleScript), "Android");
    }

    public void refresh() {
      setBaseUrl();
      load();
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

    private void setBaseUrl() {
        if(options == null) return;
        String env = Objects.requireNonNull(options.get("environment")).toString();
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

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private String getStyleScript() {

    ArrayList<String> commands = new ArrayList<>();

    for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>) theme.entrySet()) {
      HashMap<String, String> obj = ((HashMap<String, String>) entry.getValue());
      if (entry.getKey().equals("color")) {
        if (!Objects.equals((String) obj.get("primary"), null)) {
          commands.add("rootStyle.setProperty('--lean-color-primary', '"  + obj.get("primary") + "')");
        }
        if (!Objects.equals((String) obj.get("secondary"), null)) {
          commands.add("rootStyle.setProperty('--lean-color-secondary', '" + obj.get("secondary") + "')");
        }
        if (!Objects.equals((String) obj.get("error"), null)) {
          commands.add("rootStyle.setProperty('--lean-color-error', '" + obj.get("error") + "')");
        }
        if (!Objects.equals((String) obj.get("textPrimary"), null)) {
          commands.add("rootStyle.setProperty('--lean-color-text-primary', '" + obj.get("textPrimary") + "')");
        }
        if (!Objects.equals((String) obj.get("textSecondary"), null)) {
          commands.add("rootStyle.setProperty('--lean-color-text-secondary', '" + obj.get("textSecondary") + "')");
        }
        if (!Objects.equals((String) obj.get("textInteractive"), null)) {
          commands.add("rootStyle.setProperty('--lean-color-text-interactive', '" + obj.get("textInteractive") + "')");
        }
      } else if (entry.getKey().equals("fontFamily")  && entry.getValue() != null) {
        commands.add("rootStyle.setProperty('--lean-font-family', '" + entry.getValue() + "')");
      } else if (entry.getKey().equals("fontWeight")) {
        if (!Objects.equals((String) obj.get("light"), null)) {
          commands.add("rootStyle.setProperty('--lean-font-weight-light', '" + obj.get("light") + "')");
        }
        if (!Objects.equals((String) obj.get("regular"), null)) {
          commands.add("rootStyle.setProperty('--lean-font-weight-regular', '" + obj.get("regular") + "')");
        }
        if (!Objects.equals((String) obj.get("medium"), null)) {
          commands.add("rootStyle.setProperty('--lean-font-weight-medium', '" + obj.get("medium") + "')");
        }
        if (!Objects.equals((String) obj.get("semibold"), null)) {
          commands.add("rootStyle.setProperty('--lean-font-weight-semibold', '" + obj.get("semibold") + "')");
        }
        if (!Objects.equals((String) obj.get("bold"), null)) {
          commands.add("rootStyle.setProperty('--lean-font-weight-bold', '" + obj.get("bold") + "')");
        }
      }
    }

    if (commands.size() <= 0) return "";

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < commands.size(); i++) {

      sb.append(commands.get(i));

      // if not the last item
      if (i != commands.size() - 1) {
        sb.append("; ");
      }

    }
    System.out.println(sb.toString());
    return  "const rootStyle = document.documentElement.style;" + sb.toString();
  }

    public void setToken(String token) {
      this.token = token;
    }

    public void setOptions(HashMap<String, Object> options) {
      this.options = options;
    }

    public void setTheme(HashMap<String, Object> theme) {
      this.theme = theme;
    }
}
