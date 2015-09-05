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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.andrewpark.alarm.R;
import com.example.andrewpark.alarm.model.Alarm;
import com.example.andrewpark.alarm.model.Melody;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by andrewpark on 8/27/15.
 */
public class ExpandableAlarmAdapter extends BaseExpandableListAdapter {

    final static String LOG_TAG = ExpandableAlarmAdapter.class.getSimpleName();

    private Activity context;
    private List<Alarm> alarms;
    boolean repeat;

    boolean[] days = new boolean[7];

    public ExpandableAlarmAdapter(Activity context, List<Alarm> alarms) {
        this.context = context;
        this.alarms = alarms;
    }

    @Override
    public int getGroupCount() {
        if (!alarms.isEmpty())
            return alarms.size();
        else {
            return 1;
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
        if (alarms.isEmpty()) {
            return null;
        } else {
            return alarms.get(groupPosition);
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    //set up viewholder for group view
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.alarm_list_item, null);
        }

        final Alarm alarm = (Alarm)getGroup(groupPosition);

        TextView day_text = (TextView)v.findViewById(R.id.alarm_day);
        setCurrentTimeOnView(v, alarm);

        final ImageButton repeat_btn = (ImageButton)v.findViewById(R.id.repeat_btn);
        repeat_btn.setFocusable(false);
        repeat = true;

        repeat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeat) {
                    repeat_btn.setAlpha((float) 0.25);
                    repeat = false;
//                    alarm.setRepeat(false);
                } else {
                    repeat_btn.setAlpha((float) 1);
                    repeat = true;
//                    alarm.setRepeat(true);
                }
            }
        });

//        final CheckBox alarm_onoff = (CheckBox) v.findViewById(R.id.alarm_onoff);
//        final View finalV = v;
//        alarm_onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                setUpDay(finalV);
//            }
//        });

//        setUpAlarm(v, alarm);

        Log.v(LOG_TAG, "inside get group view");

        return v;
    }

    private void setCurrentTimeOnView(View v, final Alarm alarm) {
        final TextView time_txt = (TextView) v.findViewById(R.id.alarm_time);
        final TextView time_txt_ampm = (TextView) v.findViewById(R.id.alarm_time_ampm);

        final Calendar calendar = Calendar.getInstance();
        if (alarm.getTimeMinute() == -1 || alarm.getTimeHour() == -1) {
            alarm.setTimeHour(calendar.get(Calendar.HOUR_OF_DAY));
            alarm.setTimeMinute(calendar.get(Calendar.MINUTE));
            calendar.get(Calendar.AM_PM);
        }

        TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                alarm.setTimeHour(selectedHour);
                alarm.setTimeMinute(selectedMinute);
                setTimeText(time_txt, time_txt_ampm, alarm.getTimeHour(), alarm.getTimeMinute());
            }
        };

        final TimePickerDialog dialog = new TimePickerDialog(context, timePickerListener, alarm.getTimeHour(), alarm.getTimeMinute(), false);

        setTimeText(time_txt, time_txt_ampm, alarm.getTimeHour(), alarm.getTimeMinute());
        time_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    //set up time
    private void setTimeText(TextView time, TextView ampm, int hour, int minute) {
        String hour_string = null;
        String minute_string;
        String zone;
        if (hour > 12) {
            zone = "PM";
            hour_string = "" + (hour-12);
        } else {
            hour_string = "" + hour;
            zone = "AM";
        }
        if (minute >= 10) {
            minute_string = "" + minute;
        } else {
            minute_string = "0" + minute;
        }
        time.setText(hour_string + ":" + minute_string + " ");
        ampm.setText(zone);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = null;

        if (v == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.alarm_list_item_expanded, null);
        }

        TextView sound_list = (TextView) v.findViewById(R.id.sound_list);
        sound_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMelodyDialog();
            }
        });

//        setUpDays(v);

        Log.v(LOG_TAG,"inside child group");

        return v;
    }

//    public void setUpDay(View v) {
//
//        ToggleButton sun = (ToggleButton)v.findViewById(R.id.SUN_btn);
//        ToggleButton mon = (ToggleButton)v.findViewById(R.id.MON_btn);
//        ToggleButton tue = (ToggleButton)v.findViewById(R.id.TUE_btn);
//        ToggleButton wed = (ToggleButton)v.findViewById(R.id.WED_btn);
//        ToggleButton thu = (ToggleButton)v.findViewById(R.id.THU_btn);
//        ToggleButton fri = (ToggleButton)v.findViewById(R.id.FRI_btn);
//        ToggleButton sat = (ToggleButton)v.findViewById(R.id.SAT_btn);
//
//        if (sun.isChecked())
//            days[0]=true;
//        if (!sun.isChecked())
//            days[0]=false;
//        if (mon.isChecked())
//            days[1]=true;
//        if (!mon.isChecked())
//            days[1]=false;
//        if (tue.isChecked())
//            days[2]=true;
//        if (!tue.isChecked())
//            days[2]=false;
//        if (wed.isChecked())
//            days[3]=true;
//        if (!wed.isChecked())
//            days[3]=false;
//        if (thu.isChecked())
//            days[4]=true;
//        if (thu.isChecked())
//            days[4]=false;
//        if (fri.isChecked())
//            days[5]=true;
//        if (!fri.isChecked())
//            days[5]=false;
//        if (sat.isChecked())
//            days[6]=true;
//        if (!sat.isChecked())
//            days[6]=false;
//    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void showMelodyDialog() {

        ArrayList<Melody> melodyList = new ArrayList<Melody>();

        //retrieve ringtones in phone through intent
        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarm_cursor = ringtoneManager.getCursor();

        while (alarm_cursor.moveToNext()) {
            int currentPosition = alarm_cursor.getPosition();
            String alarm_title = ringtoneManager.getRingtone(currentPosition).getTitle(context);
            Uri alarm_uri = ringtoneManager.getRingtoneUri(currentPosition);
            Melody melody = new Melody(alarm_title, alarm_uri);
            melodyList.add(melody);
        }

        //setting up the melody alert dialog
        LayoutInflater vi = LayoutInflater.from(context);
        View view = vi.inflate(R.layout.melody_dialog, null);
        ListView melody_listview = (ListView)view.findViewById(R.id.melody_listView);

        final MelodyListAdapter melodyListAdapter = new MelodyListAdapter(context, R.layout.melody_list_item, R.id.melody_listView, melodyList);
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

                Log.v(LOG_TAG,"melody name: " + melody.getMelody_name());
                Log.v(LOG_TAG,"melody uri: " + melody_uri.toString());

                //get ringtone and play melody
                r[0] = RingtoneManager.getRingtone(context,melody_uri);
                r[0].play();
            }
        });

        //get index of song chosen
        AlertDialog.Builder builderSong = new AlertDialog.Builder(context);
        builderSong.setTitle("Please Choose a Melody");
        builderSong.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSong.setView(view);
        builderSong.show();

        int index = melodyListAdapter.selectedPosition;
//        String melody_text = melodyListAdapter.getItem(index);
        //notify with melody
//        if (melody_text != null) {
//            Intent intent = new Intent(context,AlarmReceiver.class).putExtra(Intent.EXTRA_TEXT,melody_text);
//        }
    }

    private void showGameDialog(){

    }

//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private void setAlarm(Alarm alarm) {
//        String melody = alarm.();
//        boolean vibrate = alarm.isVibrate();
//
//        //get alarm manager
//        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmReceiver.class).putExtra(Intent.EXTRA_TEXT,melody).putExtra("vibrate",vibrate);
//        PendingIntent pi = PendingIntent.getBroadcast(context,0,intent,0);
//
//        //set up alarm
//        int hour = alarm.hour;
//        int minute = alarm.minute;
//
//        Calendar setup = Calendar.getInstance();
//        setup.set(Calendar.HOUR,hour);
//        setup.set(Calendar.MINUTE, minute);
//
//        for (int i=0; i<7; i++) {
//            switch(i) {
//                case 0:
//                    setup.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
//                    break;
//                case 1:
//                    setup.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
//                    break;
//                case 2:
//                    setup.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
//                    break;
//                case 3:
//                    setup.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
//                    break;
//                case 4:
//                    setup.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
//                    break;
//                case 5:
//                    setup.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
//                    break;
//                case 6:
//                    setup.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
//                    break;
//            }
//        }
//
//        if (alarm.isRepeat()) {
//            //find days it repeats, the 1000*5 is for how often to repeat
//            am.setRepeating(AlarmManager.RTC_WAKEUP,setup.getTimeInMillis(), 1000*5, pi);
//        } else {
//            am.setExact(AlarmManager.RTC_WAKEUP, setup.getTimeInMillis(), pi);
//        }
//    }

//    private void setUpAlarm(View v, final Alarm alarm) {
//
//        final AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        final PendingIntent pendingIntent = null;
//
//        final CheckBox checkBox = (CheckBox)v.findViewById(R.id.alarm_onoff);
//        checkBox.setFocusable(false);
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.set(Calendar.HOUR_OF_DAY, alarm.hour);
//                    calendar.set(Calendar.MINUTE, alarm.minute);
//                    for (int i=0; i<7; i++) {
//
//                    }
//                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
//                    Intent intent = new Intent(context, AlarmReceiver.class);
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
//                    alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
//                } else {
//                    alarmManager.cancel(pendingIntent);
//                }
//            }
//        });
//    }

}
