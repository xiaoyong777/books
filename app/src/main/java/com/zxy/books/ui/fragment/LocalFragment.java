package com.zxy.books.ui.fragment;

import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.zxy.books.R;
import com.zxy.books.model.LocalFile;
import com.zxy.books.ui.adapter.LocalFileAdapter;
import com.zxy.books.ui.base.BaseFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地文件界面Fragment
 */
public class LocalFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    //  private RecyclerView searcheRecyclerView;
    private List<LocalFile> localFiles;
    private LocalFile currentFile;//当前目录
    private LocalFileAdapter localFileAdapter;
    View view;

    @Override
    public View setContentView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_local, null);
        currentFile = new LocalFile(new File(java.io.File.separator), getActivity());
        localFiles = new ArrayList<>();
        this.view = view;
        Snackbar.make(view, R.string.localFragment, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        return view;
    }

    @Override
    protected void initFindViewById(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.local_fragment_fileRecyclerView);
        //searcheRecyclerView = (RecyclerView) view.findViewById(R.id.local_fragment_SearcheRecyclerView);
    }

    @Override
    public void initData() {
        LocalFile currentParent = new LocalFile(new File(currentFile.getFile().getPath()), getActivity());//new一个LocalFile 属性为上一个目录文件夹的属性
        currentParent.setFileName(getResources().getString(R.string.root_folder));
        currentParent.setLastModified(currentFile.getFile().getPath());
        localFiles.add(currentParent);//将这个LocalFile加入list 打开这个文件夹的时候会打开上一个目录的文件夹 就相当于返回上一个目录
        localFiles.addAll(currentFile.openfolder());
    }

    @Override
    protected void initView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        localFileAdapter = new LocalFileAdapter(getActivity(), localFiles);
        mRecyclerView.setAdapter(localFileAdapter);
    }

    @Override
    protected void setOnClick() {
        localFileAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(View view, Object data) {
        LocalFile openLocalFile = (LocalFile) data;//获取被点击的文件
        initFileListInfo(openLocalFile, view);
    }


    /**
     * 父activity返回按钮监听
     *
     * @return
     */
    public boolean OnBack() {
        if (currentFile.getFile().getPath().equals(java.io.File.separator)) {//当前目录为根目录返回true 事件继续传播
            return true;
        } else {
            LocalFile openLocalFile = new LocalFile(new File(currentFile.getFile().getParent()), getActivity());
            initFileListInfo(openLocalFile, view);
            return false;
        }

    }

    /**
     * 打开文件
     *
     * @param openLocalFile
     * @param view
     */
    private void initFileListInfo(LocalFile openLocalFile, View view) {
        if (!openLocalFile.getFile().canRead()) {//打开文件权限不足
            Snackbar.make(view, R.string.folder_uncanread, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        if (openLocalFile.getFile().isDirectory())//判断是否为文件夹
        {
            localFiles.clear();//将localFiles的数据清除
            LocalFile currentParent = null;
            if (!openLocalFile.getFile().getPath().equals(java.io.File.separator)) {//判断要打开的目录是否为根目录
                String currentParentPath = openLocalFile.getFile().getParent();
                currentParent = new LocalFile(new File(currentParentPath), getActivity());
                currentParent.setFileName(getResources().getString(R.string.return_parent_folder));
                currentParent.setLastModified(openLocalFile.getFile().getPath());
            } else {
                currentParent = new LocalFile(new File(openLocalFile.getFile().getPath()), getActivity());
                currentParent.setFileName(getResources().getString(R.string.root_folder));
                currentParent.setLastModified(openLocalFile.getFile().getPath());
            }
            currentFile = openLocalFile;
            localFiles.add(currentParent);
            localFiles.addAll(openLocalFile.openfolder());
            localFileAdapter.notifyDataSetChanged();
            return;
        } else {
            openLocalFile.openFile();//直接打开文件
        }
    }

    @Override
    protected void notifyDataSetChanged(String changed, Boolean isClose) {

    }
}
