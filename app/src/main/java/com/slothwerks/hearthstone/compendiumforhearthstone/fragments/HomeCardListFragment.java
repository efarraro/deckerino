package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.CardListPagerAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventUpdateClassTheme;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Utility;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 9/16/2014.
 */
public class HomeCardListFragment extends Fragment {

    ViewPager mViewPager;
    CardListPagerAdapter mPagerAdapter;
    protected PagerSlidingTabStrip mTabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_card_list, container, false);

        mViewPager = (ViewPager)rootView.findViewById(R.id.card_list_pager);
        mPagerAdapter = new CardListPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mTabs =
                (PagerSlidingTabStrip)rootView.findViewById(R.id.card_list_pager_title_strip);
        mTabs.setViewPager(mViewPager);
        mTabs.setOnPageChangeListener(pageChangeListener);
        mTabs.setTextColor(Color.WHITE);
        mTabs.setIndicatorColor(Color.WHITE);

        // force the tabs color to occur on the first view
        pageChangeListener.onPageSelected(0);

        return rootView;
    }

    // changes the color of the tabs when the page changes
    private final ViewPager.OnPageChangeListener pageChangeListener =
            new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {}
        @Override
        public void onPageSelected(int i) {
            PlayerClass playerClass = mPagerAdapter.getPlayerClassAtPosition(i);
            mTabs.setBackground(new ColorDrawable(
                    Utility.getPrimaryColorForClass(playerClass, getResources())));
            getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(
                    Utility.getPrimaryColorForClass(playerClass, getResources())));

            // send an event to notify listeners to update the class theme as needed (colors etc..)
            EventBus.getDefault().post(new EventUpdateClassTheme(playerClass));
        }
        @Override
        public void onPageScrollStateChanged(int i) {}
    };
}
