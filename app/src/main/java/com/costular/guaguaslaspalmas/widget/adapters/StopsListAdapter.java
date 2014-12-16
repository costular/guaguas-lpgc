package com.costular.guaguaslaspalmas.widget.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.utils.Provider;
import com.costular.guaguaslaspalmas.widget.views.CircleView;
import com.costular.guaguaslaspalmas.widget.views.ToggleImageButton;

/**
 * Created by Diego on 26/11/2014.
 */
public class StopsListAdapter extends SimpleCursorAdapter{

    private int mLayout;
    private String mColor;

    public StopsListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, String color) {
        super(context, layout, c, from, to, flags);
        mLayout = layout;
        mColor = color;
    }

    public View newView(Context context, Cursor cursor, ViewGroup group) {
        return LayoutInflater.from(context).inflate(mLayout, null);
    }

    public void bindView(View v, Context context, Cursor cursor) {

        String name = cursor.getString(cursor.getColumnIndex(Provider.Stops.NAME_COL));
        String character = "" + name.charAt(0);

        TextView nameView = (TextView) v.findViewById(R.id.name);

        nameView.setText(name);
    }
}
