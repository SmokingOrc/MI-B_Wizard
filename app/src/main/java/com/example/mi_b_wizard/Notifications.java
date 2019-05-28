package com.example.mi_b_wizard;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


public class Notifications {
    protected Context context;
    public Notifications(Context context) {
        this.context = context;
    }


    public void showMsg(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }


    public void showNotification(String title, String message) {
        if (context != null) {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                    .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                    .setContentTitle(title) // title for notification
                    .setContentText(message)// message for notification
                    .setAutoCancel(true); // clear notification after click
            Intent intent = new Intent(context, JoinGameActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pi);
            mNotificationManager.notify(0, mBuilder.build());
        } else{
        System.out.println("context null");
    }
}}
