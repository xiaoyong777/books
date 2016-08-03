package com.zxy.books.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxy.books.R;
import com.zxy.books.model.BookChapter;
import com.zxy.books.ui.base.BaseAdapter;

import java.util.List;

/**
 * Created by zxy on 2015/12/30.
 * 章节显示对话框 Adapter
 */
public class ChapterDialogAdapter extends BaseAdapter {
    private Context context;
    private List<BookChapter> chapterList;

    public ChapterDialogAdapter(Context context, List<BookChapter> chapterList) {
        this.context = context;
        this.chapterList = chapterList;
    }

    @Override
    public void showData(RecyclerView.ViewHolder viewHolder, int position) {
        ChapterDialogRecyclerViewHolder holder = (ChapterDialogRecyclerViewHolder) viewHolder;
        holder.itemView.setTag(position);
        holder.tvchapterTitle.setText(chapterList.get(position).getChapterTitle());
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_chapter_dialog, viewGroup, false);
        v.setOnClickListener(this);
        return v;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return new ChapterDialogRecyclerViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class ChapterDialogRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvchapterTitle;

        public ChapterDialogRecyclerViewHolder(View v) {
            super(v);
            tvchapterTitle = (TextView) v.findViewById(R.id.content);
        }
    }
}
