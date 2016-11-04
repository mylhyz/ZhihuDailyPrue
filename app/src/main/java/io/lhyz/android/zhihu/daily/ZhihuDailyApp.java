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

package io.lhyz.android.zhihu.daily;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.bugly.crashreport.CrashReport;

import timber.log.Timber;

public class ZhihuDailyApp extends Application {

    private static ZhihuDailyApp mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        Fresco.initialize(this);
        CrashReport.initCrashReport(getApplicationContext(), "8712cf3fff", false);
        mApp = this;
    }

    public static Context getAppContext() {
        return mApp;
    }
}
