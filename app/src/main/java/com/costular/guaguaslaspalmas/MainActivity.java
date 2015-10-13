package com.costular.guaguaslaspalmas;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.costular.guaguaslaspalmas.fragments.MapStopsFragment;
import com.costular.guaguaslaspalmas.fragments.RoutesFragment;
import com.costular.guaguaslaspalmas.fragments.RoutesListFragment;
import com.costular.guaguaslaspalmas.fragments.StopsFavoritesFragment;
import com.costular.guaguaslaspalmas.model.FavoriteStop;
import com.costular.guaguaslaspalmas.tutorial.TutorialActivity;
import com.costular.guaguaslaspalmas.utils.PrefUtils;
import com.costular.guaguaslaspalmas.utils.Utils;
import com.costular.guaguaslaspalmas.widget.DrawerListItem;
import com.costular.guaguaslaspalmas.widget.adapters.DrawerListAdapter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity{

    private static final int ROUTES = R.id.drawer_routes;
    private static final int FAVORITES = R.id.drawer_favorites;
    private static final int STOPS = R.id.drawer_stops;
    private static final int MAP = R.id.drawer_map;

    private static final int CONTACT = R.id.drawer_contact;
    private static final int SETTINGS = R.id.drawer_settings;

    RoutesListFragment routesListFragment;
    RoutesListFragment favoritesFragment;
    StopsFavoritesFragment stopsFavoritesFragment;
    MapStopsFragment mapStopsFragment;

    // Navigation
    @InjectView(R.id.drawer_layout) DrawerLayout mDrawer;
    @InjectView(R.id.navigation_view) NavigationView mNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        // Iniciamos la toolbar activando HomeAsUpIndicator para el hamburger menu
        setUpToolbar(true);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        if(mNavigationView != null) {
            setupNavigation();
        }

        if(savedInstanceState == null) {
            loadFragment(RoutesListFragment.newInstance(getApplicationContext(), false));
            mNavigationView.getMenu().getItem(0).setChecked(true);
        }

        routesListFragment = RoutesListFragment.newInstance(getApplicationContext(), false);
        favoritesFragment = RoutesListFragment.newInstance(getApplicationContext(), true);
        stopsFavoritesFragment = new StopsFavoritesFragment();
        mapStopsFragment = new MapStopsFragment();
    }

    private void setupNavigation() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Cerramos el Drawer
                closeDrawer();

                switch (menuItem.getItemId()) {
                    case ROUTES:
                        menuItem.setChecked(true);
                        loadFragment(routesListFragment);
                        break;
                    case FAVORITES:
                        menuItem.setChecked(true);
                        loadFragment(favoritesFragment);
                        break;
                    case STOPS:
                        menuItem.setChecked(true);
                        loadFragment(stopsFavoritesFragment);
                        break;
                    case MAP:
                        menuItem.setChecked(true);
                        loadFragment(mapStopsFragment);
                        break;
                }
                return true;
            }
        });
        mNavigationView.setCheckedItem(0);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void closeDrawer() {
        mDrawer.closeDrawer(GravityCompat.START);
    }

}
