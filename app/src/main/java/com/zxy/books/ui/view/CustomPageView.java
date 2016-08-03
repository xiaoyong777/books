package com.zxy.books.ui.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.zxy.books.R;
import com.zxy.books.model.BookChapter;
import com.zxy.books.network.ReadBookHelper;
import com.zxy.books.network.parser.GokankanParser;
import com.zxy.books.network.volley.NetworkHelper;
import com.zxy.books.network.volley.UIDataListener;
import com.zxy.books.ui.activity.ReadActivity;
import com.zxy.books.util.ColorUtil;
import com.zxy.books.util.ConfigUtil;
import com.zxy.books.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 阅读界面 显示内容的view
 */
public class CustomPageView extends View implements UIDataListener<BookChapter> {
    private String mInitText;// 提示内容
    private String mContent;// 文章内容
    private List<String> mLines;
    private boolean isMenuInvisible = true;//菜单是否显示了
    private int mPage = 1; // 当前看的页码
    private int mPageLines;// 每页多少行？
    private int mPageMax;// 总页数
    private ReadActivity readActivity;
    private int mMarginTopAndBottom = 15;// 顶距
    //    private int mMarginLeftAndRight = 20;// 左右边距
    private Paint mPaint;// 画笔
    private float mFontHeight;// 字体高度
    private float mPaintDescent;
    private float mPaintAscent;
    private BookChapter currentChapter;
    private Context daoContext;
    private boolean mNeedRefresh = true;//是否重新计算页数
    private Boolean isTurn = false;//是否显示倒序（章节上一章要用倒序显示）
    //  private Boolean isNetwork = false;
    private NetworkHelper<BookChapter> networkHelper;
    // private int currentChapterNo;
    private int textSize;
    private int textColorId;
    private int bgId;
    private String chapterUrlHead;
    private boolean isShowprogressBar = true;
    private GokankanParser gokankanParser;
    private BookChapter prepChapter;

    public void setBookChapter(ReadActivity readActivity, BookChapter bookChapter, int mPage) {
        this.readActivity = readActivity;
        prepChapter = bookChapter;
        if (FileUtil.isExist(FileUtil.DOWNLOAD_PATH + "/" + prepChapter.getChapterId() + ".html")) {
            gokankanParser.readbook(FileUtil.readFile(prepChapter.getChapterId()), prepChapter);
            if (!TextUtils.isEmpty(prepChapter.getChapterContext())) {
                isShowprogressBar = false;
                readActivity.progressBar.setVisibility(View.GONE);
                if (isMenuInvisible) {
                    readActivity.hideMenu();
                    isMenuInvisible = false;
                }
                currentChapter = prepChapter;
                mContent = currentChapter.getChapterContext();
                setmNeedRefresh(true);
                invalidate();
            }
        } else {
            networkHelper = new ReadBookHelper(readActivity, bookChapter);
            networkHelper.setUiDataListener(this);
            networkHelper.sendGETRequest(prepChapter.getChapterUrl(), null);
        }
     //   readActivity.coordinatorLayout.setBackgroundResource(ColorUtil.readbg[bgId]);//设置背景颜色
        //isNetwork = true;
        if (mPage != 0) {
            this.mPage = mPage;
        }
        readActivity.tvread_progress.setTextColor(getResources().getColor(ColorUtil.textColol[textColorId]));
        readActivity.tvread_chapterTtile.setTextColor(getResources().getColor(ColorUtil.textColol[textColorId]));
        readActivity.tvread_bookTitle.setTextColor(getResources().getColor(ColorUtil.textColol[textColorId]));
    }

    public CustomPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        daoContext = context;
        mLines = new ArrayList<String>();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        textSize = ConfigUtil.getTextSize(context);
        textColorId = ConfigUtil.getTextColor(context);
        bgId = ConfigUtil.getReadBg(context);
        mPaint.setTextSize(textSize);// 设置字体大小
        mPaint.setColor(getResources().getColor(ColorUtil.textColol[textColorId]));//设置字体颜色
        gokankanParser = new GokankanParser();

    }

    public void setInitText(String initText) {
        mInitText = initText;
        invalidate();
    }


    @Override
    public void onDraw(Canvas canvas) {
        FontMetrics fm = mPaint.getFontMetrics();
        mFontHeight = fm.descent - fm.top;
        mPaintDescent = mPaint.descent();
        mPaintAscent = mPaint.ascent();
        if (!TextUtils.isEmpty(mContent)) {
            if (ismNeedRefresh()) {
                splitContentToPageLines();
                readActivity.tvread_chapterTtile.setText(currentChapter.getChapterTitle());
                setmNeedRefresh(false);
            }
            if (mPage > mPageMax) {
                mPage = 1;
            }
            readActivity.tvread_progress.setText(mPage + "/" + mPageMax);
            float drawTop = mMarginTopAndBottom - mPaintAscent;// 画字的顶点坐标
            int startLine = (mPage - 1) * mPageLines;
            for (int i = startLine; i < startLine + mPageLines
                    && i < mLines.size(); i++) {
                canvas.drawText(mLines.get(i), 0, drawTop,
                        mPaint);
                drawTop += mFontHeight;
            }
        } else if (mInitText != null && !"".equals(mInitText)) {
            int initTextWidth = (int) mPaint.measureText(mInitText);
            int initTextX = (getWidth() - initTextWidth) / 2;
            int initTextY = (getHeight() - (int) mFontHeight) / 2;
            canvas.drawText(mInitText, initTextX, initTextY, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < getWidth() / 3) {//屏幕点击区域小于屏幕的3/1  上一页
                    if (isMenuInvisible) {
                        isMenuInvisible = false;
                        readActivity.hideMenu();
                    }
                    if (getmPage() == 2) {//当前页面为2 提前获取上一章内容
                        if (currentChapter.getLastChapter() != null) {
                            if (TextUtils.isEmpty(currentChapter.getLastChapter().getChapterContext())) {//判断上一章内容是否为空
                                //获取上一章节内容
                                if (FileUtil.isExist(FileUtil.DOWNLOAD_PATH + "/" + currentChapter.getLastChapter().getChapterId() + ".html")) {
                                    //本地是否下载
                                    gokankanParser.readbook(FileUtil.readFile(currentChapter.getLastChapter().getChapterId()), currentChapter.getLastChapter());
                                } else {
                                    networkHelper = new ReadBookHelper(readActivity, currentChapter.getLastChapter());
                                    networkHelper.setUiDataListener(new UIDataListener<BookChapter>() {
                                        @Override
                                        public void onDataChanged(BookChapter data) {
                                            currentChapter.setLastChapter(data);
                                        }

                                        @Override
                                        public void onErrorHappened(String errorMessage) {
                                            Toast.makeText(readActivity, getResources().getString(R.string.ahead_read), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    networkHelper.sendGETRequest(currentChapter.getLastChapter().getChapterUrl(), null);
                                }
                            }
                        }
                        setmPage(getmPage() - 1);
                        invalidate();
                    } else if (getmPage() <= mPageMax && getmPage() > 1) {
                        setmPage(getmPage() - 1);
                        invalidate();
                    } else if (getmPage() == 1) {//当前页面为0了 没有可以翻到页 显示上一章
                        isTurn = true;
                        if (currentChapter.getLastChapter() != null) {
                            if (!TextUtils.isEmpty(currentChapter.getLastChapter().getChapterContext())) {//上一章节有内容
                                currentChapter = currentChapter.getLastChapter();
                                mContent = currentChapter.getChapterContext();
                                invalidate();//换章节 界面重新绘制
                                setmNeedRefresh(true);
                                //currentChapterNo--;
                            } else {//上一章没有内容
                                if (FileUtil.isExist(FileUtil.DOWNLOAD_PATH + "/" + currentChapter.getLastChapter().getChapterId() + ".html")) {
                                    //本地是否下载
                                    gokankanParser.readbook(FileUtil.readFile(currentChapter.getLastChapter().getChapterId()), currentChapter.getLastChapter());
                                } else {
                                    readActivity.progressBar.setVisibility(View.VISIBLE);
                                    isShowprogressBar = true;
                                    networkHelper = new ReadBookHelper(readActivity, currentChapter.getLastChapter());
                                    networkHelper.setUiDataListener(this);
                                    networkHelper.sendGETRequest(currentChapter.getLastChapter().getChapterUrl(), null);
                                }
                                //    isNetwork = true;
                            }
                        } else {
                            Toast.makeText(readActivity, getResources().getString(R.string.not_lastchapter), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (event.getX() > getWidth() / 3 && event.getX() < getWidth() / 3 * 2) {//点击区域大于3/1并且小于3/2 设置
                    if (isMenuInvisible) {
                        isMenuInvisible = false;
                        readActivity.hideMenu();
                    } else {
                        isMenuInvisible = true;
                        readActivity.showMenu();
                    }
                } else if (event.getX() > getWidth() / 3 * 2) {//点击区域大于3/2 下一页

                    if (isMenuInvisible) {
                        isMenuInvisible = false;
                        readActivity.hideMenu();
                    }
                    if (getmPage() == (mPageMax - 2)) {//当前章节只剩下两页可以翻 提前获取下一章节
                        if (currentChapter.getNextChapter() != null) {
                            if (TextUtils.isEmpty(currentChapter.getNextChapter().getChapterContext())) {//判断下一章内容是否为空
                                if (FileUtil.isExist(FileUtil.DOWNLOAD_PATH + "/" + currentChapter.getNextChapter().getChapterId() + ".html")) {
                                    //本地是否下载
                                    gokankanParser.readbook(FileUtil.readFile(currentChapter.getNextChapter().getChapterId()), currentChapter.getNextChapter());
                                } else {
                                    networkHelper = new ReadBookHelper(readActivity, currentChapter.getNextChapter());//获取上一章节内容
                                    networkHelper.setUiDataListener(new UIDataListener<BookChapter>() {
                                        @Override
                                        public void onDataChanged(BookChapter data) {
                                            currentChapter.setNextChapter(data);
                                        }

                                        @Override
                                        public void onErrorHappened(String errorMessage) {
                                            Toast.makeText(readActivity, getResources().getString(R.string.ahead_read), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    networkHelper.sendGETRequest(currentChapter.getNextChapter().getChapterUrl(), null);
                                }
                            }
                        }
                        setmPage(getmPage() + 1);
                        invalidate();
                    } else if (getmPage() < mPageMax && getmPage() > 0) {
                        setmPage(getmPage() + 1);
                        invalidate();
                    } else if (getmPage() == mPageMax) {//没有可以翻到页 显示下一章
                        isTurn = false;
                        if (currentChapter.getNextChapter() != null) {
                            if (!TextUtils.isEmpty(currentChapter.getNextChapter().getChapterContext())) {//下一章节有内容
                                currentChapter = currentChapter.getNextChapter();
                                mContent = currentChapter.getChapterContext();
                                invalidate();//换章节  界面重新绘制
                                setmNeedRefresh(true);
                                mPage = 1;
                                //currentChapterNo++;
                            } else {//下一章没有内容
                                if (FileUtil.isExist(FileUtil.DOWNLOAD_PATH + "/" + currentChapter.getNextChapter().getChapterId() + ".html")) {
                                    //本地是否下载
                                    gokankanParser.readbook(FileUtil.readFile(currentChapter.getNextChapter().getChapterId()), currentChapter.getNextChapter());
                                    currentChapter = currentChapter.getNextChapter();
                                    mContent = currentChapter.getChapterContext();
                                    setmNeedRefresh(true);
                                    invalidate();
                                } else {
                                    readActivity.progressBar.setVisibility(View.VISIBLE);
                                    isShowprogressBar = true;
                                    networkHelper = new ReadBookHelper(readActivity, currentChapter.getNextChapter());
                                    readActivity.progressBar.setVisibility(View.VISIBLE);
                                    networkHelper.setUiDataListener(new UIDataListener<BookChapter>() {
                                        @Override
                                        public void onDataChanged(BookChapter data) {
                                            readActivity.progressBar.setVisibility(View.GONE);
                                            mPage = 1;
                                            currentChapter = data;
                                            mContent = currentChapter.getChapterContext();
                                            setmNeedRefresh(true);
                                            invalidate();
                                        }

                                        @Override
                                        public void onErrorHappened(String errorMessage) {
                                            readActivity.progressBar.setVisibility(View.GONE);
                                            Toast.makeText(readActivity, errorMessage, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    networkHelper.sendGETRequest(currentChapter.getNextChapter().getChapterUrl(), null);
                                }
                                //isNetwork = true;
                            }

                        } else {
                            Toast.makeText(readActivity, getResources().getString(R.string.not_nextchapter), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    // 把要显示的内容切割成行，放入到mLines数组
    // 计算总页数 mPageMax
    public void splitContentToPageLines() {
        // 计算一屏可以显示多少行
        mPageLines = (int) ((float) (getHeight()) / mFontHeight);
        int start = 0, end;
        mLines.clear();
        mLines.add(currentChapter.getChapterTitle());
        mLines.add("\n");
        mContent = ToDBC(mContent);
        // 计算一屏的宽度
        int textWidth = getWidth();
        for (int i = 0; i < mContent.length(); i++) {
            end = i;
            String line = mContent.substring(start, end + 1);// 一次取一个字符。。
            if (mPaint.measureText(line) >= textWidth) {
                line = mContent.substring(start, end);// 取到一行字符串
                mLines.add(line);
                start = i;
            } else if (i == mContent.length() - 1) {
                mLines.add(line);
                start = i;
            } else if (mContent.charAt(i) == '\r'
                    && mContent.charAt(i + 1) == '\n') {
                if (start >= end) {
                    continue;
                }
                line = mContent.substring(start, end);
                mLines.add(line);
                start = i + 2;
                if (start > mContent.length() - 1) {
                    start = mContent.length() - 1;
                }
            } else if (mContent.charAt(i) == '\n') {
                if (start >= end) {
                    continue;
                }
                line = mContent.substring(start, end);
                mLines.add(line);
                start = i + 1;
            }
        }
        if (mLines.size() % mPageLines == 0) {
            mPageMax = mLines.size() / mPageLines;
        } else {
            mPageMax = mLines.size() / mPageLines + 1;
        }
        if (isTurn) {
            mPage = mPageMax;
            isTurn = false;
        }
    }

    public int getmPage() {
        return mPage;
    }

    public void setmPage(int mPage) {
        this.mPage = mPage;
    }

    public boolean ismNeedRefresh() {
        return mNeedRefresh;
    }

    public void setmNeedRefresh(boolean mNeedRefresh) {
        this.mNeedRefresh = mNeedRefresh;
    }

//    public int getCurrentChapterNo() {
//        return currentChapterNo;
//    }
//
//    public void setCurrentChapterNo(int currentChapterNo) {
//        this.currentChapterNo = currentChapterNo;
//    }

    public boolean isMenuInvisible() {
        return isMenuInvisible;
    }

    public void setIsMenuInvisible(boolean isMenuInvisible) {
        this.isMenuInvisible = isMenuInvisible;
    }

    public BookChapter getCurrentChapter() {
        return currentChapter;
    }

    public void setCurrentChapter(BookChapter currentChapter) {
        this.currentChapter = currentChapter;
        networkHelper = new ReadBookHelper(readActivity, currentChapter);
        networkHelper.sendGETRequest(currentChapter.getChapterUrl(), null);
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getBgId() {
        return bgId;
    }

    public void setBgId(int bgId) {
        this.bgId = bgId;
    }

    public int getTextColorId() {
        return textColorId;
    }

    public void setTextColorId(int textColorId) {
        this.textColorId = textColorId;
    }

    public void setChapterUrlHead(String head) {
        this.chapterUrlHead = head;
    }

    public void updateConfig() {
        if (textSize != ConfigUtil.getTextSize(readActivity)) {
            textSize = ConfigUtil.getTextSize(readActivity);
            mPaint.setTextSize(textSize);
            mNeedRefresh = true;
            invalidate();
        }
        if (textColorId != ConfigUtil.getTextColor(readActivity)) {
            textColorId = ConfigUtil.getTextColor(readActivity);
            mPaint.setColor(getResources().getColor(ColorUtil.textColol[textColorId]));
            invalidate();
            readActivity.tvread_progress.setTextColor(getResources().getColor(ColorUtil.textColol[textColorId]));
            readActivity.tvread_chapterTtile.setTextColor(getResources().getColor(ColorUtil.textColol[textColorId]));
            readActivity.tvread_bookTitle.setTextColor(getResources().getColor(ColorUtil.textColol[textColorId]));
        }
        if (bgId != ConfigUtil.getReadBg(readActivity)) {
            bgId = ConfigUtil.getReadBg(readActivity);
            readActivity.coordinatorLayout.setBackgroundResource(ColorUtil.readbg[bgId]);//设置背景颜色
        }
    }

    @Override
    public void onDataChanged(BookChapter data) {
        //    isNetwork = false;
        isShowprogressBar = false;
        readActivity.progressBar.setVisibility(View.GONE);
        if (isMenuInvisible) {
            readActivity.hideMenu();
            isMenuInvisible = false;
        }
//        if (isTurn) {
//            currentChapterNo--;
//        } else {
//            currentChapterNo++;
//        }
        currentChapter = data;
        mContent = currentChapter.getChapterContext();
        setmNeedRefresh(true);
        invalidate();
    }

    @Override
    public void onErrorHappened(String errorMessage) {
        readActivity.progressBar.setVisibility(View.GONE);
        isShowprogressBar = false;
        Toast.makeText(readActivity, errorMessage, Toast.LENGTH_SHORT).show();
        //  isNetwork = false;
        if (isMenuInvisible) {
            readActivity.hideMenu();
            isMenuInvisible = false;
        }
    }

    /**
     * 半角转换为全角
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        //去除特殊字符或将所有中文标号替换为英文标号
        input = input.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        input = m.replaceAll("").trim();
        //半角转换为全角
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }

        return new String(c);
    }

}
