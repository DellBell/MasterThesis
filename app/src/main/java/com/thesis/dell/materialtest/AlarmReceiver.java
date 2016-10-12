package com.thesis.dell.materialtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Glandaizer on 29.11.2015.
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager mNM;
        mNM = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        // Set the icon, scrolling text and timestamp

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        Notification notification = new Notification.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_bell_mid)
                .setContentTitle("Notification Alert")
                .setContentText("Your battery capacity has reached the set value. Please recharge your battery bank to avoid damaging it!")
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .build();

//                System.currentTimeMillis());
        // The PendingIntent to launch our activity if the user selects this notification

//        context.getText(R.string.alarm_service_label), "This is a Test Alarm", contentIntent);
        //notification.defaults=Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS;

        // Set the info for the views that show in the notification panel.


        // Send the notification.
        // We use a layout id because it is a unique number. We use it later to cancel.
        mNM.notify(R.string.alarm_service_label, notification);
    }


}
