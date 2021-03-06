package com.costular.guaguaslaspalmas.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.fragments.AddStopByCodeFragment;
import com.costular.guaguaslaspalmas.model.FavoriteStop;
import com.costular.guaguaslaspalmas.model.Stop;
import com.costular.guaguaslaspalmas.utils.DatabaseHelper;
import com.costular.guaguaslaspalmas.utils.Provider;
import com.costular.guaguaslaspalmas.widget.views.AddStop;
import com.costular.guaguaslaspalmas.widget.views.ColorPickerPalette;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Diego on 02/12/2014.
 */
public class AddToFavoriteDialog extends DialogFragment {

    public AddStop listener;
    private int mSelected = 0;

    @Bind(R.id.name_layout) TextInputLayout customNameInputLayout;
    @Bind(R.id.code_layout) TextInputLayout codeInputLayout;
    @Bind(R.id.custom_name) EditText customName;
    @Bind(R.id.code) EditText code;
    @Bind(R.id.palette) ColorPickerPalette palette;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_add_by_code, null);
        ButterKnife.bind(this, view);

        String[] colors = getResources().getStringArray(R.array.selectable_favorite_stop_colors);
        final int[] colorsInt = new int[colors.length];

        for(int i = 0; i < colorsInt.length; i++) {
            colorsInt[i] = Color.parseColor(colors[i]);
        }
        // Establecemos el seleccionado por defecto
        mSelected = colorsInt[0];

        palette.init(colors.length, 3, new ColorPickerPalette.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                mSelected = color;
                palette.drawPalette(colorsInt, mSelected);
            }
        });
        palette.drawPalette(colorsInt, colorsInt[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(code.getText().toString().isEmpty()) {
                    codeInputLayout.setError("No puede estar vacío.");
                    return;
                }
                int stopCode = Integer.parseInt(code.getText().toString());
                String custom = customName.getText().toString();

                // Id de la parada
                int id = Stop.getStopIdFromCode(getActivity(), stopCode);

                // Guardamos
                int stopsCount = FavoriteStop.stopsCount(getActivity()) + 1;

                Stop.addToFavorites(getActivity(), custom, mSelected, stopsCount,  id);

                DatabaseHelper helper = DatabaseHelper.getInstance(getActivity());
                SQLiteDatabase db = helper.getReadableDatabase();

                Cursor c = db.rawQuery("SELECT * FROM " + Provider.TABLE_FAVORITES_STOPS, null);
                c.moveToLast();

                FavoriteStop stop = FavoriteStop.createFromCursor(getActivity(), c);
                c.close();

                dismiss();
                Toast.makeText(getActivity(), "Guardada.", Toast.LENGTH_SHORT).show();

                if(listener != null) {
                    listener.onStopAdded(stop);
                }
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setView(view);

        return builder.create();
    }

    public void setListener(AddStop listener)
    {
        this.listener = listener;
    }

}
