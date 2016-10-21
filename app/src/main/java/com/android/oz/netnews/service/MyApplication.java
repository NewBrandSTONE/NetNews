package com.android.oz.netnews.service;

import android.app.Application;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

/**
 * Created by jonesleborn on 16/8/15.
 * 初始化一些操作
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化默认的ImagLoader
        // ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        // 一定要使用单例模式
        // ImageLoader.getInstance().init(configuration);

        // 使用自定义缓存的时候记得要先拿到缓存路径
        File cacheFileDir = getCacheFileDir();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                // 下载图片的线程数
                .threadPoolSize(3)
                // 设置缓存的大小 如果缓存的总数超过了限定数量，那么会先删除掉最近很少使用的BitMap图片
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                // 磁盘缓存 记得要先获取到缓存路径
                .diskCache(new UnlimitedDiskCache(cacheFileDir))
                // 设置本地文件名缓存规则
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                // 设置缓存最大的使用空间
                .diskCacheSize(50 * 1024 * 1024)
                // 设置最大缓存的文件数量 为1000个文件
                .diskCacheFileCount(1000)
                // 创建对象
                .build();

        // 记得一定在在这里初始化一次，在这里初始化一次之后，全局生效
        ImageLoader.getInstance().init(configuration);
    }

    public File getCacheFileDir() {
        // 获取外部存储的路径
        File sdFile = Environment.getExternalStorageDirectory();
        if (sdFile == null || !sdFile.exists()) {
            // 如果sdFile不存在或者为空的情况下，则说明没有外部存储的东西，不应该建立缓存了
            return null;
        }

        // 缓存文件创建成功
        File cacheFile = new File(sdFile, "HAHA");
        // 如果没有创建缓存路径的话就创建缓存路径
        if (cacheFile == null || !cacheFile.exists()) {
            cacheFile.mkdirs();
        }

        return cacheFile;
    }
}
