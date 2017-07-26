package com.zerotoone.n17r.zhetisoz.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.zerotoone.n17r.zhetisoz.Fragments.FirstFragment;
import com.zerotoone.n17r.zhetisoz.Fragments.SecondFragment;
import com.zerotoone.n17r.zhetisoz.Fragments.ThirdFragment;
import com.zerotoone.n17r.zhetisoz.Models.MyAlarmReceiver;
import com.zerotoone.n17r.zhetisoz.Models.UsedWordsContract;
import com.zerotoone.n17r.zhetisoz.Models.WordsDbHelper;
import com.zerotoone.n17r.zhetisoz.R;
import com.zerotoone.n17r.zhetisoz.Services.RefreshWordsTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Ref;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class MainActivity extends AppCompatActivity implements
        FirstFragment.OnFragmentInteractionListener,
        SecondFragment.OnFragmentInteractionListener,
        ThirdFragment.OnFragmentInteractionListener {

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("TAG_ONCREATE", "Activity Creted");

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
        }

        startService(new Intent(this, RefreshWordsTask.class));

//        scheduleAlarm();
//
//        String tag = "periodic";
//
//        GcmNetworkManager mScheduler = GcmNetworkManager.getInstance(getApplicationContext());

//        long periodSecs = 30L;
//
//        PeriodicTask periodic = new PeriodicTask.Builder()
//                .setService(RefreshWordsTask.class)
//                .setPeriod(periodSecs)
//                .setTag(tag)
//                .setPersisted(true)
//                .setUpdateCurrent(true).setRequiredNetwork(com.google.android.gms.gcm.Task.NETWORK_STATE_CONNECTED)
//                .build();
//        mScheduler.schedule(periodic);

    }

//    public void scheduleAlarm() {
//        Calendar cur_cal = new GregorianCalendar();
//        cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar
//
//        Calendar cal = new GregorianCalendar();
//        cal.set(Calendar.HOUR_OF_DAY, 2);
//        cal.set(Calendar.MINUTE, 15);
//        Intent intent = new Intent(MainActivity.this, RefreshWordsTask.class);
//        PendingIntent pintent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
//        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);
//    }

    public void getRandom7Words(int progress) {

        try {

            WordsDbHelper mDbHelper = new WordsDbHelper(this);
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("database.csv")));
            String next[];
            List<String[]> allWords = reader.readAll();

            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            for (int i = progress; i < progress + 7; i++)  {
                next = allWords.get(i);

                ContentValues values = new ContentValues();

                values.put(UsedWordsContract.WordsEntry.COLUMN_NAME_ENGLISH, next[0]);
                values.put(UsedWordsContract.WordsEntry.COLUMN_NAME_KAZAKH, next[1]);

                db.insert(UsedWordsContract.WordsEntry.TABLE_NAME, null, values);

            }

            db.close();

            Log.d("TAG", "Service Triggered firstly");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
