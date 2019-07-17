package com.dreamworld.smart.diary;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static com.dreamworld.smart.diary.NotificationChannelSet.CHANNEL_1_ID;

public class ReminderActivity extends AppCompatActivity {

    Shared sharePref;

    private  Toolbar toolbar;
    private TextView cancleBtn;
    private TextView setBtn;
    private EditText task;
    private TimePicker timePicker;
    private int notificationid=2;
    private NotificationManagerCompat notificationManager;
    private String message;
    private AlarmManager alarm;
    PendingIntent alarmIntent;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharePref=new Shared(this);

        if(sharePref.lodeNightModeState()==true)
        {
            setTheme(R.style.darktheme);
        }else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        cancleBtn=findViewById(R.id.cancleButton);
        setBtn=findViewById(R.id.setButton);
        task=findViewById(R.id.editTask);
        timePicker=findViewById(R.id.timeSet);

        notificationManager=NotificationManagerCompat.from(this);

        toolbar=(Toolbar)findViewById(R.id.toolbar_setting);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Set Reminder");


       intent=new Intent(ReminderActivity.this,AlarmReceiver.class);

        intent.putExtra("notificationid",notificationid);
        intent.putExtra("todo",message);


        alarmIntent=PendingIntent.getBroadcast(ReminderActivity.this,1,
                intent,PendingIntent.FLAG_CANCEL_CURRENT);

      alarm=(AlarmManager)getSystemService(ALARM_SERVICE);


        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour=timePicker.getCurrentHour();
                int minute=timePicker.getCurrentMinute();
                message="Hellow World";

                Calendar startTime= Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY,hour);
                startTime.set(Calendar.MINUTE,minute);
                startTime.set(Calendar.SECOND,0);

                Toast.makeText(getApplicationContext()," "+message,Toast.LENGTH_SHORT).show();

               setNotificationChannel();


                long alarmStartTime=startTime.getTimeInMillis();

                alarm.set(AlarmManager.RTC_WAKEUP,alarmStartTime,alarmIntent);
               // Toast.makeText(getApplicationContext(),"Done!",Toast.LENGTH_SHORT).show();

               Intent intent=new Intent(ReminderActivity.this,HomeActivity.class);
                startActivity(intent);

            }
        });

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm.cancel(alarmIntent);
                Toast.makeText(getApplicationContext(),"Cancled",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ReminderActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(),SattingActivity.class));
        return true;
    }

    private void setNotificationChannel() {
        Notification notification=new NotificationCompat.Builder(this,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Channel 1")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1,notification);
    }
}
