package com.zxy.books.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbBhelper extends SQLiteOpenHelper {
    /**
     * 书籍信息表建表语句
     */
    private final String DB_BOOK_CREATE = "create table tb_Book"
            + "(Id integer primary key autoincrement,"
            + "bookName text not null,bookAuthor text not null,bookLocalPath text "
            + ",indexUrl text,bookId text not null,type text not null,chapterUrl text,desc text);";
    /**
     * 书架表
     */
    private final String DB_BOOKSHELF_CREATE = "create table tb_Bookshelf"
            + "(Id integer primary key autoincrement,"
            + "bookId text not null);";
    /**
     * 阅读历史
     */
    private final String DB_RECENTLY_CREATE = "create table tb_Recently"
            + "(Id integer primary key autoincrement,"
            + "currentChapterNo integer,bookId text not null,currentChapterUrl text "
            + ",currentPage integer);";
    /**
     * 章节信息表
     */
    private final String DB_CHAPTER_CERATE = "create table tb_Chapter"
            + "(id integer primary key autoincrement,"
            + "bookId text integer not null,chapterNo integer not null,"
            + "chapterId text not null,chapterTitle text,chapterUrl text)";

    public DbBhelper(Context context, String name, CursorFactory factory,
                     int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DB_BOOK_CREATE);
        db.execSQL(DB_BOOKSHELF_CREATE);
        db.execSQL(DB_RECENTLY_CREATE);
        db.execSQL(DB_CHAPTER_CERATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }
}