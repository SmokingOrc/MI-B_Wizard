package com.example.mi_b_wizard;

import android.annotation.SuppressLint;

import com.example.mi_b_wizard.Data.Player;
import com.example.mi_b_wizard.Network.MessageHandler;

public class Instance {
    @SuppressLint("StaticFieldLeak")
    public static MessageHandler MH = null;
    public static Player player = null;
    public static WaitingLobby waitingLobby = null;
    public static Notifications notifications = null;
    public static GameActivity gameActivity = null;


    public static void setMh(MessageHandler mh){
        if(MH == null){
        Instance.MH = mh;}
    }

    public static void setPlayer(Player player) {
        if(player == null){
        Instance.player = player;}
    }

    public static void setWaitingLobby(WaitingLobby waitingLobby) {
        if(waitingLobby == null){
        Instance.waitingLobby = waitingLobby;}
    }

    public static void setNotifications(Notifications notifications) {
        if (notifications == null) {
            Instance.notifications = notifications;
        }
    }

    public static void setGameActivity(GameActivity gameActivity) {
        if (gameActivity == null) {
            Instance.gameActivity = gameActivity;
        }
    }

}
