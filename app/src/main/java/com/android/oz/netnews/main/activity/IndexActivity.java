package com.android.oz.netnews.main.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.oz.netnews.R;
import com.android.oz.netnews.main.fragment.MeFragment;
import com.android.oz.netnews.main.fragment.NewsFragment;
import com.android.oz.netnews.main.fragment.ReadingFragment;
import com.android.oz.netnews.main.fragment.TopicFragment;
import com.android.oz.netnews.main.fragment.VideoFragment;

/**
 * Created by jonesleborn on 16/8/13.
 */
public class IndexActivity extends FragmentActivity {

    private FragmentTabHost fth_tabs;
    // 存放底部导航栏的标题
    private String[] title;
    // 存放底部导航栏的图标id
    private int[] selector;
    // 保存Fragment的class
    private Class[] f_class;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        initView();
    }

    private void initView() {

        // 获取标题内容
        title = getResources().getStringArray(R.array.tab_title);
        // 获取对应图标的id
        // 直接使用selector
        selector = new int[]{
                R.drawable.news_selector,
                R.drawable.vido_selector,
                R.drawable.topic_selector,
                R.drawable.reading_selector,
                R.drawable.me_selector
        };

        // 保存着五个Fragment
        f_class = new Class[]{
                NewsFragment.class,
                MeFragment.class,
                ReadingFragment.class,
                TopicFragment.class,
                VideoFragment.class
        };

        fth_tabs = (FragmentTabHost) findViewById(R.id.fth_tabs);
        // 首先要设置
        fth_tabs.setup(this, getSupportFragmentManager(), R.id.tabContent);

//        // 这个0表示标示卡的内容
//        TabHost.TabSpec one = fth_tabs.newTabSpec("0");
//        // 这个one表示显示的内容
//        one.setIndicator("one");
//
//        fth_tabs.addTab(one, EmptyFragment.class, null);
//
//        TabHost.TabSpec two = fth_tabs.newTabSpec("1");
//        // 这个one表示显示的内容
//        two.setIndicator("two");
//
//        fth_tabs.addTab(two, EmptyFragment.class, null);
//
//        TabHost.TabSpec three = fth_tabs.newTabSpec("2");
//        // 这个one表示显示的内容
//        three.setIndicator("three");
//
//        fth_tabs.addTab(three, EmptyFragment.class, null);

        for (int i = 0; i < title.length; i++) {
//            TabHost.TabSpec temp = fth_tabs.newTabSpec("temp" + i);
//            temp.setIndicator(i + "");
//            fth_tabs.addTab(temp, EmptyFragment.class, null);
            View view = getTabView(i, title, selector);
            TabHost.TabSpec spec = fth_tabs.newTabSpec(title[i]);
            spec.setIndicator(view);

            // 将对应的Fragment添加到TabHost里面
            fth_tabs.addTab(spec, f_class[i], null);
        }
        fth_tabs.setCurrentTab(0);
    }

    /**
     * 返回底部导航栏的图标和名称
     *
     * @param index
     * @param titles
     * @param icons
     * @return
     */
    public View getTabView(int index, String[] titles, int[] icons) {
        View view = View.inflate(this, R.layout.item_tab, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_icon);
        TextView textView = (TextView) view.findViewById(R.id.tv_title);

        // 这样子没法加载类选择器
        // imageView.setImageResource(icons[index])
        imageView.setBackground(getResources().getDrawable(icons[index]));
        textView.setText(titles[index]);
        return view;
    }
}
