package com.example.mi_b_wizard.Network;

import android.annotation.SuppressLint;
import android.content.Context;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.mi_b_wizard.GameActivity;
import com.example.mi_b_wizard.JoinGameActivity;
import com.example.mi_b_wizard.Notifications;
import com.example.mi_b_wizard.WaitingLobby;

import java.util.ArrayList;

public class MessageHandler implements Handler.Callback {
    private Server server;
    private Handler handler = new Handler(this);
    private Context joingameContext;
    private String gameNaN ="game is null";
    private String tag = "MessageHandler";
    Notifications notifications;
    private WaitingLobby waitingLobby = WaitingLobby.getWaitingLobby();
    private GameActivity gameActivity  = GameActivity.getGameActivity();
    boolean set = false;
    boolean hostCheated = false;
    private ArrayList<Server> Clients = new ArrayList<Server>();
    private ArrayList<Integer> id = new ArrayList<Integer>();
    public ArrayList<Integer> cheaters = new ArrayList<Integer>();
    @SuppressLint("StaticFieldLeak")
    private static MessageHandler messageHandler;
    private byte  n = 0;
    public static final byte HANDLE = 2;
    public static final byte READ = 1;
    public static final byte MOVE = 3;
    public static final byte START_GAME =  4;
    public static final byte CARDS = 5;
    public static final byte SEND_CARDS =  6;
    public static final byte PREDICTED_TRICKS = 7;
    public static final byte GET_MY_ID = 8;
    public static final byte YOUR_TURN =  9;
    public static final byte WINNER = 10;
    public static final byte POINTS =  11;
    public static final byte CHEAT = 12;
    public static final byte TRUMP = 13;
    public static final byte ROUND =  17;
    public static final byte GOT_CARDS =  14;
    public static final byte NOTIFICATION = 68;
    public static final byte END = 49;
    public static final byte DETECT = 77;
    public static final byte CHEATER_FOUND = 94;
    public static final byte ROUNDEND =  18;




    public ArrayList<Integer> getId() {
        return id;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    public boolean didSomebodyCheated(){
        return (hostCheated || !cheaters.isEmpty());
    }

    public static MessageHandler messageHandler(){
        return messageHandler;
    }

    public static void setMessageHandler(MessageHandler messageHandler) {
        MessageHandler.messageHandler = messageHandler;
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

    public void resetCheaters(){
        cheaters.clear();
        hostCheated = false;
    }
    public void setHostCheated(boolean b){
        hostCheated = b;
    }
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if(!set){
        setActivities();}
        switch (msg.what) {
            case READ:
                String s = new String((byte[]) msg.obj, 1, msg.arg1);
                waitingLobby.addMsg(s);
                notifications.showNotification("new message", s);
                if (JoinGameActivity.owner) {
                    writeToAllExceptTheSender(Server.READ,s, msg.arg2);
                }
                break;

            case HANDLE:
                Object obj = msg.obj;
                setServer((Server) obj);
                    Clients.add((Server) obj);
                    id.add(msg.arg2);
                break;

            case START_GAME:
                if(gameActivity != null){
                gameActivity.start();}else{
                    waitingLobby.sendStartEvent();
                    gameActivity.start();
                }
                break;

            case SEND_CARDS:
                byte[] cards = (byte[]) msg.obj;
                if (JoinGameActivity.owner) {
                    sendCardsToPlayer(cards, msg.arg2);
                }
                break;

            case MOVE:
                byte[] move = (byte[]) msg.obj;
                if(gameActivity != null && move[1] >= 16 && move[1]< 76){
                    gameActivity.showMove(move[1]);
                if (JoinGameActivity.owner) {
                  gameActivity.playerMadeAMove(move[1], msg.arg2);
                  sendEventToAllExceptTheSender(Server.MOVE,move[1],msg.arg2);
                }}

                break;

            case TRUMP:
                byte[] trump = (byte[]) msg.obj;
                if(gameActivity != null){
                if (JoinGameActivity.owner) {
                    sendEvent(Server.TRUMP,trump[1]);
                }else {
                    gameActivity.setTrump(trump[1]);
                }}
                break;

            case PREDICTED_TRICKS:
                String tricks = new String((byte[])msg.obj,1,msg.arg1);
                if(gameActivity != null) {
                    if (JoinGameActivity.owner) {
                        writeToAllExceptTheSender(Server.TRICKS, tricks, msg.arg2);
                    }
                    gameActivity.showPredictedTricks(tricks);
                }
                break;

            case NOTIFICATION:
                String notification = new String((byte[]) msg.obj, 1, msg.arg1);
                if(gameActivity != null){
               // gameActivity.toast(notification);
                if (JoinGameActivity.owner) {
                    writeToAllExceptTheSender(Server.NOTIFICATION,notification, msg.arg2);
                }}
                break;

            case CARDS:
                byte[] givenCards = (byte[]) msg.obj;
                if (gameActivity != null){
                    gameActivity.takeCards(givenCards);}
                else {
                    Log.i(tag,gameNaN);
                }
                break;

            case CHEAT:
                if (gameActivity != null){
                if(JoinGameActivity.owner) {
                  writeToTheSender(Server.GOT_CARDS,gameActivity.getPlayerHand(),msg.arg2);
                  cheaters.add(msg.arg2);
                }}else {
                    Log.i(tag,gameNaN);
                }
                break;

            case GOT_CARDS:
                if (gameActivity != null) {
                    String cardsfromplayer = new String((byte[]) msg.obj, 1, msg.arg1);
                    gameActivity.openCheatPopUp(cardsfromplayer);
                }else{
                    Log.i(tag,gameNaN);
                }
                break;

            case YOUR_TURN:
                if (gameActivity != null){
                   gameActivity.MyTurn(); }
                else {
                    Log.i(tag,gameNaN);
                }
                break;

            case POINTS:
                String points = new String((byte[]) msg.obj, 1, msg.arg1);
                if (gameActivity != null) {
                    gameActivity.setPointsInList(points);
                    gameActivity.setPointsInDialog(points);
                if (JoinGameActivity.owner){
                    writeToAllExceptTheSender(Server.SEND_POINTS,points, msg.arg2);
                }}
                break;

            case END:
                if (gameActivity != null){
                    gameActivity.endGame();}
                else {
                    Log.i(tag,gameNaN);
                }
                break;

            case DETECT:
                if (gameActivity != null && JoinGameActivity.owner){
                    if(didSomebodyCheated()){
                        writeToTheSender(Server.CHEATER_FOUND,"1",msg.arg2);
                        findCheaters();
                        resetCheaters();
                    }else{
                        writeToTheSender(Server.CHEATER_FOUND,"3",msg.arg2);
                    }
                } else {
                    Log.i(tag,gameNaN);
                }
                break;

            case ROUND:
                if (gameActivity != null){
                    gameActivity.newRound(); }
                else { Log.i(gameNaN,tag); }
                break;

            case CHEATER_FOUND:
                String s1 = new String((byte[]) msg.obj, 1, msg.arg1);
                if (gameActivity != null){
                    gameActivity.showCheater(s1);
                    }
                else { Log.i(gameNaN,tag); }
                break;

            case WINNER:
                byte[] win = (byte[]) msg.obj;
                if (gameActivity != null){
                    gameActivity.showWhoIsTheWinner();
                }
                else {
                    Log.i(gameNaN,tag); }
                break;


                default:
                    Log.i("unknown event",tag);
        }
        return true;
    }

    public void findCheaters() {
        if(hostCheated){
            gameActivity.showCheater("2");
        }

        for (int i = 0; i <cheaters.size(); i++) {
            writeToTheSender(Server.CHEATER_FOUND,"2",cheaters.get(i));
        }
    }

    private void setActivities() {
        if(gameActivity == null){
            gameActivity = GameActivity.getGameActivity();
        }
        if(waitingLobby == null){
            waitingLobby = WaitingLobby.getWaitingLobby();
        }
        if( gameActivity != null && waitingLobby !=null){
            set = true;
        }
    }

    private void writeToAllExceptTheSender(byte event,String s, int id) {
        System.out.println(Clients.size());
        if (Clients.size() >= 1) {
            for (Server Clients : Clients) {
                if (Clients.getMyId() != id) {
                    Clients.write(event,s);
                }
            }
        }
    }

    private void writeToTheSender(byte event,String s, int id) {
        if (Clients.size() >= 1) {
            for (Server Clients : Clients) {
                if (Clients.getMyId() == id) {
                    Clients.write(event,s);
                }
            }
        }
    }

    public void write(byte event,String msg) {
        if (Clients.size() >= 1) {
            for (Server Clients : Clients) {
                Clients.write(event,msg);
            }
        } else {
            server.write(event,msg);
        }
    }

    public void sendEvent(byte whatEvent, byte card) {
        for (Server Clients : Clients) {
            Clients.event(whatEvent, card);
        }
    }

    public void sendEventToAllExceptTheSender(byte whatEvent, byte card, int id) {
            for (Server Clients : Clients) {
                if (Clients.getMyId() != id) {
                    Clients.event(whatEvent, card);
                }
            }
    }

    public void sendCardsToPlayer(byte[] cards, int id) {
        for (Server Clients : Clients) {
            if (Clients.getMyId() == id) {
                Clients.sendCards(cards);
            }
        }
    }

    public void sendEventToTheSender(byte whatEvent, byte card, int id) {
        for (Server Clients : Clients) {
            if (Clients.getMyId() == id) {
                Clients.event(whatEvent, card);
            }
        }
    }
}
