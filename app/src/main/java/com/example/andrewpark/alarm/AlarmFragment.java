package com.example.andrewpark.alarm;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TimePicker;

import com.example.andrewpark.alarm.DBHelper.AlarmDBHelper;
import com.example.andrewpark.alarm.adapter.ExpandableAlarmAdapter;
import com.example.andrewpark.alarm.model.Alarm;
import com.example.andrewpark.alarm.receiver.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by andrewpark on 8/26/15.
 */
public class AlarmFragment extends Fragment {

    static final String LOG_TAG = AlarmFragment.class.getSimpleName();

    private AlarmDBHelper alarmDBHelper;
    private ExpandableAlarmAdapter mAlarmAdapter;

    private ExpandableListView alarm_listView;
    private Button add_btn;

    public AlarmFragment() {
    }

//    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        alarmDBHelper = new AlarmDBHelper(getActivity());

        mAlarmAdapter = new ExpandableAlarmAdapter(getActivity(), alarmDBHelper.getAlarms(), AlarmFragment.this);

        alarm_listView = (ExpandableListView) view.findViewById(R.id.alarm_listView);
        alarm_listView.setAdapter(mAlarmAdapter);
        alarm_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long alarm_id = (long) view.getTag();
                Log.v(LOG_TAG ,"alarm_id: " + alarm_id);
                deleteAlarm(alarm_id);
                return true;
            }
        });

        setAddAlarmListener(view);
        return view;
    }

    public void setAddAlarmListener(View view) {

        final Alarm alarm = new Alarm();

        Calendar mcurrentTime = Calendar.getInstance();
        int currentHour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener mTimePickerListener = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                alarm.setTimeHour(selectedHour);
                alarm.setTimeMinute(selectedMinute);
                AlarmReceiver.cancelAlarms(getActivity());
                alarmDBHelper.createAlarm(alarm);
                mAlarmAdapter.setAlarms(alarmDBHelper.getAlarms());
                AlarmReceiver.setAlarms(getActivity());
                mAlarmAdapter.notifyDataSetChanged();
//                ((BaseAdapter)alarm_listView.getAdapter()).notifyDataSetChanged();
//            }
//        };

        final TimePickerDialog mAlarmTimePicker = new TimePickerDialog(getActivity(), mTimePickerListener, currentHour, currentMinute, false);
        mAlarmTimePicker.setTitle("Select Time");

        add_btn = (Button) view.findViewById(R.id.add_btn_alarm);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlarmTimePicker.show();
            }
        });
    }

    public void setAlarmEnabled(long id, boolean isEnabled) {
        Log.v(LOG_TAG,"isEnabled value: " + isEnabled);
        AlarmReceiver.cancelAlarms(getActivity());

        Alarm model = alarmDBHelper.getAlarm(id);
        model.isEnabled = isEnabled;
        alarmDBHelper.updateAlarm(model);

        AlarmReceiver.setAlarms(getActivity());
    }

    public void deleteAlarm(long id) {
        final long alarmId = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please confirm")
                .setTitle("Delete set?")
                .setCancelable(true)
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlarmReceiver.cancelAlarms(getActivity());
                        alarmDBHelper.deleteAlarm(alarmId);
                        mAlarmAdapter.setAlarms(alarmDBHelper.getAlarms());
                        ((BaseAdapter)alarm_listView.getAdapter()).notifyDataSetChanged();
                        AlarmReceiver.setAlarms(getActivity());
                    }
                }).show();
    }


}