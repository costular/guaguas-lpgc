package com.costular.guaguaslaspalmas.fragments;

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
import com.costular.guaguaslaspalmas.model.Route;
import com.costular.guaguaslaspalmas.utils.DatabaseHelper;

/**
 * Created by Diego on 23/11/2014.
 */
public class RouteDetailScheduleFragment extends Fragment{


    public static RouteDetailScheduleFragment newInstance(final Context context, final int id) {

        Bundle bundle = new Bundle();
        bundle.putInt("id", id);

        Fragment fragment = Fragment.instantiate(context, RouteDetailScheduleFragment.class.getName(), bundle);
        return (RouteDetailScheduleFragment) fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_detail_schedule, null);
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView week = (TextView) getActivity().findViewById(R.id.week);
        TextView weekend = (TextView) getActivity().findViewById(R.id.weekend);

        new ScheduleLoader(getActivity(), week, weekend, Route.getConcesionFromRouteNumber(getActivity(), getArguments().getInt("id"))).execute();
    }

    public void changeDirection() {

    }

   class ScheduleLoader extends AsyncTask<Void, Void, String[]> {

       private Context mContext;

       private TextView week, weekend;

       private int mId;

       public ScheduleLoader(Context context, TextView week, TextView weekend, int id) {
           mContext = context;

           this.week = week;
           this.weekend = weekend;

           this.mId = id;
       }

       @Override
       protected String[] doInBackground(Void... params) {

           DatabaseHelper helper = DatabaseHelper.getInstance(mContext);
           SQLiteDatabase db = helper.getReadableDatabase();

           Cursor cursor = db.rawQuery("SELECT * FROM schedules WHERE route_id = ?", new String[] {String.valueOf(mId)});

           String[] schedules = new String[2];

           if(cursor.moveToFirst()) {
               schedules[0] = cursor.getString(cursor.getColumnIndex("schedule_week"));
               schedules[1] = cursor.getString(cursor.getColumnIndex("schedule_weekend"));
           }

           cursor.close();

           return schedules;
       }

       @Override
       protected void onPostExecute(String[] result) {
           super.onPostExecute(result);

           week.setText(result[0]);
           weekend.setText(result[1]);
       }

   }
}
