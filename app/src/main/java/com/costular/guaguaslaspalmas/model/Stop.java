package com.costular.guaguaslaspalmas.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.costular.guaguaslaspalmas.utils.DatabaseHelper;
import com.costular.guaguaslaspalmas.utils.Provider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego on 24/11/2014.
 */
public class Stop {

    private int id;
    private double longitude;
    private double latitude;
    private int order;
    private String name;
    private int code;
    private int route;

    public static Stop createStopFromId(final Context context, final int id) {

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        return createStopFromCursor(db.rawQuery("SELECT * FROM "+ Provider.TABLE_STOPS +" WHERE _id = ?", new String[]{String.valueOf(id)}), true);
    }

    private static Stop createStopFromCursor(Cursor cursor, boolean closeCursor) {

        if(closeCursor) {
            cursor.moveToFirst();
        }

        Stop stop = new Stop(cursor.getInt(cursor.getColumnIndex(Provider.Stops.ID_COL)), cursor.getDouble(cursor.getColumnIndex(Provider.Stops.LONGITUDE_COL)), cursor.getDouble(cursor.getColumnIndex(Provider.Stops.LATITUDE_COL)),
                cursor.getInt(cursor.getColumnIndex(Provider.Stops.ORDER_COL)), cursor.getString(cursor.getColumnIndex(Provider.Stops.NAME_COL)),
                cursor.getInt(cursor.getColumnIndex(Provider.Stops.CODE_COL)), cursor.getInt(cursor.getColumnIndex(Provider.Stops.ROUTE_COL)));

        // Cerramos el cursor
        if(closeCursor) {
            cursor.close();
        }

        return stop;
    }

    /*
     * Obtenemos todas las paradas de la base de datos.
     */
    public static List<Stop> getAllStops(Context context) {

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        List<Stop> stops = new ArrayList<Stop>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Provider.TABLE_STOPS, null);

        if(cursor.moveToFirst()) {

            do{
                stops.add(createStopFromCursor(cursor, false));
                // Movemos el cursor.
                cursor.moveToNext();

            }while(cursor.moveToNext());
        }

        return stops;
    }

    /*
     * Obtenemos las paradas por concesion
     */
    public static List<Stop> getStopsByConcesion(Context context, int concesion) {

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Provider.TABLE_STOPS + " WHERE " + Provider.Stops.ROUTE_COL + " = '"+concesion+"'", null);

        List<Stop> stops = new ArrayList<Stop>();

        if(cursor.moveToFirst()) {

            do{
                stops.add(createStopFromCursor(cursor, false));
                cursor.moveToNext();

            }while(cursor.moveToNext());
        }
        cursor.close();

        return stops;
    }

    public static int getStopIdFromCode(final Context context, int code) {

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT _id FROM " + Provider.TABLE_STOPS + " WHERE " + Provider.Stops.CODE_COL + " = '"+code+"'", null);

        // Movemos el cursor al principio para leerlo.
        cursor.moveToFirst();

        int id;
        // Guardamos en una variable la id
        if(cursor != null && cursor.getCount() >= 1) {
            id = cursor.getInt(0);
        } else {
            id = -1;
        }

        // Cerramos el cursor
        cursor.close();

        return id;
    }

    public static Stop getStopFromCode(final Context context, int code) {

        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + Provider.TABLE_STOPS + " WHERE " + Provider.Stops.CODE_COL + " = ?",
                new String[] {String.valueOf(code)});

        // Guardamos el objeto que obtenemos a través del método en la variable.
        Stop stop = createStopFromCursor(c, true);

        //Cerramos
        c.close();

        return stop;
    }

    public static boolean isFavorite(final Context context, Stop stop) {
        return isFavorite(context, stop.getId());
    }

    public static boolean isFavorite(final Context context, final int id) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM stops_favorites WHERE stop_route = ?", new String[]{String.valueOf(id)});

        boolean ret = false;
        if(cursor.getCount() >= 1) {
            ret = true;
        }
        cursor.close();

        return ret;
    }

    public static boolean removeFromFavorites(final Context context, Route route) {
        return removeFromFavorites(context, route.getId());
    }

    public static boolean removeFromFavorites(final Context context, final int id) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        try{
            db.delete("stops_favorites", "stop_route = ?", new String[]{String.valueOf(id)});
            return true;
        }catch(Exception e) {
            return false;
        }

    }

    public static boolean addtoFavorites(final Context context, Stop stop, String name) {
        return addToFavorites(context, name, stop.getId());
    }

    public static boolean addToFavorites(final Context context, String name, int id) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        try{
            ContentValues values = new ContentValues();
            values.put("stop_route", id);
            values.put("stop_name", name);

            db.insert("stops_favorites", null, values);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public static void editStopName(final Context context, String name, final int id) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("stop_route", id);
        values.put("stop_name", name);

        db.update("stops_favorites", values, "stop_route = ?", new String[] {String.valueOf(id)});
    }

    public Stop(int id, double longitude, double latitude, int order, String name, int code, int route) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;

        this.order = order;
        this.name = name;
        this.code = code;
        this.route = route;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public boolean isFavorite(final Context context) {
       return isFavorite(context, getId());
    }

    public boolean addToFavorites(final Context context, String name) {
        return addToFavorites(context, name, getId());
    }

    public boolean removeFromFavorites(final Context context) {
        return removeFromFavorites(context, getId());
    }

    public String getFavoriteNameStop(final Context context) {
        DatabaseHelper helper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT stop_name FROM stops_favorites WHERE stop_route = ?", new String[]{String.valueOf(getId())});

        if(!cursor.moveToFirst()) {
            return getName();
        }
        String name = cursor.getString(0);

        cursor.close();

        return name;
    }

    public void updateNameStop(final Context context, String name) {
        editStopName(context, name, getId());
    }
}
