package com.example.mi_b_wizard.Network;
import android.os.Handler;
import android.app.Notification;
import android.os.Message;
import android.widget.Toast;

public class handler {
    static final int MSG = 1;
    static final int MOVE = 2;

    public static Handler eventHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG:
                    byte[] readBuff = (byte[]) msg.obj;
                    String temp = new String(readBuff, 0, msg.arg1);

                    break;

                case MOVE:
            }
            return  true;
        }

});}
