package com.example.andrewpark.alarm.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.andrewpark.alarm.model.Alarm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrewpark on 9/5/15.
 */
public class AlarmDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "alarm.db";

    private static final String SQL_CREATE_ALARM = "CREATE TABLE " + AlarmContract.AlarmModel.TABLE_NAME + " (" +
            AlarmContract.AlarmModel._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            AlarmContract.AlarmModel.COLUMN_NAME_ALARM_NAME + " TEXT," +
            AlarmContract.AlarmModel.COLUMN_NAME_ALARM_TIME_HOUR + " INTEGER," +
            AlarmContract.AlarmModel.COLUMN_NAME_ALARM_TIME_MINUTE + " INTEGER," +
            AlarmContract.AlarmModel.COLUMN_NAME_ALARM_REPEAT_DAYS + " TEXT," +
            AlarmContract.AlarmModel.COLUMN_NAME_ALARM_REPEAT_WEEKLY + " BOOLEAN," +
            AlarmContract.AlarmModel.COLUMN_NAME_ALARM_TONE + " TEXT," +
//            AlarmContract.AlarmModel.COLUMN_NAME_ALARM_TONE_NAME + " TEXT," +
            AlarmContract.AlarmModel.COLUMN_NAME_ALARM_ENABLED + " BOOLEAN" +
            " )";

    private static final String SQL_DELETE_ALARM = "DROP IF TABLE EXISTS " + AlarmContract.AlarmModel.TABLE_NAME;

    public AlarmDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ALARM);
        onCreate(db);
    }

    private Alarm populateModel(Cursor c) {
        Alarm model = new Alarm();
        model.id = c.getLong(c.getColumnIndex(AlarmContract.AlarmModel._ID));
        model.label = c.getString(c.getColumnIndex(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_NAME));
        model.timeHour = c.getInt(c.getColumnIndex(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_TIME_HOUR));
        model.timeMinute = c.getInt(c.getColumnIndex(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_TIME_MINUTE));
        model.repeatWeekly = c.getInt(c.getColumnIndex(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_REPEAT_WEEKLY)) == 0 ? false : true;
        model.alarmTone = c.getString(c.getColumnIndex(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_TONE)) != "" ? Uri.parse(c.getString(c.getColumnIndex(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_TONE))) : null;
//        model.alarmToneName = c.getString(c.getColumnIndex(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_TONE_NAME));
        model.isEnabled = c.getInt(c.getColumnIndex(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_ENABLED)) == 0 ? false : true;

        String[] repeatingDays = c.getString(c.getColumnIndex(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_REPEAT_DAYS)).split(",");
        for (int i = 0; i < repeatingDays.length; ++i) {
            model.setRepeatingDay(i, repeatingDays[i].equals("false") ? false : true);
        }

        return model;
    }

    private ContentValues populateContent(Alarm model) {
        ContentValues values = new ContentValues();
        values.put(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_NAME, model.label);
        values.put(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_TIME_HOUR, model.timeHour);
        values.put(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_TIME_MINUTE, model.timeMinute);
        values.put(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_REPEAT_WEEKLY, model.repeatWeekly);
        values.put(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_TONE, model.alarmTone != null ? model.alarmTone.toString() : "");
//        values.put(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_TONE_NAME, model.alarmToneName);
        values.put(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_ENABLED, model.isEnabled);

        String repeatingDays = "";
        for (int i = 0; i < 7; ++i) {
            repeatingDays += model.getRepeatingDay(i) + ",";
        }
        values.put(AlarmContract.AlarmModel.COLUMN_NAME_ALARM_REPEAT_DAYS, repeatingDays);

        return values;
    }

    public long createAlarm(Alarm model) {
        ContentValues values = populateContent(model);
        return getWritableDatabase().insert(AlarmContract.AlarmModel.TABLE_NAME, null, values);
    }

    public long updateAlarm(Alarm model) {
        ContentValues values = populateContent(model);
        return getWritableDatabase().update(AlarmContract.AlarmModel.TABLE_NAME, values, AlarmContract.AlarmModel._ID + " = ?", new String[] { String.valueOf(model.id) });
    }

    public Alarm getAlarm(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + AlarmContract.AlarmModel.TABLE_NAME + " WHERE " + AlarmContract.AlarmModel._ID + " = " + id;

        Cursor c = db.rawQuery(select, null);

        if (c.moveToNext()) {
            return populateModel(c);
        }
        return null;
    }

    public List<Alarm> getAlarms() {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + AlarmContract.AlarmModel.TABLE_NAME;

        Cursor c = db.rawQuery(select, null);

        List<Alarm> alarmList = new ArrayList<Alarm>();

        while (c.moveToNext()) {
            alarmList.add(populateModel(c));
        }

        if (!alarmList.isEmpty()) {
            return alarmList;
        }

        return null;
    }

    public int deleteAlarm(long id) {
        return getWritableDatabase().delete(AlarmContract.AlarmModel.TABLE_NAME, AlarmContract.AlarmModel._ID + " = ?", new String[] { String.valueOf(id) });
    }
}
