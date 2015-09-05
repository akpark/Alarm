package com.example.andrewpark.alarm.receiver;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Scanner;

/**
 * Created by andrewpark on 9/1/15.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        //make it go to a random game or no game if user doesnt want a game
//        AlarmActivity inst = AlarmActivity.instance();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        boolean vibrate = intent.getBooleanExtra("boolean",false);

        String melody = intent.getStringExtra(Intent.EXTRA_TEXT);
        Scanner reader = new Scanner(melody);
        String line1 = reader.nextLine();
        String uri = reader.nextLine();
        Uri melody_uri = Uri.parse(uri);
        Ringtone ringtone = RingtoneManager.getRingtone(context, melody_uri);
        ringtone.play();
        if (vibrate)
            vibrator.vibrate(2000);

    }

}

//retrieve the specific alarm from the list of public alarms