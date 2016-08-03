package com.zxy.books.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

/**
 * activity的父类
 * 对常用方法做了封装
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initFindViewById();
        initData();
        initView();
        setOnClick();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
              //  finishAfterTransition();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取布局控件
     */
    public void initFindViewById() {
    }

    ;

    /**
     * 初始化View的一些数据
     */
    public void initView() {
    }

    ;

    /**
     * 实现数据的初始化
     */
    public void initData() {
    }

    ;

    /**
     * 设置点击监听
     */
    public void setOnClick() {
    }

    @Override
    public void onClick(View v) {

    }
}
