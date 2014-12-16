package com.costular.guaguaslaspalmas.widget.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.model.Stop;
import com.costular.guaguaslaspalmas.utils.Provider;
import com.costular.guaguaslaspalmas.utils.Utils;
import com.costular.guaguaslaspalmas.widget.views.CircleView;

import java.util.Random;

/**
 * Created by Diego on 30/11/2014.
 */
public class FavoriteStopsListAdapter extends SimpleCursorAdapter{

    private int mLayout;
    private static int[] colors;

    public FavoriteStopsListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mLayout = layout;

        colors = new int[] {context.getResources().getColor(R.color.blue_grey_500), context.getResources().getColor(R.color.light_blue_500), context.getResources().getColor(R.color.light_green_500),
               context.getResources().getColor(R.color.red_500),
                context.getResources().getColor(R.color.teal_500)};
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup group) {
        return LayoutInflater.from(context).inflate(mLayout, null);
    }

    @Override
    public void bindView(View v, Context context, Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndex(Provider.FavoritesStops.STOP_ID));
        String customName = cursor.getString(cursor.getColumnIndex(Provider.FavoritesStops.STOP_NAME));
        String key = String.valueOf(customName.charAt(0)).toUpperCase();

        String defaultName = Stop.createStopFromId(context, id).getName();

        CircleView view = (CircleView) v.findViewById(R.id.stop_key);
        TextView customNameView = (TextView) v.findViewById(R.id.name_custom);
        TextView defaultNameView = (TextView) v.findViewById(R.id.name_default);

        int i = 0;
        if(cursor.getPosition() < colors.length) {
            i = cursor.getPosition();
        }else if(cursor.getPosition() > colors.length) {
            i = Utils.randomInt(0, colors.length);
        }

        view.setText(key);
        view.setFillColor(colors[i]);

        customNameView.setText(customName);
        defaultNameView.setText(defaultName);
    }
}
