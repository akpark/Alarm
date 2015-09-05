package com.example.andrewpark.alarm.model;

/**
 * Created by andrewpark on 8/27/15.
 */
public class Alarm {

    boolean repeat;
    boolean vibrate;
    boolean[] days = new boolean[7];
    String melody_uri;

    public int hour;
    public int minute;

    public Alarm() {
        this.hour = -1;
        this.minute = -1;
    }

    public String getMelody_uri() {
        return melody_uri;
    }

    public void setMelody_uri(String melody_uri) {
        this.melody_uri = melody_uri;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public boolean[] getDays() {
        return days;
    }

    public void setDays(boolean[] days) {
        this.days = days;
    }
}
