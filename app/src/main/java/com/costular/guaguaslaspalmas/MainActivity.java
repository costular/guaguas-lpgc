package com.costular.guaguaslaspalmas;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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


public class MainActivity extends BaseActivity{

    private static final int ROUTES = 0;
    private static final int FAVORITES = 1;
    private static final int STOPS = 2;
    private static final int MAP = 3;

    private static final int CONTACT = 0;
    private static final int SETTINGS = 1;

    // Navigation
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    // Navigation adapter and list
    private DrawerListAdapter mAdapter;
    private ListView listView;
    private ListView secondList;

    // Navigation title and ActionBar title
    private CharSequence title;
    private String[] secondItemTitles;
    private String[] itemTitles;

    // Position id of navigation menu
    private int mPosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar(false);
        setupNavigation();

        // Si lo acaba de abrir.
        if(savedInstanceState == null) {
            selectItem(ROUTES);
        }

        //startActivity(new Intent(this, TutorialActivity.class));
    }

    private void setupNavigation() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer == null) {
            return;
        }

        listView = (ListView) findViewById(R.id.left_drawer);
        secondList = (ListView) findViewById(R.id.second_list);

        // El borde para que parezca que tiene volumen Z
        drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        title = getSupportActionBar().getTitle();
        // Activamos para que se abra el menú desde el icono
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        List<DrawerListItem> items = new ArrayList<DrawerListItem>();
        // Obtenemos el array con los nombres del menú
        itemTitles = getResources().getStringArray(R.array.drawer_items_array);
        // Obtenemos un array especial para pasar de String a Integer ya que contiene los ids de los drawables
        TypedArray icons = getResources().obtainTypedArray(R.array.drawer_item_icons);

        // Un bucle para cargar el list
        for(int i = 0; i < itemTitles.length; i++) {
            // Añadimos y si el título es avisos, añadimos el contador
            items.add(new DrawerListItem(itemTitles[i], icons.getResourceId(i, -1)));
        }
        // Borramos de memoria
        icons.recycle();

        // Añadimos el adapter
        mAdapter = new DrawerListAdapter(this, items, true);
        listView.setAdapter(mAdapter);
        items.clear(); // Para ahorrar memoria :D

        toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.menu,
                R.string.app_name) {

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(title);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(R.string.app_name);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawer.setDrawerListener(toggle);

        listView.setOnItemClickListener(new DrawerItemClickListener());

        //Second list
        List<DrawerListItem> itemsSecond = new ArrayList<DrawerListItem>();
        // Obtenemos el array con los nombres del menú
        secondItemTitles = getResources().getStringArray(R.array.navigation_drawer_second_list);
        // Obtenemos un array especial para pasar de String a Integer ya que contiene los ids de los drawables
        TypedArray iconsSecond = getResources().obtainTypedArray(R.array.navigation_drawer_second_list_icons);

        // Un bucle para cargar el list
        for(int i = 0; i < secondItemTitles.length; i++) {
            // Añadimos y si el título es avisos, añadimos el contador
            itemsSecond.add(new DrawerListItem(secondItemTitles[i], iconsSecond.getResourceId(i, -1)));
        }
        // Borramos de memoria
        iconsSecond.recycle();


        secondList.setAdapter(new DrawerListAdapter(this, itemsSecond, false));
        secondList.setOnItemClickListener(new SecondListItemClickListener());
    }

    private void selectItem(int position) {

        if(mPosition == position) {
            closeDrawer();
            return;
        }

        switch(position) {

            case ROUTES:
                mPosition = ROUTES;
                loadFragment(RoutesListFragment.newInstance(getApplicationContext(), false));
                break;
            case FAVORITES:
                mPosition = FAVORITES;
                loadFragment(RoutesListFragment.newInstance(getApplicationContext(), true));
                break;
            case STOPS:
                mPosition = STOPS;
                loadFragment(new StopsFavoritesFragment());
                break;

            case MAP:
                mPosition = MAP;
                loadFragment(new MapStopsFragment());
                break;

        }
        // Ponemos el nombre en el ActionBar
        setTitle(itemTitles[position]);

        mAdapter.setmSelectedItem(mPosition);

        //Cerramos el navigation Drawer
        closeDrawer();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public void setTitle(CharSequence str) {
        this.title = str;
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
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

        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    private void closeDrawer() {
        drawer.closeDrawer(Gravity.START);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            view.setSelected(true);
            selectItem(position);
        }
    }

    private class SecondListItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            switch (position) {

                case CONTACT:
                    Intent email = new Intent(Intent.ACTION_SEND);

                    email.setType("text/plain");
                    email.putExtra(Intent.EXTRA_EMAIL, "costular@gmail.com");

                    startActivity(email);
                    break;

                case SETTINGS:
                    closeDrawer();

                    Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(settings);
                    break;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        toggle.onConfigurationChanged(newConfig);
    }

    private class serverService extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {

            try {
                Socket socket = new Socket("92.222.26.194", 30001);

                boolean connect = true;

                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                while(connect)
                {

                    String line = input.readUTF();

                    if(line.contains("ok")) {
                        output.writeUTF("ok");
                        output.flush();
                    }

                    Log.d("here", line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }



            return "";
        }
    }

}
