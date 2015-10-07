package com.costular.guaguaslaspalmas.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;
import java.text.RuleBasedCollator;

/**
 * Created by Diego on 19/11/2014.
 */
public class Provider extends ContentProvider{

    public static final String AUTHORITY = "com.costular.guaguaslaspalmas.utils";

    public static final String URI_ROUTES = "content://"+AUTHORITY+"/routes";
    public static final String URI_STOPS = "content://"+AUTHORITY+"/stops";
    public static final String URI_STOPS_FAVORITES = "content://"+AUTHORITY+"/stops_favorites";

    public static final Uri CONTENT_URI_ROUTES = Uri.parse(URI_ROUTES);
    public static final Uri CONTENT_URI_STOPS = Uri.parse(URI_STOPS);
    public static final Uri CONTENT_URI_FAVORITE_STOPS = Uri.parse(URI_STOPS_FAVORITES);

    private DatabaseHelper database;

    public static final String TABLE_ROUTES = "routes";
    public static final String TABLE_STOPS = "stops";
    public static final String TABLE_FAVORITES_STOPS = "stops_favorites";

    public static UriMatcher uriMatcher;

    // MATCHERS
    public static final int URI_MATCHER_ROUTES = 1;
    public static final int URI_MATCHER_ROUTES_ID = 2;

    public static final int URI_MATCHER_STOPS = 10;
    public static final int URI_MATCHER_STOPS_ID = 11;

    public static final int URI_MATCHER_FAVORITES_STOPS = 20;
    public static final int URI_MATCHER_FAVORITES_STOPS_ID = 21;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, TABLE_ROUTES, URI_MATCHER_ROUTES);
        uriMatcher.addURI(AUTHORITY, TABLE_ROUTES + "/#", URI_MATCHER_ROUTES_ID);

        uriMatcher.addURI(AUTHORITY, TABLE_STOPS, URI_MATCHER_STOPS);
        uriMatcher.addURI(AUTHORITY, TABLE_STOPS + "/#", URI_MATCHER_STOPS_ID);

        uriMatcher.addURI(AUTHORITY, TABLE_FAVORITES_STOPS, URI_MATCHER_FAVORITES_STOPS);
        uriMatcher.addURI(AUTHORITY, TABLE_FAVORITES_STOPS + "/#", URI_MATCHER_FAVORITES_STOPS_ID);
    }

    @Override
    public boolean onCreate() {
        database = DatabaseHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = null;

        String where = selection;
        if(uriMatcher.match(uri) == URI_MATCHER_ROUTES) {
            cursor = db.query(TABLE_ROUTES, projection, where, selectionArgs, null, null, sortOrder);
        }
        else if(uriMatcher.match(uri) == URI_MATCHER_ROUTES_ID) {
            where = Routes.ID_COL + " = " + uri.getLastPathSegment();
            cursor = db.query(TABLE_ROUTES, projection, where, selectionArgs, null, null, sortOrder);
        }
        else if(uriMatcher.match(uri) == URI_MATCHER_STOPS) {
            cursor = db.query(TABLE_STOPS, projection, where, selectionArgs, null, null, sortOrder);
        }
        else if(uriMatcher.match(uri) == URI_MATCHER_STOPS_ID) {
            where = Stops.ID_COL + " = " + uri.getLastPathSegment();
            cursor = db.query(TABLE_STOPS, projection, where, selectionArgs, null, null, sortOrder);
        }
        else if(uriMatcher.match(uri) == URI_MATCHER_FAVORITES_STOPS) {
            cursor = db.query(TABLE_FAVORITES_STOPS, projection, where, selectionArgs, null, null, sortOrder);
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        switch(uriMatcher.match(uri)) {

            case URI_MATCHER_ROUTES:
                return "vnd.android.cursor.dir/com.costular.routes";

            case URI_MATCHER_ROUTES_ID:
                return "vnd.android.cursor.item/com.costular.routes";

            case URI_MATCHER_STOPS:
                return "vnd.android.cursor.dir/com.costular.stops";

            case URI_MATCHER_STOPS_ID:
                return "vnd.android.cursor.item/com.costular.stops";

            case URI_MATCHER_FAVORITES_STOPS:
                return "vnd.android.cursor.dir/com.costular.stops_favorites";
        }

        return "";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public static final class Routes implements BaseColumns {
        public static final String ID_COL = "_id";
        public static final String NAME_COL = "name";
        public static final String NUMBER_COL = "number";
        public static final String COLOR_COL = "color";

        public static final String[] COLUMNS = new String[] {ID_COL, NAME_COL, NUMBER_COL, COLOR_COL};
    }

    public static final class Stops implements BaseColumns {
        public static final String ID_COL = "_id";
        public static final String LATITUDE_COL = "coord_lat";
        public static final String LONGITUDE_COL = "coord_long";
        public static final String ORDER_COL = "order";
        public static final String ROUTE_COL = "route";
        public static final String NAME_COL = "name";
        public static final String CODE_COL = "code";
    }

    public static final class FavoritesStops implements BaseColumns {
        public static final String ID_COL = "_id";
        public static final String STOP_ID = "stop_route";
        public static final String STOP_NAME = "stop_name";
    }
}
