package com.zxy.books.util;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.zxy.books.R;

/**
 * Created by zxy on 2015/12/25.
 * 网络错误SnackBar提示工具类
 * 根据给定的错误提示 返回给定的SnackBar提示对象
 */
public class SnackBar {
    public static Snackbar getSnackbar(String errorMessage, CoordinatorLayout coordinatorLayout, final Context context) {
        if (errorMessage.equals(context.getResources().getString(R.string.no_internet))
                || errorMessage.equals(context.getResources().getString(R.string.generic_server_down))) {//网络未开启
            final Snackbar snackbar =
                    Snackbar.make(
                            coordinatorLayout,
                            errorMessage,
                            Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.ok1,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = null;
                            // 先判断当前系统版本
                            if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                                intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                            } else {
                                intent = new Intent();
                                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                            }
                            context.startActivity(intent);
                        }
                    });
            return snackbar;
        } else {
            final Snackbar snackbar = Snackbar.make(
                    coordinatorLayout,
                    errorMessage,
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.ok2,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
            return snackbar;
        }

    }
}
