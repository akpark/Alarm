package com.example.andrewpark.alarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by andrewpark on 8/26/15.
 */
public class ClockFragment extends android.support.v4.app.Fragment {

    private static final String LOG_TAG = ClockFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container, false);

        DateFormat df = new SimpleDateFormat("EEE, MMM d");
        String date = df.format(Calendar.getInstance().getTime());
        TextView date_txtView = (TextView)view.findViewById(R.id.clock_date_text);
        date_txtView.setText(date);

        return view;
    }
}
