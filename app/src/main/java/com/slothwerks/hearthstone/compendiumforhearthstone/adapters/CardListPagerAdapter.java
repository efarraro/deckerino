package com.slothwerks.hearthstone.compendiumforhearthstone.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.MainFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 9/16/2014.
 */
public class CardListPagerAdapter extends FragmentPagerAdapter {

    protected ArrayList<PlayerClass> mPlayerClasses;

    public CardListPagerAdapter(FragmentManager fm) {

        super(fm);

        mPlayerClasses = new ArrayList<PlayerClass>();
        mPlayerClasses.add(PlayerClass.Druid);
        mPlayerClasses.add(PlayerClass.Warrior);
        mPlayerClasses.add(PlayerClass.Mage);
        mPlayerClasses.add(PlayerClass.Priest);
        mPlayerClasses.add(PlayerClass.Hunter);
        mPlayerClasses.add(PlayerClass.Warlock);
        mPlayerClasses.add(PlayerClass.Shaman);
    }

    @Override
    public Fragment getItem(int i) {

        MainFragment fragment = new MainFragment();

        Bundle args = new Bundle();
        args.putString(MainFragment.PLAYER_CLASS, mPlayerClasses.get(i).toString());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return mPlayerClasses.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPlayerClasses.get(position).toString();
    }
}