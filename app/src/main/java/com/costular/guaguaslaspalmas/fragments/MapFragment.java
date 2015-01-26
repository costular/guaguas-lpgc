package com.costular.guaguaslaspalmas.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.model.Route;
import com.costular.guaguaslaspalmas.model.Stop;
import com.costular.guaguaslaspalmas.utils.DatabaseHelper;
import com.costular.guaguaslaspalmas.utils.Provider;
import com.costular.guaguaslaspalmas.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Diego on 16/01/2015.
 */
public class MapFragment extends Fragment {

    /*
     * Mapa de Google
     */
    private GoogleMap map;

    /*
     * Route
     */
    private Route mRoute;

    /*
     * Stops
     */
    private Stop[] stops;

    /*
     * Id de la ruta
     */
    private int mId;

    private static View view;


    //---------------------------------------------------------------------------------
    /*
     * Método para crear una nueva instancia.
     */
    public static MapFragment newInstance(Context context, final int id) {

        Bundle bundle = new Bundle();
        bundle.putInt("id", id);

        Fragment fragment = Fragment.instantiate(context, MapFragment.class.getName(), bundle);
        return (MapFragment) fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        return view;
    }

    private void setupIfNeeded() {
        if(map == null) {

            map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

            if(map != null) {
                setupMap();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Asignamos la id guardada en el Bundle a la variable "mId"
        mId = getArguments().getInt("id");

        //Cargamos la ruta
        mRoute = Route.createFromCursor(Utils.getCursorFromRouteId(getActivity(), mId));

        // Cargamos el array de paradas correspondientes a la línea para mostrar su posición en el mapa.
        loadStops();

    }

    private void loadStops() {

        DatabaseHelper helper = DatabaseHelper.getInstance(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();

        // Creamos el cursor
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Provider.TABLE_STOPS+" WHERE (linea = '"+mRoute.getNumber()+"' AND direccion LIKE 'ida%')", null);

        // Asignamos el tamaño
        stops = new Stop[cursor.getColumnCount()];

        // Ahora recorremos el array para asignar todos los valores
        for(int i = 0; i < stops.length; i++) {

            if(cursor.moveToFirst()) {

                do {
                    stops[i] = Stop.createStopFromId(getActivity(), cursor.getInt(cursor.getColumnIndex("_id")));
                } while (cursor.moveToNext());
            }

        }

        //Cerramos el cursor
        cursor.close();
    }

    @Override
    public void onStart() {
        super.onStart();

        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();

        if(map != null) {
            setupMap();
        }
    }

    private void setupMap() {

        // Activamos la posición
        map.setMyLocationEnabled(true);

        // Ahora añadimos una marca por cada parada
        for(int i = 0; i < stops.length; i++) {
            // Añadimos la marca en la latitud y longitud de la parada.
            map.addMarker(new MarkerOptions().title(stops[i].getName()).position(new LatLng(stops[i].getLatitude(), stops[i].getLongitude())));

        }


        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(stops[0].getLatitude(),
                stops[0].getLongitude()), 12.0f));


    }
}
