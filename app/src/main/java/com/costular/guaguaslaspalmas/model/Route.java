package com.costular.guaguaslaspalmas.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.costular.guaguaslaspalmas.utils.DatabaseHelper;
import com.costular.guaguaslaspalmas.utils.Provider;

/**
 * Created by Diego on 24/11/2014.
 */
public class Route {

    private int id;
    private String name;
    private String number;
    private String color;

    public static Route createFromCursor(Cursor cursor) {

        if(!cursor.moveToFirst()) {
            return null;
        }

        Route route = new Route(cursor.getInt(cursor.getColumnIndex(Provider.Routes.ID_COL)), cursor.getString(cursor.getColumnIndex(Provider.Routes.NAME_COL)),
                cursor.getString(cursor.getColumnIndex(Provider.Routes.NUMBER_COL)), cursor.getString(cursor.getColumnIndex(Provider.Routes.COLOR_COL)));

        return route;
    }

    public static int getConcesionFromRouteNumber(final Context context, int id) {

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM VARIANTES WHERE concesion = '"+id+"'", null);

        int concesion = 0;

        if(cursor.moveToFirst()) {
            concesion = cursor.getInt(0);
        }

        cursor.close();

        return concesion;
    }

    public static boolean isRouteFavorite(final Context context, final int id) {

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM routes_favorites WHERE route_id = ?", new String[]{String.valueOf(id)});

        boolean ret = false;

        if(cursor.getCount() >= 1) {
            ret = true;
        }
        cursor.close();

        return ret;
    }

    public static boolean addToFavorite(final Context context, final Route route) {
        return addToFavorite(context, route.getId());
    }

    public static boolean addToFavorite(final Context context, int id) {

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("route_id", id);

        try{
            db.insert("routes_favorites", null, values);
            return true;
        }catch(Exception e) {
            return false;
        }

    }

    public static boolean deleteFromFavorite(final Context context, int id) {

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        try{
            db.delete("routes_favorites", "route_id = ?", new String[]{String.valueOf(id)});
            return true;
        }catch(Exception e) {
            return false;
        }

    }

    public Route(int id, String name, String number, String color) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isFavorite(final Context context) {
        return isRouteFavorite(context, getId());
    }

    public boolean deleteFavorite(final Context context) {

        if(!isFavorite(context)) {
            return false;
        }

        return deleteFromFavorite(context, getId());
    }

    public boolean addToFavorites(final Context context) {
        return addToFavorite(context, getId());
    }

    public int getConcesion(final Context  context) {
        return getConcesionFromRouteNumber(context, getId());
    }
}
