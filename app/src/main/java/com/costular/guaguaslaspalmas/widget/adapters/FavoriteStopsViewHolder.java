package com.costular.guaguaslaspalmas.widget.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.widget.views.CircleView;

/**
 * Created by diego on 24/02/15.
 */
public class FavoriteStopsViewHolder extends RecyclerView.ViewHolder{

    ViewGroup container;
    CircleView view;
    TextView customNameView;
    TextView defaultNameView;
    ImageView dragHandler;

    private View.OnClickListener listener;

    public ViewGroup getContainerSwipeable() {
        return container;
    }

    public FavoriteStopsViewHolder(View itemView) {
        super(itemView);
        container = (ViewGroup) itemView.findViewById(R.id.container);
        view = (CircleView) itemView.findViewById(R.id.stop_key);
        customNameView = (TextView) itemView.findViewById(R.id.name_custom);
        defaultNameView = (TextView) itemView.findViewById(R.id.name_default);
    }


    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
        container.setOnClickListener(listener);
    }
}
