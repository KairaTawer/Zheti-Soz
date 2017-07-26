package com.zerotoone.n17r.zhetisoz.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zerotoone.n17r.zhetisoz.Models.AnsweredQuestion;
import com.zerotoone.n17r.zhetisoz.R;

import java.util.ArrayList;
import java.util.List;

public class ResultListAdapter extends PagerAdapter {

    private Context context;
    private List<AnsweredQuestion> resultList = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public ResultListAdapter(Context context, List<AnsweredQuestion> resultList){
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.resultList = resultList;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = this.layoutInflater.inflate(R.layout.fragment_result_list_item, container, false);

        AnsweredQuestion currentQuestion = this.resultList.get(position);

        LinearLayout mContainer = (LinearLayout) view.findViewById(R.id.container);
        LinearLayout background = (LinearLayout) view.findViewById(R.id.layout_detector);
        ImageView detector = (ImageView) view.findViewById(R.id.detector);

        TextView mQuestion = (TextView) view.findViewById(R.id.tv_question);
        TextView mCorrectAnswer = (TextView) view.findViewById(R.id.correct_answer);
        TextView mIncorrectAnswer = (TextView) view.findViewById(R.id.incorrect_answer);

        mQuestion.setText(currentQuestion.getQuestion() + " сөзінің аудармасын табыңыз");
        mCorrectAnswer.setText(currentQuestion.getCorrectAnswer());
        if(currentQuestion.getCorrectAnswer().equals(currentQuestion.getSelectedAnswer())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                background.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.background_ok));
            } else background.setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.background_ok));
            detector.setImageDrawable(ContextCompat.getDrawable(view.getContext(),R.drawable.ok_icon));
            mIncorrectAnswer.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                background.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.background_x));
            } else background.setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.background_x));
            detector.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.x_icon));
            mIncorrectAnswer.setText(currentQuestion.getSelectedAnswer());
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}