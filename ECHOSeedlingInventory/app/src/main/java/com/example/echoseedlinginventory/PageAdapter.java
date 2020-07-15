package com.example.echoseedlinginventory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PageAdapter  extends FragmentPagerAdapter {

    private int numTabs = 2;


    PageAdapter(FragmentManager fm){
       super(fm);

    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                return new ListFragment();
            case 1:
                return  new ManageFragment();
                default:
                    return null;


        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "LIST";
            case 1:
                return "MANAGE";

        }
        return "";
    }
}
