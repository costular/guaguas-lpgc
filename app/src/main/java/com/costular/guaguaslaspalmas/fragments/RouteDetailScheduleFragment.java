package com.costular.guaguaslaspalmas.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.costular.guaguaslaspalmas.R;

/**
 * Created by Diego on 23/11/2014.
 */
public class RouteDetailScheduleFragment extends Fragment {

    public static RouteDetailScheduleFragment newInstance(final Context context, final int id) {

        Bundle bundle = new Bundle();
        bundle.putInt("id", id);

        Fragment fragment = Fragment.instantiate(context, RouteDetailScheduleFragment.class.getName(), bundle);
        return (RouteDetailScheduleFragment) fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_detail_schedule, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
