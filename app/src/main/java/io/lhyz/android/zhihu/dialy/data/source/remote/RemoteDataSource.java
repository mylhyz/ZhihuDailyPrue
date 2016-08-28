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
package io.lhyz.android.zhihu.dialy.data.source.remote;

import io.lhyz.android.zhihu.dialy.base.DefaultSubscriber;
import io.lhyz.android.zhihu.dialy.data.bean.Latest;
import io.lhyz.android.zhihu.dialy.data.bean.New;
import io.lhyz.android.zhihu.dialy.data.source.DataSource;
import io.lhyz.android.zhihu.dialy.data.source.DialyService;
import io.lhyz.android.zhihu.dialy.data.source.ServiceCreator;
import io.lhyz.android.zhihu.dialy.executor.JobExecutor;
import io.lhyz.android.zhihu.dialy.executor.PostThreadExecutor;
import io.lhyz.android.zhihu.dialy.executor.ThreadExecutor;
import io.lhyz.android.zhihu.dialy.executor.UIThread;
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

    DialyService mDialyService;

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

        mDialyService = ServiceCreator.getInstance().createService();

        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void loadLatest(final LoadLatestCallback callback) {
        Subscription subscription = mDialyService.getLatest()
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
        Subscription subscription = mDialyService.getContents(Long.toString(id))
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
