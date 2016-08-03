package com.zxy.books.network;

import android.content.Context;

import com.android.volley.VolleyError;
import com.zxy.books.model.Book;
import com.zxy.books.model.BookChapter;
import com.zxy.books.network.volley.NetworkHelper;
import com.zxy.books.network.parser.GokankanParser;

/**
 * * 书籍正文内容获取类 继承于NetworkHelper
 * <p/>
 * 实现disposeVolleyError和disposeResponse抽象方法
 * <p/>
 * 这里完成对html的解析 获取需要的数据
 *
 * @author xc
 */
public class ReadBookHelper extends NetworkHelper<BookChapter> {

    private BookChapter bookChapter;
    private GokankanParser parser;
    private Context context;

    // private List<SearchBook> data = new ArrayList<SearchBook>();

    public ReadBookHelper(Context context, BookChapter bookChapter) {
        super(context);
        parser = new GokankanParser();
        this.context = context;
        this.bookChapter = bookChapter;
    }

    @Override
    protected void disposeVolleyError(VolleyError error) {
        // TODO Auto-generated method stub
        notifyErrorHappened(error);
    }

    @Override
    protected void disposeResponse(String response) {
        // TODO Auto-generated method stub
        parser.readbook(response,bookChapter);
        notifyDataChanged(bookChapter);
    }

}
