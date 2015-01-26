package com.costular.guaguaslaspalmas.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;

/**
 * Created by Diego on 24/11/2014.
 */
public class Utils {

    public static boolean haveInternet(final Context context) {

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public static String getColorFromNumber(final Context context, final String number) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT color FROM routes WHERE number = ?", new String[]{number});

        String color = "";
        if(cursor.moveToFirst()) {
            color = cursor.getString(0);
        }
        cursor.close();

        return color;
    }

    public static Cursor getCursorFromRouteId(final Context context, final int id) {

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.query(Provider.TABLE_ROUTES, Provider.Routes.COLUMNS, "_id = ?", new String[]{String.valueOf(id)}, null, null, null);

        return cursor;
    }

    public static boolean getBooleanFromInt(final int n) {
        return n == 1;
    }

    public static String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    public static int randomInt(int Min, int Max)
    {
        return (int) (Math.random()*(Max-Min))+Min;
    }

    public static float getPixelsByDP(int dp, Resources resources) {
         return (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                 dp, resources.getDisplayMetrics());
    }
}
