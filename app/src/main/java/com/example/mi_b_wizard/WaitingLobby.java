package com.example.mi_b_wizard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mi_b_wizard.Data.Player;
import com.example.mi_b_wizard.Network.MessageHandler;
import com.example.mi_b_wizard.Network.Server;

public class WaitingLobby extends AppCompatActivity {
    boolean owner = JoinGameActivity.owner;
    Button send, start;
    Player player = MainActivity.getPlayer();
    TextView clientOrOwner, messages;
    EditText message;
    private static WaitingLobby waitingLobby ;
    byte zero = 0;

    Server mServer;
    String user;
    String usermessage;
    MessageHandler messageHandler;


    public static WaitingLobby getWaitingLobby() {
        return waitingLobby;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        System.out.println("setting messageHandler"+messageHandler);
        System.out.println(messageHandler+" "+this.messageHandler);
    }

    public MessageHandler getMessageHandler() {
        System.out.println(messageHandler+" "+this.messageHandler);
        return messageHandler;

    }
    public void setmServer(Server mServer) {
        this.mServer = mServer;
    }

    public void addMsg(String msg){
        messages.append("\n"+msg);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_lobby);
        waitingLobby = this;
        messageHandler = MessageHandler.messageHandler();
        user = player.getPlayerName();
        clientOrOwner =  findViewById(R.id.owner);
        messages = findViewById(R.id.messages);
        send = findViewById(R.id.send);
        start = findViewById(R.id.start);
        message = findViewById(R.id.message);
        messages.setMovementMethod(new ScrollingMovementMethod());


        if(owner){
            clientOrOwner.setText("HOST");
        }

        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                usermessage = (user+" says: "+ message.getText().toString());
                if(messageHandler != null){
                     if (!owner) {
                         if (mServer == null) {
                                 mServer = messageHandler.getServer();
                    }else{
                        mServer.write(usermessage);
                        addMsg(usermessage);
                        message.setText(""); }
                }else{
                    messageHandler.write(usermessage);
                    addMsg(usermessage);
                    message.setText(""); }

                if (mServer == null && !owner) {
                    serverIsNull();
                }}else{
                    messageHandler = getMessageHandler();
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(messageHandler != null){
                    if (!owner) {
                        if (mServer == null) {
                            mServer = messageHandler.getServer();
                            sendStartEvent();
                        } else {
                            sendStartEvent();
                        }
                    } else {
                        sendStartEvent();
                    }
                    if (mServer == null && !owner) {
                        serverIsNull();
                    }}else{
                    messageHandler = getMessageHandler();
                }
            }
        });
    }

    private void serverIsNull() {
        Toast.makeText(getApplicationContext(), "Please reconnect..", Toast.LENGTH_SHORT).show();
        System.out.println("Server is null....");
    }

    private void sendStartEvent() {
        Intent i = new Intent(WaitingLobby.this, GameActivity.class);
        startActivity(i);
    }
}
