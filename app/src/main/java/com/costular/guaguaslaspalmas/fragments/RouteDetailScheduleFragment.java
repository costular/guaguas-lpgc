package com.costular.guaguaslaspalmas.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.costular.guaguaslaspalmas.R;
import com.costular.guaguaslaspalmas.RouteDetailActivity;
import com.costular.guaguaslaspalmas.events.RouteDirection;
import com.costular.guaguaslaspalmas.model.Route;
import com.costular.guaguaslaspalmas.utils.DatabaseHelper;

import de.greenrobot.event.EventBus;

/**
 * Created by Diego on 23/11/2014.
 */
public class RouteDetailScheduleFragment extends Fragment{

    TextView week;
    TextView weekend;
    TextView weekendcontent;
    TextView saturday;
    TextView saturdayContent;

    private String number;
    private boolean hasSaturday = false;

    private RouteDetailActivity activity;

    public static RouteDetailScheduleFragment newInstance(final Context context, final String number) {

        Bundle bundle = new Bundle();
        bundle.putString("number", number);

        Fragment fragment = Fragment.instantiate(context, RouteDetailScheduleFragment.class.getName(), bundle);
        return (RouteDetailScheduleFragment) fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = (RouteDetailActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_detail_schedule, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        week = (TextView) getActivity().findViewById(R.id.week_content);
        weekend = (TextView) getActivity().findViewById(R.id.weekend);
        weekendcontent = (TextView) getActivity().findViewById(R.id.weekend_content);
        saturday = (TextView) getActivity().findViewById(R.id.saturday);
        saturdayContent = (TextView) getActivity().findViewById(R.id.saturday_content);

        number = getArguments().getString("number");

        // Comprobamos si los sábados tiene un horario diferente.
        checkSaturday();

        if(hasSaturday) {
            saturday.setVisibility(View.VISIBLE);
            saturdayContent.setVisibility(View.VISIBLE);

            weekend.setText("Domingos y festivos.");
        }

        new ScheduleLoader(getActivity(), week, saturdayContent, weekendcontent, Route.getConcesionFromRouteNumber(getActivity(), number, activity.type)).execute();

        //Registramos
        EventBus.getDefault().register(this);
    }

    private void checkSaturday() {
        DatabaseHelper helper = DatabaseHelper.getInstance(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT schedule_saturday FROM schedules WHERE route_id = '"+Route.getConcesionFromRouteNumber(getActivity(), number, activity.type)+"'", null);

        String saturday = "";

        if(c.moveToFirst()) {

           saturday = c.getString(0);
        }

        // Dejamos de usarlo
        c.close();

        // Comprobamos
        if(saturday == null) {
            hasSaturday = false;
        } else {
            hasSaturday = true;
        }
    }

    /*
     * Cambiamos la dirección de la línea
     */
    public void onEvent(RouteDirection type) {
        new ScheduleLoader(getActivity(), week, saturdayContent, weekendcontent, Route.getConcesionFromRouteNumber(getActivity(), number, type.getDirection())).execute();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

   class ScheduleLoader extends AsyncTask<Void, Void, String[]> {

       private Context mContext;

       private TextView week, saturday, weekend;

       private int mId;

       public ScheduleLoader(Context context, TextView week, TextView saturday, TextView weekend, int id) {
           mContext = context;

           this.week = week;
           this.saturday = saturday;
           this.weekend = weekend;

           this.mId = id;
       }

       @Override
       protected String[] doInBackground(Void... params) {

           DatabaseHelper helper = DatabaseHelper.getInstance(mContext);
           SQLiteDatabase db = helper.getReadableDatabase();

           Cursor cursor = db.rawQuery("SELECT * FROM schedules WHERE route_id = ?", new String[] {String.valueOf(mId)});

           String[] schedules = new String[3];

           if(cursor.moveToFirst()) {
               schedules[0] = cursor.getString(cursor.getColumnIndex("schedule_week"));
               schedules[1] = cursor.getString(cursor.getColumnIndex("schedule_saturday"));
               schedules[2] = cursor.getString(cursor.getColumnIndex("schedule_weekend"));
           }

           cursor.close();

           return schedules;
       }

       @Override
       protected void onPostExecute(String[] result) {
           super.onPostExecute(result);

           week.setText(result[0]);
           weekend.setText(result[2]);

           if(hasSaturday) {
               saturday.setText(result[1]);
           }
       }

   }
}
