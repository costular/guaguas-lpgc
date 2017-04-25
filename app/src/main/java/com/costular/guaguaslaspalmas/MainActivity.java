package com.costular.guaguaslaspalmas;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
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
import com.costular.guaguaslaspalmas.guaguas_notifier.GuaguaAlertBroadcast;
import com.costular.guaguaslaspalmas.services.ScheduleUpdater;

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

        startService(new Intent(this, ScheduleUpdater.class));
        // El service de fondo que se ve a dedicar a comprobar las líneas
        scheduleAlertChecker();
    }

    private void scheduleAlertChecker() {
        Intent alarm = new Intent(this, GuaguaAlertBroadcast.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(this, 0, alarm,
                PendingIntent.FLAG_NO_CREATE) != null);

        if(!alarmRunning) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(), 30000, pendingIntent);
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
