package com.costular.guaguaslaspalmas.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.costular.guaguaslaspalmas.GuaguasApp;
import com.costular.guaguaslaspalmas.model.Route;
import com.costular.guaguaslaspalmas.model.StopAlert;
import com.costular.guaguaslaspalmas.model.StopTime;
import com.costular.guaguaslaspalmas.utils.StopsTimeLoaderSync;
import com.costular.guaguaslaspalmas.utils.Utils;

import java.util.List;

public class GuaguaAlertChecker extends IntentService{

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startAction(Context context) {
        Intent intent = new Intent(context, GuaguaAlertChecker.class);
        context.startService(intent);
    }

    public GuaguaAlertChecker() {
        super("GuasguasAlertChecker");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GuaguasApp app = (GuaguasApp) getApplication();

        if(app.needCheckAlerts()) {

            for(StopAlert alert : app.getStopAlerts()) {

                List<StopTime> timeList = new StopsTimeLoaderSync(getApplicationContext(), alert.getCode()).get();
                for(StopTime currentStop : timeList) {

                    Route route = Route.createRouteFromNumber(getApplicationContext(), currentStop.getNumber());

                    if(route.getId() == alert.getRouteId()) {
                        Utils.notify(getApplicationContext(), "Guagua", "Quedan " + alert.getMinutes() + " minutos para que llegue la guagua", null);
                    }
                }
            }

        } else {
            stopSelf();
        }
    }
}

