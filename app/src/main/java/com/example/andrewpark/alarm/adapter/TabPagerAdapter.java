package com.example.andrewpark.alarm.adapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.andrewpark.alarm.AlarmFragment;
import com.example.andrewpark.alarm.ChartFragment;
import com.example.andrewpark.alarm.ClockFragment;
import com.example.andrewpark.alarm.TimerFragment;

/**
 * Created by andrewpark on 8/26/15.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    public TabPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new AlarmFragment();
            case 1:
                return new ClockFragment();
            case 2:
                return new TimerFragment();
            case 3:
                return new ChartFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
