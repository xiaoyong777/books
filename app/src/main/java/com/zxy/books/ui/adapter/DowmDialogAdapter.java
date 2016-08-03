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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxy on 2015/12/30.
 * 下载itme
 */
public class DowmDialogAdapter extends BaseAdapter {
    private Context context;
    private List<String> dowmStrings;

    public DowmDialogAdapter(Context context) {
        this.context = context;
        dowmStrings = new ArrayList<>();
        dowmStrings.add(context.getResources().getString(R.string.dowm50));
        dowmStrings.add(context.getResources().getString(R.string.dowm100));
        dowmStrings.add(context.getResources().getString(R.string.dowmunread));
        dowmStrings.add(context.getResources().getString(R.string.dowmall));
    }

    @Override
    public void showData(RecyclerView.ViewHolder viewHolder, int position) {
        DowmDialogRecyclerViewHolder holder = (DowmDialogRecyclerViewHolder) viewHolder;
        holder.itemView.setTag(position);
        holder.tvDowmTitle.setText(dowmStrings.get(position));
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_dowm_dialog, viewGroup, false);
        v.setOnClickListener(this);
        return v;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return new DowmDialogRecyclerViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return dowmStrings.size();
    }

    public class DowmDialogRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvDowmTitle;

        public DowmDialogRecyclerViewHolder(View v) {
            super(v);
            tvDowmTitle = (TextView) v.findViewById(R.id.content);
        }
    }
}
