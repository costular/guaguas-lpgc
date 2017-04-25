package com.costular.guaguaslaspalmas;

import android.app.Application;

import com.costular.guaguaslaspalmas.model.StopAlert;
import com.costular.guaguaslaspalmas.model.StopTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diego on 26/05/15.
 */
public class GuaguasApp extends Application {

    private boolean needReloadTheme;
    private List<StopAlert> mStopAlertList;

    public List<StopAlert> getStopAlerts() {
        return mStopAlertList;
    }

    public boolean needCheckAlerts() {
        return mStopAlertList.size() > 0;
    }

    public void removeCheckAlert(StopAlert stopAlert) {
        mStopAlertList.remove(stopAlert);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mStopAlertList = new ArrayList<>();
        needReloadTheme = false;
    }

    protected void addAlert(StopAlert stopAlert) {
        mStopAlertList.add(stopAlert);
    }

    protected void deleteAlert(int id) {

    }

    public void needReloadTheme() {
        needReloadTheme = true;
    }

    public void notNeedReloadTheme() {
        needReloadTheme = false;
    }

    public boolean isNeededReloadTheme() {
        return needReloadTheme;
    }
}
