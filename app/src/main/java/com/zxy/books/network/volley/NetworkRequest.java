package com.zxy.books.network.volley;


import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import java.util.List;

/**
 * NetworkRequest类 继承于volley的StringRequest
 *
 * @author xc
 */
public class NetworkRequest extends StringRequest {
    private Priority mPriority = Priority.HIGH;
    protected final String TYPE_GBK_CHARSET = "charset=gbk";

    public NetworkRequest(int method, String url, Listener<String> listener, ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(10000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public NetworkRequest(String url, List<NameValuePair> params,
                          Listener<String> listener, ErrorListener errorListener) {
        this(Method.GET, urlBuilder(url, params), listener, errorListener);
    }

    public NetworkRequest(String url, Listener<String> listener, ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String type = response.headers.get(HTTP.CONTENT_TYPE);
            if (type == null) {
                type = "text/html;charset=gbk";
                response.headers.put(HTTP.CONTENT_TYPE, type);
            } else if (!type.contains("gbk")) {
                type += ";" + TYPE_GBK_CHARSET;
                response.headers.put(HTTP.CONTENT_TYPE, type);
            }
        } catch (Exception e) {
        }
        return super.parseNetworkResponse(response);
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    private static String urlBuilder(String url, List<NameValuePair> params) {
        return url + "?" + URLEncodedUtils.format(params, "UTF-8");
    }
}