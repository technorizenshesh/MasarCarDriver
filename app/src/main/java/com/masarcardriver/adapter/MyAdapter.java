package com.masarcardriver.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.masarcardriver.fragment.PastTripFragment;
import com.masarcardriver.fragment.UpcomingFragment;


public class MyAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public MyAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PastTripFragment fragment = new PastTripFragment();
                return fragment;
            case 1:
                UpcomingFragment upcomingFragment = new UpcomingFragment();
                return upcomingFragment;

            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
