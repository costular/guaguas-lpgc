package com.costular.guaguaslaspalmas.fragments;

import android.content.Intent;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.costular.guaguaslaspalmas.utils.DatabaseHelper;
import com.costular.guaguaslaspalmas.utils.Provider;
import com.costular.guaguaslaspalmas.utils.Utils;
import com.costular.guaguaslaspalmas.widget.AddToFavoriteDialog;
import com.costular.guaguaslaspalmas.widget.adapters.FavoriteStopsListAdapter;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by Diego on 30/11/2014.
 */
public class StopsFavoritesFragment extends Fragment implements LoaderCallbacks<Cursor>, AbsListView.MultiChoiceModeListener {

    private ListView mListView;
    private FavoriteStopsListAdapter mAdapter;

    private boolean started = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stops_favorites, null);
    }

    @Override
    public void onStart() {
        super.onStart();

        mListView = (ListView) getActivity().findViewById(R.id.listview);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.attachToListView(mListView);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AddToFavoriteDialog().show(getActivity().getSupportFragmentManager(), "");
            }
        });

        mAdapter = new FavoriteStopsListAdapter(getActivity(), R.layout.list_stop_favorites, null,
                new String[] {Provider.FavoritesStops.STOP_ID, Provider.FavoritesStops.STOP_NAME},
                new int[] {R.id.name_custom, R.id.name_default}, 0);


        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) mAdapter.getItem(position);

                int idd = cursor.getInt(cursor.getColumnIndex(Provider.FavoritesStops.STOP_ID));

                Intent intent = new Intent(getActivity(), StopDetailActivity.class);
                intent.putExtra(StopDetailActivity.ID, idd);
                intent.putExtra(StopDetailActivity.STOP, Stop.createStopFromId(getActivity(), idd).getCode());

                startActivity(intent);

            }
        });

        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(this);

        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        started = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(started) {
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), Provider.CONTENT_URI_FAVORITE_STOPS, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }


    /*
     * Métodos para las acciones cuando se deja pulsado un elemento de la lista para editar o borrar...
     */
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

        int size = mListView.getCheckedItemCount();
        mode.setTitle(String.valueOf(size));

        if(size == 1) {
            mode.getMenu().clear();
            mode.getMenuInflater().inflate(R.menu.stops_favorites_action, mode.getMenu());
        } else if(size >= 2) {
            mode.getMenu().clear();
            mode.getMenuInflater().inflate(R.menu.stops_favorites_action_varios, mode.getMenu());
        }

        mode.invalidate();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.stops_favorites_action, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        int id = item.getItemId();

        SparseBooleanArray checkedItemPositions = mListView.getCheckedItemPositions();
        mode.setTitle(String.valueOf(checkedItemPositions.size()));

        if(id == R.id.action_delete) {

            for(int i = mAdapter.getCount() - 1; i >= 0; i--) {
                if(mListView.isItemChecked(i)) {
                    Cursor cursor = (Cursor) mAdapter.getItem(i);
                    deleteStop(cursor);
                }
            }
            // Cerramos la ToolBar
            mode.finish();
            // Recargamos las paradas favoritas.
            getLoaderManager().restartLoader(0, null, this);
            return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    private void deleteStop(final Cursor cursor) {
        Stop.removeFromFavorites(getActivity(), cursor.getInt(cursor.getColumnIndex(Provider.FavoritesStops.ID_COL)));
    }
}
