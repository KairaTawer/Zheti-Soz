package com.zerotoone.n17r.zhetisoz.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.zerotoone.n17r.zhetisoz.Activities.MainActivity;
import com.zerotoone.n17r.zhetisoz.Models.UsedWordsContract;
import com.zerotoone.n17r.zhetisoz.Models.WordsDbHelper;
import com.zerotoone.n17r.zhetisoz.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import android.os.Handler;

import au.com.bytecode.opencsv.CSVReader;

public class RefreshWordsTask extends Service {

    WordsDbHelper mDbHelper = new WordsDbHelper(this);
    Handler handler = new Handler();

//    @Override
//    protected void (Intent intent) {
//
//
//
//    }

    @Override
    public void onCreate() {
        super.onCreate();


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("ZHETISOZ_APP", Context.MODE_PRIVATE);

                int progress = prefs.getInt("PROGRESS", 0);

                getRandom7Words(progress);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("PROGRESS", progress + 7);
                editor.apply();

                Bitmap icon = BitmapFactory.decodeResource(getResources(),R.mipmap.zhetisoz_icon);

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(RefreshWordsTask.this)
                                .setSmallIcon(R.mipmap.zhetisoz_icon)
                                .setLargeIcon(icon)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setContentTitle("Zhetisöz")
                                .setContentText("Жаңа сөздерді үйренетін уақыт келді.")
                                .setAutoCancel(true);

                Intent notificationIntent = new Intent(RefreshWordsTask.this, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(RefreshWordsTask.this, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(contentIntent);

                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0,builder.build());

                handler.postDelayed(this,1000*60*60*24L);
            }
        };

        handler.postDelayed(runnable,1000*60*60*24L);
    }

    public void getRandom7Words(int progress) {

        try {

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

            Log.d("TAG", "Service Triggered sdakjh sajdn");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}