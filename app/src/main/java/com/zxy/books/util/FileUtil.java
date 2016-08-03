package com.zxy.books.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.zxy.books.model.BookChapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by zxy on 2015/12/11.？？
 * 文件操作工具类
 */
public class FileUtil {
//    /**
//     * 缓存文件目录
//     */
//    public static final String CACHE_PATH ="";
    /**
     * 下载文件目录
     */
    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory()
            + File.separator + "readBook" + File.separator + "download" + File.separator;

    /**
     * @param path 文件夹路径
     */
    public static boolean isExist(String path) {
        File file = new File(path);
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 章节下载至本地
     *
     * @param fileName
     * @param result
     */
    public static void writeFile(String fileName, String result) {
        File file = new File(DOWNLOAD_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fos = null;
        OutputStreamWriter out = null;
        try {
            File f = new File(FileUtil.DOWNLOAD_PATH + "/" + fileName + ".html");
            fos = new FileOutputStream(f);
            out = new OutputStreamWriter(fos);
            out.write(result);
            if (out != null) {
                out.close();
            }
            if (fos != null) {
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String chapterId) {
        File file = new File(FileUtil.DOWNLOAD_PATH + "/" + chapterId + ".html");
        String content = "";
        String lineTxt = null;
        FileInputStream fis = null;
        InputStreamReader read = null;
        try {
            if (file.isFile() && file.exists()) {
                fis = new FileInputStream(file);
                read = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(read);
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    content += lineTxt;
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (read != null) {
                    read.close();
                }
                if (fis != null) {
                    fis.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }


    /**
     * 图片保存本地
     *
     * @param bitmap
     * @param bookId
     */

    public static void saveBitmap(Bitmap bitmap, String bookId, File file) {

        if (!file.exists()) {
            file.mkdirs();
        }
        File f = new File(file.getPath(), bookId + ".jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
