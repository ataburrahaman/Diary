package com.dreamworld.smart.diary;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static com.dreamworld.smart.diary.NotificationChannelSet.CHANNEL_1_ID;
import static com.dreamworld.smart.diary.NotificationChannelSet.CHANNEL_2_ID;

public class AlarmReceiver extends BroadcastReceiver {

     private NotificationManagerCompat notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        MediaPlayer mediaPlayer=MediaPlayer.create(context,Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.start();


        NotificationManager myNotification=(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);

        int notificationId=intent.getIntExtra("notificationid",0);
        String message=intent.getStringExtra("todo");


        notificationManager=NotificationManagerCompat.from(context);



        Intent mainIntent= new Intent(context,ReminderActivity.class);
        PendingIntent contentIntent= PendingIntent.getActivity(context,1,mainIntent,0);


        Notification notification=new NotificationCompat.Builder(context,CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Channel")
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1,notification);
        Notification notification2=new NotificationCompat.Builder(context,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Message")
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(notificationId,notification2);





        Notification.Builder builder=new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Its Time")
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_MAX)
        .setDefaults(Notification.DEFAULT_ALL);

        myNotification.notify(notificationId,builder.build());
    }

}
