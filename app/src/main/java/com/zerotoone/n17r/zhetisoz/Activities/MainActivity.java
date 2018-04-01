package com.zerotoone.n17r.zhetisoz.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.zerotoone.n17r.zhetisoz.Fragments.FirstFragment;
import com.zerotoone.n17r.zhetisoz.Fragments.SecondFragment;
import com.zerotoone.n17r.zhetisoz.Fragments.ThirdFragment;
import com.zerotoone.n17r.zhetisoz.Models.UsedWordsContract;
import com.zerotoone.n17r.zhetisoz.Models.UsedWordsDbHelper;
import com.zerotoone.n17r.zhetisoz.Models.WordsDbHelper;
import com.zerotoone.n17r.zhetisoz.R;
import com.zerotoone.n17r.zhetisoz.Services.RefreshWordsTask;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class MainActivity extends AppCompatActivity implements
        FirstFragment.OnFragmentInteractionListener,
        SecondFragment.OnFragmentInteractionListener,
        ThirdFragment.OnFragmentInteractionListener {

    FragmentTransaction fragmentTransaction;
    private AlarmManager am;
    private PendingIntent pi;
    public static Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("TAG_ONCREATE", "Activity Created");

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new FirstFragment(), "MY_FRAGMENT");
        fragmentTransaction.commit();

        SharedPreferences mSharedPreferences = this.getSharedPreferences("ZHETISOZ_APP", Context.MODE_PRIVATE);
        int progress = mSharedPreferences.getInt("PROGRESS",0);

        final BottomNavigationViewEx bottomNavigationView = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);

        bottomNavigationView.enableAnimation(false);
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.enableItemShiftingMode(false);
        bottomNavigationView.setTextVisibility(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_favorite:
                                bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.flashcards);
                                bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.settings);
                                fragment = new FirstFragment();
                                item.setIcon(R.drawable.heart_active);
                                break;
                            case R.id.action_duplicate:
                                bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.heart);
                                bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.settings);
                                fragment = new SecondFragment();
                                item.setIcon(R.drawable.flashcards_active);
                                break;
                            case R.id.action_settings:
                                bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.flashcards);
                                bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.heart);
                                fragment = new ThirdFragment();
                                item.setIcon(R.drawable.settings_active);
                                break;
                        }
                        if (fragment != null) {
                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frameLayout, fragment, "MY_FRAGMENT");
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                });


        if(progress == 0) {
            getRandom7Words(0);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt("PROGRESS",progress + 7);
            editor.apply();

            WordsDbHelper mUnusedDBHelper = new WordsDbHelper(this);
            try {
                mUnusedDBHelper.CopyDataBaseFromAsset();
                Log.d("TAG_Database","Database just created");
            } catch (IOException e) {
                e.printStackTrace();
            }

            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
            pi = PendingIntent.getService(this, 101,
                    new Intent(this, RefreshWordsTask.class),PendingIntent.FLAG_UPDATE_CURRENT);

            am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            if(calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
                Log.d("TAG_Added","alarm set to tomorrow");
            }

            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);

            mSharedPreferences.edit().putLong("alarm_date",calendar.getTimeInMillis()).apply();

        }

    }

    public void getRandom7Words(int progress) {

        try {

            UsedWordsDbHelper mDbHelper = new UsedWordsDbHelper(this);
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("database.csv")));
            String next[];
            List<String[]> allWords = reader.readAll();

            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            for (int i = 7; i < 14; i++)  {
                next = allWords.get(i);

                ContentValues values = new ContentValues();

                values.put(UsedWordsContract.WordsEntry.COLUMN_NAME_ENGLISH, next[0]);
                values.put(UsedWordsContract.WordsEntry.COLUMN_NAME_KAZAKH, next[1]);

                db.insert(UsedWordsContract.WordsEntry.TABLE_NAME, null, values);

            }

            db.close();

            Log.d("TAG", "Service Triggered firstly");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
