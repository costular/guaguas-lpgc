package com.costular.guaguaslaspalmas.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import com.costular.guaguaslaspalmas.model.Stop;

/**
 * Created by Diego on 30/11/2014.
 */
public class EditFavoriteStop extends DialogFragment {

    private EditFavoriteListener mListener;

    public interface EditFavoriteListener {
        public void onStopEdited();
    }

    public static EditFavoriteStop newInstance(int mId) {

        Bundle bundle = new Bundle();
        bundle.putInt("id", mId);

        EditFavoriteStop stop = new EditFavoriteStop();
        stop.setArguments(bundle);

        return stop;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mListener = (EditFavoriteListener) activity;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final int id = getArguments().getInt("id");

        final EditText text = new EditText(getActivity());
        // Introducimos el nombre actual en el EditText para facilitar la edición.
        text.setText(Stop.createStopFromId(getActivity(), id).getFavoriteNameStop(getActivity()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cambia el nombre de la parada")
                .setMessage("Introduce el nuevo nombre que quieras darle a la parada")
                .setView(text)
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Stop.editStopName(getActivity(), text.getText().toString(), id);

                        // Informamos de que la parada fue editada.
                        mListener.onStopEdited();
                    }
                });

        return builder.create();

    }
}
