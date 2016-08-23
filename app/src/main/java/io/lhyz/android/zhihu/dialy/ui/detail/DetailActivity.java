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
package io.lhyz.android.zhihu.dialy.ui.detail;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import io.lhyz.android.zhihu.dialy.R;
import io.lhyz.android.zhihu.dialy.data.bean.New;
import io.lhyz.android.zhihu.dialy.ui.BaseActivity;

/**
 * hello,android
 * Created by lhyz on 2016/8/19.
 */
public class DetailActivity extends BaseActivity
        implements DetailContract.View {
    public static final String EXTRA_STORY_ID =
            "io.lhyz.android.zhihu.dialy.ui.detail.EXTRA_STORY_ID";

    @BindView(R.id.img_big)
    SimpleDraweeView mSimpleDraweeView;
    @BindView(R.id.tv_title)
    TextView mTextView;
    @BindView(R.id.web_content)
    TextView mWebView;

    long storyId;

    DetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storyId = getIntent().getLongExtra(EXTRA_STORY_ID, 0L);
        mPresenter = new DetailPresenter(this, storyId);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected int getLayout() {
        return R.layout.act_detail;
    }


    @Override
    public void showContent(New detail) {
        mSimpleDraweeView.setImageURI(Uri.parse(detail.getImage()));
        mTextView.setText(detail.getTitle());
        mWebView.setText(Html.fromHtml(getHtmlData(detail.getCssURL().get(0),
                detail.getBody())));
    }

    @Override
    public void showShareDialog() {

    }

    @Override
    public void hideShareDialog() {

    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {

    }

    private String getHtmlData(String cssUrl, String bodyHTML) {
        String head = "<head>" +
                "<link href=\"" + cssUrl + "\" rel=\"stylesheet\">" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
