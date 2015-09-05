package com.example.andrewpark.alarm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andrewpark.alarm.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by andrewpark on 8/26/15.
 */
public class ClockAdapter extends ArrayAdapter<String> {

    public ClockAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v==null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.main_clock_list_item, null);
        }

        DateFormat df = new SimpleDateFormat("EEE, MMM d");
        String date = df.format(Calendar.getInstance().getTime());
        TextView date_txtView = (TextView)v.findViewById(R.id.clock_date_text);
        date_txtView.setText(date);

        return v;

    }
}