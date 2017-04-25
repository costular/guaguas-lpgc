package com.costular.guaguaslaspalmas.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.model.StopTime;

/**
 * Created by diego on 26/05/15.
 */
public class GuaguaAlertDialog extends DialogFragment {

    public interface GuaguaAlertInterface {
        public void onTimeSelected(int time);
    }

    private GuaguaAlertInterface listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            listener = (GuaguaAlertInterface) context;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_guagua_alert, null);
        final EditText minutesSelectorText = (EditText) view.findViewById(R.id.minutes_selector);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view)
                .setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int minutes = Integer.parseInt(minutesSelectorText.getText().toString());

                        if(listener != null) {
                            listener.onTimeSelected(minutes);
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
