package com.android.oz.netnews.main.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.oz.netnews.R;
import com.android.oz.netnews.bean.AdsDetil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by jonesleborn on 16/8/17.
 */
public class HotImageAdapter extends PagerAdapter {

    // 保存轮播图json数据的
    private List<AdsDetil> mDetils;
    // 保存每一个View的
    private List<View> imagesView;


    public HotImageAdapter(List<AdsDetil> mDetils, List<View> imagesView) {
        this.mDetils = mDetils;
        this.imagesView = imagesView;
    }

    @Override
    public int getCount() {
        return mDetils.size();
    }

    //
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // 显示某个VIew会调用的方法
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 拿到某个ImageView
        View view = imagesView.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_img);

        // 开始加载图片
        AdsDetil adsDetil = mDetils.get(position);
        String imgsrc = adsDetil.getImgsrc();
        ImageLoader.getInstance().displayImage(imgsrc, imageView);

        // 将对象添加到ViewPager内部
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 这里删除的必须要跟上面添加的对象一样才行
        container.removeView((View) object);
    }
}
