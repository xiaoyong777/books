package com.zxy.books.network;

import android.content.Context;

import com.android.volley.VolleyError;
import com.zxy.books.model.Book;
import com.zxy.books.network.volley.NetworkHelper;
import com.zxy.books.network.parser.GokankanParser;


/**
 * 书籍简介获取类 继承与NetworkHelper
 * <p/>
 * 实现disposeVolleyError和disposeResponse抽象方法
 * <p/>
 * 这里完成对html的解析 获取需要的数据
 *
 * @author zxy
 */
public class BooKbetailHelper extends NetworkHelper<Book> {

    private Book bk;
    private GokankanParser parser;
    private Context context;

    public BooKbetailHelper(Context ctx, Book _bk) {
        super(ctx);
        parser = new GokankanParser();
        context = ctx;
        bk = _bk;
    }

    @Override
    protected void disposeVolleyError(VolleyError error) {
        notifyErrorHappened(error);
    }

    @Override
    protected void disposeResponse(String response) {
        parser.getBookDetail(response, bk);
        // bookCover.setImageUrl(bk.getCoverurl(),
        // R.drawable.book_cover_default, 0);
        // txtDescription.setText(bk.getBrief());
        // layoutProgress.setVisibility(View.GONE);
        notifyDataChanged(bk);
    }

}
