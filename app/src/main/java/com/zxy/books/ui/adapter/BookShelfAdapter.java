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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.zxy.books.R;
import com.zxy.books.db.BookShelfDao;
import com.zxy.books.db.ChapterDao;
import com.zxy.books.model.Book;
import com.zxy.books.model.BookChapter;
import com.zxy.books.network.volley.VolleyQueueController;
import com.zxy.books.ui.base.BaseAdapter;
import com.zxy.books.ui.image.BitmapCache;
import com.zxy.books.ui.image.BookImageView;
import com.zxy.books.util.AnimationUtil;

import java.util.List;

/**
 * Created by zxy on 2015/12/10.
 * 书架显示的自定义设配器
 */
public class BookShelfAdapter extends BaseAdapter {
    private List<Book> books;
    private Context context;

    public BookShelfAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public View createView(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bookshelf, parent, false);
        v.setOnClickListener(this);
        return v;
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return new BookShelfRecyclerViewHolder(view);
    }

    @Override
    public void showData(RecyclerView.ViewHolder viewHolder,final int position) {
        final Book book = books.get(position);
        BookShelfRecyclerViewHolder holder = (BookShelfRecyclerViewHolder) viewHolder;
        holder.tvName.setText(book.getBookName());
        holder.imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.alert);
                builder.setMessage(R.string.AlertDialog);
                builder.setPositiveButton(R.string.alertPositive,
                        new android.content.DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BookShelfDao bookShelfDao = new BookShelfDao(context);
                                if (bookShelfDao.delet(book.getBookId()) > -1) {
                                    books.remove(position);
                                    BookShelfAdapter.this.notifyItemRemoved(position);
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
        /**
         * 创建ImageLoader实例
         */
        ImageLoader imageLoader = new ImageLoader(VolleyQueueController.getInstance(context).getRequestQueue(), new BitmapCache(context));
        holder.ivBook.setImage(book.getBookName(), book.getType(), book.getBookId(), imageLoader);
        //书籍对象保存到itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(book);
    }



    public class BookShelfRecyclerViewHolder extends RecyclerView.ViewHolder {
        BookImageView ivBook;//书籍封面
        TextView tvName;//书籍名称
        ImageButton imgButton;

        public BookShelfRecyclerViewHolder(View v) {
            super(v);
            ivBook = (BookImageView) v.findViewById(R.id.history_item_ivBook);
            tvName = (TextView) v.findViewById(R.id.history_item_bookName);
            imgButton = (ImageButton) v.findViewById(R.id.item_overflow);
        }
    }
}

