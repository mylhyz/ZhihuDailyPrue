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
package io.lhyz.android.zhihu.dialy.ui.main;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import io.lhyz.android.zhihu.dialy.R;
import io.lhyz.android.zhihu.dialy.data.bean.Story;

/**
 * hello,android
 * Created by lhyz on 2016/8/21.
 */
public class TopAdapter extends PagerAdapter {

    List<Story> mStories;

    public TopAdapter(List<Story> stories) {
        mStories = stories;
    }

    @Override
    public int getCount() {
        return mStories == null ? 0 : mStories.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Story story = mStories.get(position);
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_top, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnStoryItemClickListener != null) {
                    mOnStoryItemClickListener.onStoryClick(story);
                }
            }
        });
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.img_big);
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        simpleDraweeView.setImageURI(Uri.parse(story.getImage()));
        textView.setText(story.getTitle());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    public void setOnStoryItemClickListener(OnStoryItemClickListener onStoryItemClickListener) {
        mOnStoryItemClickListener = onStoryItemClickListener;
    }

    private OnStoryItemClickListener mOnStoryItemClickListener;
}
