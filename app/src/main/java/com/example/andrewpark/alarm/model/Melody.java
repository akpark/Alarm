package com.example.andrewpark.alarm.model;

import android.net.Uri;

/**
 * Created by andrewpark on 9/5/15.
 */
public class Melody {

    private String melody_name;
    private Uri melody_uri;
    private boolean isSelected;

    public Melody(String melody_name, Uri melody_uri) {
        this.melody_name = melody_name;
        this.melody_uri = melody_uri;
    }

    public String getMelody_name() {
        return melody_name;
    }

    public void setMelody_name(String melody_name) {
        this.melody_name = melody_name;
    }

    public Uri getMelody_uri() {
        return melody_uri;
    }

    public void setMelody_uri(Uri melody_uri) {
        this.melody_uri = melody_uri;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
