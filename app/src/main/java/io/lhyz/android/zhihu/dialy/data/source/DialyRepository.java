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
package io.lhyz.android.zhihu.dialy.data.source;

import io.lhyz.android.zhihu.dialy.data.bean.Latest;
import io.lhyz.android.zhihu.dialy.data.bean.New;

/**
 * hello,android
 * Created by lhyz on 2016/8/19.
 */
public class DialyRepository implements DataSource {


    DataSource mLocalDataSource;
    DataSource mRemoteDataSource;

    public DialyRepository(DataSource localDataSource,
                           DataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public void loadLatest(final LoadLatestCallback callback) {
        mRemoteDataSource.loadLatest(new LoadLatestCallback() {
            @Override
            public void onLatestLoaded(Latest result) {
                callback.onLatestLoaded(result);
                mLocalDataSource.saveLatest(result);
            }

            @Override
            public void onNoLatestAvailable() {
                callback.onNoLatestAvailable();
            }
        });
    }

    @Override
    public void loadNewContent(long id, final LoadNewCallback callback) {
        mRemoteDataSource.loadNewContent(id, new LoadNewCallback() {
            @Override
            public void onNewLoaded(New content) {
                callback.onNewLoaded(content);
            }

            @Override
            public void onNoNewAvailable() {
                callback.onNoNewAvailable();
            }
        });
    }

    @Override
    public void saveLatest(Latest latest) {
        //Ignore
    }

    @Override
    public void cancel() {
        mLocalDataSource.cancel();
        mRemoteDataSource.cancel();
    }
}
