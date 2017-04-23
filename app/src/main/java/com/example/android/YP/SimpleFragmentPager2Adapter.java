package com.example.android.YP;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 *  Адаптер для ViewPager отвеающий за отображение Фрагментов HistoryFragment, FavoriteWordFreagment
 */


public class SimpleFragmentPager2Adapter extends FragmentPagerAdapter {

    private String fragment [] = {"История", "Избранное"};

    public SimpleFragmentPager2Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HistoryFragment();
        } else{
            return new FavoriteWordFragment();
        }
    }

    @Override
    public int getCount() {
        return fragment.length;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return fragment[position];
    }
}

