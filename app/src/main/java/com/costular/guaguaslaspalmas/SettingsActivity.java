package com.costular.guaguaslaspalmas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.costular.guaguaslaspalmas.fragments.Preferences;
import com.costular.guaguaslaspalmas.utils.PrefUtils;
import com.costular.guaguaslaspalmas.utils.ThemeUtils;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

/**
 * Created by Diego on 18/11/2014.
 */
public class SettingsActivity extends BaseActivity implements Preferences.IThemeChangeListener{

    public static final String TAG = "SettingsActivity";

    private boolean scheduledRestart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Inicializamos toolbar
        setUpToolbar(true);
    }

    @Override
    public void onThemeChanged(String theme) {
        super.onThemeChanged(theme);

        //Decimos que el tema ha cambiado
        ((GuaguasApp) getApplication()).needReloadTheme();

        finish();
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
