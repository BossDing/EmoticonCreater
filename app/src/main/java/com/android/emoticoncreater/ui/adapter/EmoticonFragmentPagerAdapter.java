package com.android.emoticoncreater.ui.adapter;


import com.android.emoticoncreater.ui.activity.EmoticonFragment;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * 一个表情适配器
 */

public class EmoticonFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments;
    private String[] mTitles;

    public EmoticonFragmentPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);

        this.mTitles = titles;
        mFragments = new ArrayList<>();
        for (String title : mTitles) {
            mFragments.add(EmoticonFragment.newInstance(title));
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
