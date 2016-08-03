package com.zxy.books.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxy.books.R;
import com.zxy.books.model.Book;
import com.zxy.books.ui.base.BaseAdapter;

import java.util.List;

/**
 * Created by zxy on 2015/12/17.
 * 搜索结果Adapter
 * <p/>
 * 被弃用  搜索结果直接跳转界面
 */
public class SearchResultAdapter extends BaseAdapter {
    List<Book> bookList;
    Context context;

    public SearchResultAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @Override
    public void showData(RecyclerView.ViewHolder viewHolder, int position) {
        Book book = bookList.get(position);
        SearchResultViewHolder holder = (SearchResultViewHolder) viewHolder;
        holder.tvbookAuthor.setText(book.getBookAuthor());
        holder.tvbookName.setText(book.getBookName());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return new SearchResultViewHolder(view);
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_search_result, viewGroup, false);
        v.setOnClickListener(this);
        return v;
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView tvbookName;
        private TextView tvbookAuthor;

        public SearchResultViewHolder(View v) {
            super(v);
            tvbookName = (TextView) v.findViewById(R.id.search_result_item_primary);
            tvbookAuthor = (TextView) v.findViewById(R.id.search_result_item_secondary);
        }
    }
}
