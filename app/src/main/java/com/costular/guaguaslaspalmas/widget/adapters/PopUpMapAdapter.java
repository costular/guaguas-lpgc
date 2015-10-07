package com.costular.guaguaslaspalmas.widget.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.costular.guaguaslaspalmas.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by diego on 23/02/15.
 */
public class PopUpMapAdapter implements GoogleMap.InfoWindowAdapter {

    private LayoutInflater inflater;
    private View popup = null;

    public PopUpMapAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        if(popup == null) {
            popup = inflater.inflate(R.layout.popup_map_stop, null);
        }

        TextView title = (TextView) popup.findViewById(R.id.title);

        title.setText(marker.getTitle());

        return popup;
    }
}
