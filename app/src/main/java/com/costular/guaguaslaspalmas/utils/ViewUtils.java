package com.costular.guaguaslaspalmas.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Interpolator;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.costular.guaguaslaspalmas.R;

/**
 * Created by Diego on 12/01/2015.
 */
public class ViewUtils {

    public static final int SCALE_FACTOR = 30;

        public static boolean hitTest(View v, int x, int y) {
            final int tx = (int) (ViewCompat.getTranslationX(v) + 0.5f);
            final int ty = (int) (ViewCompat.getTranslationY(v) + 0.5f);
            final int left = v.getLeft() + tx;
            final int right = v.getRight() + tx;
            final int top = v.getTop() + ty;
            final int bottom = v.getBottom() + ty;

            return (x >= left) && (x <= right) && (y >= top) && (y <= bottom);
        }

    public static View findParentViewHolderItemView(View v) {
        final ViewParent parent = v.getParent();
        if (parent instanceof RecyclerView) {
            // returns the passed instance if the parent is RecyclerView
            return v;
        } else if (parent instanceof View) {
            // check the parent view recursively
            return findParentViewHolderItemView((View) parent);
        } else {
            return null;
        }
    }

    public static void hideViewByScale(final Context context, final View view) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // get the center for the clipping circle
            int cx = (view.getLeft() + view.getRight()) / 2;
            int cy = (view.getTop() + view.getBottom()) / 2;

            // get the initial radius for the clipping circle
            int initialRadius = view.getWidth();

            // create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });

            // start the animation
            anim.start();

        } else {
            ViewPropertyAnimator propertyAnimator = view.animate().setStartDelay(SCALE_FACTOR)
                    .scaleX(0)
                    .scaleY(0)
                    .setInterpolator(AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if(view.getVisibility() == View.VISIBLE) {
                                view.setVisibility(View.GONE);
                            }

                        }
                    });

            propertyAnimator.start();
        }

    }

    public static void showViewByScale(final Context context, final View view) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = (view.getLeft() + view.getRight()) / 2;
            int cy = (view.getTop() + view.getBottom()) / 2;

            // get the final radius for the clipping circle
            int finalRadius = Math.max(view.getWidth(), view.getHeight());

            // create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

            // make the view visible and start the animation
            view.setVisibility(View.VISIBLE);
            anim.start();

        } else {
            ViewPropertyAnimator propertyAnimator = view.animate().setStartDelay(SCALE_FACTOR)
                    .scaleX(1)
                    .setInterpolator(AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in))
                    .scaleY(1);

            view.setVisibility(View.VISIBLE);
            propertyAnimator.start();
        }

    }

    public static View getViewOfListViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
