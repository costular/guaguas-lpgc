package com.costular.guaguaslaspalmas.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.RouteDetailActivity;
import com.costular.guaguaslaspalmas.model.Stop;
import com.costular.guaguaslaspalmas.utils.Provider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by diego on 10/02/15.
 */
public class MapStopsFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap map;

    SupportMapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);

        if(mapFragment != null)
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map = googleMap;

        // Config the map
        googleMap.setMyLocationEnabled(true);
        // Activamos los botones de zoom.
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        new LoadStops(map).execute();
    }

    private class LoadStops extends AsyncTask<Void, Void, List<Stop>> {


        GoogleMap map;

        public LoadStops(GoogleMap map) {
            this.map = map;
        }

        @Override
        protected List<Stop> doInBackground(Void... params) {

            return Stop.getAllStops(getActivity());
        }

        @Override
        protected void onPostExecute(List<Stop> stops) {

            for(Stop stop : stops) {
                map.addMarker(new MarkerOptions().title(stop.getName()).position(new LatLng(stop.getLatitude(), stop.getLongitude())));
            }

            map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(stops.get(stops.size() - 1).getLatitude(), stops.get(stops.size() - 1).getLongitude())));

            Log.d(getClass().getSimpleName(), "Esto ocurre?");
        }
    }

}
