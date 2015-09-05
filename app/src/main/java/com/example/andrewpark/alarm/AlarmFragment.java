package com.example.andrewpark.alarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.andrewpark.alarm.adapter.ExpandableAlarmAdapter;
import com.example.andrewpark.alarm.model.Alarm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrewpark on 8/26/15.
 */
public class AlarmFragment extends Fragment {

    static final String LOG_TAG = AlarmFragment.class.getSimpleName();

    ExpandableListView alarm_listView;
    ExpandableListAdapter mAlarmAdapter;
    Button add_btn;
    Button record;
    public List<Alarm> alarms;
    SharedPreferences sharedPreferences;

    public AlarmFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String value = sharedPreferences.getString("alarm_list", null);

        if (value != null) {
            Type type = new TypeToken<List<Alarm>>() {
            }.getType();
            alarms = (List<Alarm>) gson.fromJson(value, type);
        } else {
            alarms = new ArrayList<Alarm>();
        }

        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        mAlarmAdapter = new ExpandableAlarmAdapter(getActivity(), alarms);

        alarm_listView = (ExpandableListView) view.findViewById(R.id.alarm_listView);
        alarm_listView.setAdapter(mAlarmAdapter);
        alarm_listView.setClickable(true);
        alarms.add(new Alarm());

        add_btn = (Button) view.findViewById(R.id.add_btn_alarm);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add alarm
                alarms.add(new Alarm());
                ((BaseAdapter) alarm_listView.getAdapter()).notifyDataSetChanged();
            }
        });

        alarm_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //alert dialogue
                AlertDialog.Builder delete_dialog = new AlertDialog.Builder(getActivity());
                delete_dialog.setTitle("Delete?");
                delete_dialog.setCancelable(true);
                delete_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alarms.remove(position);
                        ((BaseAdapter) alarm_listView.getAdapter()).notifyDataSetChanged();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = delete_dialog.create();
                alertDialog.show();
                return true;
            }
        });

        record = (Button) view.findViewById(R.id.record_btn);

        Log.v(LOG_TAG,"alarms: " + alarms.toString());

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor e = sharedPreferences.edit();
        Gson gson = new Gson();
        String value = gson.toJson(alarms);
        e.putString("alarm_list", value);
        e.commit();
    }
}