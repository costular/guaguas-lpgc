package com.costular.guaguaslaspalmas.fragments;

import android.app.Activity;
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
import com.costular.guaguaslaspalmas.RouteDetailActivity;
import com.costular.guaguaslaspalmas.model.Route;
import com.costular.guaguaslaspalmas.model.Stop;
import com.costular.guaguaslaspalmas.utils.DatabaseHelper;
import com.costular.guaguaslaspalmas.utils.Provider;
import com.costular.guaguaslaspalmas.utils.Utils;
import com.costular.guaguaslaspalmas.widget.adapters.PopUpMapAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Diego on 16/01/2015.
 */
public class RouteMapFragment extends Fragment {

    /*
     * Activity
     */
    private RouteDetailActivity activity;

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
     * Número de la ruta
     */
    private String number;

    private static View view;


    //---------------------------------------------------------------------------------
    /*
     * Método para crear una nueva instancia.
     */
    public static RouteMapFragment newInstance(Context context, final String number) {

        Bundle bundle = new Bundle();
        bundle.putString("number", number);

        Fragment fragment = Fragment.instantiate(context, RouteMapFragment.class.getName(), bundle);
        return (RouteMapFragment) fragment;
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
        number = getArguments().getString("number");

        //Cargamos la ruta
        mRoute = Route.createRouteFromNumber(getActivity(), number);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (RouteDetailActivity) activity;
    }


    @Override
    public void onStart() {
        super.onStart();

        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();

        if(map != null) {
            setupMap();
        }
        map.setInfoWindowAdapter(new PopUpMapAdapter(getLayoutInflater(getArguments())));
    }

    private void setupMap() {

        if(mRoute == null) {
            return;
        }

        // Activamos la posición
        map.setMyLocationEnabled(true);

        Stop last = null;

        for(Stop stop : Stop.getStopsByConcesion(getActivity(), mRoute.getConcesion(getActivity(), activity.type))) {

            map.addMarker(new MarkerOptions().title(stop.getName()).position(new LatLng(stop.getLatitude(), stop.getLongitude())));
            last = stop;
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(last.getLatitude(),
                last.getLongitude()), 12.0f));


    }
}
