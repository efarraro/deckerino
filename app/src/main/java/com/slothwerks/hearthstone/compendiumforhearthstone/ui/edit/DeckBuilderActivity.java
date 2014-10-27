package com.slothwerks.hearthstone.compendiumforhearthstone.ui.edit;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.slothwerks.hearthstone.compendiumforhearthstone.ui.BaseActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.shared.DeckListArrayAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.data.database.DeckDbAdapter;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventDeckUpdated;
import com.slothwerks.hearthstone.compendiumforhearthstone.events.EventRequestDisplayDeck;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.CardQuantityPair;
import com.slothwerks.hearthstone.compendiumforhearthstone.models.Deck;
import com.slothwerks.hearthstone.compendiumforhearthstone.util.Utility;

import junit.framework.Assert;

import de.greenrobot.event.EventBus;

public class DeckBuilderActivity extends BaseActivity implements IntentConstants {

    protected DrawerLayout mDeckDrawerLayout;
    protected long mDeckId;
    protected Deck mCurrentDeck;
    protected ListView mDeckDrawer;
    protected ArrayAdapter<CardQuantityPair> mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_builder);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // check to see if we have a deck ID for this deck
        mDeckId = getIntent().getLongExtra(DECK_ID, -1);

        Assert.assertTrue("Expects deck_id", mDeckId != -1);

        Deck deck = new DeckDbAdapter(this.getApplicationContext()).getDeckById(mDeckId);

        // TODO understand savedInstanceState better
        if (savedInstanceState == null) {

            // add the deck summary at the top of the page (card count etc... )
            /*getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_deck_summary, new DeckSummaryFragment())
                    .commit();*/

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

            // set the title ("Deck Builder (0/30)")
            setTitle(String.format(getString(R.string.activity_deck_builder), 0, 30));
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // theme the action bar header to match the class
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Utility.getPrimaryColorForClass(
                        deck.getPlayerClass(), getResources())));

        // I believe this is required, or the color would not show up for some reason
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
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

        if(mCurrentDeck != e.getDeck()) {
            mListAdapter =
                    new DeckListArrayAdapter(getApplicationContext(), e.getDeck().getCards());

            mCurrentDeck = e.getDeck();
            mDeckDrawer.setAdapter(mListAdapter);
        }

        // set the title ("Deck Builder (0/30)")
        setTitle(String.format(getString(R.string.activity_deck_builder),
                 mCurrentDeck.getCardCount(), 30));

        mListAdapter.notifyDataSetChanged();
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
