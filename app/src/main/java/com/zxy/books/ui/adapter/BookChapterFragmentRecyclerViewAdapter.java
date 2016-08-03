package com.zxy.books.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxy.books.R;
import com.zxy.books.model.BookChapter;
import com.zxy.books.ui.fragment.BookChapterListFragment;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BookChapter} and makes a call to the
 * specified {@link BookChapterListFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 * 书籍章节adapter
 */
public class BookChapterFragmentRecyclerViewAdapter extends RecyclerView.Adapter<BookChapterFragmentRecyclerViewAdapter.ViewHolder> {

    private final List<BookChapter> mValues;
    private final BookChapterListFragment.OnListFragmentInteractionListener mListener;

    public BookChapterFragmentRecyclerViewAdapter(List<BookChapter> items, BookChapterListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getChapterTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public BookChapter mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
