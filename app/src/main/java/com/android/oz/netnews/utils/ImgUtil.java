package com.android.oz.netnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.android.oz.netnews.constant.FileConstant;

import java.io.File;

/**
 * Created by jonesleborn on 16/8/12.
 */
public class ImgUtil {
    public static Bitmap getImageByName(String Md5name) {
        File sdFile = Environment.getExternalStorageDirectory();
        if (sdFile == null || !sdFile.exists()) {
            return null;
        }

        //缓存文件没创建的话,没下载
        File catcheFile = new File(sdFile, FileConstant.cacheFileDir);
        if (catcheFile == null || !catcheFile.exists()) {
            return null;
        }


        File imageFile = new File(catcheFile, Md5name);
        if (imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            if (bitmap != null) {
                return bitmap;
            }
        }
        return null;
    }
}
