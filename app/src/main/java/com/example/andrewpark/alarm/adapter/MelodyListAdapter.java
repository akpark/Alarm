package com.example.andrewpark.alarm.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.andrewpark.alarm.R;
import com.example.andrewpark.alarm.model.Melody;

import java.util.ArrayList;

/**
 * Created by andrewpark on 8/29/15.
 */
public class MelodyListAdapter extends ArrayAdapter<Melody> {

    final String LOG_TAG = MelodyListAdapter.class.getSimpleName();
    int selectedPosition = 0;
    int melody_position;
    private Uri melody_uri;
    private String melody_name;
    private ArrayList<Melody> melody_list;


    public MelodyListAdapter(Context context, int resource, int textViewResourceId, ArrayList<Melody> list) {
        super(context, resource, textViewResourceId, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v==null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.melody_list_item, null);
        }

        Melody melody = getItem(position);
        melody_uri = melody.getMelody_uri();
        melody_name = melody.getMelody_name();

        TextView melody_title_textview = (TextView)v.findViewById(R.id.melody_title_txt);
        melody_title_textview.setText(melody_name);

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
