package com.costular.guaguaslaspalmas.fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.RouteDetailActivity;
import com.costular.guaguaslaspalmas.utils.DatabaseHelper;
import com.costular.guaguaslaspalmas.utils.Provider;
import com.costular.guaguaslaspalmas.utils.Utils;
import com.costular.guaguaslaspalmas.widget.adapters.RoutesListAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

/**
 * Created by Diego on 22/11/2014.
 */
public class RoutesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ObservableScrollViewCallbacks {

    private static int NORMAL_ROUTES = 0;
    private static int FAVORITE_ROUTES = 1;

    public static RoutesListFragment newInstance(final Context context, boolean favorites) {

        Bundle bundle = new Bundle();
        bundle.putBoolean("favorites", favorites);

        Fragment fragment = Fragment.instantiate(context, RoutesListFragment.class.getName(), bundle);
        return (RoutesListFragment) fragment;
    }

    private RoutesListAdapter mAdapter;
    private ListView mListView;

    private boolean isFavorite;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_routes, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isFavorite = getArguments().getBoolean("favorites");

        mListView = (ListView) getActivity().findViewById(R.id.list);

        //Cuando hacemos scroll se esconde el toolbar, pero lo hemos quitado ya que no es necesario.
        //mListView.setScrollViewCallbacks(this);

        getBar().setShowHideAnimationEnabled(true);

        mAdapter = new RoutesListAdapter(getActivity(), R.layout.routes_list_item, null,
                new String[] {Provider.Routes.NUMBER_COL, Provider.Routes.NAME_COL},
                new int[] {R.id.number, R.id.name}, 0);

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) mAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), RouteDetailActivity.class);
                intent.putExtra("number", cursor.getString(cursor.getColumnIndex(Provider.Routes.NUMBER_COL)));

                getActivity().startActivity(intent);
            }
        });

        if(isFavorite) {
            getLoaderManager().initLoader(FAVORITE_ROUTES, null, this);

            // Mostramos cuando no tenga líneas
            mListView.setEmptyView(((RelativeLayout) getActivity().findViewById(R.id.empty_favorite_routes)));

        } else {
            getLoaderManager().initLoader(NORMAL_ROUTES, null, this);
        }

    }

    private String[] getIdOfFavoriteStops() {
        DatabaseHelper helper = DatabaseHelper.getInstance(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT route_id FROM routes_favorites", null);

        if(!cursor.moveToFirst()) {
           return new String[0];
        }

        String[] array = new String[cursor.getCount()];
        for(int i = 0; i < array.length; i++) {
            array[i] = String.valueOf(cursor.getInt(0));
            cursor.moveToNext();
        }
        // Cerramos
        cursor.close();

        return array;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        switch(i) {

            case 0:
                return new CursorLoader(getActivity(), Provider.CONTENT_URI_ROUTES, null, null, null, null);

            case 1:
                String[] ids = getIdOfFavoriteStops();

                if (ids.length < 1) {
                    //Está vacío y no devolvemos nada
                    return null;
                }

                return new CursorLoader(getActivity(), Provider.CONTENT_URI_ROUTES, null, "_id IN (" + Utils.makePlaceholders(ids.length) + ")", ids, null);

            // Fail
            default:
                return null;
        }

    }


    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }


    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }

    private ActionBar getBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

}
