package com.costular.guaguaslaspalmas.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.Toast;

import com.costular.guaguaslaspalmas.model.Stop;

/**
 * Created by Diego on 30/11/2014.
 */
public class RouteFavoriteDialog extends DialogFragment {

    private FavoriteStopListener mListener;

    private int mId;
    private String mDefaultName;

    public interface FavoriteStopListener {
        public void onStopFavorited();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mListener = (FavoriteStopListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    public static RouteFavoriteDialog newInstance(Stop mStop) {

        Bundle bundle = new Bundle();
        bundle.putInt("id", mStop.getId());
        bundle.putString("default_name", mStop.getName());

        RouteFavoriteDialog dialog = new RouteFavoriteDialog();
        dialog.setArguments(bundle);

        return dialog;
    }

    public RouteFavoriteDialog() {

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mId = getArguments().getInt("id");
        mDefaultName = getArguments().getString("default_name");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText text = new EditText(getActivity());

        builder.setTitle("Escoge el nombre:")
                .setMessage("Escoge el nombre de la parada para identificarla de forma más sencilla")
                .setView(text)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String name = mDefaultName;

                        if(!text.getText().toString().isEmpty()) {
                            name = text.getText().toString();
                        }

                        Stop.addToFavorites(getActivity(), name, mId);
                        Toast.makeText(getActivity(), "Añadida a favoritos", Toast.LENGTH_SHORT);

                        // Llamamos al listener para decirle que se hizo favorito.
                        mListener.onStopFavorited();
                    }
                });

        return builder.create();
    }
}
