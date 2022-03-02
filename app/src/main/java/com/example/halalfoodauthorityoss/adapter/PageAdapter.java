package com.example.halalfoodauthorityoss.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.halalfoodauthorityoss.fragments.Favorite;
import com.example.halalfoodauthorityoss.fragments.Profile;
import com.example.halalfoodauthorityoss.fragments.Home;

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
            case 0: return new Home();
            case 1: return new Profile();
            case 2: return new Favorite();
            default:  return null;
        }

    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
