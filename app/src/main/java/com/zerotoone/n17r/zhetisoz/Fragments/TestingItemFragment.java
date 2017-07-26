package com.zerotoone.n17r.zhetisoz.Fragments;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.zerotoone.n17r.zhetisoz.Activities.TestingActivity;
import com.zerotoone.n17r.zhetisoz.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by асус on 20.07.2017.
 */
public class TestingItemFragment extends Fragment {

    public TestingItemFragment() {}

    public interface onSomeEventListener {
        public void someEvent(String question,String correctAnswer,String selectedAnswer);
    }

    onSomeEventListener someEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_testing_item, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView mQuestionText = (TextView) view.findViewById(R.id.tv_question);
        final GridLayout mAnswers = (GridLayout) view.findViewById(R.id.answers);

        Bundle bundle = this.getArguments();

        final String questionText = bundle.getString("QUESTION", "Default question");
        final String correctAnswer = bundle.getString("CORRECT_ANSWER", "correct answer");
        ArrayList<String> allAnswers = bundle.getStringArrayList("OTHER_ANSWER");
        allAnswers.add(correctAnswer);

        Collections.shuffle(allAnswers);

        mQuestionText.setText(questionText + " сөзінің аудармасын табыңыз");

        for (int i = 0; i < 4; i++) {
            final Button button = (Button) mAnswers.getChildAt(i);
            button.setText(allAnswers.get(i));
            setDefaultStyleToButton(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    someEventListener.someEvent(questionText,correctAnswer,button.getText().toString());
                    for (int i = 0; i < mAnswers.getChildCount(); i++) {
                        mAnswers.getChildAt(i).setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_item));
                    }
                    button.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_item_selected));
                }
            });
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    public void setDefaultStyleToButton(Button p) {

        p.setTransformationMethod(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((GridLayout.LayoutParams) p.getLayoutParams()).columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            ((GridLayout.LayoutParams) p.getLayoutParams()).rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            ((GridLayout.LayoutParams) p.getLayoutParams()).leftMargin = 10;
            ((GridLayout.LayoutParams) p.getLayoutParams()).rightMargin = 10;
            ((GridLayout.LayoutParams) p.getLayoutParams()).bottomMargin = 10;
        }

    }

}

