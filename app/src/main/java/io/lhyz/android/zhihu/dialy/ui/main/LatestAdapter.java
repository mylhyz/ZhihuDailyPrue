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
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
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
public class LatestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TOP = 227;
    private static final int TYPE_NORMAL = 917;
    private static final int TYPE_HEADER = 729;

    Latest mLatest;
    List<Story> mStories;
    List<Story> mTopStories;

    private OnStoryItemClickListener mOnStoryItemClickListener;

    public LatestAdapter() {
    }

    public void setOnStoryItemClickListener(OnStoryItemClickListener onStoryItemClickListener) {
        mOnStoryItemClickListener = onStoryItemClickListener;
    }

    public void setStoryList(Latest latest) {
        mLatest = latest;
        if (mStories == null) {
            mStories = new ArrayList<>();
        }
        if (mTopStories == null) {
            mTopStories = new ArrayList<>();
        }
        if (mStories.size() != 0) {
            mStories.clear();
        }
        if (mTopStories.size() != 0) {
            mTopStories.clear();
        }
        mStories.addAll(latest.getStories());
        mTopStories.addAll(latest.getTopStories());
        notifyDataSetChanged();
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        SimpleDraweeView mSimpleDraweeView;
        View itemView;

        public NormalViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_title);
            mSimpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.img_icon);
            this.itemView = itemView;
        }
    }

    public static class HeaderViewHolder extends NormalViewHolder {
        TextView mHeader;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mHeader = (TextView) itemView.findViewById(R.id.tv_header);
        }
    }

    public static class CarouselViewHolder extends RecyclerView.ViewHolder {
        ViewPager mViewPager;

        public CarouselViewHolder(View itemView) {
            super(itemView);
            mViewPager = (ViewPager) itemView.findViewById(R.id.pager_carousel);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOP) {
            return new CarouselViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_carousel, parent, false));
        }
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_story_header, parent, false));
        }
        return new NormalViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_story, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(holder.getAdapterPosition());
        if (type == TYPE_TOP) {
            final List<Story> topStories = mTopStories;
            final CarouselViewHolder carouselViewHolder = (CarouselViewHolder) holder;
            final ViewPager viewPager = carouselViewHolder.mViewPager;
            final TopAdapter adapter = new TopAdapter(topStories);
            adapter.setOnStoryItemClickListener(mOnStoryItemClickListener);
            viewPager.setOffscreenPageLimit(5);
            viewPager.setAdapter(adapter);

            /**
             * 下面的代码实现的是轮播图3s自动切换的效果
             *
             * 加入了手指滑动监听
             */
            final CountDownTimer timer = new CountDownTimer(3000, 3000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    int size = mTopStories.size();
                    int pos = viewPager.getCurrentItem();
                    if (pos == size - 1) {
                        pos = 0;
                    } else {
                        pos += 1;
                    }
                    viewPager.setCurrentItem(pos);
                }
            };
            timer.start();
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    //手指拖动的时候，停止计时
                    if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                        timer.cancel();
                    }
                    if (state == ViewPager.SCROLL_STATE_SETTLING) {
                        timer.start();
                    }
                }
            });

        } else if (type == TYPE_HEADER) {
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            final Story story = mStories.get(0);
            headerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnStoryItemClickListener.onStoryClick(story);
                }
            });
            headerViewHolder.mHeader.setText("今日新闻");
            headerViewHolder.mTextView.setText(story.getTitle());
            final String imageURL = story.getImages().get(0);
            if (imageURL != null) {
                headerViewHolder.mSimpleDraweeView.setImageURI(Uri.parse(imageURL));
            }
        } else {
            final NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
            final Story story = mStories.get(holder.getAdapterPosition() - 1);
            normalViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnStoryItemClickListener.onStoryClick(story);
                }
            });
            normalViewHolder.mTextView.setText(story.getTitle());
            final String imageURL = story.getImages().get(0);
            if (imageURL != null) {
                normalViewHolder.mSimpleDraweeView.setImageURI(Uri.parse(imageURL));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOP;
        }
        if (position == 1) {
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }


    @Override
    public int getItemCount() {
        return mLatest == null ? 0 : mStories.size() + 1;
    }
}
