package com.costular.guaguaslaspalmas.widget.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.model.FavoriteStop;
import com.costular.guaguaslaspalmas.utils.ItemTouchHelperAdapter;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by diego on 24/02/15.
 */
public class FavoriteStopsRecyclerAdapter extends RecyclerView.Adapter<FavoriteStopsViewHolder> implements ItemTouchHelperAdapter{


    private Activity activity;
    public FavoriteStopsViewHolder holder;
    public List<FavoriteStop> stops;
    private View.OnClickListener listener;

    public FavoriteStopsRecyclerAdapter(Activity activity) {
        this.activity = activity;
        stops = new ArrayList<FavoriteStop>();
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return stops.get(position).getId();
    }

    @Override
    public FavoriteStopsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_stop_favorites, parent, false);
        return new FavoriteStopsViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return stops.size();
    }

    @Override
    public void onBindViewHolder(FavoriteStopsViewHolder viewHolder, int position) {

        if(stops == null) {
            return;
        }

        this.holder = viewHolder;

        if(holder.getListener() == null && listener != null) {
            holder.setListener(listener);
        }

        // Esto son las paradas y demas
        FavoriteStop stop = stops.get(position);

        int id = stop.getId();
        String customName = stop.getCustom();
        String key = stop.getLetter().toUpperCase();
        String defaultName = stop.getDefaul();

        holder.view.setText(key);
        holder.view.setFillColor(stop.getColor());

        holder.customNameView.setText(customName);
        holder.defaultNameView.setText(defaultName);
    }

    public void swapData(List<FavoriteStop> data) {
        this.stops = data;
        notifyDataSetChanged();
    }

    public void addStop(FavoriteStop stop) {
        addStop(stop, stops.size() - 1);
    }

    public void addStop(FavoriteStop stop, int position) {
        stops.add(stop);
        notifyItemInserted(position);
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(stops, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(stops, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void saveStopsPosition() {
        for(int i = 0; i < stops.size(); i++) {
            FavoriteStop stop = stops.get(i);
            stop.updateOrder(activity, i + 1);
        }
    }

    @Override
    public void onItemDismiss(int position) {
        notifyItemRemoved(position);
        FavoriteStop item = stops.remove(position);
        item.deleteFromDatabase(activity);

        Snackbar.make(activity.findViewById(android.R.id.content), activity.getString(R.string.stop_deleted),
                Snackbar.LENGTH_LONG).show();
    }
}
