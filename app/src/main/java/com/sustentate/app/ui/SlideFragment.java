package com.sustentate.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sustentate.app.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

/*
 * Created by mzorilla on 11/5/17.
 */

public class SlideFragment extends Fragment {

    private String titleText;
    private String subTitleText;
    private int backgroundColor;
    private int imageResource;

    public static SlideFragment newInstance(String title, String subtitle, int backgroundColor, int image) {
        SlideFragment fragmentFirst = new SlideFragment();
        Bundle args = new Bundle();
        args.putString("slideTitle", title);
        args.putString("slideSubtitle", subtitle);
        args.putInt("slideColor", backgroundColor);
        args.putInt("slideImage", image);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleText = getArguments().getString("slideTitle");
        subTitleText = getArguments().getString("slideSubtitle");
        backgroundColor = getArguments().getInt("slideColor");
        imageResource = getArguments().getInt("slideImage");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide, container, false);

        ImageView image = view.findViewById(R.id.fragment_image);
        TextView title = view.findViewById(R.id.fragment_title);
        TextView subTitle = view.findViewById(R.id.fragment_sub_title);
        View background = view.findViewById(R.id.fragment_bg);

        ImageView bgImage = view.findViewById(R.id.fragment_bg_image);

        title.setText(titleText);
        subTitle.setText(subTitleText);
        background.setBackgroundColor(ContextCompat.getColor(getActivity(), backgroundColor));
        Glide.with(this).load(imageResource).into(image);
        Glide.with(this).load(R.drawable.bg_main).apply(RequestOptions.bitmapTransform(new BlurTransformation(100))).into(bgImage);

        return view;
    }
}
