package com.slothwerks.hearthstone.compendiumforhearthstone.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.CardListFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.MainFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;

/**
 * Created by Eric on 9/17/2014.
 */
public class DeckBuilderPagerAdapter extends FragmentPagerAdapter {

    public DeckBuilderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        MainFragment fragment = new MainFragment();

        Bundle args = new Bundle();
        args.putString(MainFragment.PLAYER_CLASS, PlayerClass.Druid.toString());
                fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Test";
    }
}
