package io.lhyz.android.zhihu.dialy.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.lhyz.android.zhihu.dialy.R;
import io.lhyz.android.zhihu.dialy.activity.ContentActivity;
import io.lhyz.android.zhihu.dialy.bean.StorySimple;

public class LabsFragment extends Fragment {

    private StorySimple mStoryHot;
    private Context mContext;

    public LabsFragment() {

    }

    public static LabsFragment getInstance(Context context,StorySimple simple){
        LabsFragment fragment = new LabsFragment();
        fragment.mContext = context;
        fragment.mStoryHot = simple;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_pager_item, container, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra(ContentActivity.EXTRA_ID,mStoryHot.getId());
                startActivity(intent);
            }
        });
        return v;
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