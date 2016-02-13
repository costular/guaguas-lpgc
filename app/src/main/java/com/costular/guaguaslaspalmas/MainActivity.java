package com.costular.guaguaslaspalmas;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.costular.guaguaslaspalmas.fragments.MapStopsFragment;
import com.costular.guaguaslaspalmas.fragments.RoutesListFragment;
import com.costular.guaguaslaspalmas.fragments.StopsFavoritesFragment;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity{

    private static final int ROUTES = R.id.drawer_routes;
    private static final int FAVORITES = R.id.drawer_favorites;
    private static final int STOPS = R.id.drawer_stops;

    private static final int CONTACT = R.id.drawer_contact;
    private static final int SETTINGS = R.id.drawer_settings;

    RoutesListFragment routesListFragment;
    RoutesListFragment favoritesFragment;
    StopsFavoritesFragment stopsFavoritesFragment;
    MapStopsFragment mapStopsFragment;

    // Navigation
    @Bind(R.id.drawer_layout) DrawerLayout mDrawer;
    @Bind(R.id.navigation_view) NavigationView mNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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

    @Override
    public void onResume() {
        super.onResume();

        if(((GuaguasApp)getApplication()).isNeededReloadTheme()) {
            ((GuaguasApp)getApplication()).notNeedReloadTheme(); // Decimos que ya no es necesario actualizar el tema

            finish();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        }
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
                    case SETTINGS:
                        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(i);
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
