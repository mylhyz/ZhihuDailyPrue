/*
 * Copyright (c) 2016 lhyz Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lhyz.android.zhihu.dialy.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import io.lhyz.android.zhihu.dialy.Navigator;
import io.lhyz.android.zhihu.dialy.R;
import io.lhyz.android.zhihu.dialy.data.bean.Latest;
import io.lhyz.android.zhihu.dialy.data.bean.Normal;
import io.lhyz.android.zhihu.dialy.data.bean.Top;
import io.lhyz.android.zhihu.dialy.ui.BaseActivity;

/**
 * hello,android
 * Created by lhyz on 2016/8/19.
 */
public class MainActivity extends BaseActivity
        implements MainContract.View
        , NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.list_story)
    RecyclerView mRecyclerView;

    MainContract.Presenter mPresenter;
    LatestAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadLatest();
            }
        });

        mAdapter = new LatestAdapter();
        mAdapter.setOnStoryItemClickListener(mOnStoryItemClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new MainPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    protected int getLayout() {
        return R.layout.act_main;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }


    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLatest(Latest latest) {
        mAdapter.setStoryList(latest);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {

    }

    private final OnStoryItemClickListener mOnStoryItemClickListener =
            new OnStoryItemClickListener() {
                @Override
                void onNormalClick(Normal story) {
                    Navigator.navigateToDetailActivity(getActivity(), story.getId());
                }

                @Override
                void onTopClick(Top top) {
                    Navigator.navigateToDetailActivity(getActivity(), top.getId());
                }
            };
}
