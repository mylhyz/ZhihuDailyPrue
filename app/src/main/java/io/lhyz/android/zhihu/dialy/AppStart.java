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

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import io.lhyz.android.zhihu.dialy.base.DefaultSubscriber;
import io.lhyz.android.zhihu.dialy.data.bean.StartImage;
import io.lhyz.android.zhihu.dialy.data.source.DialyService;
import io.lhyz.android.zhihu.dialy.data.source.ServiceCreator;
import io.lhyz.android.zhihu.dialy.mvp.BaseActivity;
import io.lhyz.android.zhihu.dialy.util.TagHelper;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * hello,android
 * Created by lhyz on 2016/8/19.
 */
public class AppStart extends BaseActivity {

    private static final String TAG = TagHelper.from(AppStart.class);

    @BindView(R.id.img_start)
    SimpleDraweeView mSimpleDraweeView;
    @BindView(R.id.tv_author)
    TextView mTextView;

    Subscription mSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int width = getResources().getDisplayMetrics().widthPixels;
        String size;
        if (width < 320) {
            size = "320*432";
        } else if (width < 480) {
            size = "480*728";
        } else if (width < 720) {
            size = "720*1184";
        } else {
            size = "1080*1776";
        }

        DialyService service = ServiceCreator.getInstance().createService();
        mSubscription = service.getStartImage(size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mStartImageSubscriber);
    }

    private final Subscriber<StartImage> mStartImageSubscriber = new DefaultSubscriber<StartImage>() {
        @Override
        protected void onSuccess(StartImage result) {
            mSimpleDraweeView.setImageURI(Uri.parse(result.getImg()));
            mTextView.setText(result.getText());
        }

        @Override
        public void onError(Throwable e) {
            Logger.e(TAG, e.getMessage());
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (mSubscription != null) {
            if (mSubscription.isUnsubscribed()) {
                mSubscription.unsubscribe();
            }
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.act_start;
    }

    @Override
    protected void setWindowFeature() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
