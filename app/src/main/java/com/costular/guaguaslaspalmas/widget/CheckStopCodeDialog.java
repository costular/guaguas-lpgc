package com.costular.guaguaslaspalmas.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.StopDetailActivity;
import com.costular.guaguaslaspalmas.model.Stop;
import com.nispok.snackbar.Snackbar;

/**
 * Created by Diego on 26/01/2015.
 */
public class CheckStopCodeDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint(getResources().getString(R.string.dialog_hint_message_check_stop_by_code));

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
                            Snackbar snackbar = Snackbar.with(getActivity());
                            snackbar.text("No hay ninguna parada con ese código");

                            snackbar.show(getActivity());

                            dismiss();
                            return;
                        }

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
