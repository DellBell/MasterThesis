package com.thesis.dell.materialtest;

import android.app.Application;
import android.content.Context;

/**
 * Created by DellMain on 11.09.2015.
 */
import android.app.Application;
import android.content.Context;

import com.thesis.dell.materialtest.database.MyDatabase;

/**
 * Created by DellMain on 04.06.2015.
 */

/**
 *Creates an application context so that the context can be used by other (classes).
 */
public class MyApplication extends Application {
    //Read
    public static final String API_KEY = "";
    //Read, Update
    //public static final String API_KEY = "";
    //CRUD
    //public static final String API_KEY = "";
    public static final String FEED_ID = "";
    private static MyApplication sInstance;

    private static MyDatabase mDatabase;

    @Override
    public void onCreate(){
        super.onCreate();
        sInstance=this;
        mDatabase = new MyDatabase(this);
    }

    public static MyApplication getsInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }

    // singleton instance that is returned here.
    // if db = null construct it, if not return it.
    public synchronized static MyDatabase getWritableDatabase() {
        if(mDatabase == null){
            mDatabase = new MyDatabase(getAppContext());
        }
        return mDatabase;
    }
}

