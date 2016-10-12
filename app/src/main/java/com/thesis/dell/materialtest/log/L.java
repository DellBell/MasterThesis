package com.thesis.dell.materialtest.log;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by DellMain on 14.09.2015.
 */
public class L {
    public static void m(String message) {
        Log.d("DELLY", "" + message);
    }
    public static void t(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
