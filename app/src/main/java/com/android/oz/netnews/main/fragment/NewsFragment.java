package com.android.oz.netnews.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.oz.netnews.R;
import com.android.oz.netnews.main.adapter.NewsViewPagerAdapter;
import com.android.oz.netnews.main.bean.FragmentInfo;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

/**
 * Created by jonesleborn on 16/8/14.
 */
public class NewsFragment extends Fragment {

    private ViewPager vp_content;
    private ArrayList<FragmentInfo> mClasses;
    private NewsViewPagerAdapter mAdapter;
    private FrameLayout fl_content;
    private SmartTabLayout viewpagertab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        vp_content = (ViewPager) view.findViewById(R.id.vp_content);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        // 记得这里是需要添加到父控件上面的
        fl_content.addView(inflater.inflate(R.layout.include_tab, fl_content, false));

        viewpagertab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 每个新闻页的标题头
        String[] news_titles = getResources().getStringArray(R.array.news_tiltes);

        // 对应的子标题 头条->XXX的Fragment
        mClasses = new ArrayList<>();

        for (int i = 0; i < news_titles.length; i++) {
            if (i == 0) {
                // 热门
                mClasses.add(new FragmentInfo(news_titles[i], HotFragment.class));
            } else {
                mClasses.add(new FragmentInfo(news_titles[i], EmptyFragment.class));
            }
        }

        mAdapter = new NewsViewPagerAdapter(getActivity(), getFragmentManager(), mClasses);

        vp_content.setAdapter(mAdapter);

        viewpagertab.setViewPager(vp_content);
    }

}
