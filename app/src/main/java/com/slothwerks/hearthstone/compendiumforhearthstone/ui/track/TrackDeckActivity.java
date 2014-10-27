package com.slothwerks.hearthstone.compendiumforhearthstone.ui.track;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.slothwerks.hearthstone.compendiumforhearthstone.ui.BaseActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;

public class TrackDeckActivity extends BaseActivity implements IntentConstants {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
