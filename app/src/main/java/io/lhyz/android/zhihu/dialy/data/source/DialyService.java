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
import io.lhyz.android.zhihu.dialy.data.bean.StartImage;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * hello,android
 * Created by lhyz on 2016/8/19.
 */
public interface DialyService {

    String API_ENDPOINT = "http://news-at.zhihu.com/api/4/";

    @GET("start-image/{size}")
    Observable<StartImage> getStartImage(@Path("size") String size);

    @GET("news/latest")
    Observable<Latest> getLatest();

    @GET("news/{id}")
    Observable<New> getContents(@Path("id") String id);

}
