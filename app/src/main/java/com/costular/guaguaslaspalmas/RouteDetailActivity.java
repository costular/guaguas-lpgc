package com.costular.guaguaslaspalmas;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.costular.guaguaslaspalmas.fragments.AddStopByCodeFragment;
import com.costular.guaguaslaspalmas.fragments.RouteDetailScheduleFragment;
import com.costular.guaguaslaspalmas.fragments.RoutesDetailStopsFragment;
import com.costular.guaguaslaspalmas.fragments.RoutesFragment;
import com.costular.guaguaslaspalmas.fragments.RoutesListFragment;
import com.costular.guaguaslaspalmas.model.Route;
import com.costular.guaguaslaspalmas.utils.PrefUtils;
import com.costular.guaguaslaspalmas.utils.Utils;
import com.costular.guaguaslaspalmas.widget.views.SlidingTabLayout;

/**
 * Created by Diego on 23/11/2014.
 */
public class RouteDetailActivity extends ActionBarActivity{

    // Tab ids
    public static final int STOPS_TAB = 0;
    public static final int SCHEDULE_TAB = 1;
    public static final int MAP_TAB = 2;

    private static final int FAVORITE_MENU = 0;
    private static final int SCHEDULE_MENU = 1;
    private static final int MAP_MENU = 2;

    private SlidingTabLayout tabs;
    private ViewPager viewPager;

    private int mId;
    private Route mRoute;

    private String mGoing, mReturn;
    private boolean mDoubleDestiny;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        PrefUtils.loadTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);

        loadToolbar();

        // Obtenemos la id de la ruta.
        mId = getIntent().getIntExtra("id", -1);

        Cursor cursor = Utils.getCursorFromRouteId(getApplicationContext(), mId);
        mRoute = Route.createFromCursor(cursor);
        // Y cerramos el cursor
        cursor.close();

        mDoubleDestiny = mRoute.getName().contains("-");

        if(mDoubleDestiny) {
            String[] destinies = mRoute.getName().split("-");
            mGoing = destinies[0];
            mReturn = destinies[1];
        }

        // Quitamos la elevación para que no se vea mal.
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        getSupportActionBar().setTitle(mRoute.getNumber() + ": " + mRoute.getName());

        if(mDoubleDestiny) {
            // Tabs y ViewPager
            tabs = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
            viewPager = (ViewPager) findViewById(R.id.pager);

            viewPager.setAdapter(new MyPagerAdapter(this, getSupportFragmentManager(), mId));
            tabs.setViewPager(viewPager);

            final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                    .getDisplayMetrics());
            viewPager.setPageMargin(pageMargin);

        } else {


        }

    }

    private void loadToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(mRoute != null) {

            if(mRoute.isFavorite(getApplicationContext())) {
                MenuItemCompat.setShowAsAction(menu.add(Menu.NONE, FAVORITE_MENU, Menu.NONE, "Favorita").setIcon(R.drawable.ic_action_star), MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }else {
                MenuItemCompat.setShowAsAction(menu.add(Menu.NONE, FAVORITE_MENU, Menu.NONE, "Favorita").setIcon(R.drawable.ic_action_star_outline), MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }

        }else {
            MenuItemCompat.setShowAsAction(menu.add(Menu.NONE, FAVORITE_MENU, Menu.NONE, "Favorita").setIcon(R.drawable.ic_action_star_outline), MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == FAVORITE_MENU) {
            if(mRoute != null) {

                if(mRoute.isFavorite(getApplicationContext())) {
                    if(mRoute.deleteFavorite(getApplicationContext())) {
                        Toast.makeText(getApplicationContext(), "Eliminada de favoritos", Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.ic_action_star_outline);
                    }
                }else {
                    if(mRoute.addToFavorites(getApplicationContext())) {
                        Toast.makeText(getApplicationContext(), "Añadida a favoritos", Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.ic_action_star);
                    }
                }
            }
            return true;
        } else if(item.getItemId() == SCHEDULE_MENU) {

            // Esto es temporal

        }

        return false;
    }

    public class MyPagerAdapter extends FragmentPagerAdapter  {
        private int NUM_ITEMS = 3;

        private final Context mContext;
        private int localId;

        public MyPagerAdapter(final Context context, FragmentManager fragmentManager, final int id) {
            super(fragmentManager);

            mContext = context;
            localId = id;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case STOPS_TAB: // Fragment # 0 - This will show FirstFragment
                    return RoutesDetailStopsFragment.newInstance(mContext, localId);
                case SCHEDULE_TAB:
                    return RouteDetailScheduleFragment.newInstance(mContext, localId);
                case MAP_TAB:
                    return RoutesDetailStopsFragment.newInstance(mContext, localId);
            }

            return null;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
           switch(position) {
               case STOPS_TAB:
                   return "Paradas";

               case SCHEDULE_TAB:
                   return "Horarios";

               case MAP_TAB:
                   return "Mapa";

               default: return "";
           }

        }

    }

}
