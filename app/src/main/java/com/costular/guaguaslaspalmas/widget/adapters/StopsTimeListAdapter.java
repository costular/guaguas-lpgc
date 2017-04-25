package com.costular.guaguaslaspalmas.widget.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.model.StopTime;
import com.costular.guaguaslaspalmas.widget.views.CircleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego on 27/11/2014.
 */
public class StopsTimeListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<StopTime> mData;

    public StopsTimeListAdapter(final Context context) {
        mData = new ArrayList<StopTime>();
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<StopTime> items) {
        mData = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.stop_detail_item, null);
        }

        StopTime item = mData.get(position);

        CircleView circle = (CircleView) convertView.findViewById(R.id.number);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView minutesLeft = (TextView) convertView.findViewById(R.id.minutes);

        circle.setText(item.getNumber());
        circle.setFillColor(Color.parseColor(item.getColor()));

        name.setText(item.getName());
        minutesLeft.setText(item.getMinutes());

        return convertView;
    }
}
