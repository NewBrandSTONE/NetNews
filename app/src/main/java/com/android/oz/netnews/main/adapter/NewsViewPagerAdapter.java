package com.android.oz.netnews.main.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.oz.netnews.main.bean.FragmentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonesleborn on 16/8/14.
 */
public class NewsViewPagerAdapter extends FragmentPagerAdapter {

    private List<FragmentInfo> mClasses;
    private Context mContext;


    public NewsViewPagerAdapter(Context context, FragmentManager fm, ArrayList<FragmentInfo> mClasses) {
        super(fm);
        this.mClasses = mClasses;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        FragmentInfo clz = mClasses.get(position);
        Fragment fragment = Fragment.instantiate(mContext, clz.getClz().getName());
        return fragment;
    }

    @Override
    public int getCount() {
        return mClasses.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        FragmentInfo aClass = mClasses.get(position);
        return aClass.getTitle();
    }
}
