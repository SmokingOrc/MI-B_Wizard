package com.example.mi_b_wizard.Network;

import android.content.Context;

import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;

import com.example.mi_b_wizard.Data.Game;
import com.example.mi_b_wizard.GameActivity;
import com.example.mi_b_wizard.JoinGameActivity;
import com.example.mi_b_wizard.Notifications;
import com.example.mi_b_wizard.WaitingLobby;

import java.util.ArrayList;

public class MessageHandler implements Handler.Callback {
    Server server;
    Handler handler = new Handler(this);
    Context joingameContext;
    Notifications notifications;
    WaitingLobby waitingLobby = WaitingLobby.getWaitingLobby();
    GameActivity gameActivity  = GameActivity.getGameActivity();

    private ArrayList<Server> Clients = new ArrayList<Server>();
    private ArrayList<Integer> id = new ArrayList<Integer>();
    private static MessageHandler messageHandler;
    byte  n = 0;
    public static final int HANDLE = 0x400 + 2;
    public static final int READ = 0x400 + 1;
    public static final int MOVE = 0x400 + 3;
    public static final int START_GAME = 0x400 + 4;
    public static final int CARDS = 0x400 + 5;
    public static final int SEND_CARDS = 0x400 + 6;
    public static final int PREDICTED_TRICKS = 0x400 + 7;
    public static final int GET_MY_ID = 0x400 + 8;
    public static final int YOUR_TURN = 0x400 + 9;
    public static final int WINNER = 0x400 + 10;
    public static final int POINTS = 0x400 + 11;
    public static final int CHEAT = 0x400 + 12;
    public static final int TRUMP = 0x400 + 13;
    public static final int ROUND = 0x400 + 17;
    public static final int GOT_CARDS =  0x400 + 14;



    public ArrayList<Integer> getId() {
        return id;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
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

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if(gameActivity == null){
            gameActivity = GameActivity.getGameActivity();
        }
        if(waitingLobby == null){
            waitingLobby = WaitingLobby.getWaitingLobby();
        }
        switch (msg.what) {
            case READ:
                String s = new String((byte[]) msg.obj, 1, msg.arg1);
                System.out.println(msg.arg2);
                waitingLobby.addMsg(s);
                notifications.showNotification("new message", s);
                if (JoinGameActivity.owner) {
                    writeToAllExceptTheSender(s, msg.arg2);
                }
                break;

            case HANDLE:
                Object obj = msg.obj;
                setServer((Server) obj);
                if (JoinGameActivity.owner) {
                    Clients.add((Server) obj);
                    id.add(msg.arg2);
                }
                break;

            case START_GAME:
                gameActivity.start();
                break;

            case SEND_CARDS:
                byte[] cards = (byte[]) msg.obj;
                if (JoinGameActivity.owner) {
                    sendCardsToPlayer(cards, msg.arg2);
                }
                break;

            case MOVE:
                byte[] move = (byte[]) msg.obj;
                if (JoinGameActivity.owner) {
                  gameActivity.playerMadeAMove(move[1], msg.arg2);
                  sendEventToAllExceptTheSender(Server.MOVE,move[1],n,n,msg.arg2);
                    System.out.println("Player made a new move"+ move[1]);
                }else {
                    System.out.println("host made a move "+ move[1]);
                    gameActivity.showMove(move[1]);
                }
                break;

            case TRUMP:
                byte[] trump = (byte[]) msg.obj;
                if (JoinGameActivity.owner) {
                    sendEvent(Server.TRUMP,trump[1],n,n);
                }else {
                    gameActivity.setTrump(trump[1]);
                }
                break;

            case PREDICTED_TRICKS:
                byte[] tricks = (byte[]) msg.obj;
                if (JoinGameActivity.owner) {
                    sendEventToAllExceptTheSender(tricks[0], tricks[1], tricks[2], tricks[3], msg.arg2);
                    gameActivity.showPredictedTricks(tricks[1],msg.arg2);
                }else{
                    gameActivity.showPredictedTricks(tricks[1],msg.arg2);
                }
                break;

            case CARDS:
                byte[] givenCards = (byte[]) msg.obj;
                if (gameActivity != null){
                    gameActivity.takeCards(givenCards);}
                else {
                    System.out.println("game is null");
                }
                break;

            case CHEAT:
                byte[] cheat = (byte[]) msg.obj;
                if(JoinGameActivity.owner) {
                  writeToTheSender(Server.GOT_CARDS,gameActivity.getPlayerHand(),msg.arg2);
                }
                break;

            case GOT_CARDS:
                String cardsfromplayer = new String((byte[])msg.obj,1,msg.arg1);
                gameActivity.openCheatPopUp(cardsfromplayer);
                System.out.println("cards "+cardsfromplayer);
                break;

            case YOUR_TURN:
                if (gameActivity != null){
                   gameActivity.MyTurn();}
                else {
                    System.out.println("game is null");
                }
                System.out.println("Player got cards");
                break;

            case POINTS:
                byte[] points = (byte[]) msg.obj;
                if (gameActivity != null){
                    gameActivity.showPoints(points);
                    gameActivity.madeTrickUpdate();
                }
                else {
                    System.out.println("game is null");
                }
                break;

            case ROUND:
                if (gameActivity != null){
                    gameActivity.newRound();
                }
                else {
                    System.out.println("game is null");
                }
                break;

            case WINNER:
                byte[] winner = (byte[]) msg.obj;
                if (gameActivity != null){
                    gameActivity.showWhoIsTheWinner();
                }
                else {
                    System.out.println("game is null"); }
                break;


        }
        return true;
    }

    private void writeToAllExceptTheSender(String s, int id) {
        System.out.println(Clients.size());
        if (Clients.size() >= 1) {
            for (Server Clients : Clients) {
                if (Clients.getMyId() == id) {
                    System.out.println(" CLient with id : " + id + " has sent a message");
                } else {
                    Clients.write(Server.READ,s);
                    System.out.println(" sent id : " + id +" "+Clients.getMyId());
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
        System.out.println("New message sent");
        if (Clients.size() >= 1) {
            for (Server Clients : Clients) {
                Clients.write(event,msg);
            }
        } else {
            server.write(event,msg);
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

    public void sendEventToAllExceptTheSender(byte whatEvent, byte card, byte cardColor, byte player, int id) {
        if (Clients.size() >= 1) {
            for (Server Clients : Clients) {
                if (Clients.getMyId() == id) {
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

    public void sendCardsToPlayer(byte[] cards, int id) {
        for (Server Clients : Clients) {
            if (Clients.getMyId() == id) {
                System.out.println("sent to "+id);
                Clients.sendCards(cards);
            }
        }
    }

    public void sendEventToTheSender(byte whatEvent, byte card, byte cardColor, byte player, int id) {
        for (Server Clients : Clients) {
            if (Clients.getMyId() == id) {
                System.out.println("sent to "+id);
                Clients.event(whatEvent, card, cardColor, player);
            }
        }
    }
}
