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

import io.lhyz.android.zhihu.dialy.Injections;
import io.lhyz.android.zhihu.dialy.data.bean.Latest;
import io.lhyz.android.zhihu.dialy.data.source.DataSource;
import io.lhyz.android.zhihu.dialy.data.source.DialyRepository;
import rx.subscriptions.CompositeSubscription;

/**
 * hello,android
 * Created by lhyz on 2016/8/19.
 */
public class MainPresenter implements MainContract.Presenter {

    MainContract.View mView;

    DialyRepository mRepository;

    CompositeSubscription mCompositeSubscription;

    public MainPresenter(MainContract.View view) {
        mView = view;
        mView.setPresenter(this);

        mCompositeSubscription = new CompositeSubscription();

        mRepository = Injections.provideRepository();
    }

    @Override
    public void loadLatest() {
        mView.showLoading();
        mRepository.loadLatest(new DataSource.LoadLatestCallback() {
            @Override
            public void onLatestLoaded(Latest result) {
                mView.hideLoading();
                mView.showLatest(result);
            }

            @Override
            public void onNoLatestAvailable() {
                mView.hideLoading();
            }
        });
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {
        mCompositeSubscription.clear();
    }

    @Override
    public void destroy() {
        mCompositeSubscription.clear();
        mView = null;
    }
}
