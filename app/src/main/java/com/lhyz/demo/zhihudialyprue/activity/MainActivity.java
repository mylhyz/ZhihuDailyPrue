package com.lhyz.demo.zhihudialyprue.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.lhyz.demo.zhihudialyprue.R;
import com.lhyz.demo.zhihudialyprue.adapter.NewsListAdapter;
import com.lhyz.demo.zhihudialyprue.bean.StorySimple;
import com.lhyz.demo.zhihudialyprue.service.UpdateService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PRE_DATE = "date";

    private LoaderManager mLoaderManager;
    private FragmentManager mFragmentManager;
    private Context mContext = this;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NewsListAdapter mAdapter;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private List<StorySimple> mDataTodays;
    private List<StorySimple> mDataHots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


//        String date = PreferenceManager.getDefaultSharedPreferences(this)
//                .getString(PRE_DATE, DateUtil.getToady());
//        PreferenceManager.getDefaultSharedPreferences(this)
//                .edit()
//                .putString(PRE_DATE, DateUtil.getToady())
//                .commit();
//        if (!DateUtil.getToady().equals(date)) {
//            DataSource.getInstance(this).moveData();
//        }

//        mLoaderManager = getSupportLoaderManager();
//        mFragmentManager = getSupportFragmentManager();
//
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setTitle(getResources().getString(R.string.app_name));
//        setSupportActionBar(mToolbar);
//
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
//        mSwipeRefreshLayout.setColorSchemeResources(
//                R.color.blue1,
//                R.color.blue2,
//                R.color.blue3,
//                R.color.blue4
//        );
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.listview);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setAdapter(null);

        startService(new Intent(this, UpdateService.class));
    }
}
