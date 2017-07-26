package com.zerotoone.n17r.zhetisoz.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.zerotoone.n17r.zhetisoz.Activities.PairMatchingPreviewActivity;
import com.zerotoone.n17r.zhetisoz.Activities.TestingPreviewActivity;
import com.zerotoone.n17r.zhetisoz.R;

public class FirstFragment extends Fragment {

    private OnFragmentInteractionListener listener;

    public static FirstFragment newInstance() {
        return new FirstFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout pairMatching = (LinearLayout) view.findViewById(R.id.pair_matching);
        LinearLayout testing = (LinearLayout) view.findViewById(R.id.testing);

        pairMatching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(),PairMatchingPreviewActivity.class));
            }
        });

        testing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(),TestingPreviewActivity.class));
            }
        });


    }

    public interface OnFragmentInteractionListener {
    }

    public void adaptThatView (final LinearLayout view) {
        ViewTreeObserver mLayoutObserver = view.getViewTreeObserver();

        mLayoutObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {

            @Override
            public void onGlobalLayout()
            {
                DisplayMetrics metrics = getResources().getDisplayMetrics();

                int deviceWidth = metrics.widthPixels;

                int deviceHeight = metrics.heightPixels;

                float widthInPercentage =  ( (float) 320 / 768 )  * 100; // 280 is the width of my LinearLayout and 320 is device screen width as i know my current device resolution are 320 x 480 so i'm calculating how much space (in percentage my layout is covering so that it should cover same area (in percentage) on any other device having different resolution

                float heightInPercentage =  ( (float) 280 / 1280 ) * 100; // same procedure 300 is the height of the LinearLayout and i'm converting it into percentage

                int mLayoutWidth = (int) ( (widthInPercentage * deviceWidth) / 100 );

                int mLayoutHeight = (int) ( (heightInPercentage * deviceHeight) / 100 );

                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.height = mLayoutHeight;
                layoutParams.width = mLayoutWidth;

                view.setLayoutParams(layoutParams);
            }
        });
    }

}