package com.thesis.dell.materialtest.callbacks;

import com.thesis.dell.materialtest.pojo.Datastream;

import java.util.ArrayList;

/**
 * Created by DellMain on 30.09.2015.
 */
public interface ValuesLoadedListener {
    public void onValuesLoaded(ArrayList<Datastream> listDatastream);
}
