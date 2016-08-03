package com.zxy.books.network;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.android.volley.VolleyError;
import com.zxy.books.model.Book;
import com.zxy.books.model.BookChapter;
import com.zxy.books.network.volley.NetworkHelper;
import com.zxy.books.network.parser.GokankanParser;

/**
 * 书籍章节获取类 继承于NetworkHelper
 * <p/>
 * 实现disposeVolleyError和disposeResponse抽象方法
 * <p/>
 * 这里完成对html的解析 获取需要的数据
 *
 * @author zxy
 */
public class ChapterListNetworkHelper extends NetworkHelper<List<BookChapter>> {
    private List<BookChapter> data = new ArrayList<BookChapter>();
    private GokankanParser parser;
    private Context context;
    private Book book;

    public ChapterListNetworkHelper(Context ctx, Book _book) {
        super(ctx);
        parser = new GokankanParser();
        context = ctx;
        book = _book;
    }

    @Override
    protected void disposeVolleyError(VolleyError error) {
        notifyErrorHappened(error);
    }

    @Override
    protected void disposeResponse(String response) {
        data = parser.getChaptersByIndexUrl(response, book);
        notifyDataChanged(data);
    }

}
