package com.costular.guaguaslaspalmas.widget.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.utils.PrefUtils;
import com.costular.guaguaslaspalmas.widget.DrawerListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego on 29/10/2014.
 */
public class DrawerListAdapter extends BaseAdapter {

    private Context context;
    private int mSelectedItem;

    private List<DrawerListItem> items;

    public DrawerListAdapter(Context context, List<DrawerListItem> _items) {
        this.context = context;

        items = new ArrayList<DrawerListItem>();
        items.addAll(_items);
    }

    public int getmSelectedItem() {
        return mSelectedItem;
    }

    public void setmSelectedItem(int mSelectedItem) {
        this.mSelectedItem = mSelectedItem;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.drawer_item_layout, null);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        TextView title = (TextView) convertView.findViewById(R.id.title);

        // Obtenemos el item por la posición
        DrawerListItem item = items.get(position);

        // Y rellenamos los campos
        icon.setImageResource(item.getIcon());
        title.setText(item.getTitle());

        if (position == mSelectedItem) {
            title.setTextColor(context.getResources().getColor(PrefUtils.color));
            title.setTypeface(Typeface.DEFAULT_BOLD);
            icon.setColorFilter(context.getResources().getColor(PrefUtils.color));


        } else {
            title.setTextColor(context.getResources().getColor(android.R.color.black));
            title.setTypeface(Typeface.DEFAULT);

            icon.setColorFilter(null);
        }

        return convertView;

    }
}
