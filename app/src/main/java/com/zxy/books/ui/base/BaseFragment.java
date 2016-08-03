package com.zxy.books.ui.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxy.books.ui.activity.MainActivity;


public abstract class BaseFragment extends Fragment implements View.OnClickListener, BaseAdapter.OnRecyclerViewItemClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MainActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();
        View view = setContentView(inflater);
        initFindViewById(view);
        initData();
        initView();
        setOnClick();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    protected abstract View setContentView(LayoutInflater inflater);

    /**
     * 获取布局控件
     */
    protected abstract void initFindViewById(View view);

    /**
     * 实现数据的初始化
     */
    protected abstract void initData();

    /**
     * 初始化View的一些数据
     */
    protected abstract void initView();

    /**
     * 设置点击事件
     */
    protected abstract void setOnClick();

    /**
     * 界面刷新
     *
     * @param changed
     * @param isClose
     */
    protected abstract void notifyDataSetChanged(String changed, Boolean isClose);

    /**
     * 给父activity的SearchView搜索事件触发回调
     *
     * @param query            搜索框输入的内容
     * @param isSearchViewShow SearchView是否显示
     */
    public void searchViewOnQueryText(String query, Boolean isSearchViewShow) {
        notifyDataSetChanged(query, isSearchViewShow);
    }
}
