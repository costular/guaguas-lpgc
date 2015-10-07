package com.costular.guaguaslaspalmas.tutorial;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.costular.guaguaslaspalmas.R;

/**
 * Created by diego on 6/03/15.
 */
public class BeginTutorialFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial_begin, container, false);
    }
}
