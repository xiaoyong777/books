package com.zxy.books.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zxy.books.R;
import com.zxy.books.model.Book;
import com.zxy.books.network.BookSearchNetworkHelper;
import com.zxy.books.ui.image.BookImageView;
import com.zxy.books.network.volley.NetworkHelper;
import com.zxy.books.network.volley.UIDataListener;
import com.zxy.books.ui.adapter.BookAdapter;
import com.zxy.books.ui.base.BaseActivity;
import com.zxy.books.ui.base.BaseAdapter;
import com.zxy.books.util.SnackBar;

import java.net.URLEncoder;
import java.util.List;

/**
 * 网上书库书籍查询或分类榜单列表
 */
public class BookListActivity extends BaseActivity implements UIDataListener<List<Book>>,
        BaseAdapter.OnRecyclerViewItemClickListener {
    private String bookType;
    private String serchBookName;
    private String title;
    private RecyclerView bookListRecyclerView;
    private BookAdapter bookAdapter;
    private ProgressBar booklistprogressBar;
    private NetworkHelper<List<Book>> networkHelper;
    private String searchUrl = "http://www.7kankan.com/modules/article/sosearch.php?searchtype=articlename&searchkey=";
    private List<Book> bookList;
    private String typeUrl = "http://www.7kankan.com/files/article/";
    private String url = "";
    private String[] typeName;
    private String[] topName;
    private TextView tvnotsearch;
    private CoordinatorLayout coordinatorLayout;//父容器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        typeName = getResources().getStringArray(R.array.string_array_online_leaderboard);
        topName = getResources().getStringArray(R.array.string_array_online_sort);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        if (bookList == null || bookList.size() == 0) {//bookList为空  界面刷新
            networkHelper.sendGETRequest(url, null);
            booklistprogressBar.setVisibility(View.VISIBLE);
            tvnotsearch.setVisibility(View.GONE);
        }
        super.onRestart();
    }

    @Override
    public void initFindViewById() {
        booklistprogressBar = (ProgressBar) findViewById(R.id.book_list_progressBar);
        bookListRecyclerView = (RecyclerView) findViewById(R.id.book_list_fileRecyclerView);
        tvnotsearch = (TextView) findViewById(R.id.boo_list_notsearch_tv);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        booklistprogressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        networkHelper = new BookSearchNetworkHelper(this); // 实例化
        networkHelper.setUiDataListener(this);
        bookType = getIntent().getStringExtra("bookType");
        if (!TextUtils.isEmpty(bookType)) {
            title = bookType;
            /**
             * 类型查找
             */
            if (bookType.equals(getResources().getString(R.string.top_allvisit))) {//总榜
                url = typeUrl + "topallvisit/0/1.htm";
            } else if (bookType.equals(getResources().getString(R.string.top_allvote))) {//总推荐
                url = typeUrl + "topallvote/0/1.htm";
            } else if (bookType.equals(getResources().getString(R.string.top_goodnum))) {//收藏榜
                url = typeUrl + "topgoodnum/0/1.htm";
            } else if (bookType.equals(getResources().getString(R.string.top_postdate))) {//新书
                url = typeUrl + "toppostdate/0/1.htm";
            } else if (bookType.equals(getResources().getString(R.string.sort1))) {//玄幻奇幻
                url = typeUrl + "sort1/0/1.htm";
            } else if (bookType.equals(getResources().getString(R.string.sort2))) {//都市言情
                url = typeUrl + "sort3/0/1.htm";
            } else if (bookType.equals(getResources().getString(R.string.sort3))) {//武侠修仙
                url = typeUrl + "sort2/0/1.htm";
            } else if (bookType.equals(getResources().getString(R.string.sort4))) {//历史军事
                url = typeUrl + "sort4/0/1.htm";
            } else if (bookType.equals(getResources().getString(R.string.sort6))) {//科幻灵异
                url = typeUrl + "sort6/0/1.htm";
            }
            networkHelper.sendGETRequest(url, null);
            return;
        }
        serchBookName = getIntent().getStringExtra("bookName");
        if (!TextUtils.isEmpty(serchBookName)) {
            title = serchBookName;
            try {
                url = searchUrl + URLEncoder.encode(serchBookName, "gbk");
            } catch (Exception e) {
                e.printStackTrace();
            }
            networkHelper.sendGETRequest(url, null);
        }
    }


    @Override
    public void onItemClick(View view, Object data) {
        Book book = (Book) data;
        Intent intent = new Intent(BookListActivity.this, BookInfoActivity.class);
        ImageView imageView = (BookImageView) view.findViewById(R.id.book_item_ivBook);
        imageView.setDrawingCacheEnabled(true);
        Bundle bd = new Bundle();
        bd.putSerializable("book", book);
        bd.putParcelable("bitmap", Bitmap.createBitmap(imageView.getDrawingCache()));
        intent.putExtras(bd);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(BookListActivity.this,
                        view.findViewById(R.id.book_item_ivBook), getString(R.string.transition_book_img));
        ActivityCompat.startActivity(BookListActivity.this, intent, options.toBundle());
    }

    @Override
    public void onDataChanged(List<Book> data) {
        booklistprogressBar.setVisibility(View.GONE);
        if (data.size() != 0) {
            tvnotsearch.setVisibility(View.GONE);
            bookList = data;
            bookAdapter = new BookAdapter(BookListActivity.this, bookList);
            bookAdapter.setOnItemClickListener(this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(BookListActivity.this);
            bookListRecyclerView.setLayoutManager(layoutManager);
            bookListRecyclerView.setAdapter(bookAdapter);
        } else {
            tvnotsearch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onErrorHappened(String errorMessage) {
        booklistprogressBar.setVisibility(View.GONE);
        tvnotsearch.setVisibility(View.VISIBLE);
        tvnotsearch.setText(R.string.error);
        SnackBar.getSnackbar(errorMessage, coordinatorLayout, BookListActivity.this).show();
    }

}
