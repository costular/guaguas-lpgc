package com.costular.guaguaslaspalmas.widget.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;

import com.costular.guaguaslaspalmas.model.FavoriteStop;
import com.costular.guaguaslaspalmas.model.StopTime;
import com.costular.guaguaslaspalmas.utils.DatabaseHelper;
import com.costular.guaguaslaspalmas.utils.Provider;
import com.costular.guaguaslaspalmas.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by diego on 1/03/15.
 */
public class FavoriteStopsTaskLoader extends AsyncTaskLoader<List<FavoriteStop>>{

        private List<FavoriteStop> mData;


        public FavoriteStopsTaskLoader(Context context) {
            super(context);
        }

        @Override
        public List<FavoriteStop> loadInBackground() {

            //Inicializamos
            mData = new ArrayList<FavoriteStop>();

            // Cargamos las paradas
            DatabaseHelper helper = DatabaseHelper.getInstance(getContext());
            SQLiteDatabase db = helper.getReadableDatabase();

            Cursor c = db.query(Provider.TABLE_FAVORITES_STOPS, null, null, null, null, null, "orden ASC");

            if(c.moveToFirst()) {

                do {
                    mData.add(FavoriteStop.createFromCursor(getContext(), c));

                }while(c.moveToNext());
            }
            // Lo cerramos.
            c.close();

            return mData;
        }

        @Override
        protected void onStopLoading() {
            // The Loader is in a stopped state, so we should attempt to cancel the
            // current load (if there is one).
            cancelLoad();

            // Note that we leave the observer as is. Loaders in a stopped state
            // should still monitor the data source for changes so that the Loader
            // will know to force a new load if it is ever started again.
        }

        @Override
        protected void onReset() {
            // Ensure the loader has been stopped.
            onStopLoading();

            // At this point we can release the resources associated with 'mData'.
            if (mData != null) {
                mData = null;
            }
        }

        @Override
        protected void onStartLoading() {

            if (mData != null) {
                // Deliver any previously loaded data immediately.
                deliverResult(mData);
            }

            if (takeContentChanged() || mData == null) {
                // When the observer detects a change, it should call onContentChanged()
                // on the Loader, which will cause the next call to takeContentChanged()
                // to return true. If this is ever the case (or if the current data is
                // null), we force a new load.
                forceLoad();
            }
        }

}
