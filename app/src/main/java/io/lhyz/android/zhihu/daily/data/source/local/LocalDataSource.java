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
package io.lhyz.android.zhihu.daily.data.source.local;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import io.lhyz.android.zhihu.daily.data.bean.Latest;
import io.lhyz.android.zhihu.daily.data.bean.Normal;
import io.lhyz.android.zhihu.daily.data.bean.Top;
import io.lhyz.android.zhihu.daily.data.source.DataSource;


/**
 * hello,android
 * Created by lhyz on 2016/8/19.
 */
public class LocalDataSource implements DataSource {

    private static volatile LocalDataSource INSTANCE;

    public static LocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }

    Dao<Normal, Long> mNormalLongDao;
    Dao<Top, Long> mTopLongDao;


    private LocalDataSource(Context context) {
        mNormalLongDao = new NormalDBHelper(context).getDao();
        mTopLongDao = new TopDBHelper(context).getDao();
    }

    @Override
    public void loadLatest(LoadLatestCallback callback) {

    }

    @Override
    public void loadNewContent(long id, LoadNewCallback callback) {

    }

    @Override
    public void saveLatest(Latest latest) {
        try {
            List<Normal> normalList = latest.getStories();
            for (Normal normal : normalList) {
                normal.setDate(latest.getDate());
                if (normal.getImages() != null) {
                    normal.setImage(normal.getImages().get(0));
                }
                mNormalLongDao.createOrUpdate(normal);
            }
            List<Top> topList = latest.getTopStories();
            for (Top top : topList) {
                top.setDate(latest.getDate());
                mTopLongDao.createOrUpdate(top);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel() {

    }
}
