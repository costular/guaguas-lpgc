package com.costular.guaguaslaspalmas.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.utils.PrefUtils;

/**
 * Created by Diego on 18/11/2014.
 */
public class Preferences extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.screen);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {

        // Aquí comprobamos para cuando se cambie X cosas de los ajustes.
        if("notifications".equals(key)) {

            //Obtenemos la categoría a través del PreferenceScreen


            if(sharedPreferences.getBoolean("notifications", true)) {
                // Activamos la categoría de notificaciones
                getPreferenceScreen().findPreference("notification_sound").setEnabled(true);
                getPreferenceScreen().findPreference("notification_vibrate").setEnabled(true);

            } else {
                // Desactivamos la categoría de notificaciones
                getPreferenceScreen().findPreference("notification_sound").setEnabled(false);
                getPreferenceScreen().findPreference("notification_vibrate").setEnabled(false);
            }

        } else if("theme".equals(key)) {
            PrefUtils.setTheme(sharedPreferences.getString("theme", "indigo"));
            Log.d("Preferences", sharedPreferences.getString("theme", ""));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


        // Comprobamos que esté desactivado o no
        if(getPreferenceScreen().getSharedPreferences().getBoolean("notifications", true)) {
            // Activamos la categoría de notificaciones
            getPreferenceScreen().findPreference("notification_sound").setEnabled(true);
            getPreferenceScreen().findPreference("notification_vibrate").setEnabled(true);

        } else {
            // Desactivamos la categoría de notificaciones
            getPreferenceScreen().findPreference("notification_sound").setEnabled(false);
            getPreferenceScreen().findPreference("notification_vibrate").setEnabled(false);
        }

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}

