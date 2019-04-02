package com.example.mi_b_wizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

public class MainActivity extends AppCompatActivity {
    Button ButtonJoin = null;
    Button ButtonRegeln;
    Button ButtonCreate;
    EditText Userid;
    TextView online;
    String user;
    Socket mSocket;
    String onLineUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButtonJoin = findViewById(R.id.btnJoin); //Wechsel zu Join Game
        online = findViewById(R.id.online);

        SocketIO app = (SocketIO) getApplication();
        mSocket = app.getmSocket();

        mSocket.on("connected" , onConnect);
        mSocket.on("usersonline", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                onLineUsers = ("online users : " + Integer.toString((int) args[0]));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(onLineUsers);
                        online.setText(onLineUsers);
                    }
                });
            }
        });
        ButtonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, JoinGameActivity.class);
                startActivity(i);
            }
        });
        mSocket.connect();
        ButtonRegeln = findViewById(R.id.btnRegeln);//Wechsel zu Regeln
        ButtonRegeln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MainActivity.this, RulesActivity.class);
                startActivity(i2);
            }
        });

        ButtonCreate = findViewById(R.id.btnCreate);//Wechsel zu Create Game
        ButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(MainActivity.this, NewGameActivity.class);
                startActivity(i3);
            }
        });

        Userid = findViewById(R.id.Userid);
        user = Userid.getText().toString();// Username
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("connected");
                    Toast.makeText(MainActivity.this, "Connected to Wizard!",Toast.LENGTH_LONG).show();
                }
            });
        }
    };


  /*  public void buttonRegeln(View view) {
        Intent i2 = new Intent(MainActivity.this, RulesActivity.class);
        startActivity(i2);

    }*/
}
