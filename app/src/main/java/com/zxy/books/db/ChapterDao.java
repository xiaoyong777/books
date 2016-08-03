package com.zxy.books.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zxy.books.model.BookChapter;

public class ChapterDao {
    private String DB_NAME1 = "book.db3";
    private int DB_VERSION = 1;
    private SQLiteDatabase db;
    private Context con;
    private DbBhelper dh;

    public ChapterDao(Context con) {
        super();
        this.con = con;
    }

    public void open() {
        dh = new DbBhelper(con, DB_NAME1, null, DB_VERSION);
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


    public long insert(BookChapter his) {
        long re = -1;
        open();
        ContentValues value = new ContentValues();
        value.put("bookId", his.getBookId());
        value.put("chapterNo", his.getChapterNo());
        value.put("chapterId", his.getChapterId());
        value.put("chapterTitle", his.getChapterTitle());
        value.put("chapterUrl", his.getChapterUrl());
        re = db.insert("tb_Chapter", null, value);
        close();
        return re;

    }

//
//    public long update(long id, BookChapter his) {
//        open();
//        long re = -1;
//        ContentValues value = new ContentValues();
//        value.put("bookId", his.getBookId());
//        value.put("chapterNo", his.getChapterNo());
//        value.put("chapterId", his.getChapterId());
//        value.put("chapterTitle", his.getChapterTitle());
//        value.put("chapterUrl", his.getChapterUrl());
//        re = db.update("tb_Chapter", value, "bookId =" + id, null);
//        close();
//        return re;
//    }

    public long delet(String bookId) {
        long result = -1;
        open();
        result = db.delete("tb_Chapter", "bookId=" + bookId, null);
        close();
        return result;
    }

//    public List<BookChapter> getallinfo() {
//        open();
//        Cursor res = db.query("tb_Chapter", null, null, null, null, null,
//                null);
//        List<BookChapter> list = conventtochapter(res);
//        close();
//        return list;
//    }


    public List<BookChapter> getBookChapter(String bookId) {
        open();
        Cursor res = db.query("tb_Chapter", null, "bookId=" + bookId,
                null, null, null, null);
        List<BookChapter> list = conventtochapter(res);
        close();
        return list;

    }

    public List<BookChapter> conventtochapter(Cursor cu) {
        List<BookChapter> results = new ArrayList<BookChapter>();
        int resultnumber = cu.getCount();
        if (resultnumber == 0 || !cu.moveToFirst()) {
            return results;
        }

        for (int i = 0; i < resultnumber; i++) {
            BookChapter p = new BookChapter();
            p.setBookId(cu.getString(1));
            p.setChapterNo(cu.getInt(2));
            p.setChapterId(cu.getString(3));
            p.setChapterTitle(cu.getString(4));
            p.setChapterUrl(cu.getString(5));
            cu.moveToNext();
            if (results.size() != 0) {//如果章节列表不为空
                p.setLastChapter(results.get(results.size() - 1));//将上一个章节给本章节LastChapter
                results.get(results.size() - 1).setNextChapter(p);//将本章节给上一个章节的NextChapter
            }
            results.add(p);
        }
        return results;

    }
}
