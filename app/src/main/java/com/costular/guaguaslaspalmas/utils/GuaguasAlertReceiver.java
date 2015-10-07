package com.costular.guaguaslaspalmas.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.costular.guaguaslaspalmas.services.GuaguaAlertChecker;

public class GuaguasAlertReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.costular.guaguaslaspalmas.GUAGUAS_RECEIVER";

    public GuaguasAlertReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        GuaguaAlertChecker.startAction(context);
    }
}
