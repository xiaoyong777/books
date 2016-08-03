package com.zxy.books.ui.image;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by zxy on 2015/12/24.
 * 图片缓存处理类
 */
public class ImageCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> mCache;

    public ImageCache(Context context) {
        //获取系统能够使用的内存
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        //缓存内存的大小取其1/8
        int cacheSize = 1024 * 1024 * memClass / 8;
        mCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }


    /**
     * 此方法来加载本地图片
     *
     * @param url
     * @return
     */
    public Bitmap loadNativeImage(String url) {
        //先获取内存中的Bitmap
        Bitmap bitmap = getBitmapFromMemCache(url);
        //若该Bitmap不在内存缓存中，去加载本地的图片，并将Bitmap加入到mMemoryCache中
        if (bitmap == null) {
            // File f = new File();
//            if (f.exists()) {
//                //获取图片
//                Bitmap mBitmap = BitmapFactory.decodeFile(url);
//                //将图片加入到内存缓存
//                addBitmapToMemoryCache(url, mBitmap);
//            }
        }
        return bitmap;
    }

    /**
     * 往内存缓存中添加Bitmap
     *
     * @param key
     * @param bitmap
     */
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && bitmap != null) {
            mCache.put(key, bitmap);
        }
    }

    /**
     * 根据key来获取内存中的图片
     *
     * @param key
     * @return
     */
    private Bitmap getBitmapFromMemCache(String key) {
        return mCache.get(key);
    }
}
