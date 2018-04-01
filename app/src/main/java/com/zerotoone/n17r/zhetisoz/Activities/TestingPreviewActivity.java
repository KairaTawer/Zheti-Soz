package com.zerotoone.n17r.zhetisoz.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wefika.horizontalpicker.HorizontalPicker;
import com.zerotoone.n17r.zhetisoz.Models.UsedWordsDbHelper;
import com.zerotoone.n17r.zhetisoz.R;

public class TestingPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_preview);

        final HorizontalPicker horizontalPicker = (HorizontalPicker) findViewById(R.id.picker);

        final CharSequence cs[];

        UsedWordsDbHelper mHelper = new UsedWordsDbHelper(this);
        SQLiteDatabase mDb = mHelper.getReadableDatabase();

        if(mDb.rawQuery("select* from words",null).getCount() == 7) {
            cs = new CharSequence[]{"5 сөз"};
        } else if(mDb.rawQuery("select* from words",null).getCount() == 14) {
            cs = new CharSequence[]{"5 сөз","10 сөз"};
        } else {
            cs = new CharSequence[]{"5 сөз","10 сөз","15 сөз","20 сөз"};
        }

        horizontalPicker.setValues(cs);

        findViewById(R.id.button_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestingPreviewActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(TestingPreviewActivity.this, TestingActivity.class)
                            .putExtra("WORD_COUNT", horizontalPicker.getSelectedItem())
                            .putExtra("FULL_COUNT",cs.length)
                );
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
