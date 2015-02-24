package com.costular.guaguaslaspalmas.fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.widget.AdapterView;
import android.widget.ListView;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.RouteDetailActivity;
import com.costular.guaguaslaspalmas.StopDetailActivity;
import com.costular.guaguaslaspalmas.model.Route;
import com.costular.guaguaslaspalmas.model.Stop;
import com.costular.guaguaslaspalmas.model.StopTime;
import com.costular.guaguaslaspalmas.utils.DatabaseHelper;
import com.costular.guaguaslaspalmas.utils.Provider;
import com.costular.guaguaslaspalmas.utils.Utils;
import com.costular.guaguaslaspalmas.widget.adapters.StopsListAdapter;

/**
 * Created by Diego on 25/11/2014.
 */
public class RoutesDetailStopsFragment extends Fragment implements LoaderCallbacks<Cursor> {


    /*
     * Variable que guarda la id de la línea.
     */
    private String number;

    /*
     * Variable que guarda si la dirección es ida o vuelta.
     */
    private int type;

    /*
     * Objeto de la clase Route.
     */
    private Route mRoute;


    private ListView mListView;
    private StopsListAdapter mAdapter;

    public static RoutesDetailStopsFragment newInstance(final Context context, final String number, int type) {

        Bundle bundle = new Bundle();
        bundle.putString("number", number);
        bundle.putInt("type", type);

        Fragment fragment = Fragment.instantiate(context, RoutesDetailStopsFragment.class.getName(), bundle);
        return (RoutesDetailStopsFragment) fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_detail_stops, null);
    }

    @Override
    public void onStart() {
        super.onStart();

        number = getArguments().getString("number");
        type = getArguments().getInt("type");

        Log.d(getClass().getSimpleName(), "number: " + number);

        mRoute = Route.createRouteFromNumber(getActivity(), number);

        //cargamos la lista y eso
        mListView = (ListView) getActivity().findViewById(R.id.stops_list);
        mAdapter = new StopsListAdapter(getActivity(), R.layout.route_detail_stops_list, null,
                new String[] {Provider.Stops.NAME_COL, Provider.Stops.NAME_COL, Provider.Stops.NAME_COL},
                new int[] {R.id.name},
                0, mRoute.getColor());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new ListViewListener());


        getLoaderManager().initLoader(0, null, this);
    }

    private class ListViewListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) mAdapter.getItem(position);

            Intent intent = new Intent(getActivity(), StopDetailActivity.class);

            Bundle bundle = new Bundle();
            bundle.putInt(StopDetailActivity.STOP, cursor.getInt(cursor.getColumnIndex(Provider.Stops.CODE_COL)));
            bundle.putInt(StopDetailActivity.ID, cursor.getInt(cursor.getColumnIndex(Provider.Stops.ID_COL)));

            intent.putExtras(bundle);

            startActivity(intent);
        }
    }

    public void changeDirection() {

        if(type == RouteDetailActivity.IDA) {
            type = RouteDetailActivity.VUELTA;
        } else {
            type = RouteDetailActivity.IDA;
        }

        // Actualizamos
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        DatabaseHelper helper = DatabaseHelper.getInstance(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM VARIANTES WHERE concesion = '"+mRoute.getNumber()+"' LIMIT 2", null);

        int mVarianteId = -1;
        // Solo cogemos la ida, OJO!!!

        if(type == RouteDetailActivity.IDA) {
            if(cursor.moveToFirst()) {
                mVarianteId = cursor.getInt(0);
            }
        } else {
            if(cursor.moveToFirst()) {
                // Pasamos al siguiente
                cursor.moveToNext();

                mVarianteId = cursor.getInt(0);
            }

        }
        cursor.close();

            return new CursorLoader(getActivity(), Provider.CONTENT_URI_STOPS, null, "(" + Provider.Stops.ROUTE_COL + "= ?)",
                    new String[] {String.valueOf(mVarianteId)}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

}
