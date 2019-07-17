package com.dreamworld.smart.diary;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationChannelSet extends Application {

    public static final String CHANNEL_1_ID="channel1";
    public static final String CHANNEL_2_ID="channel2";
    public static final String CHANNEL_3_ID="channel3";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChennel();


    }

    private void createNotificationChennel() {

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel1=new NotificationChannel(
                    CHANNEL_1_ID,
                    "Update",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Show The New Update");

            NotificationChannel channel2=new NotificationChannel(
                    CHANNEL_2_ID,
                    "Reminder",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("Show The Reminder");
            NotificationChannel channel3=new NotificationChannel(
                    CHANNEL_3_ID,
                    "Event",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel3.setDescription("Show The Event");

            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);
        }

    }
}

