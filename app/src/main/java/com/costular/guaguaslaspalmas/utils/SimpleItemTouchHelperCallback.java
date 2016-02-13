package com.costular.guaguaslaspalmas.utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by diego on 13/02/16.
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback{

    private ItemTouchHelperAdapter adapter;
    private static final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    private static final int swipeFlags = ItemTouchHelper.END;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        super(dragFlags, swipeFlags);
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
}
