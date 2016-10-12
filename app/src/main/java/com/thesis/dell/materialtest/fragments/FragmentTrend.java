package com.thesis.dell.materialtest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.thesis.dell.materialtest.MyApplication;
import com.thesis.dell.materialtest.R;
import com.thesis.dell.materialtest.log.L;
import com.thesis.dell.materialtest.pojo.Datastream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 24.03.2015.
 */
public class FragmentTrend extends Fragment /*implements SwipeRefreshLayout.OnRefreshListener*/ {

    private static final String STATE_DATASTREAM_TREND = "state_datastream_trend";
    private ArrayList<Datastream> listDatastream = new ArrayList<>();
    private View view;
    private LineGraphSeries<DataPoint> mVolt;
    private int count;


    public FragmentTrend() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //@param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * //@return A new instance of fragment FragmentTrend.
     **/

    // TODO: Rename and change types and number of parameters
    public static FragmentTrend newInstance(String param1, String param2) {
        FragmentTrend fragment = new FragmentTrend();
        Bundle args = new Bundle();
        //put any extra arguments that you may want to supply to this fragment
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_trend, container, false);

        //L.m("onCreateView");
        //setupSwipe();
        //printAllValuesInDatastream();


        if (savedInstanceState != null) {
            //if this fragment starts after a rotation or configuration change,
            // load the existing data from a parcelable
            //L.t(getActivity(), "savedInstanceState not null");
            listDatastream = savedInstanceState.getParcelableArrayList(STATE_DATASTREAM_TREND);
        } else {
            //if this fragment starts for the first time, load the list of data from a database
            //get a reference to our DB running on our main thread
            //L.t(getActivity(), "savedInstanceState is null");
            listDatastream = MyApplication.getWritableDatabase().readDatastreams();
        }
        if (listDatastream != null && !listDatastream.isEmpty()) {
            drawGraphVolt();
        }

        //getCalendar();
        return view;
    }

    //Sun Nov 01 01:51:48 GMT+01:00 2015
    public void getCalendar() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        System.out.println("Date format: " + date);
        System.out.println("Date format List: " + listDatastream.get(0).getCurrent_valueAt());
    }

    public void printAllValuesInDatastream() {

//        // The Iterator object is obtained using iterator() method
//        Iterator it = listDatastream.iterator();
//        // To iterate through the elements of the collection we can use hasNext() and next() methods of Iterator
//        while (it.hasNext()) {
//            L.m("Elements in the iterator: " + it.next());
//        }


        for (int i = 0; i < getNumberOfElementsInDatabase(); i++) {
            L.m("List elements in Datastream: " +
                    listDatastream.get(i).getCurrent_value().toString() + " at pos: " + i);
        }
    }

    public void drawGraphVolt() {
        // Setting up and drawing volt graph
        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        mVolt = new LineGraphSeries<>(generateData());
        graph.addSeries(mVolt);

        // styling grid/labels
        graph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.colorGraphGrid));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.colorGraphText));
        graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.colorGraphText));
        graph.getGridLabelRenderer().setTextSize(45);
        graph.getGridLabelRenderer().reloadStyles();

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yy");
        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    long valueMillis = (new Double(value)).longValue();
                    return simpleDateFormat.format(valueMillis);
                } else {
                    // show percent for y values
                    return super.formatLabel(value, isValueX) + "%";
                }
            }

        });
        graph.getGridLabelRenderer().setNumHorizontalLabels(2); // only 2 because of the space


        // styling series
        mVolt.setTitle("Capacity");
        mVolt.setColor(getResources().getColor(R.color.colorAccent));
        mVolt.setThickness(4);
        //series.setDrawBackground(true);
        //series.setBackgroundColor(getResources().getColor(R.color.gridGraphColor));

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        graph.getLegendRenderer().setTextSize(45);
        graph.getLegendRenderer().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        graph.getLegendRenderer().setTextColor(getResources().getColor(R.color.colorPrimaryText));
        graph.getLegendRenderer().setPadding(15);
        graph.getLegendRenderer().setMargin(0);


        //Set Y-axis
        graph.getViewport().setYAxisBoundsManual(true);
//        graph.getViewport().setMinY((int)graph.getViewport().getMinY(false));
//        graph.getViewport().setMaxY((int)graph.getViewport().getMaxY(false));
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);

        //Set X-axis
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(graph.getViewport().getMinX(false));
        graph.getViewport().setMaxX(graph.getViewport().getMaxX(false));
        // set manual x bounds to have nice steps
//        graph.getViewport().setMinX(listDatastream.get());
//        graph.getViewport().setMaxX(d3.getTime());

        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
    }

    //generates data
    private DataPoint[] generateData() {

        listDatastream = MyApplication.getWritableDatabase().readDatastreams();

        count = listDatastream.size();
        DataPoint[] values = new DataPoint[count];
        for (int i = 0; i < count; i++) {

            //int x = i;
            Date x = listDatastream.get(i).getCurrent_valueAt();
            int y = Integer.parseInt(listDatastream.get(i).getCurrent_value());
            DataPoint v = new DataPoint(x.getTime(), y);
            values[i] = v;
        }
        return values;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_DATASTREAM_TREND, listDatastream);
    }

    public int getNumberOfElementsInDatabase() {
        if (MyApplication.getWritableDatabase().getTotalNumberOfElements() == -1) {
            return 0;
            // TO DO: check if db is empty or not
        }
        int countListDatastream = MyApplication.getWritableDatabase().getTotalNumberOfElements();
        return countListDatastream;
    }
}

