package com.zxy.books.network;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.android.volley.VolleyError;
import com.zxy.books.model.Book;
import com.zxy.books.network.volley.NetworkHelper;
import com.zxy.books.network.parser.GokankanParser;

/**
 * 书籍查找 网络帮助类 继承NetworkHelper
 * <p/>
 * 实现disposeVolleyError和disposeResponse抽象方法
 * <p/>
 * 这里完成对html的解析 获取需要的数据
 *
 * @author zxy
 */
public class BookSearchNetworkHelper extends NetworkHelper<List<Book>> {
    private List<Book> data = new ArrayList<Book>();
    private GokankanParser parser;
    private Context context;

    public BookSearchNetworkHelper(Context ctx) {
        super(ctx);
        parser = new GokankanParser();
        context = ctx;
    }

    @Override
    protected void disposeVolleyError(VolleyError error) {
        notifyErrorHappened(error);
    }

    @Override
    protected void disposeResponse(String response) {
        data = parser.getSearchBookList(response, context);
        notifyDataChanged(data);
    }
}
