package com.zxy.books.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.zxy.books.R;
import com.zxy.books.ui.base.BaseFragment;
import com.zxy.books.ui.fragment.BookShelfFragment;
import com.zxy.books.ui.fragment.LocalFragment;
import com.zxy.books.ui.fragment.OnlineFragment;
import com.zxy.books.ui.fragment.RecentlyReadFragment;

/**
 * 主activity
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private Toolbar toolbar;
    private RecentlyReadFragment recentlyReadFragment;
    private BookShelfFragment bookshelfFragment;
    private LocalFragment localFragment;
    private OnlineFragment onlineFragment;
    private FragmentManager frgmetManager;
    private BaseFragment mFragment;//当前显示的Fragment
    private SearchView mSearchView;//搜索菜单按钮
    private boolean isShowSearchView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        closeSearchView();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (mFragment == localFragment) {
            if (localFragment.OnBack())
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        mSearchView = (SearchView) menu.findItem(R.id.ab_search).getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        SpannableString spanText = new SpannableString(getResources().getString(R.string.search));
        mSearchView.setQueryHint(spanText);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isShowSearchView) {
            menu.findItem(R.id.ab_search).setVisible(true);
        } else {
            menu.findItem(R.id.ab_search).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 搜索按钮提交监听事件
     * 输入发搜索或者回车按钮触发
     *
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        if (mFragment == onlineFragment)
            mFragment.searchViewOnQueryText(query, true);
        return false;
    }

    /**
     * 关闭mSearchView
     */
    public void closeSearchView() {
        String strQuery = mSearchView.getQuery().toString();
        mSearchView.setIconified(true);
        if (TextUtils.isEmpty(strQuery)) {
            return;
        } else {
            mSearchView.setIconified(true);
        }
    }

    /**
     * 搜索按钮文本输入框监听事件
     * 只要输入框内数据发生变化就会触发
     *
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        //     mFragment.searchViewOnQueryText(newText, true);
        return false;
    }

    /**
     * 搜索框关闭触发
     *
     * @return
     */
    @Override
    public boolean onClose() {
        //  mFragment.searchViewOnQueryText(null, false);
        return false;
    }

    /**
     * 返回按钮点击事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mFragment == localFragment) {
                if (!localFragment.OnBack())
                    return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        closeSearchView();
        isShowSearchView = false;
        int id = item.getItemId();
        /**
         * 侧滑菜单
         * 切换主界面的 Fragment
         */
        FragmentTransaction transaction = frgmetManager.beginTransaction();
        switch (id) {
            case R.id.nav_recentlyRead:
                toolbar.setTitle(R.string.nav_recentlyRead);
                if (recentlyReadFragment == mFragment)
                    break;
                transaction.hide(mFragment);
                if (recentlyReadFragment == null) {
                    recentlyReadFragment = new RecentlyReadFragment();
                    transaction.add(R.id.frameLayout, recentlyReadFragment);
                } else {
                    transaction.show(recentlyReadFragment);
                }
                mFragment = recentlyReadFragment;
                break;
            case R.id.nav_bookshelf:
                toolbar.setTitle(R.string.nav_bookshelf);
                if (bookshelfFragment == mFragment)
                    break;
                transaction.hide(mFragment);
                if (bookshelfFragment == null) {
                    bookshelfFragment = new BookShelfFragment();
                    transaction.add(R.id.frameLayout, bookshelfFragment);
                } else {
                    transaction.show(bookshelfFragment);
                }
                mFragment = bookshelfFragment;
                break;
            case R.id.nav_local:
                toolbar.setTitle(R.string.nav_local);
                if (localFragment == mFragment)
                    break;
                transaction.hide(mFragment);
                if (localFragment == null) {
                    localFragment = new LocalFragment();
                    transaction.add(R.id.frameLayout, localFragment);
                } else {
                    transaction.show(localFragment);
                }
                mFragment = localFragment;
                break;
            case R.id.nav_online:
                toolbar.setTitle(R.string.nav_online);
                isShowSearchView = true;
                if (onlineFragment == mFragment)
                    break;
                transaction.hide(mFragment);
                if (onlineFragment == null) {
                    onlineFragment = new OnlineFragment();
                    transaction.add(R.id.frameLayout, onlineFragment);
                } else {
                    transaction.show(onlineFragment);
                }
                mFragment = onlineFragment;
                break;
            case R.id.nav_adout:
                Intent it=new Intent(MainActivity.this,AboutActivity.class);
                startActivity(it);
                break;
        }
        /**
         * 初始化侧滑菜单DrawerLayout NavigationView 和toolbar
         */
        transaction.commit();
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * findId
     */
    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    /**
     * 界面初始化
     */
    private void initView() {
        //初始化Fragmet碎片
        frgmetManager = getSupportFragmentManager();
        FragmentTransaction transaction = frgmetManager.beginTransaction();
        if (recentlyReadFragment == null)
            recentlyReadFragment = new RecentlyReadFragment();
        transaction.add(R.id.frameLayout, recentlyReadFragment);
        transaction.commit();
        mFragment = recentlyReadFragment;

        //NavigationView初始化
        toolbar.setTitle(R.string.nav_recentlyRead);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        //
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Intent it = new Intent(MainActivity.this, BookInfoActivity.class);
//                startActivity(it, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
//            }
//        });
    }
//
//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }
}

