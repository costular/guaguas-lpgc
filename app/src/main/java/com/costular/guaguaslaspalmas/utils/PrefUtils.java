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
    public static boolean themeChanged = false;
    public static int theme = R.style.AppTheme_Indigo;
    public static int color = R.color.main_indigo;

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

    public static void loadTheme(final Activity activity) {

        setTheme(getSharedPreferences(activity).getString("theme", "indigo"));

        activity.setTheme(theme);
    }

    public static void setTheme(String str) {
        int[] array = getTheme(str);

        theme = array[0];
        color = array[1];
    }

    public static int[] getTheme(String str) {
        String theme = str;

        if("indigo".equalsIgnoreCase(theme)) {
            return new int[] {R.style.AppTheme_Indigo, R.color.main_indigo};
        }
        else if("pink".equalsIgnoreCase(theme)) {
            return new int[] {R.style.AppTheme_Pink, R.color.main_pink};
        }
        else if("purple".equalsIgnoreCase(theme)) {
            return new int[] {R.style.AppTheme_Purple, R.color.main_purple};
        }
        else if("red".equalsIgnoreCase(theme)) {
            return new int[] {R.style.AppTheme_Red, R.color.main_red};
        }
        else if("blue".equalsIgnoreCase(theme)) {
            return new int[] {R.style.AppTheme_Blue, R.color.main_blue};
        }
        else if("green".equalsIgnoreCase(theme)) {
            return new int[] {R.style.AppTheme_Green, R.color.main_green};
        }
        else if("orange".equalsIgnoreCase(theme)) {
            return new int[]{ R.style.AppTheme_Orange, R.color.main_orange};
        }
        else if("yellow".equalsIgnoreCase(theme)) {
            return new int[] {R.style.AppTheme_Yellow, R.color.main_yellow};
        }
        else if("gray".equalsIgnoreCase(theme)){
            return new int[] {R.style.AppTheme_Gray, R.color.main_gray};
        }

        return new int[] {R.style.AppTheme, R.color.main_indigo};
    }
}