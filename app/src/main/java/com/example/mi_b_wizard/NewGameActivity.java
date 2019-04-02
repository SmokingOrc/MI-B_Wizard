package com.example.mi_b_wizard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;


public class NewGameActivity extends AppCompatActivity {
    EditText roomName;
    String username;
    Socket mSocket;
    Button start;
    int spieler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        roomName = findViewById(R.id.roomname);
        start = findViewById(R.id.start);
        spieler = 0;
        SocketIO app = (SocketIO) getApplication();
        mSocket = app.getmSocket();

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rdbGp1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton zweisp = (RadioButton) findViewById(R.id.zweispieler);
                RadioButton dreisp = (RadioButton) findViewById(R.id.dreispieler);
                RadioButton viersp = (RadioButton) findViewById(R.id.vierspieler);
                if (zweisp.isChecked()){
                    DisplayToast("2 Spieler ausgewählt.");
                    spieler = 2;
                }
                else if (dreisp.isChecked()){
                    DisplayToast("3 Spieler ausgewählt.");
                    spieler = 3;
                }
                else {
                    DisplayToast("4 Spieler ausgewählt.");
                    spieler = 4;
                }
            }
        });
        mSocket.on("alert", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DisplayToast("Room name already taken, pick another one..");
                    }
                });
            }
        });
        mSocket.on("connecttoroom", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                username = (String) args[1];
                System.out.println(username);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DisplayToast("Room created");
                    }
                });
            }
        });
        mSocket.connect();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mSocket.connected()){
                if(spieler != 0 && roomName.length() != 0 ){
                mSocket.emit("createroom", roomName.getText().toString(), spieler);
            }else{DisplayToast("Fill out the fields..");}}
                else{DisplayToast("You are not connected to server..");}}
        });
/*
        //Wechsel zu Activity, wo weitere Spieler dazukommen

        ButtonStart = findViewById(R.id.start);//Wechsel zu ...
        ButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewGameActivity.this, MUSTER.class);
                startActivity(i);
            }
        });*/
    }


    private void DisplayToast (String msg){
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }
}


