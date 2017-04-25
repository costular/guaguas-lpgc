package com.costular.guaguaslaspalmas.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

public class ScheduleUpdater extends IntentService {

    public static final String TAG = "ScheduleAdapter";
    private static final String[] ROUTES = {"1", "2", "6", "7", "8", "9", "10", "11",
    "12", "13", "17", "19", "20", "21", "22", "24", "25", "26", "32", "33", "35", "41", "44",
    "45", "46", "47", "48", "50", "51", "52", "53", "54", "55", "70", "80", "81", "82", "84",
    "91", "L1", "L2", "L3"};

    public ScheduleUpdater() {
        super("ScheduleUpdater");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: Comenzado método");
        for (String route : ROUTES) {
            Log.d(TAG, "onHandleIntent: Route " + route);
        }
        Log.d(TAG, "onHandleIntent: Finished");
    }


}
