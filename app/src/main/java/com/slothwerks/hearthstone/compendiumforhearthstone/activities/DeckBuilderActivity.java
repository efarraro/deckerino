package com.slothwerks.hearthstone.compendiumforhearthstone.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.adapters.DeckListArrayAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventCardQuantityUpdated;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventCardTapped;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventDeckUpdated;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventRequestDisplayDeck;
import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.DeckBuilderFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.DeckSummaryFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Card;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.CardQuantityPair;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.PlayerClass;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class DeckBuilderActivity extends BaseFragmentActivity {

    protected Deck mDeck;
    protected DrawerLayout mDeckDrawerLayout;
    protected ListView mDeckDrawer;
    protected ArrayAdapter<CardQuantityPair> mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_builder);

        mDeck = new Deck();

        if (savedInstanceState == null) {

            // add the deck summary at the top of the page (card count etc... )
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_deck_summary, new DeckSummaryFragment())
                    .commit();

            // add the rest of the deck builder
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DeckBuilderFragment())
                    .commit();
        }

        mDeckDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDeckDrawer = (ListView)findViewById(R.id.deck_builder_deck_drawer);

        mListAdapter =
                new DeckListArrayAdapter(this, mDeck.getCards());
        mDeckDrawer.setAdapter(mListAdapter);

        // TODO look to instance for the player's class
        setTitle(String.format(getString(R.string.activity_deck_builder), PlayerClass.Druid));
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(EventCardTapped e) {

        // TODO need to push this into the Deck itself
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

        mListAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(EventRequestDisplayDeck e) {

        mDeckDrawerLayout.openDrawer(mDeckDrawer);
    }
}
