package com.zerotoone.n17r.zhetisoz.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.zerotoone.n17r.zhetisoz.Activities.MainActivity;
import com.zerotoone.n17r.zhetisoz.Models.UsedWordsContract;
import com.zerotoone.n17r.zhetisoz.Models.UsedWordsDbHelper;
import com.zerotoone.n17r.zhetisoz.Models.WordsDbHelper;
import com.zerotoone.n17r.zhetisoz.R;

import java.util.Calendar;
import java.util.Random;

public class RefreshWordsTask extends Service {

    private WordsDbHelper mUnusedDBHelper = new WordsDbHelper(this);
    SQLiteDatabase mUnusedDb;

    private UsedWordsDbHelper mUsedDBHelper = new UsedWordsDbHelper(this);
    SQLiteDatabase mUsedDb;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences prefs = this.getSharedPreferences("ZHETISOZ_APP",MODE_PRIVATE);
        long date = prefs.getLong("alarm_date",100);
        Log.d("TAG_info", date + " " + Calendar.getInstance().get(Calendar.DATE));
        if(Calendar.getInstance().getTimeInMillis() > date){
            prefs.edit().putLong("alarm_date",date + 1000*60*60*24L).apply();
            getRandom7Words();
            showNotification();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void getRandom7Words() {

        mUsedDb = mUsedDBHelper.getWritableDatabase();
        mUnusedDb = mUnusedDBHelper.getWritableDatabase();

        Random rand = new Random();
        for (int i = 0; i < 7; i++)  {
            int r = rand.nextInt(mUnusedDb.rawQuery("select * from words", null).getCount()) + 1;
            Cursor cursor = mUnusedDb.rawQuery("select * from words where id = " + r,null);
            ContentValues values = new ContentValues();

            if(cursor != null && cursor.moveToFirst()){
                values.put(UsedWordsContract.WordsEntry.COLUMN_NAME_ENGLISH, cursor.getString(1));
                values.put(UsedWordsContract.WordsEntry.COLUMN_NAME_KAZAKH, cursor.getString(2));
                mUsedDb.insert(UsedWordsContract.WordsEntry.TABLE_NAME, null, values);
                mUnusedDb.delete("words", "id = " + r, null);

                cursor.close();
            }

        }

        mUnusedDb.close();
        mUsedDb.close();

        Log.d("TAG", "Service Triggered");

    }

    public void showNotification() {
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
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}