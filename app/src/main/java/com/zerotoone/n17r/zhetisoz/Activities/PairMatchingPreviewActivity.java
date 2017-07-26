package com.zerotoone.n17r.zhetisoz.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zerotoone.n17r.zhetisoz.R;

public class PairMatchingPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_matching_preview);

        Button mButtonClose = (Button) findViewById(R.id.button_close);
        Button mButtonStart = (Button) findViewById(R.id.button_start);

        mButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PairMatchingPreviewActivity.this,MainActivity.class));
            }
        });

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PairMatchingPreviewActivity.this,PairMatchingActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
    }
}
