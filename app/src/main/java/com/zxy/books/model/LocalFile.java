package com.zxy.books.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.zxy.books.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bb on 2015/12/17.
 * 文件实体类
 */
public class LocalFile {
    private File file;
    private Context context;
    private String fileName;//文件名
    private String fileEnd;//文件格式
    private String lastModified;

    public LocalFile(File file, Context context) {
        this.file = file;
        this.context = context;
        fileName = file.getName();
        getlastModified();
        if (!file.isDirectory())//如果文件不是文件夹  通过文件名取得文件格式 并且转换为小写
            fileEnd = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
        else
            fileEnd = "folde";//如果为文件夹 文件格式为 folde
    }

    public String getFileEnd() {
        return fileEnd;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public String getFileSizeOreEND() {
        if (file.isDirectory())
            return "";
        else {
            long fileSize = file.length();
            String strSizt = "";
            if (fileSize > 1024 * 1024) {
                strSizt = (fileSize / 1024 / 1024) + "MB";
            } else if (fileSize > 1024) {
                strSizt = (fileSize / 1024) + " KB";
            } else {
                strSizt = fileSize + " B";
            }
            return " - " + strSizt + " - " + fileEnd;
        }
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModified() {
        return lastModified;
    }

    /**
     * 根据文件名判断显示的文件图标和图片背景色
     *
     * @return 图标id 颜色id
     */
    public int[] getFileIco() {
        if (file.isDirectory()) {
            return new int[]{R.drawable.iconfolder, R.color.orange};
        } else if (fileEnd.equals("txt")) {
            return new int[]{R.drawable.aitxt, R.color.light_bule};
        } else {
            return new int[]{R.drawable.aiunknow, R.color.fileIco_unkonw};
        }
    }

    /**
     * 获取文件最后修改时间
     *
     * @return 最后修改的时间
     */
    public void getlastModified() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String strLastModified = dateFormat.format(new Date(file.lastModified()));
        this.lastModified = strLastModified;
    }

    /**
     * 打开文件夹 获取本文件夹目录下的所有文件
     *
     * @return 所有文件的List
     */
    public List<LocalFile> openfolder() {
        List<LocalFile> folderList = new ArrayList<>();
        List<LocalFile> fileList = new ArrayList<>();
        File[] mFiles = file.listFiles();
        if (mFiles != null) {
            for (File mCurrentFile : mFiles) {
                LocalFile file = new LocalFile(mCurrentFile, context);
                if (mCurrentFile.isDirectory())//判断是否为文件夹
                    folderList.add(file);
                else
                    fileList.add(file);
            }
        }
        folderList.addAll(fileList);
        return folderList;
    }

    /**
     * 打开文件 调用系统的方法，来打开文件的方法
     */
    public void openFile() {
        String type = "";
        if (fileEnd.equals("m4a") || fileEnd.equals("mp3") || fileEnd.equals("mid") || fileEnd.equals("xmf") || fileEnd.equals("ogg") || fileEnd.equals("wav")) {
            type = "audio/*";// 系统将列出所有可能打开音频文件的程序选择器
        } else if (fileEnd.equals("3gp") || fileEnd.equals("mp4")) {
            type = "video/*";// 系统将列出所有可能打开视频文件的程序选择器
        } else if (fileEnd.equals("jpg") || fileEnd.equals("gif") || fileEnd.equals("png") || fileEnd.equals("jpeg") || fileEnd.equals("bmp")) {
            type = "image/*";// 系统将列出所有可能打开图片文件的程序选择器
        } else if (fileEnd.equals("txt")) {
            openText();
            return;
        } else {
            type = "*/*"; // 系统将列出所有可能打开该文件的程序选择器
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        //设置当前文件类型
        intent.setDataAndType(Uri.fromFile(file), type);
        context.startActivity(intent);
    }

    /**
     * 打开txt格式
     */
    private void openText() {

    }
}
