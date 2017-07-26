package com.zerotoone.n17r.zhetisoz.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zerotoone.n17r.zhetisoz.Fragments.AfterPairMatchingBestResultFragment;
import com.zerotoone.n17r.zhetisoz.Fragments.AfterPairMatchingResultFragment;
import com.zerotoone.n17r.zhetisoz.R;

public class AfterPairMatchingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_pair_matching);

        float result = getIntent().getFloatExtra("RESULT_EXTRA",0.0f);

        SharedPreferences prefs = this.getSharedPreferences("ZHETISOZ_APP", Context.MODE_PRIVATE);

        float bestScore = prefs.getFloat("HIGH_SCORE", 1000.0f);

        if(bestScore < result) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            Fragment fragment = new AfterPairMatchingResultFragment();

            Bundle bundle = new Bundle();
            bundle.putFloat("SCORE", result);
            fragment.setArguments(bundle);

            transaction.replace(R.id.main_layout, fragment);

            transaction.commit();

        } else if(bestScore > result) {

            SharedPreferences.Editor editor = prefs.edit();
            editor.putFloat("HIGH_SCORE",result);
            editor.apply();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            Fragment fragment = new AfterPairMatchingBestResultFragment();

            Bundle bundle = new Bundle();
            bundle.putFloat("SCORE", result);
            fragment.setArguments(bundle);

            transaction.replace(R.id.main_layout, fragment);

            transaction.commit();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,PairMatchingPreviewActivity.class));
    }
}
