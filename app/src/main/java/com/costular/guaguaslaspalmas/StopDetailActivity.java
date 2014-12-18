package com.costular.guaguaslaspalmas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.costular.guaguaslaspalmas.model.Stop;
import com.costular.guaguaslaspalmas.model.StopTime;
import com.costular.guaguaslaspalmas.utils.PrefUtils;
import com.costular.guaguaslaspalmas.utils.StopsTimeLoader;
import com.costular.guaguaslaspalmas.utils.Utils;
import com.costular.guaguaslaspalmas.widget.EditFavoriteStop;
import com.costular.guaguaslaspalmas.widget.RouteFavoriteDialog;
import com.costular.guaguaslaspalmas.widget.adapters.StopsTimeListAdapter;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.util.List;

/**
 * Created by Diego on 26/11/2014.
 */
public class StopDetailActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<List<StopTime>>, SwipeRefreshLayout.OnRefreshListener,
        RouteFavoriteDialog.FavoriteStopListener, EditFavoriteStop.EditFavoriteListener {

    public static final String STOP = "STOP_CODE";
    public static final String ID = "ID";

    public static final int FAVORITE_MENU = 1;

    private ListView mListView;
    private StopsTimeListAdapter mAdapter;

    private Stop mStop;
    private int mStopCode;

    private MenuItem star;

    private SwipeRefreshLayout swipeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        PrefUtils.loadTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops_detail);

        // Cargamos el toolbar
        loadToolbar();

        mStopCode = getIntent().getIntExtra(STOP, 0);
        int id = getIntent().getIntExtra(ID, 0);

        mStop = Stop.createStopFromId(getApplicationContext(), id);

        // Quitamos la elevación para que no se vea mal.
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mStop.getFavoriteNameStop(getApplicationContext()));
        getSupportActionBar().setSubtitle(mStop.getName());

        TypedArray a = getTheme().obtainStyledAttributes(R.style.AppTheme, new int[] {android.R.attr.colorPrimary});
        int color = a.getColor(0, 0);
        a.recycle();

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(color);

        mListView = (ListView) findViewById(R.id.stops_hour_list);
        mAdapter = new StopsTimeListAdapter(getApplicationContext());

        mListView.setAdapter(mAdapter);

        checkInternet();

        // Empezamos a cargar
        swipeLayout.setRefreshing(true);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    private void loadToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void refresh() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onRefresh() {

     if(!checkInternet()) {
         return;
     }

       refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        star = menu.add(Menu.NONE, FAVORITE_MENU, Menu.NONE, "Favorita");

        if(mStop.isFavorite(getApplicationContext())) {
            MenuItemCompat.setShowAsAction(star.setIcon(R.drawable.ic_action_star), MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }else {
            MenuItemCompat.setShowAsAction(star.setIcon(R.drawable.ic_action_star_outline), MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }


        return true;
    }

    private boolean checkInternet() {

        boolean internet = Utils.haveInternet(getApplicationContext());

        if(!internet) {
            Snackbar.with(getApplicationContext()) // context
                    .text("No hay conexión a internet.") // text to display
                    .actionLabel("Reintentar") // action button label
                    .actionListener(new ActionClickListener() {
                        @Override
                        public void onActionClicked(Snackbar snackbar) {
                            refresh();
                            snackbar.dismiss();
                        }
                    }) // action button's ActionClickListener
                    .show(this); // activity where it is displayed
        }

        return internet;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {

            case FAVORITE_MENU:
                if(mStop.isFavorite(getApplicationContext())) {

                if(mStop.removeFromFavorites(getApplicationContext())) {
                    mStop.removeFromFavorites(getApplicationContext());
                    item.setIcon(R.drawable.ic_action_star_outline);
                    Toast.makeText(getApplicationContext(), "Eliminada de favoritos", Toast.LENGTH_SHORT).show();
                }
            }else {
                // Mostramos el diálogo para obtener el nombre de la línea favorita
                RouteFavoriteDialog.newInstance(mStop).show(getSupportFragmentManager(), "");
            }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStopFavorited() {
        // Jeje
        star.setIcon(R.drawable.ic_action_star);

        getSupportActionBar().setTitle(mStop.getFavoriteNameStop(getApplicationContext()));
    }

    @Override
    public void onStopEdited() {
        getSupportActionBar().setTitle(mStop.getFavoriteNameStop(getApplicationContext()));
    }

    @Override
    public Loader<List<StopTime>> onCreateLoader(int i, Bundle bundle) {
        return new StopsTimeLoader(getApplicationContext(), mStopCode);
    }

    @Override
    public void onLoadFinished(Loader<List<StopTime>> listLoader, List<StopTime> stopTimes) {
        mAdapter.setData(stopTimes);

        if(swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<StopTime>> listLoader) {
        swipeLayout.setRefreshing(true);

        mListView.setAdapter(null);
    }


}
