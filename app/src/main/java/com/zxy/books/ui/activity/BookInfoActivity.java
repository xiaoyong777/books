package com.zxy.books.ui.activity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zxy.books.R;
import com.zxy.books.db.BookDao;
import com.zxy.books.db.BookShelfDao;
import com.zxy.books.db.ChapterDao;
import com.zxy.books.db.RecentlyDao;
import com.zxy.books.model.Book;
import com.zxy.books.model.BookChapter;
import com.zxy.books.ui.base.BaseActivity;
import com.zxy.books.ui.fragment.BookChapterListFragment;
import com.zxy.books.ui.fragment.BookDescFragment;
import com.zxy.books.util.SnackBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 书籍详细信息页面
 */
public class BookInfoActivity extends BaseActivity implements
        BookChapterListFragment.OnListFragmentInteractionListener {
    private Toolbar toolbar;
    private Book book;
    private ViewPager mViewPager;
    private Bitmap bitmap;
    private CoordinatorLayout coordinatorLayout;
    private BookShelfDao bookShelfDao;
    private AlertDialog.Builder builder;
    ChapterDao chapterDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read, menu);
        return true;
    }

    @Override
    public void initFindViewById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }

    @Override
    public void initData() {
        bookShelfDao = new BookShelfDao(BookInfoActivity.this);
        Bundle bd = getIntent().getExtras();
        book = (Book) bd.getSerializable("book");
        bitmap = bd.getParcelable("bitmap");
    }

    @Override
    public void initView() {
        builder = new AlertDialog.Builder(BookInfoActivity.this);
        builder.setTitle(getResources().getString(R.string.alert));
        builder.setPositiveButton(getResources().getString(R.string.ok3),
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                //noinspection SimplifiableIfStatement
                if (id == R.id.readActivity_settings) {

                    if (bookShelfDao.getOneBook(book.getBookId()).size() == 0) {
                        if (bookShelfDao.insert(book.getBookId()) > 0) {
                            Toast.makeText(BookInfoActivity.this, getResources().getString(R.string.alert_ok), Toast.LENGTH_SHORT).show();

                            /**
                             * 章节信息存数据库
                             */
                            chapterDao = new ChapterDao(BookInfoActivity.this);
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
                            /**
                             * 查询书籍表是否有此书的信息 并存数据库
                             */
                            BookDao bookDao = new BookDao(BookInfoActivity.this);
                            List<Book> books = bookDao.getOneBook(book.getBookId());
                            if (books == null || books.size() == 0) {//无记录 插入数据库
                                bookDao.insert(book);
                            }
                        } else {
                            Toast.makeText(BookInfoActivity.this, getResources().getString(R.string.alert_failure), Toast.LENGTH_SHORT).show();
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (book.getBookChapters() == null) {
                    Snackbar.make(view, getResources().getString(R.string.not_chapter), Snackbar.LENGTH_SHORT).setAction(R.string.ok3, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                    return;
                }
                /**
                 * 查询阅读历史 看是否有记录 没有记录默认第一章 有记录从阅读历史开始阅读
                 */
                RecentlyDao recentlyDao = new RecentlyDao(BookInfoActivity.this);
                List<Book> books = recentlyDao.getOneBook(book.getBookId());
                if (books == null || books.size() == 0) {//查询无记录
                    if (book.getBookChapters().size() != 0) {
                        read(0, 0);
                    }
                } else {//查询有记录
                    read(books.get(0).getCurrentChapterNo(), books.get(0).getCurrentPage());
                }
            }
        });
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(book.getBookName());
        ImageView ivImage = (ImageView) findViewById(R.id.ivImage);
//        ImageLoader imageLoader = new ImageLoader(VolleyQueueController.getInstance(BookInfoActivity.this)
//                .getRequestQueue(), new BitmapCache(BookInfoActivity.this));
        ivImage.setImageBitmap(bitmap);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.desc));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.bookChaper));
        tabLayout.setupWithViewPager(mViewPager);
    }


    private void setupViewPager(ViewPager mViewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BookDescFragment.newInstance(book), getResources().getString(R.string.desc));

        adapter.addFragment(BookChapterListFragment.newInstance(book), getResources().getString(R.string.bookChaper));
        mViewPager.setAdapter(adapter);
    }


    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    /**
     * 章节列表ListFragment内的点击事件
     *
     * @param position
     */
    @Override
    public void onListFragmentInteraction(int position) {
        read(position, 0);
    }

    public void showSnackBar(String errorMessage) {
        SnackBar.getSnackbar(errorMessage, coordinatorLayout, BookInfoActivity.this).show();
    }

    private void read(int position, int page) {
        Intent it = new Intent(BookInfoActivity.this, ReadActivity.class);
        Bundle bd = new Bundle();
        bd.putSerializable("Book", book);
        bd.putInt("chapterIndex", position);
        bd.putInt("currentPage", page);
        it.putExtras(bd);
//        startActivity(it,
//                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        startActivity(it);
    }
}
