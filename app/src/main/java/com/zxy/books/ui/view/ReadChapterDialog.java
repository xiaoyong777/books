package com.zxy.books.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zxy.books.R;
import com.zxy.books.model.BookChapter;
import com.zxy.books.ui.adapter.ChapterDialogAdapter;
import com.zxy.books.ui.base.BaseAdapter;

import java.util.List;

/**
 * Created by zxy on 2015/12/30.
 * 章节显示对话框
 */
public class ReadChapterDialog extends Dialog implements BaseAdapter.OnRecyclerViewItemClickListener {
    private Context context;
    private List<BookChapter> chapterList;
    private BookChapter currentChapter;
    private TextView tvCurrChapter;
    private RecyclerView recyclerView;
    private ChapterDialogAdapter chapterDialogAdapter;
    private ImageButton tbClose;
    private Handler mHandler;

    public ReadChapterDialog(Context context, List<BookChapter> chapterList, BookChapter currentChapter, Handler mHandler) {
        super(context, R.style.Dialog);
        this.mHandler = mHandler;
        this.context = context;
        this.chapterList = chapterList;
        this.currentChapter = currentChapter;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View customerLayout =
                inflater.inflate(R.layout.dialog_read_chapter, null);
        tvCurrChapter = (TextView) customerLayout.findViewById(R.id.chapter_dialog_currchapter);
        recyclerView = (RecyclerView) customerLayout.findViewById(R.id.list);
        tbClose = (ImageButton) customerLayout.findViewById(R.id.close);
        if (currentChapter == null) {
        } else
            tvCurrChapter.setText(context.getResources().getString(R.string.currentChapter) + ":" + currentChapter.getChapterTitle());
        tbClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadChapterDialog.this.dismiss();
            }
        });
        if (chapterList != null) {
            chapterDialogAdapter = new ChapterDialogAdapter(context, chapterList);
            chapterDialogAdapter.setOnItemClickListener(this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(chapterDialogAdapter);
        }
        setContentView(customerLayout);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(View view, Object data) {
        Message msg = new Message();
        msg.what = 100001;
        msg.arg1 = (int) data;
        mHandler.sendMessage(msg);
        dismiss();
    }
}
