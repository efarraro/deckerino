package com.slothwerks.hearthstone.compendiumforhearthstone.adapters;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventCardTapped;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventDeckUpdated;
import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.CardListFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.DeckListFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 9/17/2014.
 */
public class DeckBuilderPagerAdapter extends FragmentPagerAdapter {

    //protected Deck mCurrentDeck;
    protected PlayerClass mCurrentClass;

    public DeckBuilderPagerAdapter(FragmentManager fm, PlayerClass playerClass) {
        super(fm);

        mCurrentClass = playerClass;

        //EventBus.getDefault().register(this);
    }

    @Override
    public Fragment getItem(int i) {

        CardListFragment fragment = new CardListFragment();

        // 3 tabs -- cards for current class, neutral cards, current deck under construction
        if(i == 0) {

            Bundle args = new Bundle();
            args.putString(CardListFragment.PLAYER_CLASS, mCurrentClass.toString());
            fragment.setArguments(args);
            return fragment;
        }
        else {
            Bundle args = new Bundle();
            args.putString(CardListFragment.PLAYER_CLASS, PlayerClass.Neutral.toString());
            fragment.setArguments(args);
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        // 3 tabs -- cards for current class, neutral cards, current deck under construction
        if(position == 0) {
            return PlayerClass.Druid.toString();
        }
        else {
            return PlayerClass.Neutral.toString();
        }
    }

    /*
    public void onEventMainThread(EventDeckUpdated e) {

        mCurrentDeck = e.getDeck();
        notifyDataSetChanged();
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
    }

    // TODO unregister EventBus - where?*/
}
