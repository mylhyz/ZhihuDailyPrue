package com.lhyz.demo.zhihudialyprue.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhyz.demo.zhihudialyprue.R;
import com.lhyz.demo.zhihudialyprue.bean.StorySimple;
import com.squareup.picasso.Picasso;

public class LabsFragment extends Fragment {

    private StorySimple mStoryHot;
    private Context mContext;

    public LabsFragment() {

    }

    public static LabsFragment getInstance(Context context,StorySimple storyHot){
        LabsFragment fragment = new LabsFragment();
        fragment.mContext = context;
        fragment.mStoryHot = storyHot;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_pager_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageView imageView = (ImageView)view.findViewById(R.id.image);
        Picasso.with(mContext)
                .load(mStoryHot.getImage())
                .fit()
                .centerCrop()
                .into(imageView);
        TextView textView = (TextView)view.findViewById(R.id.title);
        textView.setText(mStoryHot.getTitle());
    }
}