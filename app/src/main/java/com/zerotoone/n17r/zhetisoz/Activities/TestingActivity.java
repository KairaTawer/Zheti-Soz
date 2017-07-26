package com.zerotoone.n17r.zhetisoz.Activities;

import android.graphics.Color;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zerotoone.n17r.zhetisoz.Fragments.TestingItemFragment;
import com.zerotoone.n17r.zhetisoz.Models.AnsweredQuestion;
import com.zerotoone.n17r.zhetisoz.Models.CircleProgressBar;
import com.zerotoone.n17r.zhetisoz.Models.Question;
import com.zerotoone.n17r.zhetisoz.Models.UsedWordsContract;
import com.zerotoone.n17r.zhetisoz.Models.WordsDbHelper;
import com.zerotoone.n17r.zhetisoz.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestingActivity extends AppCompatActivity implements TestingItemFragment.onSomeEventListener {

    int NUM_PAGES = 5;
    List<Question> questions;
    CharSequence[] questionResult = null;

    ArrayList<AnsweredQuestion> resultList = new ArrayList<>();
    CircleProgressBar circleProgressBar;
    TextView mProgress;
    Button mNextButton;

    int questionNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        mNextButton = (Button) findViewById(R.id.button_next);
        Button mCloseButton = (Button) findViewById(R.id.button_close);
        circleProgressBar = (CircleProgressBar) findViewById(R.id.custom_progressBar);
        mProgress = (TextView) findViewById(R.id.tv_progress);

        Intent intent = getIntent();
        int word_count_id = intent.getIntExtra("WORD_COUNT", 123456);
        int all_words_count = intent.getIntExtra("FULL_COUNT", 123456);

        switch (all_words_count) {
            case 1:
                NUM_PAGES = 5;
            case 2:
                if(word_count_id == 0) NUM_PAGES = 5;
                else NUM_PAGES = 10;
            case 4:
                if(word_count_id == 0) NUM_PAGES = 5;
                else if(word_count_id == 1) NUM_PAGES = 10;
                else if(word_count_id == 2) NUM_PAGES = 15;
                else NUM_PAGES = 20;
        }

        questions = getRandomQuestions(NUM_PAGES);
        circleProgressBar.setMax(NUM_PAGES);
        circleProgressBar.setColor(Color.parseColor("#6DEE87"));

        loadNextQuestion(questionNumber);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButtonClicked();
            }
        });

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestingActivity.this,TestingPreviewActivity.class));
            }
        });

    }

    public void nextButtonClicked() {
        if(questionResult != null) {
            resultList.add(new AnsweredQuestion(
                    String.valueOf(questionResult[0]),
                    String.valueOf(questionResult[1]),
                    String.valueOf(questionResult[2])));
            questionResult = null;
            if(questionNumber < questions.size()) loadNextQuestion(questionNumber);
            else {
                circleProgressBar.setProgressWithAnimation(NUM_PAGES);
                mProgress.setText(String.valueOf(NUM_PAGES));
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(TestingActivity.this, TestingResultActivity.class);
                        intent.putParcelableArrayListExtra("RESULT_LIST", resultList);
                        startActivity(intent);
                    }
                }, 300);
            }
        }
    }

    public void loadNextQuestion(int id) {

        circleProgressBar.setProgressWithAnimation(id);
        mProgress.setText(String.valueOf(id));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putInt("QUESTION_NUMBER", id);
        bundle.putString("QUESTION", questions.get(id).getQuestion());
        bundle.putString("CORRECT_ANSWER", questions.get(id).getCorrectAnswer());
        bundle.putStringArrayList("OTHER_ANSWER", (ArrayList) questions.get(id).getOtherVariances());
        TestingItemFragment fragment = new TestingItemFragment();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.layout, fragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

        questionNumber++;

        mNextButton.setBackgroundColor(Color.parseColor("#506665FF"));

    }

    public List<Question> getRandomQuestions(int count) {
        List<Question> questions = new ArrayList<>();

        WordsDbHelper mDbHelper = new WordsDbHelper(this);
        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
        ArrayList<Integer> alreadyUsed = new ArrayList<>();
        Random rnd = new Random();

        SharedPreferences prefs = this.getSharedPreferences("ZHETISOZ_APP", Context.MODE_PRIVATE);
        int progress = prefs.getInt("PROGRESS", 7);

        for (int i = 0; i < count; i++) {
            Question question = new Question();
            int randomId = rnd.nextInt(progress);
            while (alreadyUsed.contains(randomId) || randomId == 0) {
                randomId = rnd.nextInt(progress);
            }
            alreadyUsed.add(randomId);
            Cursor mCursor = mDb.rawQuery(
                    "SELECT * FROM " + UsedWordsContract.WordsEntry.TABLE_NAME + " WHERE " + UsedWordsContract.WordsEntry._ID + " = " + randomId,
                    null);
            mCursor.moveToFirst();
            String questionText = mCursor.getString(1);
            question.setQuestion(questionText);
            String correctAnswer = mCursor.getString(2);
            question.setCorrectAnswer(correctAnswer);
            List<String> incorrectAnswers = new ArrayList<>();

            mCursor = mDb.rawQuery(
                    "SELECT * FROM " + UsedWordsContract.WordsEntry.TABLE_NAME + " WHERE " + UsedWordsContract.WordsEntry.COLUMN_NAME_KAZAKH + "<>'" + correctAnswer + "' ORDER BY RANDOM() LIMIT 3",
                    null);
            while(mCursor.moveToNext()) {
                incorrectAnswers.add(mCursor.getString(2));
            }
            question.setOtherVariances(incorrectAnswers);

            questions.add(question);

            mCursor.close();
        }

        return questions;
    }

    @Override
    public void someEvent(String question,String correctAnswer,String selectedAnswer) {
        mNextButton.setBackgroundColor(Color.parseColor("#6665FF"));
        questionResult = new CharSequence[]{question,correctAnswer,selectedAnswer};
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,TestingPreviewActivity.class));
    }
}
