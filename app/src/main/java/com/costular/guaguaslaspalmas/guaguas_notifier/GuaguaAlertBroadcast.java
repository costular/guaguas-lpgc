package com.costular.guaguaslaspalmas.guaguas_notifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by costular on 10/09/16.
 */
public class GuaguaAlertBroadcast extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("GuaguasAlertBroadcast", "onReceive called.");
        Intent stopChecker = new Intent(context, GuaguaAlertCheckerService.class);
        startWakefulService(context, stopChecker);
    }

}
