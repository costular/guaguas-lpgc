package com.costular.guaguaslaspalmas.fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.NinePatchDrawable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.StopDetailActivity;
import com.costular.guaguaslaspalmas.model.FavoriteStop;
import com.costular.guaguaslaspalmas.model.Stop;
import com.costular.guaguaslaspalmas.utils.Provider;
import com.costular.guaguaslaspalmas.widget.AddToFavoriteDialog;
import com.costular.guaguaslaspalmas.widget.CheckStopCodeDialog;
import com.costular.guaguaslaspalmas.widget.adapters.DragSortRecycler;
import com.costular.guaguaslaspalmas.widget.adapters.FavoriteStopsRecyclerAdapter;
import com.costular.guaguaslaspalmas.widget.adapters.FavoriteStopsTaskLoader;
import com.costular.guaguaslaspalmas.widget.views.AddStop;

import com.melnykov.fab.FloatingActionButton;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by Diego on 30/11/2014.
 */
public class StopsFavoritesFragment extends Fragment implements LoaderCallbacks<List<FavoriteStop>>{

    //private ListView mListView;
    //private FavoriteStopsListAdapter mAdapter;

    private RecyclerView mRecycler;
    private FavoriteStopsRecyclerAdapter mRecyclerAdapter;


    private ActionBar bar;

    private boolean started = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_stops_favorites, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        bar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        mRecycler = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        fab.attachToRecyclerView(mRecycler);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AddToFavoriteDialog dialog = new AddToFavoriteDialog();
                dialog.setListener(new AddStop() {
                    @Override
                    public void onStopAdded(FavoriteStop stop) {
                        addStop(stop);
                    }
                });

                dialog.show(getActivity().getSupportFragmentManager(), "");
            }
        });

        mRecyclerAdapter = new FavoriteStopsRecyclerAdapter(getActivity());
        mRecyclerAdapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mRecycler.getChildPosition(v);

                FavoriteStop stop = mRecyclerAdapter.stops.get(position);

                int idd = stop.getStopId();

                Intent intent = new Intent(getActivity(), StopDetailActivity.class);
                intent.putExtra(StopDetailActivity.ID, idd);
                intent.putExtra(StopDetailActivity.STOP, Stop.createStopFromId(getActivity(), idd).getCode());

                startActivity(intent);
            }
        });

        HorizontalDividerItemDecoration separator = new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(R.color.light_gray)
                .margin(72, 0).build();

        mRecycler.setAdapter(mRecyclerAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setItemAnimator(null);
        mRecycler.addItemDecoration(separator);

        DragSortRecycler dragSortRecycler = new DragSortRecycler();
        dragSortRecycler.setViewHandleId(R.id.drag);
        dragSortRecycler.setFloatingAlpha(0.4f);
        dragSortRecycler.setFloatingBgColor(Color.LTGRAY);
        dragSortRecycler.setAutoScrollSpeed(0.25f);
        dragSortRecycler.setAutoScrollWindow(0.1f);

        dragSortRecycler.setOnItemMovedListener(new DragSortRecycler.OnItemMovedListener() {
            @Override
            public void onItemMoved(int from, int to) {

                if(from == to) {
                    return;
                }

                FavoriteStop item = mRecyclerAdapter.stops.remove(from);
                mRecyclerAdapter.stops.add(to, item);
                mRecyclerAdapter.notifyDataSetChanged();

                item.changeOrder(getActivity(), from, to);
            }
        });

        dragSortRecycler.setOnDragStateChangedListener(new DragSortRecycler.OnDragStateChangedListener() {
            @Override
            public void onDragStart() {

            }

            @Override
            public void onDragStop() {

            }
        });

        mRecycler.addItemDecoration(dragSortRecycler);
        mRecycler.addOnItemTouchListener(dragSortRecycler);
        mRecycler.setOnScrollListener(dragSortRecycler.getScrollListener());

        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        started = true;
    }

    /*
    private void onItemViewClick(View v) {
        int position = mRecycler.getChildPosition(v);

        FavoriteStop stop = mRecyclerAdapter.stops.get(position);

        int idd = stop.getStopId();

        Intent intent = new Intent(getActivity(), StopDetailActivity.class);
        intent.putExtra(StopDetailActivity.ID, idd);
        intent.putExtra(StopDetailActivity.STOP, Stop.createStopFromId(getActivity(), idd).getCode());

        startActivity(intent);
    }
    */

    @Override
    public void onResume() {
        super.onResume();

        if(started) {
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Limpiamos el ActionBar
        menu.clear();

        inflater.inflate(R.menu.stops_favorites_default, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.check_stop_by_code) {

            new CheckStopCodeDialog().show(getActivity().getSupportFragmentManager(), "");
            return true;
        }

        return false;
    }

    @Override
    public Loader<List<FavoriteStop>> onCreateLoader(int i, Bundle bundle) {
        return new FavoriteStopsTaskLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<FavoriteStop>> cursorLoader, List<FavoriteStop> data) {
        //mRecyclerAdapter.swapCursor(cursor);
        mRecyclerAdapter.swapData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<FavoriteStop>> data) {
        //mRecyclerAdapter.swapCursor(null);
        //mRecyclerAdapter.swapData(null);
    }

    private void addStop(FavoriteStop stop) {
        mRecyclerAdapter.addStop(stop);
    }

}
