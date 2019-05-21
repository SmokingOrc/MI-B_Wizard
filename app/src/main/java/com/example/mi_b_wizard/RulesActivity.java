package com.example.mi_b_wizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class RulesActivity extends AppCompatActivity {

    Button ButtonJoinGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        WebView webView = findViewById(R.id.webView1);
        webView.loadUrl("file:///android_asset/regeln.html");


        ButtonJoinGame = findViewById(R.id.btn_join);
        ButtonJoinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RulesActivity.this, JoinGameActivity.class);
                startActivity(i);
            }
        });
    }
}
