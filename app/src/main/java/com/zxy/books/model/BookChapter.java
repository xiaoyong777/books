package com.zxy.books.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zxy on 2015/12/18.
 * 书籍章节类
 */
public class BookChapter implements Serializable {
    private String bookId;
    private String chapterId;
    private int chapterNo;
    private String chapterTitle;
    private String chapterUrl;
    private BookChapter lastChapter;//上一个章节信息
    private BookChapter nextChapter;//下一个章节信息
    private String chapterContext;//章节内容

    public int getChapterNo() {
        return chapterNo;
    }

    public void setChapterNo(int chapterNo) {
        this.chapterNo = chapterNo;
    }

    public String getChapterContext() {
        return chapterContext;
    }

    public void setChapterContext(String chapterContext) {
        this.chapterContext = chapterContext;
    }

    public BookChapter getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(BookChapter lastChapter) {
        this.lastChapter = lastChapter;
    }

    public BookChapter getNextChapter() {
        return nextChapter;
    }

    public void setNextChapter(BookChapter nextChapter) {
        this.nextChapter = nextChapter;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

}
