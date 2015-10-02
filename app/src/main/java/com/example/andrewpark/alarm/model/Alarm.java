package com.example.andrewpark.alarm.model;

import android.content.Context;
import android.net.Uri;

/**
 * Created by andrewpark on 8/27/15.
 */
public class Alarm {

    private final static String LOG_TAG = Alarm.class.getSimpleName();
    Context context;

    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    public long id = -1;
    public int timeHour;
    public int timeMinute;
    public boolean repeatingDays[];
    public boolean repeatWeekly;
    public Uri alarmTone;
    public String alarmToneName;
    public String label;
    public boolean isEnabled;
    public boolean isVibrate = false;
    public boolean isSound;

    public Alarm() {
        this.timeHour = -1;
        this.timeMinute = -1;
        repeatingDays = new boolean[7];
    }

    public void setRepeatingDay(int dayOfWeek, boolean value) {
        repeatingDays[dayOfWeek] = value;
    }

    public boolean getRepeatingDay(int dayOfWeek) {
        return repeatingDays[dayOfWeek];
    }

    public void setTimeHour(int timeHour) {
        this.timeHour = timeHour;
    }

    public void setTimeMinute(int timeMinute) {
        this.timeMinute = timeMinute;
    }

    public int getTimeHour() {
        return timeHour;
    }

    public int getTimeMinute() {
        return timeMinute;
    }

    public void setAlarmTone(Uri alarmTone) {
        this.alarmTone = alarmTone;
    }

    public void setAlarmToneName(String alarmName) {
        this.alarmToneName = alarmName;
    }
}
