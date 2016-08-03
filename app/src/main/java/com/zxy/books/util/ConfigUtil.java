package com.zxy.books.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zxy on 2016/1/2.
 * 用户设置工具类
 */
public class ConfigUtil {
    private static SharedPreferences sp;
    private final static String SHAREDPREFERENCESNAME = "ConfigUtil";
    private final static String textSize = "TEXTSIZE";
    private final static String textColor = "TEXTCOLOR";
    private final static String readbg = "READBACKGROUND";
    private final static String brighr = "BRIGHTNESS";

    private static SharedPreferences getSharedPreferences(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(SHAREDPREFERENCESNAME, 0);
        }
        return sp;
    }

    public static void setTextSize(Context context, int value) {
        getSharedPreferences(context).edit().putInt(textSize, value).commit();
    }

    public static void setTextColor(Context context, int value) {
        getSharedPreferences(context).edit().putInt(textColor, value).commit();
    }

    public static void setreadbg(Context context, int value) {
        getSharedPreferences(context).edit().putInt(readbg, value).commit();
    }

    public static int getTextSize(Context context) {
        return getSharedPreferences(context).getInt(textSize, 45);
    }

    public static int getTextColor(Context context) {
        return getSharedPreferences(context).getInt(textColor, 5);
    }

    public static int getReadBg(Context context) {
        return getSharedPreferences(context).getInt(readbg, 11);
    }

    public static int getbright(Context context) {
        return getSharedPreferences(context).getInt(brighr, -1);
    }

    public static void setbright(Context context, int value) {
        getSharedPreferences(context).edit().putInt(brighr, value).commit();
    }
}
