package com.costular.guaguaslaspalmas.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.costular.guaguaslaspalmas.R;

/**
 * Created by Diego on 17/11/2014.
 */
public class PrefUtils {

    /*
     *  [Boolean] Contiene si es la primera vez que abre la app para dejarle abierto el navigation Drawer y darle un par de instrucciones.
     */
    public static final String FIRST_TIME = "first_time";

    //-----------------------------------------------------------------------------------------------------------------------------------------

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setNotFirstTime(final Context context) {
        getSharedPreferences(context).edit().putBoolean(FIRST_TIME, false).commit();
    }

    public static boolean isFirstTime(final Context context) {
        return getSharedPreferences(context).getBoolean(FIRST_TIME, true);
    }

    public static boolean canNotify(final Context context) {
        return getSharedPreferences(context).getBoolean("notifications", true);
    }

    public static boolean canPlaySound(final Context context) {
        return getSharedPreferences(context).getBoolean("notification_sound", true);
    }

    public static boolean canVibrate(final Context context) {
        return getSharedPreferences(context).getBoolean("notification_vibrate", true);
    }

    public static int getAdTimes(final Context context) {
        return getSharedPreferences(context).getInt("ad_times", 0);
    }

    public static String getTheme(final Context context) {
        return getSharedPreferences(context).getString("theme", "indigo");
    }

    public static void setTheme(final Context context, String theme) {
        getSharedPreferences(context).edit().putString("theme", theme).commit();
    }

    public static int getScheduleTextsize(final Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("schedules_textsize", "16"));
    }
}