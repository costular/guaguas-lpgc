package com.costular.guaguaslaspalmas;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Spinner;

import com.costular.guaguaslaspalmas.utils.ThemeUtils;

import java.util.Arrays;

/**
 * Created by diego on 29/05/15.
 */
public class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Handler handler;

    private ThemeUtils mThemeUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThemeUtils = ThemeUtils.getInstance(getApplicationContext());
        handler = new Handler();
        setTheme(mThemeUtils.getTheme());

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(mThemeUtils.getPrimaryDarkColor());
        }
    }

    @Override
    public void onStop() {
        mThemeUtils.onStop();
        super.onStop();
    }

    protected void setUpToolbar(boolean homeEnabled) {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        if(homeEnabled) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected Toolbar getToolbar() {
        return toolbar;
    }

    protected void onThemeChanged(String theme) {
        mThemeUtils.onThemeChanged(theme);
    }

    protected void setToolbarTransparent(boolean value) {

        if(value) {
            //toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            toolbar.setBackgroundColor(Color.BLACK);
            toolbar.getBackground().setAlpha(20);
        } else {
            toolbar.setBackgroundColor(getPrimaryColor());
        }
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }

    protected void setToolbarTitle(final CharSequence title) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle(title);
            }
        });
    }

    protected void setToolbarColor(int color) {
        toolbar.setBackgroundColor(color);
    }

    protected int getPrimaryColor() {
        TypedValue typedValue = new  TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);

        return typedValue.data;
    }

    protected void hideToolbar() {

    }

    protected void showToolbar() {

    }
}
