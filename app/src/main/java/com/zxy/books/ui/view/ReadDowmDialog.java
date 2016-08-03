package com.zxy.books.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.zxy.books.R;
import com.zxy.books.model.Book;
import com.zxy.books.model.BookChapter;
import com.zxy.books.service.DownService;
import com.zxy.books.ui.adapter.DowmDialogAdapter;
import com.zxy.books.ui.base.BaseAdapter;

import java.util.List;

/**
 * Created by zxy on 2015/12/30.
 * 下载对话框
 */
public class ReadDowmDialog extends Dialog implements BaseAdapter.OnRecyclerViewItemClickListener {
    private Context context;
    private List<BookChapter> chapterList;
    private BookChapter currentChapter;
    private RecyclerView recyclerView;
    private DowmDialogAdapter dowmDialogAdapter;
    private ImageButton tbClose;
    private Book book;

    public ReadDowmDialog(Context context, List<BookChapter> chapterList, BookChapter currentChapter, Book book) {
        super(context, R.style.Dialog);
        this.book = book;
        this.context = context;
        this.chapterList = chapterList;
        this.currentChapter = currentChapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View customerLayout =
                inflater.inflate(R.layout.dialog_read_dowm, null);
        recyclerView = (RecyclerView) customerLayout.findViewById(R.id.list);
        tbClose = (ImageButton) customerLayout.findViewById(R.id.close);
        tbClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadDowmDialog.this.dismiss();
            }
        });
        dowmDialogAdapter = new DowmDialogAdapter(context);
        dowmDialogAdapter.setOnItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dowmDialogAdapter);
        setContentView(customerLayout);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(View view, Object data) {
        int index = (int) data;
        Intent it = new Intent(context, DownService.class);
        Bundle bd = new Bundle();
        bd.putSerializable("book", book);
        if (index == 0) {
            bd.putInt("startNo", currentChapter.getChapterNo());
            bd.putInt("endNo", currentChapter.getChapterNo() + 50);
        } else if (index == 1) {
            bd.putInt("startNo", currentChapter.getChapterNo());
            bd.putInt("endNo", currentChapter.getChapterNo() + 100);
        } else if (index == 2) {
            bd.putInt("startNo", currentChapter.getChapterNo());
            bd.putInt("endNo", book.getBookChapters().size()-1);
        } else if (index == 3) {
            bd.putInt("startNo", 0);
            bd.putInt("endNo", book.getBookChapters().size()-1);
        }
        it.putExtra("bd", bd);
        context.startService(it);
        dismiss();
    }
}
