package com.costular.guaguaslaspalmas.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.widget.views.CirclePageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by diego on 6/03/15.
 */
public class TutorialFragment extends Fragment {

    private static final String TAG = "TutorialFragment";
    @Bind(R.id.view_pager) ViewPager viewPager;
    @Bind(R.id.indicator) CirclePageIndicator indicator;
    @Bind(R.id.button_next) ImageButton nextFragmentButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tutorial_root, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewPager.setAdapter(new TutorialPagerAdapter(getChildFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position)
            {
                Log.d(TAG, "posición: " + position);

                if(position < viewPager.getChildCount() -1) {
                    nextFragmentButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_chevron_right_white_36dp));
                } else {
                    nextFragmentButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_white_36dp));
                }
            }
            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                Log.d(TAG, "posición: " + position);

                if(position < viewPager.getChildCount() -1) {
                    nextFragmentButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_chevron_right_white_36dp));
                } else {
                    nextFragmentButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_white_36dp));
                }
            }
        });
        indicator.setViewPager(viewPager);

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    private class TutorialPagerAdapter extends FragmentStatePagerAdapter {

        public TutorialPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new BeginTutorialFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }

    }

    @OnClick(R.id.button_next)
    void onNextButtonCLlicked(View view) {
        int next = viewPager.getCurrentItem() + 1;

        if(next < viewPager.getChildCount()) {
            viewPager.setCurrentItem(next, true);
        }
    }

}
