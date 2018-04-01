package com.zerotoone.n17r.zhetisoz.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

}