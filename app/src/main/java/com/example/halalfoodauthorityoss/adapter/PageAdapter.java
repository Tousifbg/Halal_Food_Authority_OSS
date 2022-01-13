package com.example.halalfoodauthorityoss.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.halalfoodauthorityoss.fragments.Tab1;
import com.example.halalfoodauthorityoss.fragments.Tab2;
import com.example.halalfoodauthorityoss.fragments.Tab3;
import com.example.halalfoodauthorityoss.fragments.Tab4;

public class PageAdapter extends FragmentPagerAdapter {
    int tabcount;

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new Tab1();
            case 1: return new Tab2();
            case 2: return new Tab3();
            case 3: return new Tab4();
            default:  return null;
        }

    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
