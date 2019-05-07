package com.example.mi_b_wizard.Network;

import android.content.Context;

import android.os.Handler;
import android.os.Message;

import com.example.mi_b_wizard.GameActivity;
import com.example.mi_b_wizard.Instance;
import com.example.mi_b_wizard.JoinGameActivity;
import com.example.mi_b_wizard.Notifications;
import com.example.mi_b_wizard.WaitingLobby;

import java.util.ArrayList;

public class MessageHandler implements Handler.Callback {
    Server server;
    Handler handler = new Handler(this);
    Context joingameContext;
    Notifications notifications;
    WaitingLobby waitingLobby;
    GameActivity gameActivity;

    private ArrayList<Server> Clients = new ArrayList<Server>();

    public static final int HANDLE = 0x400 + 2;
    public static final int READ = 0x400 + 1;
    public static final int MOVE = 0x400 + 3;
    public static final int START_GAME = 0x400 + 4;
    public static final int CARDS = 0x400 + 5;
    public static final int SEND_CARDS = 0x400 + 6;


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
        if (waitingLobby == null) {
            waitingLobby = Instance.waitingLobby;
        }
        if (gameActivity == null) {
            gameActivity = Instance.gameActivity;
        }
        switch (msg.what) {
            case READ:
                byte[] read = (byte[]) msg.obj;
                String s = new String(read, 0, msg.arg1);
                System.out.println("message is : " + s);
                // notifications.showMsg(s);
                waitingLobby.addMsg(s);
                notifications.showNotification("new message", s);
                if (JoinGameActivity.owner) {
                    writeToAllExceptTheSender(s, msg.arg2);
                }
                break;
            case HANDLE:
                System.out.println("setting server....");
                Object obj = msg.obj;
                setServer((Server) obj);
                if (JoinGameActivity.owner) {
                    Clients.add((Server) obj);
                }
                break;
            case START_GAME:
                byte[] start = (byte[]) msg.obj;
                if (JoinGameActivity.owner) {
                    sendEventToAllExceptTheSender(start[0], start[1], start[2], start[3], msg.arg2);
                }
                System.out.println("game started");
                break;

            case SEND_CARDS:
                byte[] card = (byte[]) msg.obj;
                if (JoinGameActivity.owner) {
                    sendEventToTheSender(Server.CARDS, card[1], card[2], card[3], msg.arg2);
                }
                // TODO Send cards from Game class.
                System.out.println("Player wants a cards");
                break;

            case MOVE:
                byte[] move = (byte[]) msg.obj;
                if (JoinGameActivity.owner) {
                    sendEventToAllExceptTheSender(move[0], move[1], move[2], move[3], msg.arg2);
                }
                System.out.println("Player made a new move");
                break;

            case CARDS:
                byte[] givenCard = (byte[]) msg.obj;
                if (gameActivity != null){
                    gameActivity.takeCards(givenCard[1], givenCard[2]);}
                else {
                    System.out.println("game is null");
                }
                System.out.println("Player got cards");
                break;
        }
        return true;
    }

    private void writeToAllExceptTheSender(String s, int id) {
        System.out.println(Clients.size());
        if (Clients.size() >= 1) {
            for (Server Clients : Clients) {
                if ((int) Clients.getId() == id) {
                    System.out.println(" CLient with id : " + id + " has sent a message");
                } else {
                    Clients.write(s);
                }
            }
        }
    }

    public void write(String msg) {
        System.out.println("New message sent");
        System.out.println(Clients.size());
        if (Clients.size() >= 1) {
            for (Server Clients : Clients) {
                Clients.write(msg);
            }
        } else {
            server.write(msg);
        }
    }


    public void sendEvent(byte whatEvent, byte card, byte cardColor, byte player) {
        System.out.println("New event sent");
        if (Clients.size() >= 1) {
            for (Server Clients : Clients) {
                Clients.event(whatEvent, card, cardColor, player);
            }
        } else {
            server.event(whatEvent, card, cardColor, player);
        }
    }

    private void sendEventToAllExceptTheSender(byte whatEvent, byte card, byte cardColor, byte player, int id) {

        if (Clients.size() >= 1) {
            for (Server Clients : Clients) {
                if ((int) Clients.getId() == id) {
                    System.out.println(" CLient with id : " + id + " has sent a event");
                } else {
                    Clients.event(whatEvent, card, cardColor, player);
                    System.out.println(" CLient with id : " + id + " has sent a event");
                }
            }
        } else {
            server.event(whatEvent, card, cardColor, player);
        }
    }

    private void sendEventToTheSender(byte whatEvent, byte card, byte cardColor, byte player, int id) {
        for (Server Clients : Clients) {
            if ((int) Clients.getId() == id) {
                System.out.println("sent to "+id);
                Clients.event(whatEvent, card, cardColor, player);
            }
        }
    }
}
