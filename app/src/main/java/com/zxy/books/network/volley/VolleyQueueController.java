package com.zxy.books.network.volley;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.content.Context;

/**
 * Volley的RequestQueue调节器
 * <p/>
 * 所有网络请求的RequestQueue从这里获取
 * <p/>
 * 保证全局只有一个RequestQueue对象
 *
 * @author zxy
 */
public class VolleyQueueController {

    private static VolleyQueueController mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private VolleyQueueController(Context context) {
        mCtx = context;
    }

    public static synchronized VolleyQueueController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyQueueController(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public void start() {
        getRequestQueue().start();
    }

    public void stop() {
        getRequestQueue().stop();
    }
}
