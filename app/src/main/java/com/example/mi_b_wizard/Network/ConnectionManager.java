package com.example.mi_b_wizard.Network;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

import com.example.mi_b_wizard.Data.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {
    private static ConnectionManager singleton=null;
    private static MessageHandler messageHandler;
    static Server mServer;
    public static boolean owner = false;
    private Player[] players;
    HashMap<Player, WifiP2pDevice> playerDeviceMap = new HashMap<>();
    WifiP2pDevice mdevice;

    private ConnectionManager(Server mServer, MessageHandler messageHandler) {
       this.mServer = mServer;
        this.messageHandler = messageHandler;
    }

    private ConnectionManager(){
       // Singleton Pattern
    }


        // Usage for Server
    static public ConnectionManager getInstance(Server mServer, MessageHandler messageHandler){
        if (singleton==null) singleton = new ConnectionManager(mServer, messageHandler);
        return singleton;
    }
        //Usage for Client
    static public ConnectionManager getInstance(){
        if (singleton==null){
            singleton = new ConnectionManager();
            return singleton;
        }else {
            return singleton;
        }

    }

    public void setConnection(Server mServer, MessageHandler messageHandler){
        ConnectionManager.mServer = mServer;
        ConnectionManager.messageHandler=messageHandler;

    }

    public Server getServer(){
        return mServer;
    }

    public MessageHandler getMessageHandler(){
        return messageHandler;
    }

    public void setIsOwner(boolean owner){
        this.owner = owner;
    }


    public boolean isOwner(){
        return owner;

    }

    public boolean setPlayers(ArrayList<WifiP2pDevice> playerlist){
        players = new Player[playerlist.size()];
        int i = 0;
        for (WifiP2pDevice mdevice : playerlist) {
            Player player = new Player(mdevice.deviceName);
            player.setDevice(mdevice);
            players[i] = player;
            playerDeviceMap.put(players[i], mdevice);
            i++;
        }
        return true;
    }

    public Player[] getPlayers() {
        return players;
    }
}
