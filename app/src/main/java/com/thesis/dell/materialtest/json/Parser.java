package com.thesis.dell.materialtest.json;

import com.thesis.dell.materialtest.pojo.Datastream;
import com.thesis.dell.materialtest.log.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static com.thesis.dell.materialtest.extras.Constants.NA;
import static com.thesis.dell.materialtest.extras.Keys.EndpointDatastream.*;

/**
 * Created by DellMain on 15.09.2015.
 */
public class Parser {

    public static ArrayList<Datastream> parseValuesJSON(JSONObject response) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        ArrayList<Datastream> listDatastream = new ArrayList<>();
        if (response != null && response.length() > 0) {
            try {
                JSONArray arrayDatastream = response.getJSONArray(KEY_DATASTREAMS);
                for (int i = 0; i < arrayDatastream.length(); i++) {
                    //default values inside the loop to reset it after each round.
                    String value = NA;
                    String dateAt = NA;

                    JSONObject currentData = arrayDatastream.getJSONObject(i);

                    //get the value of current_value in datastreams
                    if (Utils.contains(currentData, KEY_CURRENT_VALUE)) {
                        value = currentData.getString(KEY_CURRENT_VALUE);
                    }
                    //get the date in datastreams for current_value
                    if (Utils.contains(currentData, KEY_CURRENT_VALUE_AT)) {
                        dateAt = currentData.getString(KEY_CURRENT_VALUE_AT);
                    }

                    //setting values to data
                    Datastream data = new Datastream();
                    data.setCurrent_value(value);
                    //try parse the json date string
                    Date date = dateFormat.parse(dateAt);

                    data.setCurrent_valueAt(date);

                    //adding data to array list listDatastream
                    if (!value.equals(NA)) {
                        listDatastream.add(data);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                L.m("Parsing date error: " +e + "\n");
                e.printStackTrace();
            }
        }

//        L.m("List contents: " + listDatastream.get(0).getCurrent_value() + " " + listDatastream.get(0).getCurrent_valueAt());
        return listDatastream;
    }
}
