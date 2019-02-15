package com.kevin.testim.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Kevin on 2019/1/22<br/>
 * Blog:http://student9128.top/<br/>
 * Describe:<br/>
 */
public class NoSmoothViewPager extends ViewPager {
    public NoSmoothViewPager(@NonNull Context context) {
        super(context);
    }

    public NoSmoothViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }
}
