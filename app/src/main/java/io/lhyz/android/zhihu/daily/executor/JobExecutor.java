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
package io.lhyz.android.zhihu.daily.executor;

import android.support.annotation.NonNull;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * hello,android
 * Created by lhyz on 2016/8/20.
 */
public class JobExecutor implements ThreadExecutor {

    private static final int INITIAL_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 5;

    private static final int KEEP_ALIVE_TIME = 10;

    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final ThreadPoolExecutor threadPoolExecutor;

    public JobExecutor() {
        BlockingDeque<Runnable> workQueue = new LinkedBlockingDeque<>();
        ThreadFactory threadFactory = new JobThreadFactory();
        this.threadPoolExecutor = new ThreadPoolExecutor(
                INITIAL_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                workQueue,
                threadFactory
        );
    }

    @Override
    public void execute(@NonNull Runnable command) {
        this.threadPoolExecutor.execute(command);
    }

    private static class JobThreadFactory implements ThreadFactory {

        private static final String THREAD_NAME = "android_zhihu_dialy_";
        private AtomicInteger counter = new AtomicInteger(0);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, THREAD_NAME + counter.getAndIncrement());
        }
    }
}
