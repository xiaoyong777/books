package com.zxy.books.ui.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.books.R;
import com.zxy.books.model.LocalFile;
import com.zxy.books.ui.base.BaseAdapter;
import com.zxy.books.util.AnimationUtil;

import java.util.List;

/**
 * Created by zxy on 2015/12/16.
 * 本地文件 适配器
 */
public class LocalFileAdapter extends BaseAdapter {
    private List<LocalFile> fileList;
    private Context context;
    private int lastAnimatedPosition = -1;//lastAnimatedPosition表示最后执行动画的itme序号

    public LocalFileAdapter(Context context, List<LocalFile> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    @Override
    public void showData(RecyclerView.ViewHolder viewHolder, int position) {

        LocalFile localFile = fileList.get(position);

        //holder类型转换
        LocalFileRecyclerViewHolder holder = (LocalFileRecyclerViewHolder) viewHolder;
        //初始化动画
        if (position > lastAnimatedPosition) {//lastAnimatedPosition表示最后执行动画的itme序号
            lastAnimatedPosition = position;
            AnimationUtil.runEnterAnimationX(holder.itemView, position, context);
        }
        viewHolder.itemView.setTag(localFile);
        int[] fileIco = localFile.getFileIco();
        holder.fileIco.setImageResource(fileIco[0]);
        /**
         * 修改shape背景颜色
         */
        GradientDrawable myGrad = (GradientDrawable) holder.fileIco.getBackground();
        myGrad.setColor(context.getResources().getColor(fileIco[1]));
        holder.fileName.setText(localFile.getFileName());
        holder.lastModified.setText(localFile.getLastModified() + localFile.getFileSizeOreEND());
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_local_file, viewGroup, false);
        v.setOnClickListener(this);
        return v;
    }

//    public void notifyData(List<LocalFile> fileList) {
//        this.fileList.clear();
//        this.fileList.addAll(fileList);
//        notifyDataSetChanged();
//    }

    //    public void addItem(){
//
//    }
//    public void add(ViewModel item, int position) {
//        items.add(position, item);
//        notifyItemInserted(position);
//    }
//
//    public void remove(ViewModel item) {
//        int position = items.indexOf(item);
//        items.remove(position);
//        notifyItemRemoved(position);
//    }
    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return new LocalFileRecyclerViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class LocalFileRecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView fileIco;
        private TextView fileName;
        private TextView lastModified;

        public LocalFileRecyclerViewHolder(View v) {
            super(v);
            fileIco = (ImageView) v.findViewById(R.id.local_fifle_item_fileIco);
            fileName = (TextView) v.findViewById(R.id.local_fifle_item_typeName);
            lastModified = (TextView) v.findViewById(R.id.local_fifle_item_lastModified);
        }
    }
}
