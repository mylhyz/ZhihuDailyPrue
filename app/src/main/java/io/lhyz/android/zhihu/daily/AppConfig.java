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

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import io.lhyz.android.zhihu.daily.data.bean.StartImage;

/**
 * hello,android
 * Created by lhyz on 2016/8/20.
 */
public class AppConfig {
    private static final String CONFIG_NAME = "zhihu_dialy";

    private static final String KEY_START_IMAGE_URL = "start_image_url";
    private static final String KEY_START_IMAGE_TITLE = "start_image_title";

    private SharedPreferences mSharedPreferences;

    private AppConfig() {
        mSharedPreferences = ZhihuDailyApp.getAppContext()
                .getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
    }

    private static class Holder {
        private static final AppConfig instance = new AppConfig();
    }

    public static AppConfig getInstance() {
        return Holder.instance;
    }


    public StartImage readStartImage() {
        String url = mSharedPreferences.getString(KEY_START_IMAGE_URL, null);
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String title = mSharedPreferences.getString(KEY_START_IMAGE_TITLE, null);
        return new StartImage(title, url);
    }

    public void saveStartImage(@NonNull StartImage startImage) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_START_IMAGE_URL, startImage.getImg());
        editor.putString(KEY_START_IMAGE_TITLE, startImage.getText());
        editor.apply();
    }
}
