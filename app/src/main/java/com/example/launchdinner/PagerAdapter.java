package com.example.launchdinner;


import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragments = new ArrayList<>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(new Fragment2());
        fragments.add(new Fragment2Second());
        fragments.add(new Fragment2Third());
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
