package com.costular.guaguaslaspalmas.widget;

import android.app.Activity;
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
import com.costular.guaguaslaspalmas.model.Stop;

import org.w3c.dom.Text;

/**
 * Created by Diego on 02/12/2014.
 */
public class AddToFavoriteDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_add_by_code, null);

        final EditText customName = (EditText) view.findViewById(R.id.custom_name);
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

                    error.animate().setDuration(270).alpha(.9f).start();
                    return;
                }
                int code = Integer.parseInt(text.getText().toString());
                String custom = customName.getText().toString();

                // Id de la parada
                int id = Stop.getStopIdFromCode(getActivity(), code);

                // Guardamos
                Stop.addToFavorites(getActivity(), custom, id);

                dismiss();
                Toast.makeText(getActivity(), "Guardada.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }


}
