package com.zxy.books.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zxy.books.R;
import com.zxy.books.model.Book;
import com.zxy.books.network.BooKbetailHelper;
import com.zxy.books.network.volley.NetworkHelper;
import com.zxy.books.network.volley.UIDataListener;
import com.zxy.books.ui.activity.BookInfoActivity;


public class BookDescFragment extends Fragment implements UIDataListener<Book> {

    private Book book;
    private TextView tvDesc;
    private TextView latsUpate;
    private ProgressBar bookDesc_progressBar;

    public BookDescFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BookDescFragment newInstance(Book param1) {
        BookDescFragment fragment = new BookDescFragment();
        Bundle args = new Bundle();
        args.putSerializable("book", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = (Book) getArguments().getSerializable("book");
            NetworkHelper<Book> networkHelper = networkHelper = new BooKbetailHelper(getActivity(), book);
            networkHelper.setUiDataListener(this);
            networkHelper.sendGETRequest(book.getIndexUrl(), null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_desc, container, false);
        tvDesc = (TextView) view.findViewById(R.id.fragment_desc);
        latsUpate = (TextView) view.findViewById(R.id.fragment_desc_lastUpdate);
        bookDesc_progressBar = (ProgressBar) view.findViewById(R.id.bookDesc_progressBar);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDataChanged(Book data) {
        bookDesc_progressBar.setVisibility(View.GONE);
        if (TextUtils.isEmpty(book.getLastChapter()))
            latsUpate.setText("");
        else
            latsUpate.setText(getResources().getString(R.string.upate) + book.getLastChapter());
        String desc = data.getBookDesc().replace(" ", "");
        if (desc.equals("br/>")) {
            tvDesc.setText(R.string.not_desc);
            return;
        }
        tvDesc.setText(data.getBookDesc());
    }

    @Override
    public void onErrorHappened(String errorMessage) {
        bookDesc_progressBar.setVisibility(View.GONE);
        BookInfoActivity mactivity = (BookInfoActivity) getActivity();
        mactivity.showSnackBar(errorMessage);
    }
}
