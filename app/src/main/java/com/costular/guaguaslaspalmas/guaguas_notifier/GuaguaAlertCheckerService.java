package com.costular.guaguaslaspalmas.guaguas_notifier;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.costular.guaguaslaspalmas.GuaguasApp;
import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.model.Route;
import com.costular.guaguaslaspalmas.model.Stop;
import com.costular.guaguaslaspalmas.model.StopAlert;
import com.costular.guaguaslaspalmas.model.StopTime;
import com.costular.guaguaslaspalmas.utils.StopsTimeLoaderSync;
import com.costular.guaguaslaspalmas.utils.Utils;

import java.util.Iterator;
import java.util.List;

public class GuaguaAlertCheckerService extends IntentService {

    public static final String TAG = "GuaguaAlertChecker";

    public GuaguaAlertCheckerService() {
        super("GuasguasAlertChecker");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GuaguasApp app = (GuaguasApp) getApplication();

        if (!app.needCheckAlerts()) {
            stopSelf();
        }

        Iterator<StopAlert> alertIterator = app.getStopAlerts().iterator();

        while(alertIterator.hasNext()) {
            StopAlert alert = (StopAlert) alertIterator.next();

            List<StopTime> timeList = new StopsTimeLoaderSync(getApplicationContext(),
                    alert.getCode()).get();

            for (StopTime currentStop : timeList) {
                if (currentStop.getNumber().equals(alert.getNumber())) {

                    try {
                        String stopMinutesText = currentStop.getMinutes().replace("min","");
                        int stopMinutes = Integer.parseInt(stopMinutesText);

                        if (stopMinutes <= alert.getMinutes()) {
                            int finalMinutes = Math.min(alert.getMinutes(), stopMinutes);

                            Utils.notify(getApplicationContext(), getString(R.string.stop_alert_notification_title),
                                    String.format(getString(R.string.stop_alert_notification),
                                            alert.getNumber(), finalMinutes,
                                            alert.getFavoriteNameStop(this)), null);
                            alertIterator.remove();
                        }
                    } catch (NumberFormatException e ) {
                        e.printStackTrace();
                        alertIterator.remove();
                    }
                }
            }
        }

        GuaguaAlertBroadcast.completeWakefulIntent(intent);
    }
}

