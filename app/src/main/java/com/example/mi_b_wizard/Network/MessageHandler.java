package com.example.mi_b_wizard.Network;

import android.content.Context;

import android.os.Handler;
import android.os.Message;

import com.example.mi_b_wizard.Data.Card;
import com.example.mi_b_wizard.JoinGameActivity;
import com.example.mi_b_wizard.Notifications;
import java.util.ArrayList;

public class MessageHandler implements Handler.Callback{
    Handler handler = new Handler(this);
    Server server;

    Context joingameContext;
    Notifications notifications;
    private ArrayList<Server> Clients = new ArrayList<Server>();

    public static final int HANDLE = 0x400+2;
    public static final int READ = 0x400+1;
    public static final int MOVE = 0x400+3;

    public void setServer(Server server) {
        this.server = server;
    }
    public Server getServer() {
        return server;
    }

    private void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

    public void setJoingameContext(Context joingameContext) {
        this.joingameContext = joingameContext;
        setNotifications(new Notifications(joingameContext));
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case READ:
                byte[] read = (byte[]) msg.obj;
                String s = new String(read,0, msg.arg1);
                System.out.println(s);
                int id = msg.arg2;
                System.out.println(id);
                notifications.showMsg(s);
                notifications.showNotification("new message",s);
                if(JoinGameActivity.owner){writeToAllExceptTheSender(s, id);}
                break;

            case HANDLE:
                System.out.println("setting server....");
                Object obj = msg.obj;
                setServer((Server) obj);
                if(JoinGameActivity.owner) {
                    Clients.add((Server) obj);
                }
                break;

            case MOVE:
                System.out.println("new move made..");
                // TODO what happens on move.
        }
        return true;
    }

    private void writeToAllExceptTheSender(String s, int id) {
            for (Server Clients : Clients) {
                if ((int) Clients.getId() == id) {
                    System.out.println(" CLient with id : "+id+" has sent a message");
                } else {
                    Clients.write(s);
                }
            }
        }

    public void write(String msg){
            for (Server Clients : Clients) {
                Clients.write(msg);
            }
    }

    public void move(String msg){
        for (Server Clients : Clients) {
            Clients.write(msg);
        }
    }

    public void sendCard(Card card){
        for (Server Clients : Clients) {
            Clients.sendCard(card);
        }
    }

}
