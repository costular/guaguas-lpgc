package com.costular.guaguaslaspalmas.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.TypedValue;
import com.costular.guaguaslaspalmas.R;


/**
 * Created by Diego on 24/11/2014.
 */
public class Utils {

    private static int notificationId = 0;

    public static boolean haveInternet(final Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static int notify(Context context, String title, String message, Bitmap fullImage) {

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText(message);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title)
                .setContentText(message)
                .setStyle(style)
                .setSmallIcon(R.drawable.ic_stat_maps_directions_bus);

        if(fullImage != null) {
            builder.setLargeIcon(fullImage);
        }

        NotificationManagerCompat.from(context).notify(notificationId++, builder.build());
        return notificationId;
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

        Cursor cursor = database.query(Provider.TABLE_ROUTES, Provider.Routes.COLUMNS, Provider.Routes.NUMBER_COL + " = ?", new String[]{String.valueOf(id)}, null, null, null);

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
