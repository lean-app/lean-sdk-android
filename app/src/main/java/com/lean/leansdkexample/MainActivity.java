package com.lean.leansdkexample;

import androidx.appcompat.app.AppCompatActivity;

import com.lean.leansdk.LeanMain;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView)findViewById(R.id.textView1);
        textView.setText(LeanMain.getMessage());

    }
}