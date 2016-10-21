package com.android.oz.netnews.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import com.android.oz.netnews.bean.Adert;
import com.android.oz.netnews.bean.Ads;
import com.android.oz.netnews.constant.FileConstant;
import com.android.oz.netnews.helper.MD5Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by jonesleborn on 16/8/11.
 */
public class AdsIntentService extends IntentService {


    public AdsIntentService() {
        super("AdsIntentService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AdsIntentService(String name) {
        super(name);
    }

    // 在这里下载图片
    @Override
    protected void onHandleIntent(Intent intent) {
        Ads ads = (Ads) intent.getSerializableExtra("data");
        List<Adert> adsList = ads.getAds();

        for (Adert adert : adsList) {
            List<String> res_url = adert.getRes_url();
            // 获取下载图片的url
            String img_url = res_url.get(0);
            if (!TextUtils.isEmpty(img_url)) {
                // 如果url不为空，则生成md5
                String img_url_md5 = MD5Helper.MD5(img_url);
                File checkFile = checkImageIsExists(img_url_md5);
                if (checkFile != null && checkFile.exists()) {
                    // 直接使用保存在SD卡中的图片
                } else {
                    // 再去下载
                    downloadImg(img_url, img_url_md5);
                }
            }
        }
    }

    private void downloadImg(String imgUrl, String imgUrlMD5) {
        try {

            // 需要从url流中获取图片
            URL url = null;
            url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            if (bitmap != null) {
                // 说明图片读取完整，可以保存图片了
                saveToSDcard(bitmap, imgUrlMD5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveToSDcard(Bitmap bitmap, String imgUrlMD5) {
        // 先将图片缓存起来
        // 获取SDCard路径
        File file = Environment.getExternalStorageDirectory();
        File cacheFile = new File(file, FileConstant.cacheFileDir);

        // 如果对应目录不存在的话，就创建目录
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        // 保存图片
        File image = new File(cacheFile, imgUrlMD5);

        // 如果已经有了这个图片就return回去了
        if (image.exists()) {
            return;
        }

        // 从内存中输出数去
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public File checkImageIsExists(String md5Name) {
        File checkFile = getFileByName(md5Name);
        if (checkFile != null && checkFile.exists()) {
            return checkFile;
        }
        return null;
    }

    private File getFileByName(String md5Name) {
        File sd = Environment.getExternalStorageDirectory();
        if (!sd.exists()) {
            // 检测SD卡是否存在
            // SD卡不存在则返回null
            return null;
        }

        // 检测缓存目录在不在
        File cacheFile = new File(sd, FileConstant.cacheFileDir);
        if (!cacheFile.exists()) {
            // 缓存文件不存在
            return null;
        }

        File imageFile = new File(sd, md5Name);

        return imageFile;
    }

}
