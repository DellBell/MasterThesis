package com.thesis.dell.materialtest.services;


import android.support.v4.widget.SwipeRefreshLayout;

import com.thesis.dell.materialtest.pojo.Datastream;
import com.thesis.dell.materialtest.log.L;
import com.thesis.dell.materialtest.callbacks.ValuesLoadedListener;
import com.thesis.dell.materialtest.task.TaskLoadValues;

import java.util.ArrayList;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;


/**
 * Created by DellMain on 17.09.2015.
 */
public class MyService  extends JobService implements ValuesLoadedListener{
    private JobParameters jobParameters;


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //L.t(this, "onStartJob - MyService");
        this.jobParameters = jobParameters;
        //construct object of task
        new TaskLoadValues(this).execute();
        // if you're running asynctask, you need to return true to indicate that
        // the processing is taking place in the background thread.
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        //L.t(this, "onStopJob - MyService");
        return false;
    }

    public void onValuesLoaded(ArrayList<Datastream> listDatastream) {
        //L.t(this, "onValuesLoaded - MyService");
        //jobParam = rescheduling: for fex. internet connection? timer? etc.
        jobFinished(jobParameters, false);
    }
}
