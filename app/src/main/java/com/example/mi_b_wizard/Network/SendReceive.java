package com.example.mi_b_wizard.Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class SendReceive extends Thread {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;


    @Override
    public void run() {
        super.run();
        byte[] buffer = new byte[1024];
        int bytes;

        while (socket!=null){
            try {
                bytes = inputStream.read();
                if(bytes>0) {
                    handler.eventHandler.obtainMessage(handler.MSG, bytes, -1, buffer).sendToTarget();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void write(byte[] bytes){
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SendReceive(Socket socket1){
        socket = socket1;
        try {
            inputStream = socket.getInputStream();
            outputStream =  socket.getOutputStream();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
