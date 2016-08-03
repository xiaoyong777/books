package com.zxy.books.network;


import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.zxy.books.model.BookChapter;
import com.zxy.books.network.volley.VolleyQueueController;
import com.zxy.books.util.FileUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * 章节下载
 */
public class ChaptersDownHelper {
    private static ChaptersDownHelper mInstance;
    private static Context context;
    private RequestQueue mRequestQueue;
    private BookChapter downchapter;
    private OnResponse onResponse;

    public void setDownchapter(BookChapter downchapter) {
        this.downchapter = downchapter;
        StringRequest srReq = new StringRequest(Request.Method.GET, downchapter.getChapterUrl(),
                new StrListener(), new StrErrListener()) {

            protected final String TYPE_GBK_CHARSET = "charset=gbk";

            // 重写parseNetworkResponse方法改变返回头参数解决乱码问题
            // 主要是看服务器编码，如果服务器编码不是UTF-8的话那么就需要自己转换，反之则不需要
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                try {
                    String type = response.headers.get(HTTP.CONTENT_TYPE);
                    if (type == null) {
                        type = "text/html;" + TYPE_GBK_CHARSET;
                        response.headers.put(HTTP.CONTENT_TYPE, type);
                    } else if (!type.contains("gbk")) {
                        type += ";" + TYPE_GBK_CHARSET;
                        response.headers.put(HTTP.CONTENT_TYPE, type);
                    }
                } catch (Exception e) {
                    Log.e("wfz", e.toString());
                }
                return super.parseNetworkResponse(response);
            }
        };
        srReq.setShouldCache(true); // 控制是否缓存
        this.getRequestQueue().add(srReq);
    }

    public ChaptersDownHelper(Context context) {
        this.context = context;
    }

//    public static synchronized ChaptersDownHelper getInstance(Context context) {
//        if (mInstance == null) {
//            mInstance = new ChaptersDownHelper(context);
//        }
//        return mInstance;
//    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = VolleyQueueController.getInstance(context).getRequestQueue();
        }
        return mRequestQueue;
    }

    public void stop() {
        getRequestQueue().cancelAll(context);
        getRequestQueue().stop();
    }

    private class StrListener implements Listener<String> {

        @Override
        public void onResponse(String result) {
            FileUtil.writeFile(downchapter.getChapterId(), result);
            if (onResponse != null) {
                onResponse.Success();

            }
        }
    }

    private class StrErrListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError errResult) {

            if (onResponse != null) {
                onResponse.Failure();
            }
        }
    }

    public interface OnResponse {
        void Success();

        void Failure();
    }

    public void getResponse(OnResponse onResponse) {
        this.onResponse = onResponse;
    }
}
