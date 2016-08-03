package com.zxy.books.ui.fragment;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.zxy.books.R;
import com.zxy.books.ui.activity.BookListActivity;
import com.zxy.books.ui.adapter.OnlineTitleAdapter;
import com.zxy.books.ui.base.BaseFragment;
import com.zxy.books.util.SystemUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 网上界面Fragment
 */
public class OnlineFragment extends BaseFragment {
    private RecyclerView leaderboard_recyclerView;
    private RecyclerView sort_recyclerView;
    private List<String> leaderboardString;
    private List<String> sortString;
    //private RecyclerView searchResult;
    //private MyLayoutManager layoutManager;

    @Override
    public View setContentView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_online, null);
        return view;
    }

    @Override
    protected void initFindViewById(View view) {
        leaderboard_recyclerView = (RecyclerView) view.findViewById(R.id.fragment_online_leaderboard_recyclerView);
        sort_recyclerView = (RecyclerView) view.findViewById(R.id.fragment_online_sort_recyclerView);
        if (!SystemUtil.isConn(mActivity)) {//判断是否开启了网络
            final Snackbar snackbar =
                    Snackbar.make(view, R.string.no_internet,
                            Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.ok1,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = null;
                            // 先判断当前系统版本
                            if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                                intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                            } else {
                                intent = new Intent();
                                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                            }
                            mActivity.startActivity(intent);
                        }
                    });
            snackbar.show();
        }

        //searchResult = (RecyclerView) view.findViewById(R.id.searcheRecyclerView);
    }

    @Override
    public void initData() {
        leaderboardString = Arrays.asList(getResources().getStringArray
                (R.array.string_array_online_leaderboard));
        sortString = Arrays.asList(getResources().getStringArray
                (R.array.string_array_online_sort));
        //   layoutManager = new MyLayoutManager(getActivity());
    }

    @Override
    protected void initView() {
        leaderboard_recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        leaderboard_recyclerView.setLayoutManager(layoutManager);
        leaderboard_recyclerView.setItemAnimator(new DefaultItemAnimator());
        OnlineTitleAdapter leaderboardAdapter = new OnlineTitleAdapter(getActivity(), leaderboardString);
        leaderboardAdapter.setOnItemClickListener(this);
        leaderboard_recyclerView.setAdapter(leaderboardAdapter);
        LinearLayoutManager sortlayoutManager = new LinearLayoutManager(getActivity());
        sort_recyclerView.setHasFixedSize(true);
        sort_recyclerView.setLayoutManager(sortlayoutManager);
        leaderboard_recyclerView.setItemAnimator(new DefaultItemAnimator());
        OnlineTitleAdapter sortAdapter = new OnlineTitleAdapter(getActivity(), sortString);
        sortAdapter.setOnItemClickListener(this);
        sort_recyclerView.setAdapter(sortAdapter);
    }

    @Override
    protected void setOnClick() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(View view, Object data) {
        Intent it = new Intent(mActivity, BookListActivity.class);
        it.putExtra("bookType", (String) data);
        mActivity.startActivity(it);
    }

    @Override
    protected void notifyDataSetChanged(String changed, Boolean isClose) {
        //  Toast.makeText(getActivity(), "搜索事件", Toast.LENGTH_SHORT).show();
        if (isClose) {
            intent(changed);
        }
//        if (isClose) {
//            searchResult.setVisibility(View.VISIBLE);
//            Book book = new Book();
//            book.setBookName(changed);
//            book.setBookAuthor("122");
//            List<Book> books = new ArrayList<>();
//            books.add(book);
//            SearchResultAdapter searchResultAdapter = new SearchResultAdapter(books, getActivity());
//            searchResult.setLayoutManager(layoutManager);
//            searchResult.setAdapter(searchResultAdapter);
//            searchResultAdapter.setOnItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
//                @Override
//                public void onItemClick(View view, Object data) {
//                    mActivity.closeSearchView();
//                    Snackbar.make(view, (String) data, Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                }
//            });
//
//        } else
//            searchResult.setVisibility(View.GONE);
    }

    private void intent(String type) {
        Intent it = new Intent(mActivity, BookListActivity.class);
        it.putExtra("bookName", type);
        mActivity.startActivity(it);
    }
}
