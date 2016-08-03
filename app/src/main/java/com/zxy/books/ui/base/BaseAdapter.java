package com.zxy.books.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zxy on 2015/12/15.
 * 作为RecyclerView.Adapter基类
 * 封装基本方法和RecyclerView.Adapter点击事件
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        showData(viewHolder, position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = createView(viewGroup, position);
        RecyclerView.ViewHolder holder = createViewHolder(view);
        return holder;
    }

    /**
     * 显示数据抽象函数
     *
     * @param viewHolder 基类ViewHolder,需要向下转型为对应的ViewHolder
     * @param position   位置
     */
    public abstract void showData(RecyclerView.ViewHolder viewHolder, int position);

    /**
     * 加载item的view,直接返回加载的view即可
     *
     * @param viewGroup 如果需要Context,可以viewGroup.getContext()获取
     * @param viewType
     * @return item 的 view
     */
    public abstract View createView(ViewGroup viewGroup, int viewType);

    /**
     * 加载一个ViewHolder,为RecyclerView.ViewHolder子类,直接返回子类的对象即可
     *
     * @param view item 的view
     * @return RecyclerView.ViewHolder
     */
    public abstract RecyclerView.ViewHolder createViewHolder(View view);


    /**
     * RecyclerView没有实现点击事件的方法
     * 自定义的一个接口 用于RecyclerView的Item点击事件的实现
     */
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Object data);
    }

    /**
     * 将点击事件接口暴露给外面用于调用
     *
     * @param listener
     */
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, v.getTag());
        }
    }
}
