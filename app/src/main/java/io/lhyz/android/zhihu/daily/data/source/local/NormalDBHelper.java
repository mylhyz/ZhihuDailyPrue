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
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import io.lhyz.android.zhihu.daily.data.bean.Normal;


/**
 * hello,android
 * Created by lhyz on 2016/8/27.
 */
public class NormalDBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "Normal.db";
    private static final int DATABASE_VERSION = 1;

    Dao<Normal, Long> mDao;

    public NormalDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Normal.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Normal.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(database);
    }

    public Dao<Normal, Long> getDao() {
        try {
            mDao = getDao(Normal.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mDao;
    }
}
