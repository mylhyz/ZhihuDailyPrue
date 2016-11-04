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
package io.lhyz.android.zhihu.daily.ui.detail;

import io.lhyz.android.zhihu.daily.Injections;
import io.lhyz.android.zhihu.daily.data.bean.New;
import io.lhyz.android.zhihu.daily.data.source.DailyRepository;
import io.lhyz.android.zhihu.daily.data.source.DataSource;

/**
 * hello,android
 * Created by lhyz on 2016/8/19.
 */
public class DetailPresenter implements DetailContract.Presenter {

    DailyRepository mRepository;
    DetailContract.View mView;

    long id;

    public DetailPresenter(DetailContract.View view, long id) {
        mView = view;
        mView.setPresenter(this);
        this.id = id;

        mRepository = Injections.provideRepository();
    }

    @Override
    public void loadNewContent() {
        mRepository.loadNewContent(id, new DataSource.LoadNewCallback() {
            @Override
            public void onNewLoaded(New content) {
                if (content == null) {
                    onNoNewAvailable();
                    return;
                }
                mView.showContent(content);
            }

            @Override
            public void onNoNewAvailable() {

            }
        });
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {
        mRepository.cancel();
    }

    @Override
    public void destroy() {
        mRepository.cancel();
        mView = null;
    }
}
