package com.zxy.books.util;

import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by zxy on 2015/12/11.
 * 动画工具类
 */
public class AnimationUtil {


    /**
     * 用于书籍页面初始化载入动画
     * Y轴移动动画 从屏幕下方移动到屏幕顶部
     *
     * @param view
     * @param position
     * @param context
     */
    public static void runEnterAnimationY(View view, int position, Context context) {
        /**
         *  屏幕高度除以view的高度 得出一屏显示view的最多数目
         *  大于这个数目时 初始化时候不在第一屏显示 不执行动画
         *  （高度获取有问题 暂时固定为4）
         */
        if (position >= 4) {
            return;
        }
        view.setTranslationY(ScreenUtil.getScreenWidth(context));
        view.animate()
                .translationY(0)//目标y坐标
                .setStartDelay(200 * position)//延迟时间
                .setInterpolator(new DecelerateInterpolator(3.f))//动画效果
                .setDuration(1000)//动画执行时间
                .start();
    }

    /**
     * 用于网络书城页面itme初始动画
     * X轴移动动画 从屏幕右侧移动到左侧
     *
     * @param view
     * @param position
     * @param context
     */
    public static void runEnterAnimationX(View view, int position, Context context) {
        /**
         *  屏幕高度除以view的高度 得出一屏显示view的最多数目
         *  大于这个数目时 初始化时候不在第一屏显示 不执行动画
         *  （高度获取有问题 暂时固定为7）
         */
        //  int position=ScreenUtil.getScreenHeight(context)-ScreenUtil.getNavigationBarHeight()
        if (position >= 10) {
            return;
        }
        view.setTranslationX(ScreenUtil.getScreenHeight(context));
        view.animate()
                .translationX(0)//目标x坐标
                .setStartDelay(50 * position)//延迟时间
                .setInterpolator(new DecelerateInterpolator(3.f))//动画效果
                .setDuration(500)//动画执行时间
                .start();
    }
}