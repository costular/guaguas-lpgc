package com.costular.guaguaslaspalmas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.costular.guaguaslaspalmas.events.RouteDirection;
import com.costular.guaguaslaspalmas.fragments.RouteMapFragment;
import com.costular.guaguaslaspalmas.fragments.RouteDetailScheduleFragment;
import com.costular.guaguaslaspalmas.fragments.RoutesDetailStopsFragment;
import com.costular.guaguaslaspalmas.model.Route;
import com.costular.guaguaslaspalmas.utils.Utils;
import com.costular.guaguaslaspalmas.widget.views.CircleView;
import com.costular.guaguaslaspalmas.widget.views.SlidingTabLayout;
import com.melnykov.fab.FloatingActionButton;

import de.greenrobot.event.EventBus;

/**
 * Created by Diego on 23/11/2014.
 */
public class RouteDetailActivity extends BaseActivity {

    public static final String TAG = "RouteDetailActivity";

    private Toolbar toolbar;

    // Constantes estáticas para decirle si es ida o vuelta·
    public static final int IDA = 1;
    public static final int VUELTA = 2;

    public int type = IDA;

    // Tab ids
    public static final int STOPS_TAB = 0;
    public static final int SCHEDULE_TAB = 1;
    public static final int MAP_TAB = 2;

    private static final int FAVORITE_MENU = 0;
    private static final int SCHEDULE_MENU = 1;
    private static final int MAP_MENU = 2;

    private SlidingTabLayout tabs;
    private ViewPager viewPager;

    private FloatingActionButton fab;

    private String number;
    private Route mRoute;

    private String mGoing, mReturn;
    private boolean mDoubleDestiny;

    private boolean showingAnimation = false;

    /*
     * Fragments
     */
    RoutesDetailStopsFragment stopsFragment;
    RouteDetailScheduleFragment schedulesFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);

        setUpToolbar(true);

        // Obtenemos la id de la ruta.
        number = getIntent().getStringExtra("number");
        mRoute = Route.createRouteFromNumber(getApplicationContext(), number);

        //Cargamos el número en el círculo
        CircleView circleView = new CircleView(this, String.valueOf(mRoute.getNumber()), mRoute.getColor());

        // Tabs y ViewPager
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Ponemos el nombre de la Toolbar
        setToolbarTitle(mRoute.getNumber() + ": " + mRoute.getName());

        mDoubleDestiny = mRoute.getName().contains("-");

        if (mDoubleDestiny) {
            String[] destinies = mRoute.getName().split("-");
            mGoing = destinies[0];
            mReturn = destinies[1];

            // Si tiene ida y vuelta ponemos el destino.
            getSupportActionBar().setSubtitle(getResources().getString(R.string.destination) + " " + mGoing);

            // El botón flotante para cambiar la dirección de la línea

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(showingAnimation) {
                        return;
                    }

                    fab.animate()
                            .rotationBy(360f)
                            .setDuration(250)
                            .setInterpolator(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? AnimationUtils.loadInterpolator(getApplicationContext(), R.interpolator.fast_out_slow_in)
                                    : new AccelerateDecelerateInterpolator())
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    showingAnimation = false;
                                }
                            })
                            .start();

                    showingAnimation = true;

                    // Cambiamos la dirección
                    changeDirection();
                    // Enviamos el bus
                    EventBus.getDefault().post(new RouteDirection(type));

                }
            });

        } else {

            // El botón de cambiar sentido no tiene SENTIDO jajaxd
            fab.setVisibility(View.GONE);
        }

        tabs.setDistributeEvenly(true);
        viewPager.setAdapter(new MyPagerAdapter(this, getSupportFragmentManager(), number));
        tabs.setViewPager(viewPager);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);

    }

    private void changeDirection() {
        if(type == IDA) {
            type = VUELTA;

            //Actualizamos el título
            getSupportActionBar().setSubtitle(getResources().getString(R.string.destination) + " " + mReturn);

        } else {
            type = IDA;

            //Actualizamos el título
            getSupportActionBar().setSubtitle(getResources().getString(R.string.destination) + " " + mGoing);
        }

        /*
        // Actualizamos
        if(stopsFragment != null)
        stopsFragment.changeDirection();

        if(schedulesFragment != null) {
            schedulesFragment.changeDirection(type == IDA ? VUELTA : IDA);
        }
        */

    }

    private void loadToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        }

        return false;
    }

    public class MyPagerAdapter extends FragmentPagerAdapter  {
        private int NUM_ITEMS = 3;

        private final Context mContext;
        private String number;

        public MyPagerAdapter(final Context context, FragmentManager fragmentManager, final String number) {
            super(fragmentManager);

            mContext = context;
            this.number = number;
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
                    if(stopsFragment == null) stopsFragment = RoutesDetailStopsFragment.newInstance(mContext, mRoute.getNumber(), type);

                    return stopsFragment;

                case SCHEDULE_TAB:
                    if(schedulesFragment == null) schedulesFragment = RouteDetailScheduleFragment.newInstance(mContext, mRoute.getNumber());

                    return schedulesFragment;

                case MAP_TAB:
                    return RouteMapFragment.newInstance(mContext, mRoute.getNumber());
            }

            return null;
        }

        // Returns the page titldee for the top indicator
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
