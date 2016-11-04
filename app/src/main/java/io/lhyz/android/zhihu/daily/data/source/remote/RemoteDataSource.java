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
package io.lhyz.android.zhihu.daily.data.source.remote;

import io.lhyz.android.zhihu.daily.base.DefaultSubscriber;
import io.lhyz.android.zhihu.daily.data.bean.Latest;
import io.lhyz.android.zhihu.daily.data.bean.New;
import io.lhyz.android.zhihu.daily.data.source.DailyService;
import io.lhyz.android.zhihu.daily.data.source.DataSource;
import io.lhyz.android.zhihu.daily.data.source.ServiceCreator;
import io.lhyz.android.zhihu.daily.executor.JobExecutor;
import io.lhyz.android.zhihu.daily.executor.PostThreadExecutor;
import io.lhyz.android.zhihu.daily.executor.ThreadExecutor;
import io.lhyz.android.zhihu.daily.executor.UIThread;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * hello,android
 * Created by lhyz on 2016/8/19.
 */
public class RemoteDataSource implements DataSource {

    ThreadExecutor mThreadExecutor;
    PostThreadExecutor mPostThreadExecutor;

    DailyService mDailyService;

    CompositeSubscription mCompositeSubscription;

    private static class Holder {
        private static final RemoteDataSource instance = new RemoteDataSource();
    }

    public static RemoteDataSource getInstance() {
        return Holder.instance;
    }

    private RemoteDataSource() {
        mThreadExecutor = new JobExecutor();
        mPostThreadExecutor = new UIThread();

        mDailyService = ServiceCreator.getInstance().createService();

        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void loadLatest(final LoadLatestCallback callback) {
        Subscription subscription = mDailyService.getLatest()
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostThreadExecutor.getScheduler())
                .subscribe(new DefaultSubscriber<Latest>() {
                    @Override
                    protected void onSuccess(Latest result) {
                        if (result == null) {
                            onError(new IllegalStateException("no data available"));
                            return;
                        }
                        callback.onLatestLoaded(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onNoLatestAvailable();
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void loadNewContent(long id, final LoadNewCallback callback) {
        Subscription subscription = mDailyService.getContents(Long.toString(id))
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostThreadExecutor.getScheduler())
                .subscribe(new DefaultSubscriber<New>() {
                    @Override
                    protected void onSuccess(New result) {
                        if (result == null) {
                            callback.onNoNewAvailable();
                            return;
                        }
                        callback.onNewLoaded(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback.onNoNewAvailable();
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void saveLatest(Latest latest) {
        //Ignore
    }

    @Override
    public void cancel() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
    }
}
