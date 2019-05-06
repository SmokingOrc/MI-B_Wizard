package com.example.mi_b_wizard.Network;


import android.os.Handler;

import com.example.mi_b_wizard.Data.Card;
import com.example.mi_b_wizard.JoinGameActivity;
import com.example.mi_b_wizard.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Server extends Thread{
    private Socket socket;
    private Handler handler;
    private long id;
    Card card;

    public Server(Socket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;

    }


    private InputStream inputStream;
    private OutputStream outputStream;

    ObjectOutputStream out= null;
    ObjectInputStream in= null;

    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            out= new ObjectOutputStream(socket.getOutputStream());
            in= new ObjectInputStream(socket.getInputStream());

            byte[] buffer = new byte[1024];
            int bytes;
            handler.obtainMessage(MessageHandler.HANDLE, this).sendToTarget();

            while (true) {
                try {
                    bytes = inputStream.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    System.out.println("server: ");
                    handler.obtainMessage(MessageHandler.READ, bytes, (int)getId(), buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
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
                    System.out.println("writing-Server");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.run();
    }
    //Object Card send
    public void recieveCard() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    card = (Card) in.readObject();
                    System.out.println(card);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.run();
    }

    public void sendCard(final Card card){
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    out.writeObject(card);
                    out.flush();
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