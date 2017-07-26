package com.zerotoone.n17r.zhetisoz.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer;
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer;
import com.zerotoone.n17r.zhetisoz.Adapters.CustomPageAdapter;
import com.zerotoone.n17r.zhetisoz.Models.CustomPageTransformer;
import com.zerotoone.n17r.zhetisoz.Models.DataObject;
import com.zerotoone.n17r.zhetisoz.Models.UsedWordsContract;
import com.zerotoone.n17r.zhetisoz.Models.WordsDbHelper;
import com.zerotoone.n17r.zhetisoz.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SecondFragment extends Fragment {

    private UltraViewPager ultraViewPager;

    private PagerAdapter mPagerAdapter;

    private OnFragmentInteractionListener listener;

    public static SecondFragment newInstance() {
        return new SecondFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<DataObject> list = new ArrayList<>();

        WordsDbHelper mDbHelper = new WordsDbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                UsedWordsContract.WordsEntry._ID,
                UsedWordsContract.WordsEntry.COLUMN_NAME_ENGLISH,
                UsedWordsContract.WordsEntry.COLUMN_NAME_KAZAKH
        };

        Cursor cursor = db.query(
                UsedWordsContract.WordsEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                UsedWordsContract.WordsEntry._ID +" DESC",
                "7");

        while(cursor.moveToNext()) {
            list.add(new DataObject(cursor.getString(cursor.getColumnIndex(UsedWordsContract.WordsEntry.COLUMN_NAME_ENGLISH)),
                    cursor.getString(cursor.getColumnIndex(UsedWordsContract.WordsEntry.COLUMN_NAME_KAZAKH))));
        }

        cursor.close();

        ultraViewPager = (UltraViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = new CustomPageAdapter(view.getContext(),list);
        ultraViewPager.setAdapter(mPagerAdapter);

        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);

        ultraViewPager.setMultiScreen(0.75f);
        ultraViewPager.setItemRatio(.75f);
        ultraViewPager.setAutoMeasureHeight(true);

        ultraViewPager.setPageTransformer(false, new CustomPageTransformer());

        Button mButtonNext = (Button) view.findViewById(R.id.button_next);

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ultraViewPager.setCurrentItem(ultraViewPager.getCurrentItem() + 1,true);
            }
        });

        TextView mTodayTextView = (TextView) view.findViewById(R.id.tv_date_today);

        Calendar c = Calendar.getInstance();

        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        ArrayList<String> monthsInKazakh = new ArrayList<>();
        monthsInKazakh.add("Қаңтар");
        monthsInKazakh.add("Ақпан");
        monthsInKazakh.add("Наурыз");
        monthsInKazakh.add("Сәуір");
        monthsInKazakh.add("Мамыр");
        monthsInKazakh.add("Маусым");
        monthsInKazakh.add("Шілде");
        monthsInKazakh.add("Тамыз");
        monthsInKazakh.add("Қыркүйек");
        monthsInKazakh.add("Қазан");
        monthsInKazakh.add("Қараша");
        monthsInKazakh.add("Желтоқсан");

        mTodayTextView.setText(day + " " + monthsInKazakh.get(month) + ", " + year);

    }

    public interface OnFragmentInteractionListener {

    }
}
