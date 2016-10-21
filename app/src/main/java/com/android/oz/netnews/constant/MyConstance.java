package com.android.oz.netnews.constant;

import android.content.Context;
import android.content.Intent;

/**
 * Created by jonesleborn on 16/8/13.
 */
public class MyConstance {
    public static Intent getNewIntent(Context context, Class tClass) {
        Intent intent = new Intent();
        intent.setClass(context, tClass);
        return intent;
    }
}
