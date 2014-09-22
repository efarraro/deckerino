package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;

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

/**
 * Created by Eric on 9/16/2014.
 */
public class HomeCardListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_card_list, container, false);

        ViewPager pager = (ViewPager)rootView.findViewById(R.id.card_list_pager);
        pager.setAdapter(new CardListPagerAdapter(getActivity().getSupportFragmentManager()));

        final PagerSlidingTabStrip tabs =
                (PagerSlidingTabStrip)rootView.findViewById(R.id.card_list_pager_title_strip);
        tabs.setViewPager(pager);

        //tabs.setTabBackground(R.color.warlock_tab);
        //tabs.getChildAt(0).setBackground(getResources().getDrawable(R.drawable.warlock_tab));

        //tabs.setTabBackground(R.drawable.warlock_tab);
        //tabs.setIndicatorColor(getResources().getColor(R.color.epic));
        /*
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

                if(i % 2 == 0)
                    tabs.setIndicatorColor(getResources().getColor(R.color.epic));
                else
                    tabs.setIndicatorColor(getResources().getColor(R.color.rare));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });*/

        return rootView;
    }
}
