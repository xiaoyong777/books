package com.zxy.books.network.volley;


import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * 网络访问封装基类 所有的网络帮助类继承与它
 *
 * @param <T>
 * @author xc
 */
public abstract class NetworkHelper<T> implements Response.Listener<String>,
        ErrorListener {
    private Context context;

    public NetworkHelper(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    protected NetworkRequest getRequestForGet(String url, List<NameValuePair> params) {
        if (params == null) {
            return new NetworkRequest(url, this, this);
        } else {
            return new NetworkRequest(url, params, this, this);
        }
    }

    protected NetworkRequest getRequestForPost(String url, Map<String, String> params) {
        return new NetworkRequest(Method.POST, url, this, this);
    }

    public void sendGETRequest(String url, List<NameValuePair> params) {
        VolleyQueueController.getInstance(getContext()).getRequestQueue()
                .add(getRequestForGet(url, params));
    }

    // public void start()
    // {
    // VolleyQueueController.getInstance(getContext()).
    // getRequestQueue().start();
    // }

    public void sendPostRequest(String url, Map<String, String> params) {
        VolleyQueueController.getInstance(context).getRequestQueue()
                .add(getRequestForPost(url, params));
    }

    public void stop() {
        // VolleyQueueController.getInstance(getContext()).
        // // getRequestQueue().cancelAll(getContext());
        VolleyQueueController.getInstance(getContext()).getRequestQueue()
                .stop();
    }

    protected abstract void disposeVolleyError(VolleyError error);

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error != null) {
            disposeVolleyError(error);
        }
    }

    protected abstract void disposeResponse(String response);

    @Override
    public void onResponse(String response) {
        // Log.d("Amuro", response.toString());
        disposeResponse(response);
    }

    private UIDataListener<T> uiDataListener;

    public void setUiDataListener(UIDataListener<T> uiDataListener) {
        this.uiDataListener = uiDataListener;
    }

    protected void notifyDataChanged(T data) {
        if (uiDataListener != null) {
            uiDataListener.onDataChanged(data);
        }
    }

    protected void notifyErrorHappened(VolleyError error) {
        if (uiDataListener != null) {
            String srtError = VolleyErrorHelper.getMessage(error, context);
            uiDataListener.onErrorHappened(srtError);
        }
    }

}