package com.lean.leansdkexample;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.lean.leansdk.Lean;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Lean lean =  new Lean();
        HashMap<String, String> options = new HashMap<>();
        options.put("environment", "STAGING");
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImN1c3RvbWVyX3RialpvcjBOakwweVdINjI1X2NLViIsImVtYWlsIjoidGltK2N1c3RvbWVyNzMwQHdpdGhsZWFuLmNvbSIsImludGVyZmFjZUlkIjoiaW50ZXJmYWNlX09MVGFmTlY0QVM4NHNQNm1uQzFQMSIsInN0YXR1cyI6IkNPTkZJUk1FRCIsImlhdCI6MTY1MjgzNTcxNCwiZXhwIjoxNjUzNDQwNTE0fQ.EmMWNgI8IkvQIIa1VxGJeCUnLlx9HfukF6Kt8huQty4";
        lean.viewCardInfo(this, token, options);
    }
}