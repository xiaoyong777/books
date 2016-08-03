package com.zxy.books.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zxy.books.R;
import com.zxy.books.db.ChapterDao;
import com.zxy.books.model.Book;
import com.zxy.books.model.BookChapter;
import com.zxy.books.network.ChapterListNetworkHelper;
import com.zxy.books.network.volley.NetworkHelper;
import com.zxy.books.network.volley.UIDataListener;
import com.zxy.books.ui.activity.BookInfoActivity;
import com.zxy.books.ui.adapter.BookChapterFragmentRecyclerViewAdapter;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 * <p/>
 * 书籍章节列表Fragment
 */
public class BookChapterListFragment extends Fragment implements UIDataListener<List<BookChapter>> {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    //  private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    //  private Book book;
    private List<BookChapter> bookChapters;
    private RecyclerView recyclerView;
    private ProgressBar bookchapter_progressBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookChapterListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BookChapterListFragment newInstance(Book book) {
        BookChapterListFragment fragment = new BookChapterListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_COLUMN_COUNT, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ChapterDao chapterDao = new ChapterDao(getActivity());
            List<BookChapter> bookChapters = chapterDao.getBookChapter(((Book) getArguments().getSerializable(ARG_COLUMN_COUNT)).getBookId());
            if (bookChapters == null || bookChapters.size() == 0) {
                NetworkHelper<List<BookChapter>> networkHelper = new ChapterListNetworkHelper(getActivity(), (Book) getArguments().getSerializable(ARG_COLUMN_COUNT));
                networkHelper.setUiDataListener(this);
                networkHelper.sendGETRequest(((Book) getArguments().getSerializable(ARG_COLUMN_COUNT)).getChapterUrl(), null);
            } else {
                this.bookChapters = bookChapters;
                ((Book) getArguments().getSerializable(ARG_COLUMN_COUNT)).setBookChapters(bookChapters);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookchapterfragment_list, container, false);
        bookchapter_progressBar = (ProgressBar) view.findViewById(R.id.bookchapter_progressBar);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        if (bookChapters == null || bookChapters.size() == 0) {
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new BookChapterFragmentRecyclerViewAdapter(bookChapters, mListener));
            bookchapter_progressBar.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onDataChanged(List<BookChapter> data) {
        bookchapter_progressBar.setVisibility(View.GONE);
        bookChapters = data;
        if (bookChapters != null) {
            ((Book) getArguments().getSerializable(ARG_COLUMN_COUNT)).setBookChapters(data);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new BookChapterFragmentRecyclerViewAdapter(bookChapters, mListener));
        }
    }

    @Override
    public void onErrorHappened(String errorMessage) {
        bookchapter_progressBar.setVisibility(View.GONE);
        BookInfoActivity mactivity = (BookInfoActivity) getActivity();
        mactivity.showSnackBar(errorMessage);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(int position);
    }

}
