package com.zxy.books.ui.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zxy.books.R;
import com.zxy.books.db.RecentlyDao;
import com.zxy.books.ui.activity.ReadActivity;
import com.zxy.books.ui.adapter.BookAdapter;
import com.zxy.books.ui.base.BaseAdapter;
import com.zxy.books.ui.base.BaseFragment;
import com.zxy.books.model.Book;

import java.util.List;

/**
 * 最近阅读界面Fragment
 */
public class RecentlyReadFragment extends BaseFragment implements BaseAdapter.OnRecyclerViewItemClickListener {
    private List<Book> books;
    private RecyclerView mRecyclerView;
    //  private ProgressBar mProgressBar;
    private BookAdapter mBookAdapter;
    private TextView tv;
    private RecentlyDao recentlyDao;

    @Override
    public View setContentView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_recently, null);
        return view;
    }

    @Override
    protected void initFindViewById(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_recently_read_recyclerView);
        tv = (TextView) view.findViewById(R.id.fragment_recently_error_tv);
    }

    @Override
    public void initData() {
        recentlyDao = new RecentlyDao(mActivity);
        books = recentlyDao.getAllData();
    }

    @Override
    protected void initView() {
        if (books != null && books.size() != 0) {
            tv.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mBookAdapter = new BookAdapter(getActivity(), books);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mBookAdapter);
            return;
        }
        tv.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    protected void setOnClick() {
        if (mBookAdapter != null) {
            mBookAdapter.setOnItemClickListener(this);
        }

    }

    /**
     * BookAdapter的itme点击事件
     *
     * @param view
     * @param data
     */
    @Override
    public void onItemClick(View view, Object data) {
        Book book = (Book) data;
        List<Book> books = recentlyDao.getOneBook(book.getBookId());
        Intent it = new Intent(mActivity, ReadActivity.class);
        Bundle bd = new Bundle();
        book.setBookChapters(null);
        bd.putSerializable("Book", book);
        if (books == null || books.size() == 0) {//查询无记录
            if (book.getBookChapters().size() != 0) {
                bd.putInt("chapterIndex", 0);
                bd.putInt("currentPage", 0);
            }
        } else {//查询有记录
            bd.putInt("chapterIndex", books.get(0).getCurrentChapterNo());
            bd.putInt("currentPage", books.get(0).getCurrentPage());
        }
        it.putExtras(bd);
        startActivity(it);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {// Fragment被隐藏

        } else {// Fragment重新显示到最前端中
            initData();
            initView();
            setOnClick();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    protected void notifyDataSetChanged(String changed, Boolean isClose) {

    }
}
