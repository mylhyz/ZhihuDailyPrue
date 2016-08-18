package io.lhyz.android.zhihu.dialy.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import io.lhyz.android.zhihu.dialy.R;
import io.lhyz.android.zhihu.dialy.adapter.NewsListAdapter;
import io.lhyz.android.zhihu.dialy.loader.StoryLoader;
import io.lhyz.android.zhihu.dialy.loader.TopStoryLoader;
import io.lhyz.android.zhihu.dialy.service.UpdateService;


public class MainActivity extends AppCompatActivity {
    private LoaderManager mLoaderManager;
    private FragmentManager mFragmentManager;
    private Context mContext = this;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NewsListAdapter mAdapter;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private Cursor c1;
    private Cursor c2;

    private static boolean mk1;
    private static boolean mk2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mLoaderManager = getSupportLoaderManager();
        mFragmentManager = getSupportFragmentManager();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.blue1,
                R.color.blue2,
                R.color.blue3,
                R.color.blue4
        );
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mk1=mk2=false;
                startService(new Intent(mContext, UpdateService.class));
                mLoaderManager.restartLoader(1, null, new TopStoryLoaderCallback());
                mLoaderManager.restartLoader(2, null, new StoryLoaderCallback());
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.listview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(null);

        mk1=mk2=false;
        mLoaderManager.initLoader(1, null, new TopStoryLoaderCallback());
        mLoaderManager.initLoader(2, null, new StoryLoaderCallback());
    }

    private class TopStoryLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new TopStoryLoader(mContext);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mk2 = true;
            c2=data;
            if(mk1){
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter = new NewsListAdapter(mContext,mFragmentManager,c1,c2);
                mRecyclerView.setAdapter(mAdapter);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    private class StoryLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new StoryLoader(mContext);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mk1 = true;
            c1=data;
            if(mk2){
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter = new NewsListAdapter(mContext,mFragmentManager,c1,c2);
                mRecyclerView.setAdapter(mAdapter);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}
