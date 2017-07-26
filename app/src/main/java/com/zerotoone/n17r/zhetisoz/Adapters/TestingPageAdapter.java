package com.zerotoone.n17r.zhetisoz.Adapters;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.tmall.ultraviewpager.UltraViewPager;
import com.zerotoone.n17r.zhetisoz.Activities.TestingActivity;
import com.zerotoone.n17r.zhetisoz.Models.Question;
import com.zerotoone.n17r.zhetisoz.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestingPageAdapter extends PagerAdapter {

    private Context context;
    private List<Question> questionList;
    private LayoutInflater layoutInflater;
    TextToSpeech textToSpeech;

    public TestingPageAdapter(Context context, List<Question> questionList){
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.questionList = questionList;
    }
    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = this.layoutInflater.inflate(R.layout.fragment_testing_item, container, false);

        TextView mQuestionText = (TextView) view.findViewById(R.id.tv_question);
        GridLayout mAnswers = (GridLayout) view.findViewById(R.id.answers);

        mQuestionText.setText(questionList.get(position).getQuestion());

        List<String> allAnswers = new ArrayList<>();
        allAnswers.addAll(questionList.get(position).getOtherVariances());
        allAnswers.add(questionList.get(position).getCorrectAnswer());

        Collections.shuffle(allAnswers);

        for (int i = 0; i < allAnswers.size(); i++) {
            String eachAnswer = allAnswers.get(i);
            Button eachButton = (Button) mAnswers.getChildAt(i);
            eachButton.setText(eachAnswer);
            setDefaultStyleToButton(eachButton);
            eachButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        v.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.background_item_selected));
                    } else {
                        v.setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.background_item_selected));
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setDefaultStyleToButton(Button p) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((GridLayout.LayoutParams) p.getLayoutParams()).columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            ((GridLayout.LayoutParams) p.getLayoutParams()).rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            ((GridLayout.LayoutParams) p.getLayoutParams()).leftMargin = 10;
            ((GridLayout.LayoutParams) p.getLayoutParams()).rightMargin = 10;
            ((GridLayout.LayoutParams) p.getLayoutParams()).bottomMargin = 10;
        }

    }

}