package com.zxy.books.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.zxy.books.R;
import com.zxy.books.db.ChapterDao;
import com.zxy.books.db.RecentlyDao;
import com.zxy.books.model.BookChapter;
import com.zxy.books.network.volley.VolleyQueueController;
import com.zxy.books.ui.base.BaseAdapter;
import com.zxy.books.model.Book;
import com.zxy.books.util.AnimationUtil;
import com.zxy.books.ui.image.BitmapCache;
import com.zxy.books.ui.image.BookImageView;


import java.util.List;

/**
 * Created by zxy on 2015/12/10.
 * 阅读历史显示的自定义设配器
 */
public class BookAdapter extends BaseAdapter {
    private List<Book> books;
    private Context context;
    private int lastAnimatedPosition = -1;//lastAnimatedPosition表示最后执行动画的itme序号

    public BookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public View createView(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        v.setOnClickListener(this);
        return v;
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return new BookRecyclerViewHolder(view);
    }

    @Override
    public void showData(RecyclerView.ViewHolder viewHolder, final int position) {
        final Book book = books.get(position);
        BookRecyclerViewHolder holder = (BookRecyclerViewHolder) viewHolder;
        if (position > lastAnimatedPosition) {//lastAnimatedPosition表示最后执行动画的itme序号
            lastAnimatedPosition = position;
            AnimationUtil.runEnterAnimationY(holder.itemView, position, context);
        }
//        holder.continuereading_buton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Snackbar.make(v, "这是继续阅读按钮", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
//            }
//        });
        String mActivity = context.getClass().toString();
        if (mActivity.equals("class com.zxy.books.ui.activity.BookListActivity")) {
            holder.remove_button.setVisibility(View.INVISIBLE);
            holder.continuereading_buton.setText(R.string.book_itme_continuereading1_buton);
        } else {
            ChapterDao chapterDao = new ChapterDao(context);
            List<BookChapter> bookChapters = chapterDao.getBookChapter(book.getBookId());
            if (bookChapters != null || bookChapters.size() != 0) {
                book.setBookChapters(bookChapters);
            }
        }
        holder.remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.alert);
                builder.setMessage(R.string.AlertDialog);
                builder.setPositiveButton(R.string.alertPositive,
                        new android.content.DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RecentlyDao recentlyDao = new RecentlyDao(context);
                                if (recentlyDao.delet(book.getBookId()) > -1) {
                                    books.remove(position);
                                    BookAdapter.this.notifyItemRemoved(position);
                                    Toast.makeText(context, R.string.deleOk, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, R.string.delenot, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.setNegativeButton(R.string.alertNegative,
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });
        holder.tvName.setText(book.getBookName());
        holder.tvAuther.setText(book.getBookAuthor());
        holder.ivBook.setImageResource(R.drawable.ic_bookimagbackground);
        String strUpdateTime = book.getUpdataTime();
        if (TextUtils.isEmpty(strUpdateTime))
            holder.layoutUpdateTime.setVisibility(View.INVISIBLE);
        else
            holder.tvUpdateTime.setText(book.getLastChapter());
        /**
         * 创建ImageLoader实例
         */
        ImageLoader imageLoader = new ImageLoader(VolleyQueueController.getInstance(context).getRequestQueue(), new BitmapCache(context));
        holder.ivBook.setImage(book.getBookName(), book.getType(), book.getBookId(), imageLoader);
        //书籍对象保存到itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(book);

    }

    private void dialogShow(final Book book) {

    }

    public class BookRecyclerViewHolder extends RecyclerView.ViewHolder {
        BookImageView ivBook;//书籍封面
        TextView tvName;//书籍名称
        TextView tvAuther;//作者
        TextView tvUpdateTime;//更新时间
        TextView continuereading_buton;//继续阅读
        TextView remove_button;//移出书籍
        LinearLayout layoutUpdateTime;

        public BookRecyclerViewHolder(View v) {
            super(v);
            ivBook = (BookImageView) v.findViewById(R.id.book_item_ivBook);
            tvAuther = (TextView) v.findViewById(R.id.book_item_tvAuthor);
            tvName = (TextView) v.findViewById(R.id.book_item_tvName);
            tvUpdateTime = (TextView) v.findViewById(R.id.book_item_tvUpdateTime);
            continuereading_buton = (TextView) v.findViewById(R.id.book_item_continuereading_buton);
            layoutUpdateTime = (LinearLayout) v.findViewById(R.id.book_item_layoutUpdateTime);
            remove_button = (TextView) v.findViewById(R.id.book_item_remove_button);
        }
    }
}

