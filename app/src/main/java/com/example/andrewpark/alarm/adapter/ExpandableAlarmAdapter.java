package com.example.andrewpark.alarm.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.example.andrewpark.alarm.AlarmFragment;
import com.example.andrewpark.alarm.DBHelper.AlarmDBHelper;
import com.example.andrewpark.alarm.R;
import com.example.andrewpark.alarm.model.Alarm;
import com.example.andrewpark.alarm.model.Melody;
import com.example.andrewpark.alarm.receiver.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by andrewpark on 8/27/15.
 */
public class ExpandableAlarmAdapter extends BaseExpandableListAdapter {

    final static String LOG_TAG = ExpandableAlarmAdapter.class.getSimpleName();

    private Context mContext;
    private List<Alarm> mAlarms;
    private AlarmDBHelper alarmDBHelper;
    private AlarmFragment alarmFragment;

    public ExpandableAlarmAdapter(Activity context, List<Alarm> alarms, AlarmFragment alarmFragment) {
        this.mContext = context;
        this.mAlarms = alarms;
        this.alarmFragment = alarmFragment;
    }

    @Override
    public int getGroupCount() {
        if (mAlarms != null) {
            return mAlarms.size();
        } else {
            return 0;
        }
    }

    @Override
    public void notifyDataSetChanged() {
        this.notifyDataSetChanged();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (mAlarms.isEmpty()) {
            return null;
        } else {
            return mAlarms.get(groupPosition);
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        if (mAlarms != null) {
            return mAlarms.get(groupPosition).id;
        }
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void setAlarms(List<Alarm> alarms) {
        mAlarms = alarms;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {

        alarmDBHelper = new AlarmDBHelper(mContext);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alarm_list_item, null);
        }

        final Alarm alarm = (Alarm) getGroup(groupPosition);

        Log.v(LOG_TAG, "alarm info: " + "\n alarm time: " + alarm.timeHour + " " + alarm.timeMinute + "\nalarm melody: " + alarm.alarmToneName);

        setTime(view, alarm);
        setUpDayText(view, alarm);

        ImageButton repeat_btn = (ImageButton)view.findViewById(R.id.repeat_btn);
        repeat_btn.setFocusable(false);

        //set alarm when checked
        CheckBox alarm_onoff = (CheckBox) view.findViewById(R.id.alarm_onoff);
        alarm_onoff.setChecked(alarm.isEnabled);
        alarm_onoff.setTag(Long.valueOf(alarm.id));
        alarm_onoff.setFocusable(false);
        alarm_onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarmFragment.setAlarmEnabled(alarm.id, isChecked);
            }
        });
        view.setTag(alarm.id);
        return view;
    }

    public void setTime(View view, final Alarm alarm) {

        final TextView time_txt = (TextView)view.findViewById(R.id.alarm_time);
        final TextView time_txt_ampm  = (TextView)view.findViewById(R.id.alarm_time_ampm);

        Calendar calendar = Calendar.getInstance();
        Log.v(LOG_TAG, "alarm time: " + alarm.getTimeHour() + " " + alarm.getTimeMinute());
        if (alarm.getTimeHour() == -1 || alarm.getTimeMinute() == -1) {
            alarm.setTimeHour(calendar.get(Calendar.HOUR_OF_DAY));
            alarm.setTimeMinute(calendar.get(Calendar.MINUTE));
        }

        TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                AlarmReceiver.cancelAlarms(mContext);
                alarm.setTimeHour(hourOfDay);
                alarm.setTimeMinute(minute);
                setTimeText(time_txt, time_txt_ampm, alarm.getTimeHour(), alarm.getTimeMinute());
                alarmDBHelper.updateAlarm(alarm);
                AlarmReceiver.setAlarms(mContext);
            }
        };

        final TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,timePickerListener,alarm.timeHour,alarm.timeMinute,false);
        setTimeText(time_txt, time_txt_ampm, alarm.getTimeHour(), alarm.getTimeMinute());

        time_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });
    }

    private void setTimeText(TextView time, TextView ampm, int hour, int minute) {
        Log.v(LOG_TAG, "time: " + hour + " " + minute);
        String hour_string = "";
        String minute_string = "";
        String ampm_string = "";
        if (hour >= 12) {
            ampm_string = "PM";
            if (hour == 12) {
                hour_string = "" + 12;
            } else {
                hour_string = "" + (hour - 12);
            }
        } else {
            ampm_string = "AM";
            if (hour == 0) {
                hour_string = ""+12;
            } else {
                hour_string = "" + hour;
            }
        }
        if (minute >= 10) {
            minute_string = "" + minute;
        } else {
            minute_string = "0" + minute;
        }
        time.setText(hour_string + ":" + minute_string + " ");
        ampm.setText(ampm_string);
    }

    private void setUpDayText(View view, Alarm alarm) {
        TextView day_text = (TextView) view.findViewById(R.id.alarm_day_text);
        Calendar calendar = Calendar.getInstance();
        Log.v(LOG_TAG, "number value for calendar day" + calendar.get(Calendar.DAY_OF_WEEK));
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

        LayoutInflater inflater = null;
        final Alarm alarm = (Alarm)getGroup(groupPosition);

        if (view == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alarm_list_item_expanded, null);
        }

        final TextView sound_list_textView = (TextView) view.findViewById(R.id.sound_list);
//        sound_list_textView.setText();
        sound_list_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMelodyDialog(sound_list_textView, alarm);
            }
        });

        setUpRepeatedDays(view, alarm);

        return view;
    }

    private void setUpRepeatedDays(View view, final Alarm alarm) {

        final ToggleButton sun_btn = (ToggleButton)view.findViewById(R.id.SUN_btn);
        final ToggleButton mon_btn = (ToggleButton)view.findViewById(R.id.MON_btn);
        final ToggleButton tue_btn = (ToggleButton)view.findViewById(R.id.TUE_btn);
        final ToggleButton wed_btn = (ToggleButton)view.findViewById(R.id.WED_btn);
        final ToggleButton thu_btn = (ToggleButton)view.findViewById(R.id.THU_btn);
        final ToggleButton fri_btn = (ToggleButton)view.findViewById(R.id.FRI_btn);
        final ToggleButton sat_btn = (ToggleButton)view.findViewById(R.id.SAT_btn);
        ToggleButton[] daysOfTheWeek = {sun_btn, mon_btn, tue_btn, wed_btn, thu_btn, fri_btn, sat_btn};

        for (int index = 0; index < 7; index++) {
            ToggleButton day = daysOfTheWeek[index];
            day.setChecked(alarm.getRepeatingDay(index));
            final int finalIndex = index;
            day.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    AlarmReceiver.cancelAlarms(mContext);
                    switch (finalIndex) {
                        case 0:
                            alarm.setRepeatingDay(alarm.SUNDAY, isChecked);
                            break;
                        case 1:
                            alarm.setRepeatingDay(alarm.MONDAY, isChecked);
                            break;
                        case 2:
                            alarm.setRepeatingDay(alarm.TUESDAY, isChecked);
                            break;
                        case 3:
                            alarm.setRepeatingDay(alarm.WEDNESDAY, isChecked);
                            break;
                        case 4:
                            alarm.setRepeatingDay(alarm.THURSDAY, isChecked);
                            break;
                        case 5:
                            alarm.setRepeatingDay(alarm.FRIDAY, isChecked);
                            break;
                        case 6:
                            alarm.setRepeatingDay(alarm.SATURDAY, isChecked);
                            break;
                    }
                    alarmDBHelper.updateAlarm(alarm);
                    AlarmReceiver.setAlarms(mContext);
                }
            });
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void showMelodyDialog(final TextView sound_list_textView, final Alarm alarm) {

        //consider DB for melodies
        final List<Melody> melodyList;
        melodyList = new ArrayList<Melody>();
        RingtoneManager ringtoneManager = new RingtoneManager(mContext);
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarm_cursor = ringtoneManager.getCursor();

        while (alarm_cursor.moveToNext()) {
            int currentPosition = alarm_cursor.getPosition();
            String alarm_title = ringtoneManager.getRingtone(currentPosition).getTitle(mContext);
            Uri alarm_uri = ringtoneManager.getRingtoneUri(currentPosition);
            Melody melody = new Melody(alarm_title, alarm_uri);
            melodyList.add(melody);
        }

        //set up the melody alert dialog to show list of melodies from default + 'saved'
        LayoutInflater vi = LayoutInflater.from(mContext);
        View view = vi.inflate(R.layout.melody_dialog, null);
        ListView melody_listview = (ListView)view.findViewById(R.id.melody_listView);

        final MelodyListAdapter melodyListAdapter = new MelodyListAdapter(mContext, R.layout.melody_list_item, R.id.melody_listView, melodyList);
        melody_listview.setAdapter(melodyListAdapter);
        melody_listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        final Ringtone[] r = new Ringtone[1];
        melody_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (r[0] != null)
                    r[0].stop();

                //get selected melody
                Melody melody = melodyListAdapter.getItem(position);
                Uri melody_uri = melody.getMelody_uri();

                //get ringtone and play melody
                r[0] = RingtoneManager.getRingtone(mContext,melody_uri);
                r[0].play();
            }
        });

        //get index of song chosen
        AlertDialog.Builder builderSong = new AlertDialog.Builder(mContext);
        builderSong.setTitle("Please Choose a Melody");
        builderSong.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (r[0] != null)
                    r[0].stop();
                sound_list_textView.setText(melodyList.get(melodyListAdapter.getSelectedPosition()).getMelody_name());
                alarm.setAlarmTone(melodyList.get(melodyListAdapter.getSelectedPosition()).getMelody_uri());
                alarm.setAlarmToneName(melodyList.get(melodyListAdapter.getSelectedPosition()).getMelody_name());
                melodyList.get(melodyListAdapter.getSelectedPosition()).setIsSelected(true);
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (r[0] != null)
                    r[0].stop();
                dialog.dismiss();
            }
        });

        builderSong.setView(view);
        builderSong.show();
    }

}
