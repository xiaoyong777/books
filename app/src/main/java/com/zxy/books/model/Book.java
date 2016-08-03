package com.zxy.books.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxy on 2015/12/10.
 * 书籍实体类
 */
public class Book implements Serializable {
    private String bookId;// 编号 用uuid
    private String bookName;//书名
    private String bookAuthor;//作者
    private String updataTime;//更新时间
    private String lastChapter;//最新章节
    /**
     * 封面路径改用通过书籍id拼凑
     */
    //    private String bookLocalImage;//书籍封面本地路径
    //    private String bookOnlineImage;//书籍封面网络路径
    private String bookDesc;//书籍简介
    private String bookLocalPath;//书籍本地路径
    private String indexUrl;// 书籍详细信息主页面地址

    private String bookProgress;//阅读进度
    private List<BookChapter> bookChapters;//书籍章节
    private String chapterUrl;//章节地址
    private String chapterUrlHead;//章节地址头
    private String Finnish;// 是否完结
    private String type;//所属类型
    private String chapterContext;// 章节内容
    private String currentChapterUrl;//当前阅读章节url地址
    private int currentChapterNo;//当前阅读章节进度
    private int currentPage;//当前阅读进度

    public String getChapterUrlHead() {
        return chapterUrlHead;
    }

    public void setChapterUrlHead(String chapterUrlHead) {
        this.chapterUrlHead = chapterUrlHead;
    }

    public int getCurrentChapterNo() {
        return currentChapterNo;
    }

    public void setCurrentChapterNo(int currentChapterNo) {
        this.currentChapterNo = currentChapterNo;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getCurrentChapterUrl() {
        return currentChapterUrl;
    }

    public void setCurrentChapterUrl(String currentChapterUrl) {
        this.currentChapterUrl = currentChapterUrl;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChapterContext() {
        return chapterContext;
    }

    public void setChapterContext(String chapterContext) {
        this.chapterContext = chapterContext;
    }

    public String getFinnish() {
        return Finnish;
    }

    public void setFinnish(String finnish) {
        Finnish = finnish;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

//    public String getBookLocalImage() {
//        return bookLocalImage;
//    }
//
//    public void setBookLocalImage(String bookLocalImage) {
//        this.bookLocalImage = bookLocalImage;
//    }
//
//    public String getBookOnlineImage() {
//        return bookOnlineImage;
//    }
//
//    public void setBookOnlineImage(String bookOnlineImage) {
//        this.bookOnlineImage = bookOnlineImage;
//    }

    public void setUpdataTime(String updataTime) {
        this.updataTime = updataTime;
    }

    public List<BookChapter> getBookChapters() {
        return bookChapters;
    }

    public void setBookChapters(List<BookChapter> bookChapters) {
        this.bookChapters = bookChapters;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public void setUpdatatime(String updataTime) {
        this.updataTime = updataTime;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    public void setBookLocalPath(String bookLocalPath) {
        this.bookLocalPath = bookLocalPath;
    }

    public void setBookProgress(String bookProgress) {
        this.bookProgress = bookProgress;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getUpdataTime() {
        return updataTime;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public String getBookLocalPath() {
        return bookLocalPath;
    }

    public String getBookProgress() {
        return bookProgress;
    }
}
