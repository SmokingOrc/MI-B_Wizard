package com.example.mi_b_wizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button click = null;
    Button buttonRegeln;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        click = findViewById(R.id.btnClick);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, JoinGameActivity.class);
                startActivity(i);
            }
        });

        buttonRegeln = findViewById(R.id.buttonRegeln);
    }

    public void buttonRegeln(View view) {
        Intent i2 = new Intent(MainActivity.this, RulesActivity.class);
        startActivity(i2);

    }
}
