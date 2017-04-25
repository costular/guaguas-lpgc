package com.costular.guaguaslaspalmas.widget.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.utils.Provider;
import com.costular.guaguaslaspalmas.widget.views.CircleView;

import org.w3c.dom.Text;

/**
 * Created by Diego on 20/11/2014.
 */
public class RoutesListAdapter extends SimpleCursorAdapter {

    private final int mLayout;

    public RoutesListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mLayout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup group) {
        return LayoutInflater.from(context).inflate(mLayout, null);
    }

    @Override
    public void bindView(View v, Context context, Cursor cursor) {
        String number = String.valueOf(cursor.getString(cursor.getColumnIndex(Provider.Routes.NUMBER_COL)));
        String name = cursor.getString(cursor.getColumnIndex(Provider.Routes.NAME_COL));

        CircleView numberView = (CircleView) v.findViewById(R.id.number);
        TextView nameView = (TextView) v.findViewById(R.id.name);

        numberView.setText(number);
        numberView.setFillColor(Color.parseColor(cursor.getString(cursor.getColumnIndex(Provider.Routes.COLOR_COL))));

        nameView.setText(name);
    }
}
