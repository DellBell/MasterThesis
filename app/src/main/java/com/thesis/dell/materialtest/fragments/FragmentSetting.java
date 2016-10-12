package com.thesis.dell.materialtest.fragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.thesis.dell.materialtest.MainActivity;
import com.thesis.dell.materialtest.MyApplication;
import com.thesis.dell.materialtest.R;
import com.thesis.dell.materialtest.pojo.Datastream;

import java.util.ArrayList;

/**
 * Created by Administrator on 24.03.2015.
 */
public class FragmentSetting extends Fragment {

    private static final String CHECKBOX_NOTIFICATION = "cbNotification";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private CheckBox cbNotification;
    private CheckBox cbFullyCharged;
    public FragmentSetting() {
        // Required empty public constructor
    }



    private ArrayList<Datastream> listDatastream = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_setting,container,false);

        cbNotification = (CheckBox) v.findViewById(R.id.cbNotificationIntent);
        cbFullyCharged = (CheckBox) v.findViewById(R.id.cbNotificationFullCharged);
        preferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        boolean boolNotification = preferences.getBoolean(CHECKBOX_NOTIFICATION, false);

        cbNotification.setChecked(boolNotification);

        cbNotification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(cbNotification.isChecked()){
                    editor.putBoolean(CHECKBOX_NOTIFICATION, true);
                    editor.apply();
                }else {
                    editor.remove(CHECKBOX_NOTIFICATION);
                    editor.apply();
                }
            }
        });

        return v;
    }





}
