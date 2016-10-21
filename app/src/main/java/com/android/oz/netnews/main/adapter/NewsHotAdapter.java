package com.android.oz.netnews.main.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.oz.netnews.R;
import com.android.oz.netnews.bean.HotNewDetil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

/**
 * Created by jonesleborn on 16/8/15.
 */
public class NewsHotAdapter extends BaseAdapter {

    private List<HotNewDetil> mData;
    private Context mContext;
    private DisplayImageOptions options;


    public NewsHotAdapter(List<HotNewDetil> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;

        // ImageLoader显示图片的配置
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                // 图片为空的时候，显示的图片
                .showImageForEmptyUri(R.drawable.ic_main_menu_gold)
                // 加载一个缓存的图片，在图片还在加载的时候显示出来
                .showImageOnLoading(R.drawable.biz_tie_user_avater_default)
                // 解码失败或者加载失败的时候，显示的图片内容
                .showImageOnFail(R.drawable.ic_main_menu_gold)
                // 使用内存缓存
                .cacheInMemory(true)
                // 使用磁盘缓存
                .cacheOnDisk(true)
                // 设置图片的编码格式，使用质量比较差的
                .bitmapConfig(Bitmap.Config.RGB_565)
                // 设置展示的渐变效果 1秒钟显示出来
                .displayer(new FadeInBitmapDisplayer(1000))
                // 还可以设置图片以圆角的形式显示出来
                //.displayer(new CircleBitmapDisplayer())
                .build();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = null;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(mContext, R.layout.news_detail_item_layout, null);
            holder = new ViewHolder();
            holder.img = (ImageView) view.findViewById(R.id.img);
            holder.news_title = (TextView) view.findViewById(R.id.news_title);
            holder.news_from = (TextView) view.findViewById(R.id.news_from);
            holder.special_topic = (TextView) view.findViewById(R.id.special_topic);
            holder.numbers = (TextView) view.findViewById(R.id.numbers);

            view.setTag(holder);
        }

        setHolder(holder, mData.get(position));

        return view;
    }

    public void setHolder(ViewHolder holder, HotNewDetil detil) {
        holder.news_title.setText(detil.getTitle());
        holder.news_from.setText(detil.getSource());
        holder.numbers.setText(detil.getReplyCount() + "");
        // 在这里别忘记传递ImagLoader的options参数
        ImageLoader.getInstance().displayImage(detil.getImg(), holder.img, options);
    }

    class ViewHolder {
        ImageView img;
        TextView news_title;
        TextView news_from;
        TextView special_topic;
        TextView numbers;
    }


}
