package com.zxy.books.util;

import android.util.Log;

import com.zxy.books.R;

import java.util.Random;


/**
 * Created by zxy on 2015/12/11.
 * 颜色工具类
 */
public class ColorUtil {
    /**
     * 随机颜色库
     */
    public final static int[] colors =
            {R.color.red, R.color.pink, R.color.puple,
                    R.color.deep_purplr, R.color.bule,
                    R.color.gree, R.color.teal, R.color.deep_orange};
    /**
     * 字体颜色库
     */
    public final static int[] textColol =
            {R.color.bg_1, R.color.bg_2, R.color.bg_3, R.color.bg_4,
                    R.color.bg_5, R.color.bg_6, R.color.bg_7, R.color.bg_8};
    /**
     * 阅读背景库
     */
    public final static int[] readbg =
            {R.color.bg_8, R.color.bg_7, R.color.bg_6, R.color.bg_5,
                    R.color.bg_4, R.color.bg_3, R.color.bg_2, R.color.bg_1,
                    R.drawable.read_bg1,
                    R.drawable.read_bg2,
                    R.drawable.read_bg3,
                    R.drawable.read_bg4};

    /**
     * 从颜色库中随机取一种颜色
     *
     * @return
     */
    public static int getColor() {
        Random random = new Random();
        int colorNo = random.nextInt(colors.length - 1);
        return colors[colorNo];
    }

}
