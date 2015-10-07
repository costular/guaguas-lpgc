package com.costular.guaguaslaspalmas.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.StopDetailActivity;
import com.costular.guaguaslaspalmas.model.Stop;
import com.costular.guaguaslaspalmas.utils.DatabaseHelper;
import com.costular.guaguaslaspalmas.widget.adapters.BurtuAdapter;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego on 26/01/2015.
 */
public class CheckStopCodeDialog extends DialogFragment {

    private static final String TAG = "CheckStopCodeDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final AutoCompleteTextView input = new AutoCompleteTextView(getActivity());

        ArrayList<String> history = new ArrayList<String>();
        history.addAll(DatabaseHelper.getInstance(getActivity()).checkStopsHistory());

        if(history.size() > 0) {

            BurtuAdapter adapter = new BurtuAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, history);
            input.setAdapter(adapter);
        }


        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint(getResources().getString(R.string.dialog_hint_message_check_stop_by_code));
        input.requestFocus();


        builder.setTitle(getResources().getString(R.string.dialog_title_check_stop_by_code))
                .setMessage(getResources().getString(R.string.dialog_message_check_stop_by_code))
                .setView(input)
                .setPositiveButton(getResources().getString(R.string.dialog_positive_check_stop_by_code), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (input.getText().toString().isEmpty()) {
                            return;
                        }
                        int code = Integer.parseInt(input.getText().toString());
                        int id = Stop.getStopIdFromCode(getActivity(), code);

                        if (id == -1) {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "No hay ninguna parada con ese código", Snackbar.LENGTH_LONG)
                                    .show();

                            dismiss();
                            return;
                        }
                        // Guardamos en la base de datos el registro de la parada que ha mirado.
                        DatabaseHelper helper = DatabaseHelper.getInstance(getActivity());
                        helper.insertHistory(String.valueOf(code));

                        Intent intent = new Intent(getActivity(), StopDetailActivity.class);
                        intent.putExtra(StopDetailActivity.STOP, code);
                        intent.putExtra(StopDetailActivity.ID, id);

                        startActivity(intent);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_negative_check_stop_by_code), null);

        return builder.create();
    }
}
