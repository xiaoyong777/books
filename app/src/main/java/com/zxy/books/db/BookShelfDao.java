package com.zxy.books.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zxy.books.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxy on 2016/1/6.
 */
public class BookShelfDao {
    private String DB_NAME = "book.db3";
    private int DB_VERSION = 1;
    private SQLiteDatabase db;
    private Context con;
    private DbBhelper dh;

    public BookShelfDao(Context con) {
        super();
        this.con = con;
    }

    public void open() {
        dh = new DbBhelper(con, DB_NAME, null, DB_VERSION);
        try {
            db = dh.getWritableDatabase();
        } catch (Exception e) {
            db = dh.getReadableDatabase();
        }
    }

    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }

    }

    public long insert(String bookId) {
        long re = -1;
        open();
        ContentValues value = new ContentValues();
        value.put("bookId", bookId);
        re = db.insert("tb_Bookshelf", null, value);
        close();
        return re;

    }

//    public long update(long id, Book his) {
//        open();
//        long re = -1;
//        ContentValues value = new ContentValues();
//        value.put("bookId", his.getBookId());
//        value.put("bookName", his.getBookName());
//        value.put("bookAuthor", his.getBookAuthor());
//        value.put("bookLocalPath", his.getBookLocalPath());
//        value.put("indexUrl", his.getIndexUrl());
//        value.put("type", his.getType());
//        value.put("chapterUrl", his.getCurrentChapterUrl());
//        re = db.update("tb_Book", value, "bookId =" + id, null);
//        close();
//        return re;
//    }

    public long delet(String bookId) {
        long result = -1;
        open();
        result = db.delete("tb_Bookshelf", " bookId=" + bookId, null);
        open();
        return result;
    }

    public List<Book> getAllData() {
        open();
        Cursor res = db.query("tb_Bookshelf", null, null, null, null, null, null);
        List<Book> list = conventtohistory(res);
        close();
        if (list == null || list.size() == 0) {
            return null;
        }
        BookDao bookDao = new BookDao(con);
        ChapterDao chapterDao = new ChapterDao(con);
        for (int i = 0; i < list.size(); i++) {
            bookDao.getOneBook(list.get(i));
            list.get(i).setBookChapters(chapterDao.getBookChapter(list.get(i).getBookId()));
        }
        return list;
    }

    public List<Book> getOneBook(String id) {
        open();
        Cursor res = db.query("tb_Bookshelf", null, "bookId=" + id,
                null, null, null, null);
        List<Book> list = conventtohistory(res);
        close();
        return list;

    }


    public List<Book> conventtohistory(Cursor cu) {
        List<Book> results = new ArrayList<Book>();
        int resultnumber = cu.getCount();
        if (resultnumber == 0 || !cu.moveToFirst()) {
            return results;
        }
        for (int i = 0; i < resultnumber; i++) {
            Book p = new Book();
            p.setBookId(cu.getString(1));
//            p.setBookAuthor(cu.getString(2));
//            p.setBookLocalPath(cu.getString(3));
//            p.setIndexUrl(cu.getString(4));
//            p.setBookId(cu.getString(5));
            cu.moveToNext();
            results.add(p);
        }
        return results;

    }
}
