package com.costular.guaguaslaspalmas.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.costular.guaguaslaspalmas.R;

/**
 * Created by Diego on 02/12/2014.
 */
public class AddStopByCodeFragment extends Fragment {

    public static AddStopByCodeFragment newInstance() {
        return new AddStopByCodeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_add_by_code, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
