package com.slothwerks.hearthstone.compendiumforhearthstone.activities;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.slothwerks.hearthstone.compendiumforhearthstone.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.TrackDeckFragment;
import com.slothwerks.hearthstone.compendiumforhearthstone.fragments.ViewDeckFragment;

public class TrackDeckActivity extends FragmentActivity implements IntentConstants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_deck);
        if (savedInstanceState == null) {

            TrackDeckFragment viewDeckFragment = new TrackDeckFragment();
            Bundle args = new Bundle();
            long deckId = getIntent().getExtras().getLong(DECK_ID);

            args.putLong(DECK_ID, deckId);
            viewDeckFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,viewDeckFragment)
                    .commit();
        }
    }
}
