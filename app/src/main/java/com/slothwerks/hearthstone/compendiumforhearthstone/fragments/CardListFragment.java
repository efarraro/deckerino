package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
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
public class CardListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_list, container, false);

        ViewPager pager = (ViewPager)rootView.findViewById(R.id.card_list_pager);
        pager.setAdapter(new CardListPagerAdapter(getActivity().getSupportFragmentManager()));

        PagerSlidingTabStrip tabs =
                (PagerSlidingTabStrip)rootView.findViewById(R.id.card_list_pager_title_strip);
        tabs.setViewPager(pager);

        return rootView;
    }
}
