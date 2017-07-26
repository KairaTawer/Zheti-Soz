package com.zerotoone.n17r.zhetisoz.Activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zerotoone.n17r.zhetisoz.Adapters.ResultListAdapter;
import com.zerotoone.n17r.zhetisoz.Models.AnsweredQuestion;
import com.zerotoone.n17r.zhetisoz.Models.CircleProgressBar;
import com.zerotoone.n17r.zhetisoz.Models.RandomCircle;
import com.zerotoone.n17r.zhetisoz.R;

import java.util.ArrayList;

public class TestingResultActivity extends AppCompatActivity {

    ArrayList<AnsweredQuestion> answeredQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_result);

        CircleProgressBar circleProgressBar = (CircleProgressBar) findViewById(R.id.custom_progressBar);
        TextView mPercentage = (TextView) findViewById(R.id.tv_progress);
        TextView mResultLevel = (TextView) findViewById(R.id.tv_result_level);
        Button mButtonRetry = (Button) findViewById(R.id.button_retry);
        Button mButtonClose = (Button) findViewById(R.id.button_close);
        RelativeLayout mSwipeContainer = (RelativeLayout) findViewById(R.id.mainLayout);
        LinearLayout shareButton = (LinearLayout) findViewById(R.id.button_share);
        FrameLayout mCircleContainer = (FrameLayout) findViewById(R.id.circle_container);

        answeredQuestions = getIntent().getParcelableArrayListExtra("RESULT_LIST");
        final int questionsCount = answeredQuestions.size();
        int correctCount = 0;

        for(AnsweredQuestion each:answeredQuestions) {
            if(each.getCorrectAnswer().equals(each.getSelectedAnswer())) {
                correctCount++;
            }
            Log.i("SELECTED",each.getSelectedAnswer());
            Log.i("CORRECT",each.getCorrectAnswer());
        }

        int percentage = (int) (correctCount * 100/questionsCount);
        mPercentage.setText(String.valueOf(percentage) + "%");

        if(percentage > 75) {
            circleProgressBar.setColor(Color.parseColor("#F87D7F"));
            mResultLevel.setText("Керемет");
            mResultLevel.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.nice_background_xml));
        } else if(percentage > 50) {
            circleProgressBar.setColor(Color.parseColor("#59D656"));
            mResultLevel.setText("Жақсы");
            mResultLevel.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.good_background_xml));
        } else if(percentage > 25) {
            circleProgressBar.setColor(Color.parseColor("#F6AE01"));
            mResultLevel.setText("Орташа");
            mResultLevel.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.medium_background_xml));
        } else {
            circleProgressBar.setColor(Color.parseColor("#E6D8D8"));
            mResultLevel.setText("Нашар");
            mResultLevel.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.bad_background_xml));
        }

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(circleProgressBar, "progress", percentage);
        objectAnimator.setDuration(1000);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();

        for (int i = 0; i < 7; i++) {
            RandomCircle circle = new RandomCircle(this);
            mCircleContainer.addView(circle);
        }

        final int finalCorrectCount = correctCount;
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                if(finalCorrectCount != 0)
                    sharingIntent.putExtra(Intent.EXTRA_TEXT,"Мен " + questionsCount + " сұрақтың " + finalCorrectCount + " сұрағына дұрыс жауап бердім. Менен көбірек ұпай жинай аласың ба? Онда мына сілтемені басып, қосымшаны жазып ал!");
                else
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "Мына сілтемені басып, қосымшаны жазып ал!");

                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        mButtonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestingResultActivity.this,TestingPreviewActivity.class));
            }
        });

        mButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestingResultActivity.this, MainActivity.class));
            }
        });

        final GestureDetector gdt = new GestureDetector(new GestureListener());
        mSwipeContainer.setOnTouchListener(new View.OnTouchListener() {
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
            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                Intent intent = new Intent(TestingResultActivity.this,ResultListActivity.class);
                intent.putParcelableArrayListExtra("RESULT_LIST",answeredQuestions);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                return false; // Bottom to top
            }

            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this,TestingPreviewActivity.class));
    }
}
