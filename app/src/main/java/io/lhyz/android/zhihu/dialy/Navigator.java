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
package io.lhyz.android.zhihu.dialy;

import android.content.Context;
import android.content.Intent;

import io.lhyz.android.zhihu.dialy.ui.browser.NestedBrowser;
import io.lhyz.android.zhihu.dialy.ui.detail.DetailActivity;
import io.lhyz.android.zhihu.dialy.ui.main.MainActivity;

/**
 * hello,android
 * Created by lhyz on 2016/8/20.
 */
public class Navigator {
    public static void navigateToMainActivity(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void navigateToDetailActivity(Context context, long id) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_STORY_ID, id);
        context.startActivity(intent);
    }

    public static void navigateToNestedBrowser(Context context, String url) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, NestedBrowser.class);
        intent.putExtra(NestedBrowser.EXTRA_URL, url);
        context.startActivity(intent);
    }
}
