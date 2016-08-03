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
import com.zxy.books.ui.base.BaseAdapter;
import com.zxy.books.util.AnimationUtil;
import com.zxy.books.util.ColorUtil;

import java.util.List;

/**
 * Created by zxy on 2015/12/12.
 * 网上书城界面显示的榜单、类型的适配器
 */
public class OnlineTitleAdapter extends BaseAdapter {
    List<String> typeNames;
    Context context;

    public OnlineTitleAdapter(Context context, List<String> typeNames) {
        this.context = context;
        this.typeNames = typeNames;
    }

    @Override
    public View createView(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_onlin_type, parent, false);
        v.setOnClickListener(this);
        return v;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return new OnlineTitleViewHolder(view);
    }

    @Override
    public void showData(RecyclerView.ViewHolder viewHolder, int position) {
        OnlineTitleViewHolder holder = (OnlineTitleViewHolder) viewHolder;
        AnimationUtil.runEnterAnimationX(holder.itemView, position, context);
        String typeName = typeNames.get(position);
        //保存到itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(typeName);
        holder.imageTextview.setText(typeName.substring(0, 1));
        holder.typeName.setText(typeName);
        GradientDrawable myGrad = (GradientDrawable) holder.imageBackground.getBackground();
        myGrad.setColor(context.getResources().getColor(ColorUtil.getColor()));
    }

    @Override
    public int getItemCount() {
        return typeNames.size();
    }

    public class OnlineTitleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBackground;
        TextView imageTextview;
        TextView typeName;

        public OnlineTitleViewHolder(View v) {
            super(v);
            imageBackground = (ImageView) v.findViewById(R.id.onlin_type_item_ivType);
            imageTextview = (TextView) v.findViewById(R.id.onlin_type_item_tvType);
            typeName = (TextView) v.findViewById(R.id.onlin_type_item_typeName);
        }
    }
}
