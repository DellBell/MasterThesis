package com.thesis.dell.materialtest.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.thesis.dell.materialtest.MainActivity;
import com.thesis.dell.materialtest.MyApplication;
import com.thesis.dell.materialtest.R;
import com.thesis.dell.materialtest.callbacks.OnButtonPressListener;
import com.thesis.dell.materialtest.log.L;

/**
 * Created by Administrator on 24.03.2015.
 */
public class FragmentAlarm extends Fragment implements SeekBar.OnSeekBarChangeListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String CAPACITY_ALARM_VALUE = "CapacityAlarmValue";




    private SeekBar alarmSeekBar;
    private TextView alarmTextView;
    private ToggleButton tglButton;
    private int alarmValue;
    private boolean alarmStatus = false;
    public MainActivity mActivity;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public FragmentAlarm() {
        // Required empty public constructor
    }

//    public interface DataPassListener{
//        public void passData(String data);
//    }
//    DataPassListener mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try{
//            mCallback = (DataPassListener)activity;
//        }catch (ClassCastException e){
//            throw new ClassCastException(activity.toString() + " must implement DataPassListener");
//        }

    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment FragmentAlarm.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static FragmentAlarm newInstance(String param1, String param2) {
//        FragmentAlarm fragment = new FragmentAlarm();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        //put any extra arguments that you may want to supply to this fragment
//        fragment.setArguments(args);
//        return fragment;
//    }

//    OnButtonPressListener buttonListener;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_alarm,container,false);



        tglButton = (ToggleButton) v.findViewById(R.id.toggleButton);
        alarmTextView = (TextView)v.findViewById(R.id.textViewAlarmValue);
        alarmSeekBar = (SeekBar) v.findViewById(R.id.alarmSeekBar);
        alarmSeekBar.setOnSeekBarChangeListener(this);
        preferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();
        int myValue = preferences.getInt(CAPACITY_ALARM_VALUE, -1);

        if (myValue != -1) {
            alarmValue = myValue;
            alarmTextView.setText("" + alarmValue);
            alarmSeekBar.setProgress(alarmValue);
            tglButton.setChecked(true);
        }
        tglButton.setOnCheckedChangeListener(toggleButtonChangeListener);
        //tglButton.setOnClickListener(toggleButtonListener);

//        tglButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//            }
//        });


        return v;
    }

/*    OnClickListener toggleButtonListener = new OnClickListener(){

        @Override
        public void onClick(View v) {
            //buttonListener.onButtonPressed("testMessage");

//            mCallback.passData("testMessage");
//
//            Fragment fragment = new FragmentAlarm();
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            FragmentTransaction ft = fragmentManager.beginTransaction();
//            ft.replace(R.id.refreshHome, fragment);
//            ft.addToBackStack(null);
//            Bundle args = new Bundle();
//            // Pass the values what you want to send to next fragment
//            int value = 10;
//            args.putInt("Value", value);
//            fragment.setArguments(args);
//            ft.commit();

//            String newText = mActivity.fragmentAlarm.alarmTextView.getText().toString();
//            L.m("newText: " + newText);



        }
    };*/





    final CompoundButton.OnCheckedChangeListener toggleButtonChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {
                alarmStatus = true;

                editor.putInt(CAPACITY_ALARM_VALUE, alarmValue);
                editor.apply();


            } else {
                alarmStatus = false;
                editor.remove(CAPACITY_ALARM_VALUE);
                editor.apply();
            }
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        alarmValue = progress;
        int myValue = preferences.getInt(CAPACITY_ALARM_VALUE, -1);
        alarmTextView.setText("" + alarmValue);
        if (myValue != -1) {
            editor.putInt(CAPACITY_ALARM_VALUE, alarmValue);
            editor.apply();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
