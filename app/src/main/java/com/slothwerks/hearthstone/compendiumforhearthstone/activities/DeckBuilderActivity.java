package com.slothwerks.hearthstone.compendiumforhearthstone.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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

import com.slothwerks.hearthstone.compendiumforhearthstone.IntentConstants;
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

import junit.framework.Assert;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class DeckBuilderActivity extends BaseFragmentActivity implements IntentConstants {

    protected DrawerLayout mDeckDrawerLayout;
    protected PlayerClass mCurrentClass;
    protected long mDeckId;
    protected ListView mDeckDrawer;
    protected ArrayAdapter<CardQuantityPair> mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_builder);

        // check to see if we have a deck ID for this deck
        String deckIdString = getIntent().getStringExtra((DECK_ID));
        if(deckIdString != null)
            mDeckId = Long.parseLong(deckIdString);

        Assert.assertTrue("Expects deck_id", deckIdString != null);

        // TODO debug, for now
        mCurrentClass = PlayerClass.Hunter;

        // TODO understand savedInstanceState better
        if (savedInstanceState == null) {

            // add the deck summary at the top of the page (card count etc... )
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_deck_summary, new DeckSummaryFragment())
                    .commit();

            // add the rest of the deck builder
            DeckBuilderFragment deckBuilderFragment = new DeckBuilderFragment();
            Bundle bundle = new Bundle();

            // pass in the ID of the deck we're working with
            bundle.putLong(DECK_ID, mDeckId);
            deckBuilderFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, deckBuilderFragment)
                    .commit();

            // set up for the right-bar deck list
            mDeckDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
            mDeckDrawer = (ListView)findViewById(R.id.deck_builder_deck_drawer);

            mListAdapter =
                    new DeckListArrayAdapter(this, deckBuilderFragment.getDeck().getCards());
            mDeckDrawer.setAdapter(mListAdapter);

            setTitle(String.format(getString(R.string.activity_deck_builder), mCurrentClass));
        }

        // TODO testing class specific color header
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.warlock_primary)));
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(true);
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

    public void onEventMainThread(EventRequestDisplayDeck e) {

        mDeckDrawerLayout.openDrawer(mDeckDrawer);
    }

    /**
     * Updates the deck list, whenever the deck is updated
     */
    public void onEventMainThread(EventDeckUpdated e) {

        mListAdapter.notifyDataSetChanged();
    }
}
