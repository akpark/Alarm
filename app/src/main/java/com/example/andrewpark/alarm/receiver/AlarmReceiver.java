package com.example.andrewpark.alarm.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.andrewpark.alarm.DBHelper.AlarmDBHelper;
import com.example.andrewpark.alarm.model.Alarm;
import com.example.andrewpark.alarm.service.AlarmService;

import java.util.Calendar;
import java.util.List;

/**
 * Created by andrewpark on 9/1/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = AlarmReceiver.class.getSimpleName();

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TIME_HOUR = "timeHour";
    public static final String TIME_MINUTE = "timeMinute";
    public static final String TONE = "alarmTone";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.v(LOG_TAG,"onReceive");
        setAlarms(context);
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show();
    }

    public static void setAlarms(Context context) {
        cancelAlarms(context);

        AlarmDBHelper dbHelper = new AlarmDBHelper(context);

        List<Alarm> alarms = dbHelper.getAlarms();

        for (Alarm alarm : alarms) {

            PendingIntent pendingIntent = createPendingIntent(context,alarm);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarm.timeHour);
            calendar.set(Calendar.MINUTE, alarm.timeMinute);
            calendar.set(Calendar.SECOND, 00);

            //Find next time to set
            final int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            final int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            final int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);
            boolean alarmSet = false;

            //First check if later in week
            for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
                if (alarm.getRepeatingDay(dayOfWeek - 1) && dayOfWeek >= nowDay &&
                        !(dayOfWeek == nowDay && alarm.timeHour < nowHour) &&
                        !(dayOfWeek == nowDay && alarm.timeHour == nowHour && alarm.timeMinute < nowMinute)) {
                    Log.v(LOG_TAG, "alarm info.: " + alarm.getRepeatingDay(dayOfWeek-1) + " alarm time: " + alarm.timeHour + " " + alarm.timeMinute);
                    calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                    setAlarm(context, calendar, pendingIntent);
                    alarmSet = true;
                    break;
                }
            }
            //Else check if it's earlier in the week
            if (!alarmSet) {
                Log.v(LOG_TAG,"!alarmset");
                for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
                    if (alarm.getRepeatingDay(dayOfWeek - 1) && dayOfWeek <= nowDay && alarm.repeatWeekly) {
                        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                        calendar.add(Calendar.WEEK_OF_YEAR, 1);
                        setAlarm(context, calendar, pendingIntent);
                        alarmSet = true;
                        break;
                    }
                }
            }
            Log.v(LOG_TAG, "alarm set? : " + alarmSet);
        }
    }

    public static void setAlarm(Context context, Calendar calendar, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.v(LOG_TAG, "calendar time: " + calendar.getTimeInMillis());
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public static void cancelAlarms(Context context) {
        AlarmDBHelper dbHelper = new AlarmDBHelper(context);

        List<Alarm> alarms = dbHelper.getAlarms();

        if (alarms != null) {
            for (Alarm alarm : alarms) {
                if (alarm.isEnabled) {
                    PendingIntent pendingIntent = createPendingIntent(context,alarm);

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                }
            }
        }
    }

    private static PendingIntent createPendingIntent(Context context, Alarm model) {
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra(ID, model.id);
        intent.putExtra(NAME, model.label);
        intent.putExtra(TIME_HOUR, model.timeHour);
        intent.putExtra(TIME_MINUTE, model.timeMinute);
        intent.putExtra(TONE, model.alarmTone.toString());

        return PendingIntent.getService(context, (int) model.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}