package com.thesis.dell.materialtest.network;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.thesis.dell.materialtest.MyApplication;

/**
 * Created by DellMain on 14.09.2015.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;

    public VolleySingleton() {
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
    }

    public static VolleySingleton getInstance() {

        if (sInstance == null) {
            sInstance = new VolleySingleton();
        }
        return sInstance;

    }

    //create a method that will return our request queue so that others can use it.
    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
}
