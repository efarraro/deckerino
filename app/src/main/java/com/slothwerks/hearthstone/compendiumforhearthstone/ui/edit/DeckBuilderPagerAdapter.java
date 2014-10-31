package com.slothwerks.hearthstone.compendiumforhearthstone.ui.edit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.slothwerks.hearthstone.compendiumforhearthstone.ui.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.shared.CardListFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Utility;

/**
 * Created by Eric on 9/17/2014.
 */
public class DeckBuilderPagerAdapter extends FragmentPagerAdapter implements IntentConstants {

    //protected Deck mCurrentDeck;
    protected PlayerClass mCurrentClass;
    protected  long mDeckId;
    protected Context mContext;

    public DeckBuilderPagerAdapter(
            FragmentManager fm, PlayerClass playerClass, long deckId, Context context) {
        super(fm);

        mCurrentClass = playerClass;
        mDeckId = deckId;
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {

        CardListFragment fragment = new CardListFragment();

        // 2 tabs -- cards for current class, neutral cards
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
        if(position == 0) {
            return Utility.localizedStringForPlayerClass(mCurrentClass, mContext);
        }
        else {
            return Utility.localizedStringForPlayerClass(PlayerClass.Neutral, mContext);
        }
    }
}
