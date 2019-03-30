package com.example.mi_b_wizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button ButtonJoin = null;
    Button ButtonRegeln;
    EditText Userid;
    String user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButtonJoin = findViewById(R.id.btnJoin); //Wechsel zu Join Game
        ButtonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, JoinGameActivity.class);
                startActivity(i);
            }
        });

        ButtonRegeln = findViewById(R.id.btnRegeln);//Wechsel zu Regeln
        ButtonRegeln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MainActivity.this, RulesActivity.class);
                startActivity(i2);
            }
        });
        Userid = findViewById(R.id.Userid);
        user = Userid.getText().toString();// Username
    }

  /*  public void buttonRegeln(View view) {
        Intent i2 = new Intent(MainActivity.this, RulesActivity.class);
        startActivity(i2);

    }*/
}
