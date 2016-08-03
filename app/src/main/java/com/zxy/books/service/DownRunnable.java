package com.zxy.books.service;


import android.content.Context;
import android.text.TextUtils;

import com.zxy.books.model.BookChapter;
import com.zxy.books.network.ChaptersDownHelper;
public class DownRunnable implements Runnable {

    private BookChapter bkChapter;
    private boolean finished = false;
    private Context mContext;
    private ChaptersDownHelper.OnResponse onResponse;

    public DownRunnable(BookChapter _bkchaper, Context _mcontext, ChaptersDownHelper.OnResponse onResponse) {
        bkChapter = _bkchaper;
        mContext = _mcontext;
        this.onResponse = onResponse;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public void run() {
        try {
            ChaptersDownHelper dv = new ChaptersDownHelper(mContext);
            dv.setDownchapter(bkChapter);
            dv.getResponse(onResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
