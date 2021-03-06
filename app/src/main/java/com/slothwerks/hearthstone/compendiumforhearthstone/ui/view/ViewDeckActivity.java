package com.slothwerks.hearthstone.compendiumforhearthstone.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.slothwerks.hearthstone.compendiumforhearthstone.ui.BaseActivity;
import com.slothwerks.hearthstone.compendiumforhearthstone.ui.IntentConstants;
import com.slothwerks.hearthstone.compendiumforhearthstone.R;

public class ViewDeckActivity extends BaseActivity implements IntentConstants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deck);

        Intent intent = getIntent();

        if (savedInstanceState == null) {

            ViewDeckFragment viewDeckFragment = new ViewDeckFragment();
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
