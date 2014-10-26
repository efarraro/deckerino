package com.slothwerks.hearthstone.compendiumforhearthstone.ui.edit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.slothwerks.hearthstone.compendiumforhearthstone.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.shared.CardListFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;

/**
 * Created by Eric on 9/17/2014.
 */
public class DeckBuilderPagerAdapter extends FragmentPagerAdapter implements IntentConstants {

    //protected Deck mCurrentDeck;
    protected PlayerClass mCurrentClass;
    protected  long mDeckId;

    public DeckBuilderPagerAdapter(FragmentManager fm, PlayerClass playerClass, long deckId) {
        super(fm);

        mCurrentClass = playerClass;
        mDeckId = deckId;

        //EventBus.getDefault().register(this);
    }

    @Override
    public Fragment getItem(int i) {

        CardListFragment fragment = new CardListFragment();

        // 3 tabs -- cards for current class, neutral cards, current deck under construction
        if(i == 0) {

            Bundle args = new Bundle();
            args.putString(CardListFragment.PLAYER_CLASS, mCurrentClass.toString());
            args.putLong(DECK_ID, mDeckId);
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

        // 2 tabs -- cards for current class & neutral cards
        // TODO localization?
        if(position == 0) {
            return mCurrentClass.toString();
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
