package com.costular.guaguaslaspalmas.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.costular.guaguaslaspalmas.model.FavoriteStop;
import com.costular.guaguaslaspalmas.model.StopTime;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

import cz.msebera.android.httpclient.Header;

/**
 * Created by Diego on 27/11/2014.
 */
public class StopsTimeLoader extends AsyncTaskLoader<List<StopTime>> {

    private List<StopTime> mData;
    private int mStop;

    public StopsTimeLoader(Context context, final int stop) {
        super(context);
        mStop = stop;
    }

    @Override
    public List<StopTime> loadInBackground() {
        final List<StopTime> stopTimes = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://51.254.124.106/stop/"+mStop, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                // Pull out the first event on the public timeline
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        stopTimes.add(new StopTime(jsonobject.getString("destiny"),
                                jsonobject.getString("route"), jsonobject.getString("minutes"),
                                "#3F51B5"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return stopTimes;
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