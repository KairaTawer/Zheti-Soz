package com.zerotoone.n17r.zhetisoz.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.tmall.ultraviewpager.UltraViewPager;
import com.zerotoone.n17r.zhetisoz.Adapters.ResultListAdapter;
import com.zerotoone.n17r.zhetisoz.Models.AnsweredQuestion;
import com.zerotoone.n17r.zhetisoz.Models.CustomPageTransformer;
import com.zerotoone.n17r.zhetisoz.R;

import java.util.ArrayList;

public class ResultListActivity extends AppCompatActivity {

    ArrayList<AnsweredQuestion> answeredQuestions = new ArrayList<>();
    UltraViewPager ultraViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_layout);

        answeredQuestions = getIntent().getParcelableArrayListExtra("RESULT_LIST");

        ResultListAdapter mAdapter = new ResultListAdapter(this,answeredQuestions);

        ultraViewPager = (UltraViewPager) findViewById(R.id.pager);
        ultraViewPager.setAdapter(mAdapter);

        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);

        ultraViewPager.setMultiScreen(0.75f);
        ultraViewPager.setItemRatio(.6f);
        ultraViewPager.setAutoMeasureHeight(true);

        ultraViewPager.setPageTransformer(false, new CustomPageTransformer());

        final GestureDetector gdt = new GestureDetector(new GestureListener());
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });
    }

    public static final int SWIPE_MIN_DISTANCE = 120;
    public static final int SWIPE_THRESHOLD_VELOCITY = 200;

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                Intent intent = new Intent(ResultListActivity.this,TestingResultActivity.class);
                intent.putParcelableArrayListExtra("RESULT_LIST",answeredQuestions);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                return true;
            }

            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResultListActivity.this,TestingResultActivity.class);
        intent.putParcelableArrayListExtra("RESULT_LIST",answeredQuestions);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
    }
}
