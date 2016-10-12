package com.thesis.dell.materialtest.task;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.thesis.dell.materialtest.pojo.Datastream;
import com.thesis.dell.materialtest.callbacks.ValuesLoadedListener;
import com.thesis.dell.materialtest.extras.ValueUtils;
import com.thesis.dell.materialtest.network.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by DellMain on 30.09.2015.
 */
public class TaskLoadValues extends AsyncTask<Void, Void, ArrayList<Datastream>> {
    private ValuesLoadedListener myComponent;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    public TaskLoadValues(ValuesLoadedListener myComponent) {
        
        this.myComponent = myComponent;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    @Override
    protected ArrayList<Datastream> doInBackground(Void... params) {
        //storing the list from ValueUtils
        ArrayList<Datastream> listDatastream = ValueUtils.loadValues(requestQueue);
        return listDatastream;
    }

    @Override
    protected void onPostExecute(ArrayList<Datastream> listDatastreams) {
        if (myComponent != null) {
            myComponent.onValuesLoaded(listDatastreams);
        }
    }
}
