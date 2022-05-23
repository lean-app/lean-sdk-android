package com.lean.leansdkexample;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;

import com.lean.leansdk.Lean;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImN1c3RvbWVyX3RialpvcjBOakwweVdINjI1X2NLViIsImVtYWlsIjoidGltK2N1c3RvbWVyNzMwQHdpdGhsZWFuLmNvbSIsImludGVyZmFjZUlkIjoiaW50ZXJmYWNlX09MVGFmTlY0QVM4NHNQNm1uQzFQMSIsInN0YXR1cyI6IkNPTkZJUk1FRCIsImlhdCI6MTY1MjgzNTcxNCwiZXhwIjoxNjUzNDQwNTE0fQ.EmMWNgI8IkvQIIa1VxGJeCUnLlx9HfukF6Kt8huQty4";
        HashMap<String, String> options = new HashMap<>();
        options.put("environment", "STAGING");
        HashMap<String, Object> theme = new HashMap<>();
        HashMap<String, Object> color = new HashMap<>();
        color.put("primary", "yellow");
        color.put("textPrimary", "yellow");
        color.put("textSecondary", "yellow");
        theme.put("color", color);
        Lean lean =  new Lean(this, token, options, theme);
        this.addContentView(lean.getCard(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }
}