package com.zerotoone.n17r.zhetisoz.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.TransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.zerotoone.n17r.zhetisoz.Models.CustomCard;
import com.zerotoone.n17r.zhetisoz.Models.UsedWordsContract;
import com.zerotoone.n17r.zhetisoz.Models.WordsDbHelper;
import com.zerotoone.n17r.zhetisoz.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PairMatchingActivity extends AppCompatActivity {

    TextView timerTextView;

    float millis = 0.0f;
    TextView mFine;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            millis = millis + .1f;

            timerTextView.setText(String.format("%5.1f", millis).replace('.',','));

            timerHandler.postDelayed(this,100);
        }
    };

    Button selectedCard1;
    Button selectedCard2;
    Button selectedCard;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_matching);

        mFine = (TextView) findViewById(R.id.tv_fine);
        timerTextView = (TextView) findViewById(R.id.tv_timer);

        timerHandler.postDelayed(timerRunnable, 0);

        Button mButtonClose = (Button) findViewById(R.id.button_close);

        mButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PairMatchingActivity.this,PairMatchingPreviewActivity.class));
            }
        });

        final GridLayout mMatchingContainer = (GridLayout) findViewById(R.id.pair_matching_container);

        List<String[]> strings = getRandom6Pairs();

        ArrayList<CustomCard> cards = new ArrayList<>();

        for (int i = 0; i < strings.size(); i++) {
            cards.add(new CustomCard(i,strings.get(i)[0]));
            cards.add(new CustomCard(i,strings.get(i)[1]));
        }

        Collections.shuffle(cards);

        for (int i = 0; i < cards.size(); i++) {
            final Button p = new Button(this,null,R.style.styleForPairMatching);

            mMatchingContainer.addView(p);

            p.setText(cards.get(i).getWord());
            p.setTag(cards.get(i).getTag());

            setDefaultStyleToButton(p);

            p.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedCard = p;
                    doTurn();
                }
            });
        }

    }

    public List<String[]> getRandom6Pairs() {
        List<String[]> result = new ArrayList<>();
        WordsDbHelper mDbHelper = new WordsDbHelper(this);
        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
        Cursor mCursor = mDb.rawQuery("SELECT * FROM " + UsedWordsContract.WordsEntry.TABLE_NAME + " ORDER BY RANDOM() LIMIT 7",null);
        mCursor.moveToFirst();
        while(mCursor.moveToNext()) {
            result.add(new String[]{mCursor.getString(1),mCursor.getString(2)});
        }
        return result;
    }

    public void doTurn() {
        if(selectedCard1 == null && selectedCard2 == null) {
            selectedCard1 = selectedCard;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                selectedCard.setBackground(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_selected_item));
            } else selectedCard.setBackgroundDrawable(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_selected_item));
        }
        else if(selectedCard1 != null && selectedCard1 != selectedCard && selectedCard2 == null) {
            selectedCard2 = selectedCard;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                selectedCard.setBackground(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_selected_item));
            } else selectedCard.setBackgroundDrawable(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_selected_item));

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkCards();
                }
            }, 200);
        } else if(selectedCard == selectedCard1) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setDefaultStyleToButton(selectedCard);
                    selectedCard2 = null;
                    selectedCard1 = null;
                }
            }, 200);
        }
    }

    public boolean checkIfGameIsWon(Button item) {
        GridLayout mlayout = (GridLayout)item.getParent();

        int count = mlayout.getChildCount();
        for(int i = 0 ; i <count ; i++){
            View child = mlayout.getChildAt(i);
            if(child.isEnabled()) return false;
        }

        return true;

    }

    public void checkCards() {
        if(selectedCard1.getTag().equals(selectedCard2.getTag())) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                selectedCard1.setBackground(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_correct_item));
                selectedCard2.setBackground(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_correct_item));
            } else {
                selectedCard1.setBackgroundDrawable(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_correct_item));
                selectedCard2.setBackgroundDrawable(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_correct_item));
            }

            selectedCard1.setEnabled(false);
            selectedCard2.setEnabled(false);


            if(checkIfGameIsWon(selectedCard2)) {
                timerHandler.removeCallbacksAndMessages(null);
                Intent toResultActivity = new Intent(PairMatchingActivity.this,AfterPairMatchingActivity.class);
                toResultActivity.putExtra("RESULT_EXTRA",millis);
                startActivity(toResultActivity);
            }

            selectedCard1 = null;
            selectedCard2 = null;

        } else {

            GridLayout container = (GridLayout)selectedCard.getParent();

            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_anim);
            container.startAnimation(shake);
            millis = millis + 1.0f;

            mFine.animate().alpha(1.0f).setDuration(600).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mFine.animate().alpha(0.0f).setDuration(600);
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                selectedCard1.setBackground(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_incorrect_item));
                selectedCard2.setBackground(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_incorrect_item));
            } else {
                selectedCard1.setBackgroundDrawable(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_incorrect_item));
                selectedCard2.setBackgroundDrawable(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_incorrect_item));
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setDefaultStyleToButton(selectedCard1);
                    setDefaultStyleToButton(selectedCard2);
                    selectedCard1 = null;
                    selectedCard2 = null;
                }
            }, 200);

        }
    }

    public void setDefaultStyleToButton(Button p) {

        p.setTransformationMethod(null);
        p.setTextSize(18);
        p.setTextColor(Color.parseColor("#6665FF"));
        p.setGravity(Gravity.CENTER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            p.setBackground(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_item));
        } else p.setBackgroundDrawable(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_item));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((GridLayout.LayoutParams) p.getLayoutParams()).columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            ((GridLayout.LayoutParams) p.getLayoutParams()).rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            ((GridLayout.LayoutParams) p.getLayoutParams()).leftMargin = 10;
            ((GridLayout.LayoutParams) p.getLayoutParams()).rightMargin = 10;
            ((GridLayout.LayoutParams) p.getLayoutParams()).topMargin = 10;
        }

    }

}
