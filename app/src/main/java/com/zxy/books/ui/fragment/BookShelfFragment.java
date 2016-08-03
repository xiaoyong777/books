package com.zxy.books.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zxy.books.R;
import com.zxy.books.db.BookShelfDao;
import com.zxy.books.db.ChapterDao;
import com.zxy.books.model.Book;
import com.zxy.books.ui.activity.BookInfoActivity;
import com.zxy.books.ui.adapter.BookShelfAdapter;
import com.zxy.books.ui.base.BaseFragment;
import com.zxy.books.ui.image.BookImageView;

import java.util.List;

/**
 * 书架界面Fragment
 */
public class BookShelfFragment extends BaseFragment {
    private List<Book> books;
    private RecyclerView mRecyclerView;
    private TextView textView;
    private BookShelfDao bookShelfDao;
    private BookShelfAdapter bookShelfAdapter;

    @Override
    public View setContentView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_bookshelf, null);
        return view;
    }

    @Override
    protected void initFindViewById(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_bookshelf_RecyclerView);
        textView = (TextView) view.findViewById(R.id.bookshelf_not);
    }

    @Override
    public void initData() {
        bookShelfDao = new BookShelfDao(mActivity);
        books = bookShelfDao.getAllData();
    }

    @Override
    protected void initView() {
        if (books != null && books.size() != 0) {
            textView.setVisibility(View.GONE);
            mRecyclerView.setHasFixedSize(true);
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
            mRecyclerView.setLayoutManager(layoutManager);
            bookShelfAdapter = new BookShelfAdapter(mActivity, books);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(bookShelfAdapter);
            return;
        }
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setOnClick() {
        if (bookShelfAdapter != null) {
            bookShelfAdapter.setOnItemClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(View view, Object data) {
        Book book = (Book) data;
        Intent intent = new Intent(mActivity, BookInfoActivity.class);
        ImageView imageView = (BookImageView) view.findViewById(R.id.history_item_ivBook);
        imageView.setDrawingCacheEnabled(true);
        Bundle bd = new Bundle();
        book.setBookChapters(null);
        bd.putSerializable("book", book);
        bd.putParcelable("bitmap", Bitmap.createBitmap(imageView.getDrawingCache()));
        intent.putExtras(bd);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                        view.findViewById(R.id.history_item_ivBook), getString(R.string.transition_book_img));
        ActivityCompat.startActivity(mActivity, intent, options.toBundle());

    }

    @Override
    protected void notifyDataSetChanged(String changed, Boolean isClose) {

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
}
