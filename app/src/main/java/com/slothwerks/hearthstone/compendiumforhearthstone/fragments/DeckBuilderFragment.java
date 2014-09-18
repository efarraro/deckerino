package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.DeckBuilderPagerAdapter;

/**
 * Created by Eric on 9/17/2014.
 */
public class DeckBuilderFragment extends Fragment {

    public DeckBuilderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_deck_builder, container, false);

        ViewPager pager = (ViewPager)rootView.findViewById(R.id.deck_list_pager);
        pager.setAdapter(new DeckBuilderPagerAdapter(getActivity().getSupportFragmentManager()));

        PagerSlidingTabStrip tabs =
                (PagerSlidingTabStrip)rootView.findViewById(R.id.deck_builder_tab_strip);
        tabs.setViewPager(pager);

        return rootView;
    }
}