package com.zerotoone.n17r.zhetisoz.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.zerotoone.n17r.zhetisoz.Models.CustomCard;
import com.zerotoone.n17r.zhetisoz.Models.UsedWordsContract;
import com.zerotoone.n17r.zhetisoz.Models.UsedWordsDbHelper;
import com.zerotoone.n17r.zhetisoz.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PairMatchingActivity extends AppCompatActivity {

    TextView timerTextView;
    TableLayout mMatchingContainer;
    boolean canClick = false;

    float millis = 0.0f;
    TextView mFine;

    Handler timerHandler = new Handler();
    Runnable timerRunnable;

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
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        timerRunnable = new Runnable() {

            @Override
            public void run() {
                millis = millis + .1f;

                timerTextView.setText(String.format("%5.1f", millis).replace('.', ','));

                if(millis < 240f) {timerHandler.postDelayed(this, 100);}
                else {
                    timerHandler.removeCallbacks(timerRunnable);
                    AlertDialog dialog = builder.setMessage("Ойынды бітіруге тым көп уақыт жараттыңыз, басынан бастап көріңіз")
                            .setTitle("Әттең!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(PairMatchingActivity.this, PairMatchingPreviewActivity.class));
                                }
                            }).create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#6665FF"));
                        }
                    });
                    dialog.show();
                }
            }
        };

        Button mButtonClose = (Button) findViewById(R.id.button_close);

        mButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerHandler.removeCallbacks(timerRunnable);
                startActivity(new Intent(PairMatchingActivity.this,PairMatchingPreviewActivity.class));
            }
        });

        mMatchingContainer = (TableLayout) findViewById(R.id.pair_matching_container);

        List<String[]> strings = getRandom6Pairs();

        ArrayList<CustomCard> cards = new ArrayList<>();

        for (int i = 0; i < strings.size(); i++) {
            cards.add(new CustomCard(i,strings.get(i)[0]));
            cards.add(new CustomCard(i,strings.get(i)[1]));
        }

        Collections.shuffle(cards);

        int delay = 0;
        for (int i = 0; i < 4; i++) {
            TableRow row = new TableRow(this);
            row.setWeightSum(3);
            TableLayout.LayoutParams param = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            );
            param.weight = 1;
            row.setLayoutParams(param);

            for (int j = 0; j < 3; j++) {
                final Button p = new Button(this, null, R.style.styleForPairMatching);

                setDefaultStyleToButton(p);

                row.addView(p);
                p.setAlpha(0.0f);

                p.animate().alpha(1.0f).setDuration(500).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        p.setAlpha(1.0f);
                    }
                });

                p.setText(cards.get(3 * i + j).getWord());
                p.setTag(cards.get(3 * i + j).getTag());

                p.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedCard = p;
                        doTurn();
                    }
                });
                delay = delay + 100;

            }

            mMatchingContainer.addView(row);
        }
        timerHandler.postDelayed(timerRunnable, 1600);
        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                canClick = true;
            }
        },1600);

    }

    public List<String[]> getRandom6Pairs() {
        List<String[]> result = new ArrayList<>();
        UsedWordsDbHelper mDbHelper = new UsedWordsDbHelper(this);
        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
        Cursor mCursor = mDb.rawQuery("SELECT * FROM " + UsedWordsContract.WordsEntry.TABLE_NAME + " ORDER BY RANDOM() LIMIT 7",null);
        mCursor.moveToFirst();
        while(mCursor.moveToNext()) {
            result.add(new String[]{mCursor.getString(1),mCursor.getString(2)});
        }
        return result;
    }

    public void doTurn() {
        if(selectedCard1 == null && selectedCard2 == null && canClick) {
            selectedCard1 = selectedCard;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                selectedCard.setBackground(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_selected_item));
            } else selectedCard.setBackgroundDrawable(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_selected_item));
        }
        else if(selectedCard1 != null && selectedCard1 != selectedCard && selectedCard2 == null && canClick) {
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
        } else if(selectedCard == selectedCard1 && canClick) {
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

        for(int i = 0 ; i < 4; i++){
            TableRow row = (TableRow) mMatchingContainer.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button b = (Button) row.getChildAt(j);
                if(b.isEnabled()) return false;
            }
        }

        return true;

    }

    public void checkCards() {
        if(selectedCard1.getTag().equals(selectedCard2.getTag()) && canClick) {

            selectedCard1.setEnabled(false);
            selectedCard2.setEnabled(false);

            selectedCard1.animate().alpha(0.0f).setDuration(200).setStartDelay(0).setListener(new AnimatorListenerAdapter() {
            });
            selectedCard2.animate().alpha(0.0f).setDuration(200).setStartDelay(0).setListener(new AnimatorListenerAdapter() {});

            if(checkIfGameIsWon(selectedCard2)) {
                timerHandler.removeCallbacks(timerRunnable);
                Intent toResultActivity = new Intent(PairMatchingActivity.this,AfterPairMatchingActivity.class);
                toResultActivity.putExtra("RESULT_EXTRA",millis);
                startActivity(toResultActivity);
            }

            selectedCard1 = null;
            selectedCard2 = null;

        } else if(canClick) {

            canClick = false;

            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_anim);
            selectedCard1.startAnimation(shake);
            selectedCard2.startAnimation(shake);
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
                    Handler newHandler =  new Handler();
                    newHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            canClick = true;
                        }
                    },300);
                }
            }, 200);

        }
    }

    public void setDefaultStyleToButton(Button p) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.MATCH_PARENT
        );
        params.leftMargin = 5;
        params.rightMargin = 5;
        params.topMargin = 5;
        params.bottomMargin = 5;
        params.weight = 1;

        p.setTransformationMethod(null);
        p.setTextSize(15);
        p.setTextColor(Color.parseColor("#6665FF"));
        p.setGravity(Gravity.CENTER);
        p.setLayoutParams(params);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            p.setElevation(2);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            p.setBackground(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_item));
        } else p.setBackgroundDrawable(ContextCompat.getDrawable(PairMatchingActivity.this, R.drawable.background_item));

    }

    @Override
    protected void onStop() {
        super.onStop();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timerHandler.removeCallbacks(timerRunnable);
        startActivity(new Intent(PairMatchingActivity.this,PairMatchingPreviewActivity.class));
    }
}
