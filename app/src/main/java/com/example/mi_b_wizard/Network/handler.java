package com.example.mi_b_wizard.Network;
import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;
import android.os.Build;
import android.os.Handler;
import android.app.Notification;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.widget.Toast;

import com.example.mi_b_wizard.JoinGameActivity;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


public class handler {
    static final int MSG = 1;
    static final int MOVE = 2;
    JoinGameActivity joinGameActivity = new JoinGameActivity();


    public Handler handler = new Handler(new Handler.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public boolean handleMessage(Message msg) {
            System.out.println("handler");
            switch (msg.what){
                case MSG:
                    byte[] read = (byte[]) msg.obj;
                    System.out.println(msg+"+++++++++++++++++++++"+read);
                    String s = new String(read, StandardCharsets.UTF_8);
                    System.out.println(s);
                   // String temp = new String(readBuffe,0,msg.arg1);
                    break;

                case MOVE:
                    // do something..
            }
            return true;
        }
    });

}