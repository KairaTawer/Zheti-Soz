package com.zerotoone.n17r.zhetisoz.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zerotoone.n17r.zhetisoz.Activities.MainActivity;
import com.zerotoone.n17r.zhetisoz.Activities.PairMatchingPreviewActivity;
import com.zerotoone.n17r.zhetisoz.Activities.TestingPreviewActivity;
import com.zerotoone.n17r.zhetisoz.R;

public class AfterPairMatchingBestResultFragment extends Fragment {

    public static AfterPairMatchingResultFragment newInstance() {
        return new AfterPairMatchingResultFragment();
    }

    public AfterPairMatchingBestResultFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_after_pair_matching_best_result, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView resultText = (TextView) view.findViewById(R.id.result);
        Button mCloseButton = (Button) view.findViewById(R.id.button_close);
        Button mRetryButton = (Button) view.findViewById(R.id.button_retry);

        Bundle bundle = this.getArguments();
        float i = 0.0f;
        if(bundle != null) {i = bundle.getFloat("SCORE", 0.0f);}

        resultText.setText(String.format("Сен жаңа рекордты орнаттың! \n %5.1f секунд", i).replace(".", ","));

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PairMatchingPreviewActivity.class));
            }
        });
    }
}