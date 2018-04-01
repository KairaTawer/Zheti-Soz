package com.zerotoone.n17r.zhetisoz.Models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.zerotoone.n17r.zhetisoz.Services.RefreshWordsTask;

import java.util.Calendar;

/**
 * Created by асус on 03.08.2017.
 */
public class ResetAlarm extends BroadcastReceiver {

    private AlarmManager am;
    private PendingIntent pi;
    public static Calendar calendar;

    @Override
    public void onReceive(Context context, Intent intent) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        pi = PendingIntent.getService(context, 101,
                new Intent(context, RefreshWordsTask.class), PendingIntent.FLAG_UPDATE_CURRENT);

        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
            Log.d("TAG_Added", "alarm set to tomorrow");
        }

        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);

        SharedPreferences mSharedPreferences = context.getSharedPreferences("ZHETISOZ_APP", Context.MODE_PRIVATE);
        mSharedPreferences.edit().putLong("alarm_date",calendar.getTimeInMillis()).apply();
    }
}
