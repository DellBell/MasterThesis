package com.thesis.dell.materialtest.fragments;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.NotificationCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.thesis.dell.materialtest.MainActivity;
import com.thesis.dell.materialtest.callbacks.ValuesLoadedListener;
import com.thesis.dell.materialtest.pojo.Datastream;
import com.thesis.dell.materialtest.log.L;
import com.thesis.dell.materialtest.MyApplication;
import com.thesis.dell.materialtest.R;
import com.thesis.dell.materialtest.services.AlarmService;
import com.thesis.dell.materialtest.task.TaskLoadValues;

import java.util.ArrayList;
import java.util.Random;

import pl.pawelkleczkowski.customgauge.CustomGauge;

/**
 * Created by Administrator on 24.03.2015.
 */

public class FragmentHome extends Fragment implements ValuesLoadedListener, SwipeRefreshLayout.OnRefreshListener {

    //The key used to store arraylist of movie objects to and from parcelable
    private static final String STATE_DATASTREAM = "state_datastream";
    public final static String DATA_RECEIVE = "data_receive";
    private static final int TIMER_ON_REFRESH_MILLIS = 3000;
    private static final int TIMER_ON_REFRESH_END = 100;
    //the arraylist containing our list of values and dates
    private ArrayList<Datastream> listDatastream = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private CustomGauge gauge;
    private CustomGauge outerRim;
    private CustomGauge innerRim;
    private TextView percentNumTextView;
    private TextView percentSymbolTextView;
    private TextView mTextError;
    private static final int notificationID = 1337;
    public boolean swipeRefresh = false;
    private AlarmService alarmService;

    private static final String CHECKBOX_NOTIFICATION = "cbNotification";

    private SharedPreferences preferences;
    private int globalValue;


    public FragmentHome() {
        // Required empty public constructor
    }

//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     //* @param param1 Parameter 1.
//     //* @param param2 Parameter 2.
//     * @return A new instance of fragment FragmentHome.
//     **/
//
//    // TODO: Rename and change types and number of parameters
//    public static FragmentHome newInstance(String param1, String param2) {
//        FragmentHome fragment = new FragmentHome();
//        Bundle args = new Bundle();
//        //put any extra arguments that you may want to supply to this fragment
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_home,container,false);

        alarmService = new AlarmService(getContext());
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refreshHome);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        gauge = (CustomGauge) v.findViewById(R.id.gauge);
        outerRim = (CustomGauge) v.findViewById(R.id.outerRim);
        innerRim = (CustomGauge) v.findViewById(R.id.outerRim);
        mTextError = (TextView) v.findViewById(R.id.textVolleyError);
        percentNumTextView = (TextView) v.findViewById(R.id.percentNumTextView);
        percentSymbolTextView = (TextView) v.findViewById(R.id.percentTextView);
        percentNumTextView.setText(Integer.toString(gauge.getValue()));
        preferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        int myValue = preferences.getInt("CapacityAlarmValue", -1);

        globalValue = myValue;

        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change,
            // load the existing data from a parcelable
            //L.t(getActivity(), "savedInstanceState not null");

            listDatastream = savedInstanceState.getParcelableArrayList(STATE_DATASTREAM);

        }
        else {
            //if this fragment starts for the first time, load the list of data from a database
            //get a reference to our DB running on our main thread
            //L.t(getActivity(), "savedInstanceState is null");
            listDatastream = MyApplication.getWritableDatabase().readDatastreams();
            //if the database is empty, trigger an AsycnTask to download data list from the web
            if (listDatastream.isEmpty()) {
                //L.t(getActivity(), "executing task from FragementHome");
                new TaskLoadValues(this).execute();
            }
        }
        if (listDatastream != null && !listDatastream.isEmpty()) {
            String strValue = MyApplication.getWritableDatabase().getColumnValueLast();
            //String strValue = listDatastream.get(0).getCurrent_value().toString();
            int value = Integer.parseInt(strValue);
            gauge.setValue(value);
            percentNumTextView.setText(strValue);
            L.m("Showing listDatastream after creation: "
                    + listDatastream.get(0).getCurrent_value().toString());

            if (value <= 100) {
                percentNumTextView.setTextColor(Color.GREEN);
                percentSymbolTextView.setTextColor(Color.GREEN);
            }
            if (value < 90) {
                percentNumTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                percentSymbolTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            if (value < 60) {
                percentNumTextView.setTextColor(getResources().getColor(R.color.colorRed));
                percentSymbolTextView.setTextColor(getResources().getColor(R.color.colorRed));
            }

            if (value <= myValue) {

                Boolean boolNotification = preferences.getBoolean(CHECKBOX_NOTIFICATION, false);
                if (boolNotification) {
                    //setupNotification();
                    //setupAlarm();
                    alarmService.startAlarm();
                }

            }
        }

        return v;
    }

//    public void home_updateText(int t){
//        b_received = t;
//        L.m("Message from Alarm: " + b_received);
//    }

//    public void setMessage(String msg) {
//        L.m("message from alarm: " + msg);
//    }

    @Override
    public void onPause() {
        super.onPause();
        //mTimerOnRefresh.cancel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(STATE_DATASTREAM, listDatastream);

    }

    public void onResume(){
        super.onResume();

//        if (listDatastream.size() < getNumberOfElementsInDatabase()) {
//            listDatastream = MyApplication.getWritableDatabase().readDatastreams();
//        }
    }
    public int getNumberOfElementsInDatabase() {
        if (MyApplication.getWritableDatabase().getTotalNumberOfElements() == -1) {
            return 0;
            // TO DO: check if db is empty or not
        }
        int countListDatastream = MyApplication.getWritableDatabase().getTotalNumberOfElements();
        return countListDatastream;
    }

    //how to handle this?
    private void handleVolleyError(VolleyError error) {
        //if any error occurs in the network operations, show the TextView that contains the error message
        mTextError.setVisibility(View.VISIBLE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            mTextError.setText(R.string.error_timeout);

        } else if (error instanceof AuthFailureError) {
            mTextError.setText(R.string.error_auth_failure);
            //TODO
        } else if (error instanceof ServerError) {
            mTextError.setText(R.string.error_auth_failure);
            //TODO
        } else if (error instanceof NetworkError) {
            mTextError.setText(R.string.error_network);
            //TODO
        } else if (error instanceof ParseError) {
            mTextError.setText(R.string.error_parser);
            //TODO
        }
    }

    public void setupNotification(){

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setSmallIcon(R.drawable.ic_bell_mid);
        mBuilder.setContentTitle("Notification Alert");
        mBuilder.setContentText("Hi, This is Android Notification Detail!");
        mBuilder.setSound(soundUri);

        Intent resultIntent = new Intent(getActivity(), MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);


        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    public void setupAlarm() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mp = MediaPlayer.create(getContext(), notification);
        mp.setLooping(true);
        mp.start();

//
//        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
//        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        //      alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public void onValuesLoaded(ArrayList<Datastream> listDatastream) {
        //L.t(getActivity(), "onValuesLoaded - FragmentHome");
        swipeRefresh = true;
        listDatastream = MyApplication.getWritableDatabase().readDatastreams();
        L.m("size of listData " + listDatastream.size());
        L.m("size of inDB " + getNumberOfElementsInDatabase());
        //create adapter here to show values on UI and set listDatastream as parameter.
        //this method call will load the first item in the database at index 0

        //Get last/updated value in the DB
        int updatedValue = Integer.parseInt(MyApplication.getWritableDatabase().getColumnValueLast());
        String strValue = MyApplication.getWritableDatabase().getColumnValueLast();
        L.m("Last value in listDatastream " + updatedValue);
        L.m("size of inDB " + strValue);
        gauge.setValue(updatedValue);
        percentNumTextView.setText(strValue);
        if (updatedValue <= 100) {
            percentNumTextView.setTextColor(Color.GREEN);
            percentSymbolTextView.setTextColor(Color.GREEN);
        }
        if (updatedValue < 90) {
            percentNumTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            percentSymbolTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        if (updatedValue < 60) {
            percentNumTextView.setTextColor(getResources().getColor(R.color.colorRed));
            percentSymbolTextView.setTextColor(getResources().getColor(R.color.colorRed));
        }

        L.m("AlarmValue: " + globalValue);

        if (updatedValue <= globalValue) {

            Boolean boolNotification = preferences.getBoolean(CHECKBOX_NOTIFICATION, false);
            if (boolNotification) {
                //setupNotification();
                //setupAlarm();
                alarmService.startAlarm();
            }

        }

        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            swipeRefresh = false;
        }
    }

    @Override
    public void onRefresh() {
        //L.t(getActivity(), "OnRefresh - Home");
        new TaskLoadValues(this).execute();

        //L.m("Last value in DB: " + MyApplication.getWritableDatabase().getColumnValueLast());
        //L.m("Testing datastream " + listDatastream.get(Integer.parseInt(MyApplication.getWritableDatabase().getColumnValueLast())).getCurrent_value());
        //MyApplication.getWritableDatabase().getColumnValueLast()
    }

/*    public void setupSwipe() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refreshHome);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }*/
}


