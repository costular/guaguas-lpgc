package com.costular.guaguaslaspalmas.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.costular.guaguaslaspalmas.R;

/**
 * Created by diego on 6/03/15.
 */
public class TutorialActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.main_blue_dark));

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new TutorialFragment()).commit();
    }
}
