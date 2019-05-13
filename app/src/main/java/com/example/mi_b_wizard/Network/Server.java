package com.example.mi_b_wizard.Network;


import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Server extends Thread{
    private Socket socket;
    private Handler handler;
    public final static byte MOVE = 1;
    public final static byte START_GAME = 2;
    public final static byte GIVE_ME_CARDS = 3;
    public final static byte CARDS = 4;
    public final static byte TRICKS = 5;
    public final static byte ID = 6;
    public final static byte WINNER = 7;
    public final static byte YOUR_TURN = 8;
    public final static byte SEND_POINTS = 9;
    public final static byte TRUMP = 10;


    public Server(Socket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;

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
            handler.obtainMessage(MessageHandler.HANDLE,1,(int)getId(),this).sendToTarget();

            while (true) {
                try {
                    bytes = inputStream.read(buffer);
                    if (bytes == -1) {
                        break;
                    }

                    System.out.println("event id is : "+buffer[0]);
                    switch (buffer[0]){
                        case MOVE:
                            handler.obtainMessage(MessageHandler.MOVE, bytes, (int)getId(), buffer).sendToTarget();
                            System.out.println("move");
                            break;

                        case TRUMP:
                            handler.obtainMessage(MessageHandler.TRUMP, bytes, (int)getId(), buffer).sendToTarget();
                            System.out.println("trump");
                            break;

                        case START_GAME:
                            handler.obtainMessage(MessageHandler.START_GAME, bytes, (int)getId(), buffer).sendToTarget();
                            System.out.println("start");
                            break;

                        case GIVE_ME_CARDS:
                            handler.obtainMessage(MessageHandler.SEND_CARDS, bytes, (int)getId(), buffer).sendToTarget();
                            System.out.println("give me cards");
                            break;

                        case CARDS:
                            handler.obtainMessage(MessageHandler.CARDS, bytes, (int)getId(), buffer).sendToTarget();
                            System.out.println("cards");
                            break;

                        case ID:
                            handler.obtainMessage(MessageHandler.GET_MY_ID, bytes, (int)getId(), buffer).sendToTarget();
                            System.out.println("ID");
                            break;

                        case TRICKS:
                            handler.obtainMessage(MessageHandler.SEND_CARDS, bytes, (int)getId(), buffer).sendToTarget();
                            System.out.println("give me cards");
                            break;

                        case WINNER:
                            handler.obtainMessage(MessageHandler.WINNER, bytes, (int)getId(), buffer).sendToTarget();
                            System.out.println("winner");
                            break;

                        case YOUR_TURN:
                            handler.obtainMessage(MessageHandler.YOUR_TURN, bytes, (int)getId(), buffer).sendToTarget();
                            System.out.println("your turn");
                            break;

                            default:
                                handler.obtainMessage(MessageHandler.READ, bytes, (int)getId(), buffer).sendToTarget();
                                System.out.println("default");
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
    public void write(String msg) {
        final byte[] buffer = (msg).getBytes();

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