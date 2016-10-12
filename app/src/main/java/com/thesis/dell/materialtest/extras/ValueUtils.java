package com.thesis.dell.materialtest.extras;



import com.android.volley.RequestQueue;
import com.thesis.dell.materialtest.log.L;
import com.thesis.dell.materialtest.pojo.Datastream;
import com.thesis.dell.materialtest.MyApplication;
import com.thesis.dell.materialtest.json.Endpoints;
import com.thesis.dell.materialtest.json.Parser;
import com.thesis.dell.materialtest.json.Requestor;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DellMain on 30.09.2015.
 */
public class ValueUtils {
    public static ArrayList<Datastream> loadValues(RequestQueue requestQueue) {
        //the send
        JSONObject response = Requestor.requestValuesJSON(requestQueue, Endpoints.getRequestUrlValues());
        //the parse
        ArrayList<Datastream> listDatastream = Parser.parseValuesJSON(response);
        //L.m("Before inserting the List into db: " + listDatastream.get(0).getCurrent_value());
        //the insert
        MyApplication.getWritableDatabase().insertValues(listDatastream, false); // false to append data
        return listDatastream;
    }
}
