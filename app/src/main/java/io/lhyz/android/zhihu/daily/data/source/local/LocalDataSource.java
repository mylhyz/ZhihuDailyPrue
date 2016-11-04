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

import io.lhyz.android.zhihu.daily.data.bean.Latest;
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

    private LocalDataSource(Context context) {

    }

    @Override
    public void loadLatest(LoadLatestCallback callback) {

    }

    @Override
    public void loadNewContent(long id, LoadNewCallback callback) {

    }

    @Override
    public void saveLatest(Latest latest) {

    }

    @Override
    public void cancel() {

    }
}
