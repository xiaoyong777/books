package com.zxy.books.ui.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.zxy.books.R;
import com.zxy.books.util.ColorUtil;
import com.zxy.books.util.FileUtil;

import java.io.File;

/**
 * Created by zxy on 2015/12/23.
 * 书籍封面显示imageView
 * 基于Volley框架内的NetworkImageView
 * 在NetworkImageView的基础上加了读取本地缓存
 * <p/>
 * 先读取内存缓存 然后本地缓存 最后网络获取
 */
public class BookImageView extends ImageView {
    private final String imageViewUrl = "http://www.7kankan.com/files/article/image/";
    private String bookOnlineImageUrl;//书籍封面网络路径
    private String bookName;//书名
    private ImageLoader mImageLoader;
    private ImageLoader.ImageContainer mImageContainer;
    private String bookId;
    private File file;

    public BookImageView(Context context) {
        this(context, (AttributeSet) null);
        file = context.getCacheDir();
    }

    public BookImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        file = context.getCacheDir();
    }

    public BookImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        file = context.getCacheDir();
    }

    /**
     * 传递参数进来  显示封面图片
     *
     * @param bookName
     * @param bookId
     * @param imageLoader
     */
    public void setImage(String bookName, String type, String bookId, ImageLoader imageLoader) {
        bookOnlineImageUrl = imageViewUrl + type + "/" + bookId + "/" + bookId + "s.jpg";

        this.bookName = bookName;
        this.bookId = bookId;
        this.mImageLoader = imageLoader;
        this.loadImageIfNecessary(false);
    }

    /**
     * 图片显示
     *
     * @param isInLayoutPass
     */
    private void loadImageIfNecessary(final boolean isInLayoutPass) {
        int width = this.getWidth();
        int height = this.getHeight();
        if (width != 0 || height != 0) {
            if (this.mImageContainer != null && this.mImageContainer.getRequestUrl() != null) {
                if (this.mImageContainer.getRequestUrl().equals(this.bookOnlineImageUrl)) {
                    return;
                }//缓存显示失败  继续下执行
                this.mImageContainer.cancelRequest();
            }
            if (!TextUtils.isEmpty(this.bookId)) {
                final File f = new File(file.getPath(), bookId + ".jpg");
                if (f.exists()) {
                    Bitmap LocalBitmap = BitmapFactory.decodeFile(file.getPath() + "/" + bookId + ".jpg");
                    if (LocalBitmap != null) {
                        BookImageView.this.setImageBitmap(LocalBitmap);//显示本地文件成功
                        return;
                    }//本地显示失败 继续下执行
                    //本地文件不存在//网络获取

                }
                if (TextUtils.isEmpty(this.bookId)) {//判空处理
                    if (this.mImageContainer != null) {
                        this.mImageContainer.cancelRequest();
                        this.mImageContainer = null;
                    }
                    getDefaultBitmap();
                } else {
                    ImageLoader.ImageContainer newContainer = this.mImageLoader.get(this.bookOnlineImageUrl, new ImageLoader.ImageListener() {
                        public void onErrorResponse(VolleyError error) {//网络获取失败
                            getDefaultBitmap();
                        }

                        public void onResponse(final ImageLoader.ImageContainer response, boolean isImmediate) {//网络显示成功
                            if (isImmediate && isInLayoutPass) {
                                BookImageView.this.post(new Runnable() {
                                    public void run() {
                                        onResponse(response, false);
                                    }
                                });
                            } else {
                                if (response.getBitmap() != null) {
                                    BookImageView.this.setImageBitmap(response.getBitmap());//网络图片显示成功
                                    FileUtil.saveBitmap(response.getBitmap(), bookId, file);
                                }
                            }
                        }
                    });
                    this.mImageContainer = newContainer;

                    return;
                }
            } else {
                getDefaultBitmap();
            }

        }
    }

    private void getDefaultBitmap() {
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        // bitmapFactoryOptions.inJustDecodeBounds = true;///只是为获取原始图片的尺寸，而不返回Bitmap对象
        // bitmapFactoryOptions.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bookimage_defaultimage, bitmapFactoryOptions);
        Bitmap bitmapAltered = Bitmap.createBitmap(BookImageView.this.getWidth(), BookImageView.this.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(bitmapAltered);//得到bitmap的画布
        canvas.drawColor(getResources().getColor(ColorUtil.getColor()));//背景颜色填充
        Paint paint = new Paint();
        paint.setTextSize(120);
        paint.setColor(getResources().getColor(R.color.default_fill_color));
        String str = this.bookName.substring(0, 1);
        paint.setAntiAlias(true);
        canvas.drawText(str, canvas.getWidth() / 2 - 60, canvas.getHeight() / 2 + 60, paint);
        BookImageView.this.setImageBitmap(bitmapAltered);
    }


    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.loadImageIfNecessary(true);
    }

    protected void onDetachedFromWindow() {
        if (this.mImageContainer != null) {
            this.mImageContainer.cancelRequest();
            this.setImageBitmap((Bitmap) null);
            this.mImageContainer = null;
        }

        super.onDetachedFromWindow();
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.invalidate();
    }

}
