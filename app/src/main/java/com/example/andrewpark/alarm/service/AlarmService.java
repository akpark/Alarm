package com.example.andrewpark.alarm.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.andrewpark.alarm.AlarmScreen;
import com.example.andrewpark.alarm.receiver.AlarmReceiver;

/**
 * Created by andrewpark on 9/5/15.
 */
public class AlarmService extends Service {

    private final static String LOG_TAG = AlarmService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v(LOG_TAG, "onStartCommand");

        Intent alarmIntent = new Intent(this, AlarmScreen.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtras(intent);
        getApplication().startActivity(alarmIntent);

        AlarmReceiver.setAlarms(this);
        return super.onStartCommand(intent, flags, startId);
    }
}
