package com.example.mi_b_wizard.Network;

import android.os.Handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client extends Thread {

    private Handler handler;
    private Socket socket;
    private Server server;
    private InetAddress mAddress;

    public Client(Handler handler, InetAddress hostAddress) {
        this.handler = handler;
        this.mAddress = hostAddress;
    }

    @Override
    public void run() {
        socket = new Socket();
        try {
            socket.bind(null);
            socket.connect(new InetSocketAddress(mAddress.getHostAddress(), 8888), 5000);

            server = new Server(socket, handler);
            new Thread(server).start();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ee) {
                ee.printStackTrace();
            }
            return;
        }
    }
    public Server getServer(){return  server;}
}
           /* inputStream = socket.getInputStream();
            outputStream =  socket.getOutputStream();
            sendReceive = new SendReceive(socket);
            while (socket!=null){
                try {
                    bytes = inputStream.read();
                    System.out.println(bytes);

                    if(bytes>0) {
                        handler.handler.obtainMessage(1, bytes,0,buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void write(byte[] bytes){
        try {
            if(outputStream !=null){
                System.out.println("writing -----------------------");
                this.outputStream.write(bytes);}

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} */

