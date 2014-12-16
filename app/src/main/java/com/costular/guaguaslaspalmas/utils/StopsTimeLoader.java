package com.costular.guaguaslaspalmas.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.costular.guaguaslaspalmas.model.StopTime;

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

        //Inicializamos
        mData = new ArrayList<StopTime>();

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://paradas.guaguas.com/" + mStop).openConnection();
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                String html = readStringFromInputStream(connection.getInputStream());

                Document doc = Jsoup.parse(html);
                Elements tds = doc.select("td");

                Element[] stops = new Element[tds.size()];
                tds.toArray(stops);

                int x = 3;  // chunk size
                int len = stops.length;

                for (int i = 0; i < len - x + 1; i += x) {
                    Element[] temp = Arrays.copyOfRange(stops, i, i + x);

                    String number = temp[0].text();
                    String name = temp[1].text();
                    String minutes = temp[2].text();

                    if(minutes.contains(">>")) {
                        minutes = minutes.replace(">>", "En parada");
                    }

                    String color = Utils.getColorFromNumber(getContext(), number);

                    mData.add(new StopTime(name, number, minutes, color));
                }


            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mData;
    }

    private String readStringFromInputStream(InputStream stream) throws IOException{

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder result = new StringBuilder();

        String line;
        while((line = reader.readLine()) != null) {
            result.append(line).append("\n");
        }
        reader.close();

        return result.toString();
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