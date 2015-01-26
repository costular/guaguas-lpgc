package com.costular.guaguaslaspalmas.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Interpolator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnimationUtils;

import com.costular.guaguaslaspalmas.R;

/**
 * Created by Diego on 12/01/2015.
 */
public class ViewUtils {

    public static final int SCALE_FACTOR = 30;

    public static void hideViewByScale(final Context context, final View view) {


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

    public static void showViewByScale(final Context context, final View view) {

        if(view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }

        ViewPropertyAnimator propertyAnimator = view.animate().setStartDelay(SCALE_FACTOR)
                .scaleX(1)
                .setInterpolator(AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in))
                .scaleY(1);

        propertyAnimator.start();
    }
}
