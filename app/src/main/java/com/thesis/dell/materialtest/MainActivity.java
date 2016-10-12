package com.thesis.dell.materialtest;


import android.content.ComponentName;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.thesis.dell.materialtest.adapter.MyViewPagerAdapter;
import com.thesis.dell.materialtest.callbacks.OnButtonPressListener;
import com.thesis.dell.materialtest.fragments.FragmentAlarm;
import com.thesis.dell.materialtest.fragments.FragmentHome;
import com.thesis.dell.materialtest.log.L;
import com.thesis.dell.materialtest.services.MyService;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;


public class MainActivity extends ActionBarActivity implements MaterialTabListener /**, View.OnClickListener*//**, FragmentAlarm.DataPassListener*/ /**, OnButtonPressListener*/
{
    private static final int JOB_ID = 100;
    //Run the JobSchedulerService every x mins/seconds
    private static final long POLL_FREQUENCY_MILLIS = 1000 * 60 * 8;
    private MyViewPagerAdapter myAdapter;
    private MaterialTabHost tabHost;
    private NonSwipeableViewPager nonSwipeableViewPager;
    private JobScheduler mJobScheduler;

    public FragmentAlarm fragmentAlarm;
//    String TabFragmentB;
//
//    public void setTabFragmentHome(String t){
//        TabFragmentB = t;
//    }
//
//    public String getTabFragmentHome(){
//        return TabFragmentB;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init
//        fragmentAlarm = new FragmentAlarm();
//        FragmentManager fm = getSupportFragmentManager();
//        fragmentAlarm = (FragmentAlarm) fm.findFragmentByTag("FragmentAlarm");
//        fragmentAlarm.mActivity = this;

        methodAdapter();
        setupJob();



    }




    private void setupJob() {
        mJobScheduler = JobScheduler.getInstance(this);
        // set an initial delay with a Handler so that the data loading by the JobScheduler
        // does not clash with the loading inside the Fragment
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                constructJob();
            }
        }, 30000);
    }

    private void constructJob(){
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, MyService.class));
        //Use PersistableBundle pst = new PersitableBundle() to pass any extra parameters between
        //activities or screen rotations. Use .setExtras() after with .setPeriodic();
        builder.setPeriodic(POLL_FREQUENCY_MILLIS)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)//NO GSM OR MOBILE DATA!
                .setPersisted(true);
                //.setExtras();

        mJobScheduler.schedule(builder.build());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void methodAdapter(){

        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);

        nonSwipeableViewPager = (NonSwipeableViewPager) findViewById(R.id.viewPager);

        //Calling MyViewPagerAdapter with context.
        myAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), MainActivity.this);

        nonSwipeableViewPager.setAdapter(myAdapter);

        nonSwipeableViewPager.setOnPageChangeListener(new NonSwipeableViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });

        //Add all the Tabs to the TabHost
        for (int i = 0; i < myAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setIcon(myAdapter.getIcon(i)).setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.t(this, "onResume() activity");
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        nonSwipeableViewPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

//    @Override
//    public void onClick(View v) {
//        Fragment fragment = (Fragment) myAdapter.instantiateItem(nonSwipeableViewPager, nonSwipeableViewPager.getCurrentItem());
//        Bundle args = new Bundle();
//        args.putString(FragmentHome.DATA_RECEIVE, data);
//        fragment .setArguments(args);
//    }

//    public final static String DATA_RECEIVE = "test";
//
//    @Override
//    public void passData(String data) {
//        //Fragment fragment = (Fragment) myAdapter.instantiateItem(nonSwipeableViewPager, nonSwipeableViewPager.getCurrentItem());
//        FragmentHome fragment = (FragmentHome)myAdapter.instantiateItem(nonSwipeableViewPager, nonSwipeableViewPager.getCurrentItem());
//        Bundle args = new Bundle();
//        args.putString(DATA_RECEIVE, data);
//        fragment.setArguments(args);
//        //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentAlarm, fragmentHome ).commit();
//    }

//    @Override
//    public void onButtonPressed(String msg) {
//        FragmentHome Obj = (FragmentHome) getSupportFragmentManager().findFragmentById(R.id.refreshHome);
//        Obj.setMessage(msg);
//    }
}
