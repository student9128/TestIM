package com.kevin.testim.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevin.testim.R;

import java.util.List;

/**
 * Created by Kevin on 2019/1/22<br/>
 * Blog:http://student9128.top/<br/>
 * Describe:<br/>
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Fragment> fragments;
    private List<String> tabTitleList;
    private int[] tabIconArray;
    private int[] tabIconFilledArray;

    public TabFragmentAdapter(FragmentManager fm) {
        super(fm);
    }


    public TabFragmentAdapter(FragmentManager fm, Context context, List<Fragment> fragments, List<String> tabTitleList, int[] tabIcon) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        this.tabTitleList = tabTitleList;
        this.tabIconArray = tabIcon;
//        this.tabIconFilledArray = tabIconFilled;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitleList.get(position);
    }

    public View getTabView(int position) {
        View v = View.inflate(context, R.layout.adapter_tab_fragment, null);
        ImageView tabIcon = v.findViewById(R.id.tabIcon);
        ImageView tabIconFilled = v.findViewById(R.id.tabIconFilled);
        TextView tabTitle = v.findViewById(R.id.tabTitle);
        tabIcon.setImageResource(tabIconArray[position]);
//        tabIconFilled.setImageResource(tabIconFilledArray[position]);
        tabTitle.setText(tabTitleList.get(position));
        if (position == 0) {
            tabTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            tabIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
//            tabIcon.setAlpha(0f);
//            tabIconFilled.setAlpha(1f);
        }else {
            tabTitle.setTextColor(ContextCompat.getColor(context, R.color.grey_500));
            tabIcon.setColorFilter(ContextCompat.getColor(context, R.color.grey_500));
//            tabIcon.setAlpha(1f);
//            tabIconFilled.setAlpha(0f);
        }
        return v;
    }
}
