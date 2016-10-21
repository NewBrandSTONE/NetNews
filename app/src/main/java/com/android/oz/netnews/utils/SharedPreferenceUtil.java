package com.android.oz.netnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jonesleborn on 16/8/12.
 */
public class SharedPreferenceUtil {
    public static String fileName = "cacheFile";

    public static void saveString(Context context, String title, String content) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(title, content);
        editor.apply();
    }

    public static String getString(Context context, String tilte) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getString(tilte, "");
    }

    public static void saveInt(Context context, String title, int content) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(title, content);
        editor.apply();
    }

    public static int getInt(Context context, String title) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getInt(title, 0);
    }

    public static void saveLong(Context context, String title, long content) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(title, content);
        editor.apply();
    }

    public static long getLong(Context context, String title) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getLong(title, 0);
    }
}
