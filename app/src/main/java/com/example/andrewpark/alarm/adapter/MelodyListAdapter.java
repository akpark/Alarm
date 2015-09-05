package com.example.andrewpark.alarm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.andrewpark.alarm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by andrewpark on 8/29/15.
 */
public class MelodyListAdapter extends ArrayAdapter<String> {

    final String LOG_TAG = MelodyListAdapter.class.getSimpleName();
    int selectedPosition = 0;
    int melody_position;
    String melody_uri;


    public MelodyListAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> list) {
        super(context, resource, textViewResourceId, (List<String>) list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v==null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.melody_list_item, null);
        }

        String melody = getItem(position);
        Scanner reader = new Scanner(melody);
        String melody_title = reader.nextLine();
        melody_uri = reader.nextLine();
        TextView melody_title_textview = (TextView)v.findViewById(R.id.melody_title_txt);
        melody_title_textview.setText(melody_title);

        RadioButton radioButton = (RadioButton)v.findViewById(R.id.melody_radioButton);
        radioButton.setChecked(position == selectedPosition);
        radioButton.setTag(position);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = (Integer)v.getTag();
                notifyDataSetChanged();
            }
        });

        return v;
    }

    public int getChosenMelody() {
        return melody_position;
    }
}
