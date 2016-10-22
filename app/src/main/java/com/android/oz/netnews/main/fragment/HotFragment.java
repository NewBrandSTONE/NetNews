package com.android.oz.netnews.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.oz.netnews.R;
import com.android.oz.netnews.bean.AdsDetil;
import com.android.oz.netnews.bean.Hot;
import com.android.oz.netnews.bean.HotNewDetil;
import com.android.oz.netnews.constant.FileConstant;
import com.android.oz.netnews.constant.MyConstance;
import com.android.oz.netnews.main.activity.NewDetailActivity;
import com.android.oz.netnews.main.adapter.HotImageAdapter;
import com.android.oz.netnews.main.adapter.NewsHotAdapter;
import com.android.oz.netnews.utils.HttpResponse;
import com.android.oz.netnews.utils.HttpUtil;
import com.android.oz.netnews.utils.JsonUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jonesleborn on 16/8/14.
 */
public class HotFragment extends Fragment implements ViewPager.OnPageChangeListener, AbsListView.OnScrollListener {

    private ListView lv_content;

    private NewsHotAdapter mAdatper;

    // 初始化操作
    private final static int INIT = 1;
    // 代表加载更多的更新操作
    private final static int UPDATE = 2;

    private ViewPager vp_hot_header;
    // 保存轮播图内容的集合
    private List<AdsDetil> mAdsDetil;

    private HotImageAdapter mHotImageAdapter;
    // 轮播图的标题
    private TextView tv_title;
    // 轮播图上的点
    private LinearLayout ll_dots;

    // 轮播图上的图片
    private ArrayList<View> imageVies;
    // 轮播图上的脚表点
    private ArrayList<ImageView> imageDots;

    // 判断是否滑动到最底部
    private boolean isToEnd = false;
    // 保存热门新闻
    private ArrayList<HotNewDetil> allDetails = new ArrayList<>();

    // 当前页数
    int index = 0;

    private boolean isGettingHttp = false;

    /**
     * 获取当前页面的url
     *
     * @return
     */
    public String getUrl() {
        int start, end;
        if (index == 0) {
            start = 0;
            end = 20;
        } else {
            start = (index) * 20 + 1;
            end = (index + 1) * 20;
        }
        return FileConstant.getHotUrl(start, end);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        lv_content = (ListView) view.findViewById(R.id.lv_content);

        // 轮播图的头部
        View head = inflater.inflate(R.layout.include_hot_header, null);

        vp_hot_header = (ViewPager) head.findViewById(R.id.vp_hot_header);

        // 增加VP滑动监听
        vp_hot_header.addOnPageChangeListener(this);


        tv_title = (TextView) head.findViewById(R.id.tv_title);
        // 轮播图的指示点
        ll_dots = (LinearLayout) head.findViewById(R.id.ll_dots);

        // 将轮播图添加到ListView中
        lv_content.addHeaderView(head);

        // 初始化保存轮播图的集合
        mAdsDetil = new ArrayList<>();

        // 初始化Handler
        handler = new InnerHandler(this);

        // 增加滚动的监听
        lv_content.setOnScrollListener(this);

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HotNewDetil hotNewDetil = allDetails.get(position);
                String specialId = hotNewDetil.getSpecialID();

                if (!TextUtils.isEmpty(specialId)) {
                    // 跳转到专题页面
                    Log.v("Oz", "specialId-->" + specialId);

                } else {
                    // 跳转到详细页面
                    String docId = hotNewDetil.getDocid();
                    Log.v("Oz", "docId-->" + docId);
                    Intent newIntent = MyConstance.getNewIntent(getActivity().getApplicationContext(), NewDetailActivity.class);
                    newIntent.putExtra(NewDetailActivity.DOC_ID, docId);
                    startActivity(newIntent);
                }
            }
        });

        return view;
    }


    private InnerHandler handler;

    /**
     * 设置角标点的样式
     *
     * @param index 当前轮播图的角标
     */
    public void setDot(int index) {
        for (int i = 0; i < mAdsDetil.size(); i++) {
            ImageView dot = imageDots.get(i);
            if (i == index) {
                dot.setImageResource(R.drawable.white_dot);
            } else {
                dot.setImageResource(R.drawable.gray_dot);
            }
        }
    }

    /**
     * 设置不同图片的标题
     *
     * @param index 当前轮播图的脚标
     */
    public void setTitle(int index) {
        AdsDetil adsDetil = mAdsDetil.get(index);
        if (adsDetil != null) {
            tv_title.setText(adsDetil.getTitle());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 滑动到某一页面的百分比
    }

    @Override
    public void onPageSelected(int position) {
        position = position % mAdsDetil.size();
        // 滑动到某一页面
        setDot(position);
        setTitle(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 滑动状态的监听
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // ListView停止滑动
        if (scrollState == SCROLL_STATE_IDLE && isToEnd) {
            // 加载下一页
            getData2(false);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastPosition = view.getLastVisiblePosition();
        if (lastPosition == totalItemCount - 1) {
            isToEnd = true;
        } else {
            isToEnd = false;
        }
    }


    /**
     * 1. 将Handler类变成了静态的内部类
     * 2. 使用弱引用来持有UI界面的线程对象
     */
    public static class InnerHandler extends Handler {
        // 让HotFragment持有这个Handler的弱引用对象
        // 每次gc需要回收对象的时候，首先删除弱引用对象，其次是软引用对象，最后是强应用对象
        WeakReference<HotFragment> hot;

        // 将HotFragment变成弱引用对象
        public InnerHandler(HotFragment hotFragment) {
            hot = new WeakReference<HotFragment>(hotFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            // 获取到弱引用的对象
            HotFragment hotFragment = hot.get();
            if (hotFragment == null) {
                // 说明此时hotFragment已经被回收了
                // 没有必要进行操作了
                return;
            }
            switch (msg.what) {
                case INIT:
                    List<HotNewDetil> detils = (List<HotNewDetil>) msg.obj;
                    // 将请求到的数据添加到集合中
                    hotFragment.allDetails.addAll(detils);

                    // 取出轮播图中的数据
                    HotNewDetil hotNewDetil = detils.get(0);
                    // 这里面保存的是轮播图
                    List<AdsDetil> adsDetils = hotNewDetil.getAds();

                    hotFragment.mAdsDetil.addAll(adsDetils);

                    // 再将第一条图集信息删除
                    detils.remove(0);
                    // 显示ListView数据
                    hotFragment.initListView(detils);

                    // 显示轮播图的数据
                    hotFragment.initBanner();
                    break;
                case UPDATE:
                    List<HotNewDetil> newDetils = (List<HotNewDetil>) msg.obj;
                    hotFragment.addDate(newDetils);
                    // 这里也要添加
                    hotFragment.allDetails.addAll(newDetils);
                    break;
            }
        }
    }

    public void addDate(List<HotNewDetil> newDetails) {
        if (mAdatper != null) {
            mAdatper.addNewData(newDetails);
        }
    }

    // 初始化轮播图的数据内容
    public void initBanner() {

        if (mAdsDetil != null && mAdsDetil.size() > 0) {
            imageVies = new ArrayList<>();

            imageDots = new ArrayList<>();

            for (int i = 0; i < mAdsDetil.size(); i++) {
                // 生成一个ImageView
                View view = View.inflate(getActivity(), R.layout.item_hot_image, null);
                imageVies.add(view);

                ImageView dot = new ImageView(getActivity());
                dot.setImageResource(R.drawable.gray_dot);
                // 设置添加的样式
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                p.setMargins(10, 0, 0, 0);
                ll_dots.addView(dot, p);
                // 将角标添加到集合中
                imageDots.add(dot);
            }
            mHotImageAdapter = new HotImageAdapter(mAdsDetil, imageVies);
            // 设置Adapter
            vp_hot_header.setAdapter(mHotImageAdapter);

            // 设置轮播图的标题
            AdsDetil adsDetil = mAdsDetil.get(0);
            tv_title.setText(adsDetil.getTitle());

            // 一开始角标就在第一个
            setDot(0);

            // 将开始的位置设置成中间
            int offSize = ((Integer.MAX_VALUE / 2) % (imageVies.size()));

            vp_hot_header.setCurrentItem((Integer.MAX_VALUE / 2) - offSize);
        }
    }

    public void initListView(List<HotNewDetil> detils) {
        mAdatper = new NewsHotAdapter(detils, getActivity());
        lv_content.setAdapter(mAdatper);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // getHot();
        getData2(true);
    }

    public void getData2(final boolean isFirst) {

        if (isGettingHttp) {
            return;
        }

        // 正在获取http请求
        isGettingHttp = true;

        String url = getUrl();

        HttpUtil util = HttpUtil.getInstace();

        util.doGet(url, new HttpResponse<Hot>(Hot.class) {
            @Override
            public void onSuccess(Hot hot) {

                isGettingHttp = false;

                if (hot != null) {
                    // 热门页面数据请求成功
                    List<HotNewDetil> detils = hot.getT1348647909107();

                    if (isFirst) {
                        // 使用Handler更新UI
                        Message msg = Message.obtain();
                        msg.what = INIT;
                        msg.obj = detils;
                        handler.sendMessage(msg);
                    } else {
                        // 使用Handler更新UI
                        Message msg = Message.obtain();
                        msg.what = UPDATE;
                        msg.obj = detils;
                        handler.sendMessage(msg);
                    }


                    // 为了获取下一页的数据
                    index++;
                }
            }

            @Override
            public void onFail(String msg) {
                Log.v("Oz", msg);
                isGettingHttp = false;
            }
        });
    }

    public void getHot() {
        // 1.生成OKHttpClient
        OkHttpClient client = new OkHttpClient();

        // 2.生成Request请求
        Request request = new Request.Builder()
                .url(FileConstant.news_hot_url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("Oz", "请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 有响应
                if (!response.isSuccessful()) {
                    // 失败
                    Log.v("Oz", "onResponse Fail");
                } else {
                    String body = response.body().string();

                    Hot hot = JsonUtil.getJsonObject(body, Hot.class);

                    if (hot != null) {
                        // 热门页面数据请求成功
                        List<HotNewDetil> detils = hot.getT1348647909107();
                        // 第一个是轮播图记得删除
                        detils.remove(0);

                        Message msg = Message.obtain();
                        msg.obj = detils;
                        msg.what = INIT;
                        handler.sendMessage(msg);
                    }
                }
            }
        });
    }
}
