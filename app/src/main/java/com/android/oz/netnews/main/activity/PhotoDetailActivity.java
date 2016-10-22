package com.android.oz.netnews.main.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.android.oz.netnews.R;
import com.android.oz.netnews.bean.DocDetail;
import com.android.oz.netnews.bean.DocDetailImg;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * @author O.z Young
 * @date 16/10/22
 * @desc ${CURSOR}
 */

public class PhotoDetailActivity extends Activity {

    public static final String IMAGES = "images";

    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_layout);

        initView();

        DocDetail docDetail = (DocDetail) getIntent().getSerializableExtra(IMAGES);
        if (docDetail != null && docDetail.getImg() != null) {
            PhotoAdapter photoAdapter = new PhotoAdapter(this, docDetail.getImg());
            viewpager.setAdapter(photoAdapter);
        }
    }

    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }

    class PhotoAdapter extends PagerAdapter {
        private List<DocDetailImg> list;
        private Context context;

        DisplayImageOptions options;


        public PhotoAdapter(Context context, List<DocDetailImg> list) {

            this.list = list;
            this.context = context;

            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.ic_main_menu_gold)
                    .showImageOnLoading(R.drawable.biz_tie_user_avater_default)
                    .showImageOnFail(R.drawable.ic_main_menu_gold)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new FadeInBitmapDisplayer(500)) // 图片显示出来的效果
                    .build();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // 初始化每个页面
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            DocDetailImg docDetailImg = list.get(position);
            PhotoView view = new PhotoView(context);
            view.setBackgroundColor(Color.BLACK);
            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, p);
            ImageLoader.getInstance().displayImage(docDetailImg.getSrc(), view, options);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
