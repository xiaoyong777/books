package com.zxy.books.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.zxy.books.R;
import com.zxy.books.model.Book;
import com.zxy.books.model.BookChapter;
import com.zxy.books.network.ChaptersDownHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownService extends Service implements ChaptersDownHelper.OnResponse {

    private List<BookChapter> bookChapters;
    private Book book;
    private int startNo;
    private int endNo;
    private NotificationManager manager = null;
    private final int NOTIFY_ID = 11;
    private int curr;
    private int downLenght;
    private List<Thread> downList;
    private Notification.Builder notification;

    public DownService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bd = intent.getBundleExtra("bd");
        book = (Book) bd.getSerializable("book");
        startNo = bd.getInt("startNo");
        endNo = bd.getInt("endNo");
        bookChapters = book.getBookChapters();
        if (bookChapters.size() < endNo) {
            endNo = bookChapters.size() - 1;
        }
        downLenght = endNo - startNo;
        down();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void down() {
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
        notification = new Notification.Builder(DownService.this);
        notification.setContentIntent(contentIntent);
        notification.setTicker(getResources().getString(R.string.NotificationContentTitle)); //设置状态栏的显示的信息
        notification.setWhen(System.currentTimeMillis());//设置时间发生时间
        notification.setAutoCancel(true);//设置可以清除
        notification.setSmallIcon(R.drawable.ic_dowm_notification);//设置状态栏里面的图标（小图标）
        notification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        notification.setContentTitle(bookChapters.get(startNo).getChapterNo() + " ～ " + bookChapters.get(endNo).getChapterNo() + getResources().getString(R.string.NotificationContentTitle))//设置下拉列表里的标题
                .setContentText("0/" + downLenght);//设置上下文内容
        manager.notify(NOTIFY_ID, notification.build());
        int LOADING_THREADS = 3;
        int bookChapterLenght = 0;
        if (bookChapters == null || bookChapters.size() == 0) {
            return;
        }
        bookChapterLenght = bookChapters.size();
        if (bookChapterLenght <= 100) {
            LOADING_THREADS = 2;
        } else if (bookChapterLenght <= 1000) {
            LOADING_THREADS = 3;
        } else {
            LOADING_THREADS = 5;
        }
        ExecutorService threadPool = Executors
                .newFixedThreadPool(LOADING_THREADS);//建立一个线程池
        for (curr = startNo; curr < endNo; curr++) {
            BookChapter bookChapter = bookChapters.get(curr);
            DownRunnable chapterDown = new DownRunnable(bookChapter,
                    DownService.this, this);
            Thread t = new Thread(chapterDown);
            threadPool.execute(t);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private int failure = 0;
    private int success = 0;

    @Override
    public void Failure() {
        failure++;
        notificationShow();
    }

    @Override
    public void Success() {
        success++;
        notificationShow();
    }

    private void notificationShow() {
        notification//设置下拉列表里的标题
                .setContentText((failure + success) + "/" + downLenght);//设置上下文内容
        manager.notify(NOTIFY_ID, notification.build());
        if ((failure + success) == downLenght) {
            notification.setContentText(getResources().getString(R.string.dowmend));
            notification.build().defaults = Notification.DEFAULT_SOUND;
            notification.setTicker(getResources().getString(R.string.dowmend));
            notification.setSmallIcon(R.drawable.ic_check);
            manager.notify(NOTIFY_ID, notification.build());
            success = 0;
            failure = 0;
            Intent iService = new Intent(getApplicationContext(), DownService.class);
            iService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().stopService(iService);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("zxy","onDestroy");
    }
}
