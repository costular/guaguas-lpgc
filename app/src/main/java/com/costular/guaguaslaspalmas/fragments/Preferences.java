package com.costular.guaguaslaspalmas.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.utils.PrefUtils;

/**
 * Created by Diego on 18/11/2014.
 */
public class Preferences extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public interface IThemeChangeListener {
        void onThemeChanged(String theme);
    }

    private Preference notificationSound;
    private Preference notificationVibrate;
    private Preference notificationPriority;

    private IThemeChangeListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (IThemeChangeListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.screen);

        notificationSound = getPreferenceScreen().findPreference("notification_sound");
        notificationVibrate = getPreferenceScreen().findPreference("notification_vibrate");
        notificationPriority = getPreferenceScreen().findPreference("notification_priority");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {

        // Aquí comprobamos para cuando se cambie X cosas de los ajustes.
        if("notifications".equals(key)) {

            //Obtenemos la categoría a través del PreferenceScreen


            if(sharedPreferences.getBoolean("notifications", true)) {
                // Activamos la categoría de notificaciones
                enableNotifications();

            } else {
                // Desactivamos la categoría de notificaciones
                disableNotifications();
            }

        } else if("general_theme".equals(key)) {
            ListPreference listPreference = (ListPreference) findPreference(key);

            if(listener != null) {
                listener.onThemeChanged(listPreference.getValue());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


        // Comprobamos que esté desactivado o no
        if(getPreferenceScreen().getSharedPreferences().getBoolean("notifications", true)) {
            // Activamos la categoría de notificaciones
            enableNotifications();

        } else {
            // Desactivamos la categoría de notificaciones
            disableNotifications();
        }

    }

    private void enableNotifications() {
        notificationPriority.setEnabled(true);
        notificationSound.setEnabled(true);
        notificationVibrate.setEnabled(true);
    }

    private  void disableNotifications() {
        notificationPriority.setEnabled(false);
        notificationSound.setEnabled(false);
        notificationVibrate.setEnabled(false);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}

