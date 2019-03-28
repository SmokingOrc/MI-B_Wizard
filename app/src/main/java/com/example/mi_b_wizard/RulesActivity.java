package com.example.mi_b_wizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class RulesActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        WebView webView = findViewById(R.id.webView1);
        webView.loadUrl("file:///android_asset/regeln.html");
    }

    public void buttonZurück(View view) {
        Intent i = new Intent(RulesActivity.this, MainActivity.class);
        startActivity(i);

    }
}
