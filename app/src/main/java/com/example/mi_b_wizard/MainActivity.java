package com.example.mi_b_wizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mi_b_wizard.Data.Player;


public class MainActivity extends AppCompatActivity {
    private static Player player;
    Button ButtonJoin = null;
    Button ButtonRegeln;
    private Button btnCheat = null; //only for testing shaking
    Button ButtonSpeech;
    Button ShowCardsOnHand;
    EditText Userid;
    String user;


    public static Player getPlayer() {
        return player;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Userid = findViewById(R.id.Userid);
        user = Userid.getText().toString();
        ButtonJoin = findViewById(R.id.btnJoin); //Wechsel zu Join Game
        ButtonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.isEmpty()){
                user = Userid.getText().toString();}
                if(!user.isEmpty()) {
                    player = new Player(user);
                    Intent i = new Intent(MainActivity.this, JoinGameActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(getApplicationContext(), "You need a name to join game..", Toast.LENGTH_SHORT).show();
                }
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


        btnCheat = findViewById(R.id.btnCheat);
        btnCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCheat = new Intent(MainActivity.this, GameActivity.class);
                startActivity(iCheat);
            }

    });
    }

}
