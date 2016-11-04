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
package io.lhyz.android.zhihu.daily.data.source;


import io.lhyz.android.zhihu.daily.data.bean.Latest;
import io.lhyz.android.zhihu.daily.data.bean.New;

/**
 * hello,android
 * Created by lhyz on 2016/8/19.
 */
public interface DataSource {
    interface LoadLatestCallback {
        void onLatestLoaded(Latest result);

        void onNoLatestAvailable();
    }

    interface LoadNewCallback {
        void onNewLoaded(New content);

        void onNoNewAvailable();
    }

    void loadLatest(LoadLatestCallback callback);

    void loadNewContent(long id, LoadNewCallback callback);

    void saveLatest(Latest latest);

    void cancel();
}
