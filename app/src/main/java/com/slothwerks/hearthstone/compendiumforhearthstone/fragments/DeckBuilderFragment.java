package com.slothwerks.hearthstone.compendiumforhearthstone.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.slothwerks.hearthstone.compendiumforhearthstone.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.activities.BaseFragmentActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.DeckBuilderPagerAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DeckDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventCardQuantityUpdated;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventCardTapped;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventDeckUpdated;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.CardQuantityPair;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;

import junit.framework.Assert;

import java.sql.SQLException;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 9/17/2014.
 */
public class DeckBuilderFragment extends Fragment implements IntentConstants {

    protected Deck mDeck;
    protected long mDeckId;

    public DeckBuilderFragment() {

        mDeck = new Deck();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Assert.assertTrue("Expected deck_id bundle arg", getArguments().containsKey(DECK_ID));
        mDeckId = getArguments().getLong(DECK_ID);
        mDeck = new DeckDbAdapter(getActivity().getApplicationContext()).getDeckById(mDeckId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_deck_builder, container, false);

        ViewPager pager = (ViewPager)rootView.findViewById(R.id.deck_list_pager);
        pager.setAdapter(new DeckBuilderPagerAdapter(
                getActivity().getSupportFragmentManager(), mDeck.getPlayerClass()));

        PagerSlidingTabStrip tabs =
                (PagerSlidingTabStrip)rootView.findViewById(R.id.deck_builder_tab_strip);
        tabs.setViewPager(pager);

        return rootView;
    }

    public Deck getDeck() {
        return mDeck;
    }

    public void onEventMainThread(EventCardTapped e) {

        // add card to deck, if possible
        if(mDeck.canAddToDeck(e.getCard())) {
            mDeck.addToDeck(e.getCard());
        }
        else {
            // if we couldn't add the card due to quantity constraints, set the quantity to 0
            // allows the user to repeatedly tap a card to add/remove to get the quanity they want
            mDeck.removeAllCopies(e.getCard());
        }

        // notify listeners that the current deck has been updated
        EventBus.getDefault().post(new EventDeckUpdated(mDeck));

        // notify listeners which card was updated, specifically
        EventBus.getDefault().post(new EventCardQuantityUpdated
                (e.getCard(), mDeck.getCountForCard(e.getCard())));

        // update the database in realtime with the new card data (performance impact?)
        DeckDbAdapter deckDbAdapter = null;
        try {
            deckDbAdapter = (DeckDbAdapter)new DeckDbAdapter(getActivity()).open();
            deckDbAdapter.updateCardData(mDeck);

        } catch(SQLException ex) {

            ex.printStackTrace();
        } finally {
            if(deckDbAdapter != null)
                deckDbAdapter.close();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        String deckerino = mDeck.toDeckerinoFormat();
        Log.d("deckerino", deckerino);

        Deck d = Deck.fromDeckerinoFormat(getActivity(), deckerino);
        for(CardQuantityPair pair : d.getCards())
        {
            Log.d("TEST", pair.getCard().getName() + " " + pair.getQuantity());
        }
    }
}