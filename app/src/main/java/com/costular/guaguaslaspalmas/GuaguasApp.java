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

    private List<StopAlert> mStopAlertList;

    public List<StopAlert> getStopAlerts() {
        return mStopAlertList;
    }

    public boolean needCheckAlerts() {
        return mStopAlertList.size() > 0;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mStopAlertList = new ArrayList<>();
    }

    protected void addAlert(StopAlert stopAlert) {
        mStopAlertList.add(stopAlert);
    }

    protected void deleteAlert(int id) {

    }
}
