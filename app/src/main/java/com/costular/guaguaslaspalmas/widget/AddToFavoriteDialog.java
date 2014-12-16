package com.costular.guaguaslaspalmas.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.fragments.AddStopByCodeFragment;

import org.w3c.dom.Text;

/**
 * Created by Diego on 02/12/2014.
 */
public class AddToFavoriteDialog extends DialogFragment {

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_add_stop_layout, container);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.custompager);
        viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        return view;
    }
    */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_add_by_code, null);

        final EditText text = (EditText) view.findViewById(R.id.code);
        final TextView error = (TextView) view.findViewById(R.id.error_message);
        final Button button = (Button) view.findViewById(R.id.save_button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(text.getText().toString().isEmpty()) {
                    text.setHighlightColor(getResources().getColor(R.color.edit_text_error_color));

                    // Mostramos el TextView
                    error.setVisibility(View.VISIBLE);
                    error.setAlpha(0f);

                    error.animate().setDuration(300).alpha(.9f).start();
                    return;
                }

                // Guardamos
                dismiss();
                Toast.makeText(getActivity(), "Guardado.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        /*builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(text.getText().toString().isEmpty()) {
                    text.setHighlightColor(getResources().getColor(R.color.edit_text_error_color));

                    // Mostramos el TextView
                    error.setVisibility(View.VISIBLE);
                    error.setAlpha(0f);

                    error.animate().setDuration(300).alpha(.9f).start();
                    return;
                }

                // Guardamos
                Toast.makeText(getActivity(), "Guardado.", Toast.LENGTH_SHORT).show();
            }
        })
        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/

        return builder.create();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch(i) {
                case 0:
                    return AddStopByCodeFragment.newInstance();

                case 1:
                    return AddStopByCodeFragment.newInstance();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "Por código" : "Por ruta";
        }
    }
}
