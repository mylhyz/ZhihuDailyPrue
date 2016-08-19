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

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * hello,android
 * Created by lhyz on 2016/8/19.
 */
public class ServiceCreator {

    private Retrofit mRetrofit;

    private ServiceCreator() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(DialyService.API_ENDPOINT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static class Holder {
        private static final ServiceCreator instance = new ServiceCreator();
    }

    public static ServiceCreator getInstance() {
        return Holder.instance;
    }

    public DialyService createService() {
        return mRetrofit.create(DialyService.class);
    }
}
