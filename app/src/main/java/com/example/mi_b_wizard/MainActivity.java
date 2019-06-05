package com.example.mi_b_wizard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mi_b_wizard.Data.Player;


public class MainActivity extends AppCompatActivity {
    private static Player player = null;
    Button buttonJoin = null;
    Button buttonRegeln;
    private Button btnCheat = null; //only for testing shaking
    EditText userID;
    String user;


    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(String newPlayer) {
        if (player == null) {
            player = new Player(newPlayer);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userID = findViewById(R.id.Userid);
        user = userID.getText().toString();
        buttonJoin = findViewById(R.id.btnJoin); //Wechsel zu Join Game
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.isEmpty()){
                user = userID.getText().toString();}
                if(!user.isEmpty()) {
                    setPlayer(user);
                    Intent i = new Intent(MainActivity.this, JoinGameActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(getApplicationContext(), "You need a name to join game..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonRegeln = findViewById(R.id.btnRegeln);//Wechsel zu Regeln
        buttonRegeln.setOnClickListener(new View.OnClickListener() {
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
                /*Hand testHand = new Hand();
                testHand.addCardToHand(new Card(3,3));
                testHand.addCardToHand(new Card(4,3));
                testHand.addCardToHand(new Card(5,3));
                testHand.addCardToHand(new Card(6,3));
                testHand.addCardToHand(new Card(7,1));
                testHand.addCardToHand(new Card(8,1));
                testHand.addCardToHand(new Card(2,2));

                CheatingDialog cD = new CheatingDialog(MainActivity.this, testHand);
                cD.show();*/
            }

    });
    }

}
