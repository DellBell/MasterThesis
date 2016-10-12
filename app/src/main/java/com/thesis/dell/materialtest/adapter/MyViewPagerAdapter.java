package com.thesis.dell.materialtest.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.thesis.dell.materialtest.R;
import com.thesis.dell.materialtest.fragments.FragmentAlarm;
import com.thesis.dell.materialtest.fragments.FragmentHome;
import com.thesis.dell.materialtest.fragments.FragmentSetting;
import com.thesis.dell.materialtest.fragments.FragmentTrend;

/**
 * Created by Administrator on 24.03.2015.
 */
public class MyViewPagerAdapter extends FragmentStatePagerAdapter {


    private Context context;
    int icons[] = {R.drawable.ic_home,
            R.drawable.ic_graph,
            R.drawable.ic_bell_mid,
            R.drawable.ic_settings};

    public MyViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) // if the position is 0 we are returning the First tab
        {
            return new FragmentHome();
        }
        else if (position == 1)
        {
            return new FragmentTrend();
        }
        else if (position == 2)
        {
            return new FragmentAlarm();
        }
        else
        {
            return new FragmentSetting();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    public Drawable getIcon(int position)
    {
        return context.getResources().getDrawable(icons[position]);
    }



}
