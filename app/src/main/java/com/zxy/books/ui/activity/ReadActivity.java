package com.zxy.books.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zxy.books.R;
import com.zxy.books.db.BookDao;
import com.zxy.books.db.BookShelfDao;
import com.zxy.books.db.ChapterDao;
import com.zxy.books.db.RecentlyDao;
import com.zxy.books.model.Book;
import com.zxy.books.model.BookChapter;
import com.zxy.books.network.ChapterListNetworkHelper;
import com.zxy.books.network.volley.NetworkHelper;
import com.zxy.books.network.volley.UIDataListener;
import com.zxy.books.ui.adapter.DowmDialogAdapter;
import com.zxy.books.ui.base.BaseActivity;
import com.zxy.books.ui.view.ReadChapterDialog;
import com.zxy.books.ui.view.CustomPageView;
import com.zxy.books.ui.view.ReadDowmDialog;
import com.zxy.books.ui.view.ReadSettingDialog;
import com.zxy.books.util.ColorUtil;
import com.zxy.books.util.ConfigUtil;
import com.zxy.books.util.ScreenUtil;

import java.util.List;

public class ReadActivity extends BaseActivity {
    private Book book;
    private int index;//当前需要阅读的章节序号
    private Toolbar toolbar;
    private CardView cardViewMenu;
    private CustomPageView customPageView;
    public ProgressBar progressBar;
    public TextView tvread_bookTitle;
    public TextView tvread_chapterTtile;
    public TextView tvread_progress;
    //   private TextView tvread_system;
    private ImageButton btDaynight;
    private ImageButton btSettings;
    private ImageButton btDow;
    private ImageButton btChapter;
    public static CoordinatorLayout coordinatorLayout;
    private int mPage;
    private RecentlyDao recentlyDao;
    private ChapterDao chapterDao;
    private BookShelfDao bookShelfDao;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        /**
         * 查询书籍表是否有此书的信息 并存数据库
         */
        BookDao bookDao = new BookDao(ReadActivity.this);
        List<Book> books = bookDao.getOneBook(book.getBookId());
        if (books == null || books.size() == 0) {//无记录 插入数据库
            bookDao.insert(book);
        }
        /**
         * 查询阅读历史表是否有此书的信息 并存数据库
         */
        books = recentlyDao.getOneBook(book.getBookId());
        if (books == null || books.size() == 0) {//无记录 插入数据库
            recentlyDao.insert(book);
        }
        List<BookChapter> bookChapters = chapterDao.getBookChapter(book.getBookId());
        if (bookChapters == null || bookChapters.size() == 0) {
            if (book.getBookChapters() != null && book.getBookChapters().size() != 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < book.getBookChapters().size(); i++) {
                            chapterDao.insert(book.getBookChapters().get(i));
                        }
                    }
                }).start();
            }
        }
    }

    @Override
    protected void onPause() {
        if (customPageView.getCurrentChapter() == null) {

        } else {
            book.setCurrentChapterUrl(customPageView.getCurrentChapter().getChapterUrl());
            book.setCurrentPage(customPageView.getmPage());
            book.setCurrentChapterNo(customPageView.getCurrentChapter().getChapterNo());
            recentlyDao.update(book);
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (customPageView.isMenuInvisible()) {
            super.onBackPressed();
        } else {
            customPageView.setIsMenuInvisible(true);
            showMenu();
        }
    }

    @Override
    public void initFindViewById() {
        cardViewMenu = (CardView) findViewById(R.id.cardViewMenu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        customPageView = (CustomPageView) findViewById(R.id.customPageView);
        progressBar = (ProgressBar) findViewById(R.id.read_progressBar);
        tvread_bookTitle = (TextView) findViewById(R.id.read_bookTitle);
        tvread_chapterTtile = (TextView) findViewById(R.id.read_chapterTtile);
        tvread_progress = (TextView) findViewById(R.id.read_progress);
        btDaynight = (ImageButton) findViewById(R.id.read_nav_menu_daynight);
        btSettings = (ImageButton) findViewById(R.id.read_nav_menu_settings);
        btDow = (ImageButton) findViewById(R.id.read_nav_menu_dow);
        btChapter = (ImageButton) findViewById(R.id.read_nav_menu_chapter);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        coordinatorLayout.setBackgroundResource(ColorUtil.readbg[ConfigUtil.getReadBg(ReadActivity.this)]);//设置背景颜色
    }

    @Override
    public void initView() {
        // StatusBarCompat.compat(this);
        builder = new AlertDialog.Builder(ReadActivity.this);
        builder.setTitle(getResources().getString(R.string.alert));
        builder.setPositiveButton(getResources().getString(R.string.ok3),
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        toolbar.setTitle(book.getBookName());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvread_bookTitle.setText(book.getBookName());
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                //noinspection SimplifiableIfStatement
                if (id == R.id.readActivity_settings) {
                    if (bookShelfDao.getOneBook(book.getBookId()).size() == 0) {
                        if (bookShelfDao.insert(book.getBookId()) > 0) {
                            Toast.makeText(ReadActivity.this, getResources().getString(R.string.alert_ok), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ReadActivity.this, getResources().getString(R.string.alert_failure), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        builder.setMessage(getResources().getString(R.string.alert_repeat));
                        builder.show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read, menu);
        return true;
    }

    @Override
    public void initData() {
        recentlyDao = new RecentlyDao(ReadActivity.this);
        chapterDao = new ChapterDao(ReadActivity.this);
        bookShelfDao = new BookShelfDao(ReadActivity.this);
        Bundle bd = getIntent().getExtras();
        book = (Book) bd.getSerializable("Book");
        index = bd.getInt("chapterIndex");
        mPage = bd.getInt("currentPage");
        book.setBookChapters(chapterDao.getBookChapter(book.getBookId()));
        if (book.getBookChapters() == null || book.getBookChapters().size() == 0) {
            NetworkHelper<List<BookChapter>> networkHelper = new ChapterListNetworkHelper(ReadActivity.this, book);
            networkHelper.setUiDataListener(new UIDataListener<List<BookChapter>>() {
                @Override
                public void onDataChanged(List<BookChapter> data) {
                    book.setBookChapters(data);
                    customPageView.setBookChapter(ReadActivity.this, book.getBookChapters().get(index), mPage);
                    customPageView.setChapterUrlHead(book.getChapterUrlHead());
                }

                @Override
                public void onErrorHappened(String errorMessage) {
                    Toast.makeText(ReadActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
            networkHelper.sendGETRequest(book.getChapterUrl(), null);
        } else {
            customPageView.setBookChapter(this, book.getBookChapters().get(index), mPage);
            customPageView.setChapterUrlHead(book.getChapterUrlHead());
        }
        List<BookChapter> bookChapters = chapterDao.getBookChapter(book.getBookId());
        if (bookChapters == null || bookChapters.size() == 0) {
            if (book.getBookChapters() != null && book.getBookChapters().size() != 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < book.getBookChapters().size(); i++) {
                            chapterDao.insert(book.getBookChapters().get(i));
                        }
                    }
                }).start();
            }
        }
    }

    @Override
    public void setOnClick() {
        btDaynight.setOnClickListener(this);
        btSettings.setOnClickListener(this);
        btDow.setOnClickListener(this);
        btChapter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String str = "";
        switch (v.getId()) {
            case R.id.read_nav_menu_daynight:
                str = "夜间模式";
                break;
            case R.id.read_nav_menu_settings:
                ReadSettingDialog settingDialog = new ReadSettingDialog(ReadActivity.this, mHandler);
                settingDialog.show();
                break;
            case R.id.read_nav_menu_dow:
                ReadDowmDialog readDowmDialog = new ReadDowmDialog(ReadActivity.this, book.getBookChapters(), customPageView.getCurrentChapter(), book);
                readDowmDialog.show();
                break;
            case R.id.read_nav_menu_chapter:
                ReadChapterDialog chapterDialog = new ReadChapterDialog(ReadActivity.this, book.getBookChapters(),
                        customPageView.getCurrentChapter(), mHandler);
                chapterDialog.show();
                break;
        }
        hideMenu();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100001) {//数据随便设的。。。
                index = msg.arg1;
                customPageView.setBookChapter(ReadActivity.this, book.getBookChapters().get(index), 1);
                //   customPageView.setCurrentChapterNo(index + 1);
            } else if (msg.what == 100002) {
                customPageView.updateConfig();
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 菜单和toolbar显示出来
     */
    public void showMenu() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        cardViewMenu.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 菜单和toolbar隐藏
     */
    public void hideMenu() {
        //
        cardViewMenu.animate().translationY(ScreenUtil.getScreenHeight(this)).setInterpolator(new AccelerateInterpolator(2)).start();
        toolbar.animate().translationY(-(toolbar.getHeight() + ScreenUtil.getStatusBarHeight(ReadActivity.this))).setInterpolator(new AccelerateInterpolator(2)).start();
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        //   | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        //   | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //   | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
        //   | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
        //   | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
