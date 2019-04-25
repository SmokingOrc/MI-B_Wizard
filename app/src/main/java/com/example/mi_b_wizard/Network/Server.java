package com.example.mi_b_wizard.Network;


import android.os.Handler;

import com.example.mi_b_wizard.JoinGameActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Server implements Runnable {

    private Socket socket;
    private Handler handler;


    public Server(Socket socket, Handler handler){
        this.socket = socket;
        this.handler = handler;
    }

    private InputStream inputStream;
    private OutputStream outputStream;

    @Override
    public void run() {
            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                byte[] buffer = new byte[1024];
                int bytes;

                handler.obtainMessage(JoinGameActivity.HANDLE, this).sendToTarget();

                while (true){
                    try {
                        bytes = inputStream.read(buffer);
                        if (bytes == -1) {
                            break;
                        }
                        System.out.println("server: "+String.valueOf(buffer));
                        handler.obtainMessage(JoinGameActivity.READ,bytes,-1,buffer).sendToTarget();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void write (String msg){
                final byte[] buffer = msg.getBytes();
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                       try {
                           outputStream.write(buffer);
                           System.out.println("writing");
                       }catch (IOException e){
                           e.printStackTrace();
                       }
                    }
                };
                thread.start();
            }


    /*handler handler;
    Socket socket;
    SendReceive sendReceive;
    ServerSocket serverSocket;
    InputStream inputStream;
    OutputStream outputStream;
    ByteArrayInputStream byteArrayInputStream;
    byte[] buffer = new byte[1024];
    int bytes;


    @Override
    public void run() {
        super.run();
        handler = new handler();
        try {
            serverSocket = new ServerSocket(8888);
            socket = serverSocket.accept();
            inputStream = socket.getInputStream();
            outputStream =  socket.getOutputStream();
            sendReceive = new SendReceive(socket);

            while (socket!=null){
                try {
                    bytes = inputStream.read(buffer);
                    String s = new String(buffer);
                    if(bytes>0) {
                        handler.handler.obtainMessage(MSG, bytes, 0, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void write(String msg){
        try {
                if(outputStream !=null){
                    System.out.println("writing -----------------------");
                    outputStream.write(bytes);}


            } catch (IOException e) {
            e.printStackTrace();
        }
    } */
}