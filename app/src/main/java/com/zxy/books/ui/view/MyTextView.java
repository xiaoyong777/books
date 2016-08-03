package com.zxy.books.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zxy.books.R;
import com.zxy.books.util.ColorUtil;
import com.zxy.books.util.ConfigUtil;

import java.util.Arrays;

/**
 * Created by zxy on 2016/1/4.
 * 这是定义TextView 用于阅读设置的预览区
 */
public class MyTextView extends TextView {
    private Context context;
    private Paint mPaint;
    private int mTextSize;
    private int textColorId;
    private int bGId;
    private int viewHeight;
    private String text;

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mPaint = new Paint();
        mTextSize = ConfigUtil.getTextSize(context);
        textColorId = ConfigUtil.getTextColor(context);
        bGId = ConfigUtil.getReadBg(context);
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(ColorUtil.textColol[textColorId]));
        mPaint.setTextSize(mTextSize);
        this.setBackgroundResource(ColorUtil.readbg[bGId]);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        viewHeight = this.getHeight();
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        float baseline = fm.descent - fm.ascent;
        float x = 0;
        float y = (viewHeight/2)-baseline/2;  //由于系统基于字体的底部来绘制文本，所有需要加上字体的高度。

        if (TextUtils.isEmpty(text)) {
            text = getResources().getString(R.string.readpreview);
        }
        //文本自动换行
        String[] texts = autoSplit(text, mPaint, getWidth());
        for (String text : texts) {
            canvas.drawText(text, x, y, mPaint);  //坐标以控件左上角为原点
            y += baseline + fm.leading; //添加字体行间距
        }
        super.onDraw(canvas);
    }

    public int getmTextSize() {
        return mTextSize;
    }

    public void setmTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        if (mTextSize < 30) {
            text = getResources().getString(R.string.sizemin);
        } else if (mTextSize < 100) {
            text = getResources().getString(R.string.readpreview);
        } else if (mTextSize > 100) {
            text = getResources().getString(R.string.sizemax);
        }
        mPaint.setTextSize(mTextSize);
        invalidate();
    }

    public int getTextColorId() {
        return textColorId;
    }

    public void setTextColorId(int textColorId) {
        this.textColorId = textColorId;
        mPaint.setColor(getResources().getColor(ColorUtil.textColol[textColorId]));
        invalidate();
    }

    public int getbGId() {
        return bGId;
    }

    public void setbGId(int bGId) {
        this.bGId = bGId;
        this.setBackgroundResource(ColorUtil.readbg[bGId]);
    }

    /**
     * 自动分割文本
     *
     * @param content 需要分割的文本
     * @param p       画笔，用来根据字体测量文本的宽度
     * @param width   最大的可显示像素（一般为控件的宽度）
     * @return 一个字符串数组，保存每行的文本
     */
    private String[] autoSplit(String content, Paint p, float width) {
        int length = content.length();
        float textWidth = p.measureText(content)-4;
        if (textWidth <= width) {
            return new String[]{content};
        }

        int start = 0, end = 0, i = 0;
        int lines = (int) Math.ceil(textWidth / width); //计算行数
        String[] lineTexts = new String[lines];
        while (start < length) {
            String line = content.substring(start, end);
            if (p.measureText(line) >= width) { //文本宽度超出控件宽度时
                lineTexts[i++] = (String) content.subSequence(start, end);
                start = end;
            }
            if (end == length) { //不足一行的文本
                lineTexts[i] = (String) content.subSequence(start, end);
                break;
            }
            end += 1;
        }
        return lineTexts;
    }
}
