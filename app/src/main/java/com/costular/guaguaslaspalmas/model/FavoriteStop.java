package com.costular.guaguaslaspalmas.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.costular.guaguaslaspalmas.utils.DatabaseHelper;
import com.costular.guaguaslaspalmas.utils.Provider;

/**
 * Created by Diego on 30/11/2014.
 */
public class FavoriteStop{

    public static FavoriteStop createFromCursor(Context context, Cursor c) {

        if(c != null) {
            return new FavoriteStop(c.getInt(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("stop_name")), c.getInt(c.getColumnIndex(Provider.FavoritesStops.STOP_ID)),
                    c.getInt(c.getColumnIndex("color")), c.getInt(c.getColumnIndex("orden")), context);
        }

        return null;
    }

    public static int stopsCount(Context context) {

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT _id FROM " + Provider.TABLE_FAVORITES_STOPS, null);

        int count = c.getCount();
        // Close
        c.close();

        return count;
    }


    private Stop stop;

    private int id;
    private String letter;
    private String custom;
    private String defaul;
    private int order;
    private int color;
    private int stopId;

    public FavoriteStop(int id, String custom, int stopId, int color, int order, Context context) {
        this.id = id;
        this.custom = custom;
        this.stopId = stopId;
        this.color = color;
        this.order = order;

        stop = Stop.createStopFromId(context, stopId);

        this.defaul = stop.getName();

        if(custom != null) {

            if(!custom.isEmpty()) {
                setFirstChar(custom);

            } else {
                setFirstChar(defaul);
            }
        } else {
            setFirstChar(defaul);
        }



    }

    public void updateOrder(Context context, int position) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("orden", position);
        db.update(Provider.TABLE_FAVORITES_STOPS, values, "_id = '"+getId()+"'", null);
    }

    public void deleteFromDatabase(Context context) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(Provider.TABLE_FAVORITES_STOPS, "_id = " + String.valueOf(getId()), null);
    }

    private void setFirstChar(String from) {

        char first = from.charAt(0);

        if(Character.isDigit(first) || Character.isLetter(first)) {
            letter = String.valueOf(first);
        } else {
            letter = "?";
        }
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getDefaul() {
        return defaul;
    }

    public void setDefaul(String defaul) {
        this.defaul = defaul;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    public int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
