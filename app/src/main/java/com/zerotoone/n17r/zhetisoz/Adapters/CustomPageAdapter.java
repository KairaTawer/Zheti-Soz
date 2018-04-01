package com.zerotoone.n17r.zhetisoz.Adapters;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zerotoone.n17r.zhetisoz.Models.DataObject;
import com.zerotoone.n17r.zhetisoz.Models.RandomCircle;
import com.zerotoone.n17r.zhetisoz.R;

import java.util.List;
import java.util.Locale;

public class CustomPageAdapter extends PagerAdapter {

    private Context context;
    private List<DataObject> dataObjectList;
    private LayoutInflater layoutInflater;
    TextToSpeech textToSpeech;

    public CustomPageAdapter(Context context, List<DataObject> dataObjectList){
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.dataObjectList = dataObjectList;
    }
    @Override
    public int getCount() {
        return dataObjectList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = this.layoutInflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        final TextView englishWord = (TextView) view.findViewById(R.id.tv_word_english);
        TextView transcript = (TextView) view.findViewById(R.id.tv_word_pronunciation);
        TextView kazakhWord = (TextView) view.findViewById(R.id.tv_word_kazakh);
        FrameLayout circlesContainer = (FrameLayout) view.findViewById(R.id.circles_container);

        englishWord.setText(capitalize(this.dataObjectList.get(position).getInEnglish()));
        transcript.setText(this.dataObjectList.get(position).getInEnglish());
        kazakhWord.setText(capitalize(this.dataObjectList.get(position).getInKazakh()));

        textToSpeech = new TextToSpeech(view.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        view.findViewById(R.id.button_pronunciation).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textToSpeech.speak(englishWord.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                    }
                });

        for (int i = 0; i < 7; i++) {
            RandomCircle circle = new RandomCircle(view.getContext());
            circlesContainer.addView(circle);
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

}