package com.zerotoone.n17r.zhetisoz.Models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zerotoone.n17r.zhetisoz.Services.RefreshWordsTask;

public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, RefreshWordsTask.class);
        context.startService(i);
    }
}