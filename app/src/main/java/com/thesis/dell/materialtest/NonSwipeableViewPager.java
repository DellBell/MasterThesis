package com.thesis.dell.materialtest;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * This class will help disable the swiping on the second tab(trend tab)
 * Created by DellMain on 16.11.2015.
 */
public class NonSwipeableViewPager extends ViewPager {
    public NonSwipeableViewPager(Context context) {
        super(context);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch(getCurrentItem()){
            case 1: //disable second tab
                return false;
            default:
                return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch(getCurrentItem()){
            case 1:
                return false;
            default:
                return super.onTouchEvent(ev);
        }
    }
}
