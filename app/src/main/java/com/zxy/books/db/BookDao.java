package com.zxy.books.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zxy.books.model.Book;

public class BookDao {
    private String DB_NAME = "book.db3";
    private int DB_VERSION = 1;
    private SQLiteDatabase db;
    private Context con;
    private DbBhelper dh;

    public BookDao(Context con) {
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
        value.put("bookName", his.getBookName());
        value.put("bookAuthor", his.getBookAuthor());
        value.put("indexUrl", his.getIndexUrl());
        value.put("bookLocalPath", his.getBookLocalPath());
        value.put("type", his.getType());
        value.put("desc", his.getBookDesc());
        value.put("chapterUrl",his.getChapterUrl());
        re = db.insert("tb_Book", null, value);
        close();
        return re;

    }

    public long update(long id, Book his) {
        open();
        long re = -1;
        ContentValues value = new ContentValues();
        value.put("bookId", his.getBookId());
        value.put("bookName", his.getBookName());
        value.put("bookAuthor", his.getBookAuthor());
        value.put("bookLocalPath", his.getBookLocalPath());
        value.put("indexUrl", his.getIndexUrl());
        value.put("type", his.getType());
        value.put("chapterUrl", his.getCurrentChapterUrl());
        re = db.update("tb_Book", value, "bookId =" + id, null);
        close();
        return re;
    }

    public long delet(int pid) {
        long result = -1;
        open();
        result = db.delete("tb_Book", " bookId=" + pid, null);
        open();
        return result;
    }

    public List<Book> getAllData() {
        open();
        Cursor res = db.query("tb_Book", null, null, null, null, null, null);

        List<Book> list = conventtohistory(res);
        close();
        return list;
    }

    public List<Book> getOneBook(String id) {
        open();
        Cursor res = db.query("tb_Book", null, "bookId=" + id,
                null, null, null, null);
        List<Book> list = conventtohistory(res);
        close();
        return list;

    }

    public void getOneBook(Book book) {
        open();
        Cursor res = db.query("tb_Book", null, "bookId=" + book.getBookId(),
                null, null, null, null);
        int resultnumber = res.getCount();
        if (resultnumber != 0) {
            res.moveToFirst();
            book.setBookName(res.getString(1));
            book.setBookAuthor(res.getString(2));
            book.setBookLocalPath(res.getString(3));
            book.setIndexUrl(res.getString(4));
            book.setBookId(res.getString(5));
            book.setType(res.getString(6));
            book.setChapterUrl(res.getString(7));
            book.setBookDesc(res.getString(8));
        }
        close();
    }

    public List<Book> conventtohistory(Cursor cu) {

        int resultnumber = cu.getCount();
        if (resultnumber == 0 || !cu.moveToFirst()) {
            return null;
        }
        List<Book> results = new ArrayList<Book>();
        for (int i = 0; i < resultnumber; i++) {
            Book p = new Book();
            p.setBookName(cu.getString(1));
//            p.setBookAuthor(cu.getString(2));
//            p.setBookLocalPath(cu.getString(3));
//            p.setIndexUrl(cu.getString(4));
//            p.setBookId(cu.getString(5));
//            cu.moveToNext();
            results.add(p);
        }
        return results;

    }
}
