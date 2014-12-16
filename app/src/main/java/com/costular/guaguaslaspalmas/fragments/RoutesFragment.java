package com.costular.guaguaslaspalmas.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.transition.Slide;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ScrollView;

import com.astuetz.PagerSlidingTabStrip;
import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.utils.PrefUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by Diego on 18/11/2014.
 */
public class RoutesFragment extends Fragment{

    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.routes_tab_layout, group, false);
        mViewPager = (ViewPager) result.findViewById(R.id.pager);
        mViewPager.setAdapter(new MyPagerAdapter(getActivity(), getChildFragmentManager()));

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);

        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        private final Context mContext;

        public MyPagerAdapter(final Context context, FragmentManager fragmentManager) {
            super(fragmentManager);

            mContext = context;
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
                case 0: // Fragment # 0 - This will show FirstFragment
                    return RoutesListFragment.newInstance(mContext, false);
                case 1:
                    return RoutesListFragment.newInstance(mContext, true);
            }

            return null;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "Todas" : "Favoritas";
        }

    }
}



