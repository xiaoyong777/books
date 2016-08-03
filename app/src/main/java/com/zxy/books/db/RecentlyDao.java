package com.zxy.books.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zxy.books.model.Book;

public class RecentlyDao {
    private String DB_NAME = "book.db3";
    private int DB_VERSION = 1;
    private SQLiteDatabase db;
    private Context con;
    private DbBhelper dh;

    public RecentlyDao(Context con) {
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

    public long insert(Book his) {
        long re = -1;
        open();
        ContentValues value = new ContentValues();
        value.put("bookId", his.getBookId());
        value.put("currentChapterNo", his.getCurrentChapterNo());
        value.put("currentChapterUrl", his.getCurrentChapterUrl());
        value.put("currentPage", his.getCurrentPage());
        re = db.insert("tb_Recently", null, value);
        close();
        return re;

    }


    public long update(Book his) {
        open();
        long re = -1;
        ContentValues value = new ContentValues();
        value.put("bookId", his.getBookId());
        value.put("currentChapterNo", his.getCurrentChapterNo());
        value.put("currentChapterUrl", his.getCurrentChapterUrl());
        value.put("currentPage", his.getCurrentPage());
        re = db.update("tb_Recently", value, "bookId =" + his.getBookId(), null);
        close();
        return re;
    }


    public long delet(String pid) {
        long result = -1;
        open();
        result = db.delete("tb_Recently", " bookId=" + pid, null);
        close();
        return result;
    }


    public List<Book> getAllData() {
        open();
        Cursor res = db.query("tb_Recently", null, null, null, null, null,
                null);
        List<Book> list = conventtohistory(res);
        close();
        if (list == null || list.size() == 0) {
            return null;
        }
        BookDao bookDao = new BookDao(con);
        for (int i = 0; i < list.size(); i++) {
            bookDao.getOneBook(list.get(i));
        }
        return list;
    }

    public List<Book> getOneBook(String id) {
        open();
        Cursor res = db.query("tb_Recently", null, "bookId=" + id,
                null, null, null, null);
        List<Book> list = conventtohistory(res);
        close();
        return list;

    }

    public List<Book> conventtohistory(Cursor cu) {

        int resultnumber = cu.getCount();
        if (resultnumber == 0 || !cu.moveToFirst()) {
            return null;
        }
        List<Book> results = new ArrayList<Book>();
        for (int i = 0; i < resultnumber; i++) {
            Book p = new Book();
            p.setCurrentChapterNo(cu.getInt(1));
            p.setBookId(cu.getString(2));
            p.setCurrentChapterUrl(cu.getString(3));
            p.setCurrentPage(cu.getInt(4));
            cu.moveToNext();
            results.add(p);
        }
        return results;

    }
}
