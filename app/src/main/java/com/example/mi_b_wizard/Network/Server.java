package com.example.mi_b_wizard.Network;


import android.os.Handler;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Server extends Thread{
    private Socket socket;
    private Handler handler;
    private int id = this.hashCode();
    public final static byte MOVE = 11;
    public final static byte START_GAME = 22;
    public final static byte GIVE_ME_CARDS = 33;
    public final static byte CARDS = 44;
    public final static byte TRICKS = 55;
    public final static byte ID = 66;
    public final static byte WINNER = 77;
    public final static byte YOUR_TURN = 88;
    public final static byte SEND_POINTS = 99;
    public final static byte TRUMP = 100;
    public final static byte CHEAT = 110;
    public final static byte NEW_ROUND = 120;
    public final static byte READ = 114;
    public final static byte GOT_CARDS = 124;
    public final static byte NOTIFICATION  = 32;

    public final static  byte GIVE_ME_YOUR_CARDS = 12;


    public Server(Socket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;

    }

    public int getMyId(){
        return id;
    }

    private InputStream inputStream;
    private OutputStream outputStream;
    byte [] myCards = new byte[21];

    @Override
    public void run() {
        try {

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytes;
            handler.obtainMessage(MessageHandler.HANDLE,1,id,this).sendToTarget();

            while (true) {
                try {
                    bytes = inputStream.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    System.out.println("event id is : "+buffer[0]);
                    switch (buffer[0]){

                        case READ:
                            handler.obtainMessage(MessageHandler.READ, bytes, id, buffer).sendToTarget();
                            break;

                        case NOTIFICATION:
                            handler.obtainMessage(MessageHandler.NOTIFICATION, bytes, id, buffer).sendToTarget();
                            break;

                        case MOVE:
                            handler.obtainMessage(MessageHandler.MOVE, bytes, id, buffer).sendToTarget();
                            break;

                        case WINNER:
                            handler.obtainMessage(MessageHandler.WINNER, bytes, id, buffer).sendToTarget();
                            break;

                        case TRUMP:
                            handler.obtainMessage(MessageHandler.TRUMP, bytes, id, buffer).sendToTarget();
                            break;

                        case START_GAME:
                            handler.obtainMessage(MessageHandler.START_GAME, bytes, id, buffer).sendToTarget();
                            break;

                        case GIVE_ME_CARDS:
                            handler.obtainMessage(MessageHandler.SEND_CARDS, bytes, id, buffer).sendToTarget();
                            break;

                        case CARDS:
                            handler.obtainMessage(MessageHandler.CARDS, bytes, id, buffer).sendToTarget();
                            break;

                        case ID:
                            handler.obtainMessage(MessageHandler.GET_MY_ID, bytes, id , buffer).sendToTarget();
                            break;

                        case TRICKS:
                            handler.obtainMessage(MessageHandler.PREDICTED_TRICKS, bytes,id, buffer).sendToTarget();
                            break;

                        case YOUR_TURN:
                            handler.obtainMessage(MessageHandler.YOUR_TURN, bytes, id, buffer).sendToTarget();
                        
                        case SEND_POINTS:
                            handler.obtainMessage(MessageHandler.POINTS, bytes, id, buffer).sendToTarget();
                            System.out.println("points");
                            break;

                        case NEW_ROUND:
                            handler.obtainMessage(MessageHandler.ROUND, bytes, id, buffer).sendToTarget();
                            break;

                        case CHEAT:
                            handler.obtainMessage(MessageHandler.CHEAT, bytes, id, buffer).sendToTarget();
                            break;

                        case GOT_CARDS:
                            handler.obtainMessage(MessageHandler.GOT_CARDS, bytes, id, buffer).sendToTarget();
                            break;

                            default:
                                System.out.println("default : "+buffer[0]);
                                break;

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void write(byte event, String msg) {
        final byte[] firstbyte = {event};
        final byte[] buffer  = ArrayUtils.addAll(firstbyte,msg.getBytes());

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    outputStream.write(buffer);
                    System.out.println("writing-Server ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.run();
    }

    public void event (byte whatEvent, byte card, byte cardColor, byte player){
        final byte[] buffer = {whatEvent,card,cardColor,player};

       Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    outputStream.write(buffer);
                    System.out.println("New event");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.run();
    }

    public void sendCards (byte[] cards){
        myCards = cards;
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    outputStream.write(myCards);
                    System.out.println("cards sent");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.run();
    }


    @Override
    public long getId() {
        return super.getId();
    }
}