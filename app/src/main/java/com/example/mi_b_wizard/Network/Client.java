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

    public Client(Handler handler, InetAddress hostAddress){
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

    public Server getServer(){return server;}
}