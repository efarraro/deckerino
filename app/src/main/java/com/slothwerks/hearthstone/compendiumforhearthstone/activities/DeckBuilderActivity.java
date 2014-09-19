package com.slothwerks.hearthstone.compendiumforhearthstone.activities;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventCardTapped;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventDeckUpdated;
import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.DeckBuilderFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.DeckSummaryFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;

import de.greenrobot.event.EventBus;

public class DeckBuilderActivity extends FragmentActivity {

    protected Deck mDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_builder);

        mDeck = new Deck();

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_deck_summary, new DeckSummaryFragment())
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DeckBuilderFragment())
                    .commit();
        }

        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(EventCardTapped e) {
        mDeck.addToDeck(e.getCard());

        // notify listeners that the current deck has been updated
        EventBus.getDefault().post(new EventDeckUpdated(mDeck));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }
}
