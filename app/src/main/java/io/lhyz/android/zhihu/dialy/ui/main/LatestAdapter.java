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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import io.lhyz.android.zhihu.dialy.R;
import io.lhyz.android.zhihu.dialy.data.bean.Latest;
import io.lhyz.android.zhihu.dialy.data.bean.Story;

/**
 * hello,android
 * Created by lhyz on 2016/8/20.
 */
public class LatestAdapter extends RecyclerView.Adapter<LatestAdapter.ViewHolder> {

//    Latest mLatest;
    List<Story> mStories;

    public LatestAdapter() {
    }

    public void setStoryList(Latest latest) {
        if (mStories == null) {
            mStories = new ArrayList<>();
        }
        if (mStories.size() != 0) {
            mStories.clear();
        }
        mStories.addAll(latest.getStories());
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        SimpleDraweeView mSimpleDraweeView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_title);
            mSimpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.img_icon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_story, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Story story = mStories.get(holder.getAdapterPosition());
        holder.mTextView.setText(story.getTitle());
        final String imageURL = story.getImages().get(0);
        if (imageURL != null) {
            holder.mSimpleDraweeView.setImageURI(Uri.parse(imageURL));
        }
    }

    @Override
    public int getItemCount() {
        return mStories == null ? 0 : mStories.size();
    }
}
