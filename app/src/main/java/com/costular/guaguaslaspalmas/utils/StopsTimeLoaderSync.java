package com.costular.guaguaslaspalmas.utils;

import android.content.Context;

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
 * Created by diego on 27/05/15.
 */
public class StopsTimeLoaderSync {

    private List<StopTime> mData;
    private Context mContext;
    private int mStop;

    public StopsTimeLoaderSync(Context context, final int stop) {
        this.mContext = context;
        mStop = stop;
    }

    public List<StopTime> get() {

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

                    String color = Utils.getColorFromNumber(mContext, number);

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
}
